package com.prototype.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prototype.model.EvaluationRecord;
import com.prototype.model.Finding;
import com.prototype.model.OpenAIResponse;
import com.prototype.model.TriageResult;
import com.prototype.report.CsvReportGenerator;
import com.prototype.report.PrototypeSummary;
import com.prototype.report.ReportGenerator;
import com.prototype.statistics.CostStatistics;
import com.prototype.statistics.LearningStatistics;
import com.prototype.statistics.MetricsCalculator;
import com.prototype.statistics.PerformanceStatistics;
import com.prototype.util.OutputManager;

import java.util.ArrayList;
import java.util.List;

public class PrototypeRunner {


    private final String spotBugsXmlPath ="C:/Users/chira/eclipse-workspace/BuggyJavaProjectV2/target/spotbugsXml.xml";
    
    private final OutputManager outputManager = new OutputManager();

    private final SpotBugsParser parser = new SpotBugsParser();
    private final FeedbackStore feedbackStore = new FeedbackStore(outputManager);
    private final PromptBuilder promptBuilder = new PromptBuilder();
    private final OpenAIClient llmClient = new OpenAIClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private final GroundTruthStore groundTruthStore = new GroundTruthStore("ground-truth-v2.csv");

    private final LearningStatistics learningStatistics = new LearningStatistics();
    private final PerformanceStatistics performance = new PerformanceStatistics();
    private final CostStatistics costStatistics = new CostStatistics();

    private final List<EvaluationRecord> evaluationRecords = new ArrayList<EvaluationRecord>();

    private int tp = 0;
    private int fp = 0;
    private int fn = 0;

    public void run() {
        System.out.println("Hybrid Code Review Prototype Started");

        long totalStartTime = System.currentTimeMillis();

        long spotbugsStart = System.currentTimeMillis();
        List<Finding> findings = parser.parse(spotBugsXmlPath);
        long spotbugsEnd = System.currentTimeMillis();

        performance.setSpotBugsTime(spotbugsEnd - spotbugsStart);

        System.out.println("Findings found: " + findings.size());

        if (findings.isEmpty()) {
            System.out.println("No findings found. Add buggy Java code or verify SpotBugs XML path.");
            return;
        }

        processFindings(findings);

        generateReports(findings);

        printStatistics(totalStartTime);

        System.out.println("\nHybrid Code Review Prototype Completed");
    }

    private void processFindings(List<Finding> findings) {
        for (Finding finding : findings) {
            learningStatistics.incrementTotalFindingsAnalyzed();

            System.out.println("\n===== STATIC FINDING =====");
            System.out.println(finding);

            List<String> priorFeedback = feedbackStore.findRelevantFeedback(finding);

            if (priorFeedback != null && !priorFeedback.isEmpty()) {
                learningStatistics.incrementFindingsUsingPriorFeedback();
            }

            String prompt = promptBuilder.buildPrompt(finding, priorFeedback);

            long llmStartTime = System.currentTimeMillis();
            OpenAIResponse openAIResponse = llmClient.analyzeFinding(prompt);
            long llmEndTime = System.currentTimeMillis();

            long llmLatencyMs = llmEndTime - llmStartTime;
            performance.addLlmTime(llmLatencyMs);

            String llmResponse = openAIResponse.getContent();
            costStatistics.addUsage(openAIResponse.getTokenUsage());

            try {
                String cleanedResponse = cleanJsonResponse(llmResponse);
                TriageResult result = mapper.readValue(cleanedResponse, TriageResult.class);
                
             // ===== ONLY FOR EXPERIMENT 3 (False Negative Scenario) =====
//                if ("OS_OPEN_STREAM".equals(finding.getBugType())) {
//                    result.setClassification("False Positive");
//                    result.setConfidence(0.90);
//                }
                // ===========================================================

                String groundTruth = groundTruthStore.getLabel(finding);

                updateMetrics(result.getClassification(), groundTruth);

                addEvaluationRecord(finding, result, groundTruth);

                String evaluation =
                        getEvaluationResult(result.getClassification(), groundTruth);

                System.out.println("\n===== LLM TRIAGE RESULT =====");

                System.out.println("Ground Truth   : " + groundTruth);
                System.out.println("LLM Prediction : " + result.getClassification());
                System.out.println("Evaluation     : " + evaluation);

                System.out.println("Classification : " + result.getClassification());
                System.out.println("Confidence     : " + result.getConfidence());
                System.out.println("Reasoning      : " + result.getReasoning());
                System.out.println("Recommendation : " + result.getRecommendation());

                feedbackStore.saveFeedback(
                        finding,
                        result,
                        result.getClassification(),
                        "Prototype developer feedback: accepted LLM classification."
                );

            } catch (Exception e) {
                System.err.println("Error parsing LLM response: " + e.getMessage());
            }
        }
    }

