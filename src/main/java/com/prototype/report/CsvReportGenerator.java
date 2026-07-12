package com.prototype.report;

import java.io.FileWriter;
import java.util.List;

import com.prototype.model.EvaluationRecord;
import com.prototype.util.OutputManager;

public class CsvReportGenerator {
	
	  private OutputManager outputManager;

	    public CsvReportGenerator(OutputManager outputManager) {
	        this.outputManager = outputManager;
	    }

    public void generateCsv(List<EvaluationRecord> records) {
        try {
        	FileWriter writer = new FileWriter(outputManager.getPath("prototype-evaluation.csv"));

            writer.write("bugType,className,methodName,groundTruth,llmPrediction,confidence,result\n");

            for (EvaluationRecord record : records) {
                writer.write(escape(record.getBugType()) + ",");
                writer.write(escape(record.getClassName()) + ",");
                writer.write(escape(record.getMethodName()) + ",");
                writer.write(escape(record.getGroundTruth()) + ",");
                writer.write(escape(record.getLlmPrediction()) + ",");
                writer.write(record.getConfidence() + ",");
                writer.write(escape(record.getResult()));
                writer.write("\n");
            }

            writer.close();
            System.out.println("CSV report generated: prototype-evaluation.csv");

        } catch (Exception e) {
            System.err.println("Error generating CSV report: " + e.getMessage());
        }
    }

    private String escape(String value) {
        if (value == null) {
            return "";
        }
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
}