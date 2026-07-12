package com.prototype.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ScannerRunner {

    public int runSpotBugs() {
        try {
            ProcessBuilder builder = new ProcessBuilder("mvn", "compile", "spotbugs:spotbugs");
            builder.redirectErrorStream(true);

            Process process = builder.start();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            return process.waitFor();

        } catch (Exception e) {
            System.err.println("Error running SpotBugs: " + e.getMessage());
            return -1;
        }
    }
}