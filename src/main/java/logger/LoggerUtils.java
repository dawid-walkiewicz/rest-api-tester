package logger;

import java.util.List;

public class LoggerUtils {
    protected static void createSummary(List<String> lines, List<String> failedTests, int amountOfTests){
        int passed = amountOfTests-failedTests.size();
        int failed = failedTests.size();
        double passingRate = ((double) passed /amountOfTests) * 100;

        lines.add("\n\n\n" + "=".repeat(60));
        lines.add(" TESTS SUMMARY");

        if(!failedTests.isEmpty()){
            lines.add("=".repeat(60));
            lines.add(" FAILED TESTS:");
            failedTests.forEach((v) -> lines.add("  -> " + v));
        }

        lines.add("=".repeat(60));
        lines.add(" TESTS PASSED: " + passed + " | TESTS FAILED: " + failed + " | TESTS PASS RATE: " + passingRate + "%");
        lines.add("=".repeat(60));
    }
}
