package logger;


import java.util.List;

public interface Logger {
    void log(String message);
    void log(LogLevel level, String message);
    void summary(List<String> failedTests, int amountOfTests);
    void flush();
}

