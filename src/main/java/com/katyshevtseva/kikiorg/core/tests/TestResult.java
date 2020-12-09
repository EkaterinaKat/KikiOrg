package com.katyshevtseva.kikiorg.core.tests;

class TestResult {
    private String report = "";
    private boolean success = true;

    void addLineToReport(String s) {
        report += s + "\n";
    }

    void addLineToReport() {
        report += "\n";
    }

    void testFailed() {
        success = false;
    }

    public String getReport() {
        return report;
    }

    boolean isSuccess() {
        return success;
    }
}
