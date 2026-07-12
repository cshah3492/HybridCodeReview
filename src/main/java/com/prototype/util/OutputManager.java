package com.prototype.util;

import java.io.File;

public class OutputManager {

    private String runFolder;

    public OutputManager() {
        this.runFolder = createNextRunFolder();
    }

    private String createNextRunFolder() {
        File outputDir = new File("output");

        if (!outputDir.exists()) {
            outputDir.mkdir();
        }

        int runNumber = 1;

        while (true) {
            File runDir = new File(outputDir, "run-" + runNumber);

            if (!runDir.exists()) {
                runDir.mkdir();
                return runDir.getPath();
            }

            runNumber++;
        }
    }

    public String getRunFolder() {
        return runFolder;
    }

    public String getPath(String fileName) {
        return runFolder + File.separator + fileName;
    }
}