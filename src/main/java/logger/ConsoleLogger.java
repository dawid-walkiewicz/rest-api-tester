package logger;

import java.util.ArrayList;
import java.util.List;

public class ConsoleLogger implements Logger{
    @Override
    public void log(String message) {
        System.out.println(message);
    }

    @Override
    public void log(LogLevel level, String message) {
        System.out.println(level.name() + " | " + message);
    }

    @Override
    public void summary(List<String> failedTests, int amountOfTests) {
        List<String> lines = new ArrayList<>();
        LoggerUtils.createSummary(lines, failedTests, amountOfTests);
        lines.forEach(System.out::println);
    }

    @Override
    public void flush() {}

}
