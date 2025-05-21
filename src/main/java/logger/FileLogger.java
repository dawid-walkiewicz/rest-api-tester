package logger;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileLogger implements Logger {
    private final List<String> lines = new ArrayList<>();
    private final Path filePath;

    public FileLogger(Path filePath) {
        this.filePath = filePath;
    }

    @Override
    public void log(String message) {
        lines.add(message);
    }

    @Override
    public void log(LogLevel level, String message) {
        lines.add(level.name() + " | " + message);
    }

    @Override
    public void flush() {
        try (PrintWriter out = new PrintWriter(new FileWriter(getFileName()))) {
            lines.forEach(out::println);
        } catch (IOException e) {
            System.err.println("FileLogger error: " + e.getMessage());
        }
        System.out.println("Log generated in file: " + getFileName());
    }

    private String getFileName() {
        String fileName = filePath.getFileName().toString();
        int dotIndex = fileName.lastIndexOf('.');

        if (dotIndex > 0) {
            return fileName.substring(0, dotIndex) + "_result.log";
        } else {
            return fileName + "_result.log";
        }
    }


}
