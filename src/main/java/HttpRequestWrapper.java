import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpRequestWrapper {

    private final HttpClient client;

    public HttpRequestWrapper() {
        this.client = HttpClient.newHttpClient();
    }

    public HttpResponseData sendGet(String url) {
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

    public HttpResponseData sendHead(String url) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .method("HEAD", HttpRequest.BodyPublishers.noBody())
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Map<String, String> headerMap = new HashMap<>();
            response.headers().map().forEach((key, values) -> headerMap.put(key, String.join(", ", values)));
            return new HttpResponseData(response.statusCode(), headerMap, "");
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("HEAD request failed", e);
        }
    }

    public HttpResponseData sendPostJson(String url, String jsonBody) {
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

    private HttpResponseData getHttpResponseData(HttpRequest request) throws IOException, InterruptedException {
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Map<String, String> headerMap = new HashMap<>();
        response.headers().map().forEach((key, values) -> headerMap.put(key, String.join(", ", values)));
        return new HttpResponseData(response.statusCode(), headerMap, response.body());
    }

    public HttpBenchmarkResult benchmarkGet(String url, int times) {
        List<Long> timings = new ArrayList<>();
        int success = 0;

        for (int i = 0; i < times; i++) {
            long start = System.nanoTime();
            HttpResponseData response = sendGet(url);
            long elapsed = (System.nanoTime() - start) / 1_000_000; // ms
            timings.add(elapsed);

            if (response.statusCode() == 200) {
                success++;
            }
        }

        long min = timings.stream().min(Long::compareTo).orElse(0L);
        long max = timings.stream().max(Long::compareTo).orElse(0L);
        double avg = timings.stream().mapToLong(Long::longValue).average().orElse(0.0);

        return new HttpBenchmarkResult(times, success, min, max, avg);
    }

}