    private void updateMetrics(String prediction, String groundTruth) {
        if ("True Positive".equalsIgnoreCase(prediction)
                && "True Positive".equalsIgnoreCase(groundTruth)) {
            tp++;
        } else if ("True Positive".equalsIgnoreCase(prediction)
                && "False Positive".equalsIgnoreCase(groundTruth)) {
            fp++;
        } else if ("False Positive".equalsIgnoreCase(prediction)
                && "True Positive".equalsIgnoreCase(groundTruth)) {
            fn++;
        }
    }

    private void addEvaluationRecord(Finding finding, TriageResult result, String groundTruth) {
        EvaluationRecord evaluationRecord = new EvaluationRecord();
        evaluationRecord.setBugType(finding.getBugType());
        evaluationRecord.setClassName(finding.getClassName());
        evaluationRecord.setMethodName(finding.getMethodName());
        evaluationRecord.setGroundTruth(groundTruth);
        evaluationRecord.setLlmPrediction(result.getClassification());
        evaluationRecord.setConfidence(result.getConfidence());
        evaluationRecord.setResult(getEvaluationResult(result.getClassification(), groundTruth));

        evaluationRecords.add(evaluationRecord);
    }

    private void generateReports(List<Finding> findings) {
        ReportGenerator reportGenerator = new ReportGenerator(outputManager);
        reportGenerator.generateReport(findings);

        CsvReportGenerator csvReportGenerator = new CsvReportGenerator(outputManager);
        csvReportGenerator.generateCsv(evaluationRecords);
    }

    private void printStatistics(long totalStartTime) {
        MetricsCalculator calculator = new MetricsCalculator();
        calculator.calculate(tp, fp, fn);

        learningStatistics.setTotalFeedbackRecords(feedbackStore.getTotalFeedbackRecords());
        learningStatistics.printStatistics();

        long totalEndTime = System.currentTimeMillis();
        performance.setTotalExecutionTime(totalEndTime - totalStartTime);
        performance.printStatistics();

        costStatistics.printStatistics();

        PrototypeSummary prototypeSummary = new PrototypeSummary();
        prototypeSummary.printSummary();
    }

	private String getEvaluationResult(String prediction, String groundTruth) {

		if ("True Positive".equalsIgnoreCase(prediction) && "True Positive".equalsIgnoreCase(groundTruth)) {
			return "True Positive (TP)";
		}

		if ("True Positive".equalsIgnoreCase(prediction) && "False Positive".equalsIgnoreCase(groundTruth)) {
			return "False Positive (FP)";
		}

		if ("False Positive".equalsIgnoreCase(prediction) && "True Positive".equalsIgnoreCase(groundTruth)) {
			return "False Negative (FN)";
		}

		if ("False Positive".equalsIgnoreCase(prediction) && "False Positive".equalsIgnoreCase(groundTruth)) {
			return "True Negative (TN)";
		}

		return "Manual Review";
	}

    private String cleanJsonResponse(String response) {
        if (response == null) {
            return "";
        }

        response = response.trim();

        if (response.startsWith("```json")) {
            response = response.substring(7);
        } else if (response.startsWith("```")) {
            response = response.substring(3);
        }

        if (response.endsWith("```")) {
            response = response.substring(0, response.length() - 3);
        }

        return response.trim();
    }
}