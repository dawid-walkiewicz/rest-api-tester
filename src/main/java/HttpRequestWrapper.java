import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public class HttpRequestWrapper {

    private final HttpClient client;

    public HttpRequestWrapper() {
        this.client = HttpClient.newHttpClient();
    }

    public HttpResultData sendGet(String url) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        try {
            return getHttpResponseData(request);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("GET request failed", e);
        }
    }

    public HttpResultData sendHead(String url) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .method("HEAD", HttpRequest.BodyPublishers.noBody())
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Map<String, String> headerMap = new HashMap<>();
            response.headers().map().forEach((key, values) -> headerMap.put(key, String.join(", ", values)));
            return new HttpResultData(response.statusCode(), headerMap, "");
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("HEAD request failed", e);
        }
    }

    public HttpResultData sendPostJson(String url, String jsonBody) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        try {
            return getHttpResponseData(request);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("POST request failed", e);
        }
    }

    private HttpResultData getHttpResponseData(HttpRequest request) throws IOException, InterruptedException {
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Map<String, String> headerMap = new HashMap<>();
        response.headers().map().forEach((key, values) -> headerMap.put(key, String.join(", ", values)));
        return new HttpResultData(response.statusCode(), headerMap, response.body());
    }

    public HttpBenchmarkResult sendRequest(String url, int times, int timeout, String name) {
        switch (name) {
            case "GET" -> {
                return sendRequestBenchmark(url, times, timeout, this::sendGet);
            }
            case "HEAD" -> {
                return sendRequestBenchmark(url, times, timeout, this::sendHead);
            }

        }
        return null;
    }
    public HttpBenchmarkResult sendRequest(String url, String jsonBody,  int times, int timeout, String name) {
        switch (name) {
            case "POST" -> {
                return sendRequestBenchmark(url, times, timeout, jsonBody, this::sendPostJson);
            }

            default -> {
                return null;
            }
        }
    }

    private HttpBenchmarkResult sendRequestBenchmark(
            String url, int times, int timeout, Function<String, HttpResultData> function) {

        List<Long> timings = new ArrayList<>();
        int success = 0;

        long benchmarkStart = System.currentTimeMillis();

        for (int i = 0; i < times; i++) {
            long iterationStart = System.nanoTime();
            HttpResultData response = function.apply(url);
            long elapsed = (System.nanoTime() - iterationStart) / 1_000_000; // ms
            timings.add(elapsed);

            if (response.statusCode() == 200) {
                success++;
            }

            long totalElapsed = System.currentTimeMillis() - benchmarkStart;
            if (timeout != 0 && totalElapsed > timeout) {
                System.out.println("Benchmark timed out after " + totalElapsed + " ms");
                break;
            }
        }

        long min = timings.stream().min(Long::compareTo).orElse(0L);
        long max = timings.stream().max(Long::compareTo).orElse(0L);
        double avg = timings.stream().mapToLong(Long::longValue).average().orElse(0.0);

        return new HttpBenchmarkResult(timings.size(), success, min, max, avg);
    }

    private HttpBenchmarkResult sendRequestBenchmark(
            String url, int times, int timeout, String jsonBody,
            BiFunction<String, String, HttpResultData> function) {

        List<Long> timings = new ArrayList<>();
        int success = 0;

        long benchmarkStart = System.currentTimeMillis();

        for (int i = 0; i < times; i++) {
            long iterationStart = System.nanoTime();
            HttpResultData response = function.apply(url, jsonBody);
            long elapsed = (System.nanoTime() - iterationStart) / 1_000_000; // ms
            timings.add(elapsed);

            if (response.statusCode() == 200) {
                success++;
            }

            long totalElapsed = System.currentTimeMillis() - benchmarkStart;
            if (timeout != 0 && totalElapsed > timeout) {
                System.out.println("Benchmark timed out after " + totalElapsed + " ms");
                break;
            }
        }

        long min = timings.stream().min(Long::compareTo).orElse(0L);
        long max = timings.stream().max(Long::compareTo).orElse(0L);
        double avg = timings.stream().mapToLong(Long::longValue).average().orElse(0.0);

        return new HttpBenchmarkResult(timings.size(), success, min, max, avg);
    }

}