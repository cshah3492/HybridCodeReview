package com.prototype.report;

import java.io.FileWriter;
import java.util.List;

import com.prototype.model.Finding;
import com.prototype.util.OutputManager;

public class ReportGenerator {
	
    private OutputManager outputManager;

    public ReportGenerator(OutputManager outputManager) {
        this.outputManager = outputManager;
    }

    public void generateReport(List<Finding> findings) {
        try {
        	FileWriter writer = new FileWriter(outputManager.getPath("prototype-report.txt"));

            writer.write("Hybrid Code Review Prototype Report\n");
            writer.write("===================================\n\n");

            writer.write("Total SpotBugs Findings: " + findings.size() + "\n\n");

            for (Finding finding : findings) {
                writer.write(finding.toString());
                writer.write("\n");
            }

            writer.close();

            System.out.println("Report generated: prototype-report.txt");

        } catch (Exception e) {
            System.err.println("Error generating report: " + e.getMessage());
        }
    }
}