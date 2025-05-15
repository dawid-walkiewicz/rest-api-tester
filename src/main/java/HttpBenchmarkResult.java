import java.util.ArrayList;
import java.util.List;

public record HttpBenchmarkResult(int totalRequests, int successCount, long minTime, long maxTime,
                                  double avgTimeMillis) implements HttpResult {

    @Override
    public String toString() {
        return "Benchmark Results:\n" +
                "  Total Requests: " + totalRequests + "\n" +
                "  Successful requests (200 OK): " + successCount + " (" + (100.0 * successCount / totalRequests) + "%)\n" +
                "  Min Time: " + minTime + " ms\n" +
                "  Max Time: " + maxTime + " ms\n" +
                "  Average Time: " + String.format("%.2f", avgTimeMillis) + " ms";
    }
}

interface HttpResult {}