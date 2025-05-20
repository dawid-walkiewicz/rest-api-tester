package logger;


public interface Logger {
    int failed = 0;
    int passed = 0;

    void log(String message);
    void log(LogLevel level, String message);
    void flush();
}

