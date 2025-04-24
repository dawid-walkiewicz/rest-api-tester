import java.util.ArrayList;
import java.util.List;

public class HttpBenchmarkResult {
    public final int totalRequests;
    public final int successCount;
    public final long minTime;
    public final long maxTime;
    public final double avgTimeMillis;

    public HttpBenchmarkResult(int totalRequests, int successCount, long minTime, long maxTime, double avgTimeMillis) {
        this.totalRequests = totalRequests;
        this.successCount = successCount;
        this.minTime = minTime;
        this.maxTime = maxTime;
        this.avgTimeMillis = avgTimeMillis;
    }

    @Override
    public String toString() {
        return "Benchmark Results:\n" +
                "  Total Requests: " + totalRequests + "\n" +
                "  Successful requests (200 OK): " + successCount + " (" + (100.0 * successCount / totalRequests) + "%)\n" +
                "  Min Time: " + minTime + " ms\n" +
                "  Max Time: " + maxTime + " ms\n" +
                "  Avg Time: " + String.format("%.2f", avgTimeMillis) + " ms";
    }
}
