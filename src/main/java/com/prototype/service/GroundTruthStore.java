package com.prototype.service;

import com.prototype.model.Finding;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class GroundTruthStore {

    private Map<String, String> exactLabels = new HashMap<String, String>();
    private Map<String, String> bugTypeLabels = new HashMap<String, String>();

    public GroundTruthStore(String filePath) {
        load(filePath);
    }

    private void load(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine(); // header

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length == 4) {
                    String bugType = parts[0].trim();
                    String className = parts[1].trim();
                    String methodName = parts[2].trim();
                    String groundTruth = parts[3].trim();

                    exactLabels.put(buildKey(bugType, className, methodName), groundTruth);
                } else if (parts.length == 2) {
                    bugTypeLabels.put(parts[0].trim(), parts[1].trim());
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading ground truth: " + e.getMessage());
        }
    }

    public String getLabel(Finding finding) {
        String exactKey = buildKey(
                finding.getBugType(),
                finding.getClassName(),
                finding.getMethodName()
        );

        if (exactLabels.containsKey(exactKey)) {
            return exactLabels.get(exactKey);
        }

        if (bugTypeLabels.containsKey(finding.getBugType())) {
            return bugTypeLabels.get(finding.getBugType());
        }

        return "Manual Review";
    }

    private String buildKey(String bugType, String className, String methodName) {
        return bugType + "|" + className + "|" + methodName;
    }
}