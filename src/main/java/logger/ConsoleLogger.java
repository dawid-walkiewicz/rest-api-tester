package logger;

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
    public void flush() {}

}
