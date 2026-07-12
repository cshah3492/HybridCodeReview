package com.prototype.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prototype.model.Finding;
import com.prototype.model.TriageResult;
import com.prototype.util.OutputManager;
import com.prototype.model.FeedbackRecord;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FeedbackStore {

	private String feedbackFile;

	public FeedbackStore(OutputManager outputManager) {
	    feedbackFile = outputManager.getPath("feedback.json");
	}
    private ObjectMapper mapper = new ObjectMapper();

    public void saveFeedback(Finding finding, TriageResult result, String developerDecision, String notes) {
        try {
            List<FeedbackRecord> records = loadAllRecords();

            boolean updated = false;
            
            for (FeedbackRecord record : records) {
                if (safeEquals(record.getBugType(), finding.getBugType())
                        && safeEquals(record.getClassName(), finding.getClassName())
                        && safeEquals(record.getMethodName(), finding.getMethodName())) {

                    record.setLlmClassification(result.getClassification());
                    record.setLlmConfidence(result.getConfidence());
                    record.setDeveloperDecision(developerDecision);
                    record.setNotes(notes);
                    updated = true;
                    break;
                }
            }

            if (!updated) {
                FeedbackRecord record = new FeedbackRecord();
                record.setBugType(finding.getBugType());
                record.setClassName(finding.getClassName());
                record.setMethodName(finding.getMethodName());
                record.setLlmClassification(result.getClassification());
                record.setLlmConfidence(result.getConfidence());
                record.setDeveloperDecision(developerDecision);
                record.setNotes(notes);
                records.add(record);
            }

            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(feedbackFile), records);

        } catch (Exception e) {
            System.err.println("Error saving feedback: " + e.getMessage());
        }
    }
    
    private boolean safeEquals(String a, String b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        return a.equals(b);
    }
    
    public int getTotalFeedbackRecords() {
        return loadAllRecords().size();
    }

    public List<String> findRelevantFeedback(Finding finding) {
        List<String> feedback = new ArrayList<String>();

        try {
            List<FeedbackRecord> records = loadAllRecords();

            for (FeedbackRecord record : records) {
                if (record.getBugType() != null && record.getBugType().equals(finding.getBugType())) {
                    feedback.add("Past " + record.getBugType()
                            + " finding was marked by developer as "
                            + record.getDeveloperDecision()
                            + ". Notes: " + record.getNotes());
                }
            }

        } catch (Exception e) {
            System.err.println("Error loading relevant feedback: " + e.getMessage());
        }

        return feedback;
    }

    private List<FeedbackRecord> loadAllRecords() {
        try {
            File file = new File(feedbackFile);

            if (!file.exists()) {
                return new ArrayList<FeedbackRecord>();
            }

            FeedbackRecord[] array = mapper.readValue(file, FeedbackRecord[].class);

            List<FeedbackRecord> records = new ArrayList<FeedbackRecord>();
            for (FeedbackRecord record : array) {
                records.add(record);
            }

            return records;

        } catch (Exception e) {
            return new ArrayList<FeedbackRecord>();
        }
    }

}