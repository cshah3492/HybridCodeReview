package com.prototype.statistics;

public class MetricsCalculator {

    public void calculate(int tp, int fp, int fn) {
        double precision = 0.0;
        double recall = 0.0;
        double f1 = 0.0;

        if ((tp + fp) > 0) {
            precision = (double) tp / (tp + fp);
        }

        if ((tp + fn) > 0) {
            recall = (double) tp / (tp + fn);
        }

        if ((precision + recall) > 0) {
            f1 = 2 * ((precision * recall) / (precision + recall));
        }

        System.out.println("\n===== EVALUATION METRICS =====");
        System.out.println("TP: " + tp);
        System.out.println("FP: " + fp);
        System.out.println("FN: " + fn);
        System.out.println("Precision: " + String.format("%.2f", precision * 100) + "%");
        System.out.println("Recall: " + String.format("%.2f", recall * 100) + "%");
        System.out.println("F1-Score: " + String.format("%.2f", f1 * 100) + "%");
    }
}