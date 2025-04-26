import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpRequestWrapper {

    private final HttpClient client;

    public HttpRequestWrapper() {
        this.client = HttpClient.newHttpClient();
    }

    public HttpResultData sendGet(String url, Duration timeout) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(timeout)
                .GET()
                .build();

        try {
            return getHttpResponseData(request);
        } catch (Exception e) {
            if (e instanceof HttpTimeoutException) {
                System.out.println("Request timed out after " + timeout.toMillis() + " ms");
                return new HttpResultData(408, new HashMap<>(), "Request Timeout");
            }
            throw new RuntimeException("GET request failed", e);
        }
    }


    public HttpResultData sendHead(String url, Duration timeout) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(timeout)
                .HEAD()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Map<String, String> headerMap = new HashMap<>();
            response.headers().map().forEach((key, values) -> headerMap.put(key, String.join(", ", values)));
            return new HttpResultData(response.statusCode(), headerMap, "");
        } catch (Exception e) {
            if (e instanceof HttpTimeoutException) {
                System.out.println("Request timed out after " + timeout.toMillis() + " ms");
                return new HttpResultData(408, new HashMap<>(), "Request Timeout");
            }
            throw new RuntimeException("HEAD request failed", e);
        }
    }

    public HttpResultData sendPost(String url, String jsonBody, Duration timeout) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .timeout(timeout)
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        try {
            return getHttpResponseData(request);
        } catch (Exception e) {
            if (e instanceof HttpTimeoutException) {
                System.out.println("Request timed out after " + timeout.toMillis() + " ms");
                return new HttpResultData(408, new HashMap<>(), "Request Timeout");
            }
            throw new RuntimeException("POST request failed", e);
        }
    }

    public HttpResultData sendPut(String url, String jsonBody, Duration timeout) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .timeout(timeout)
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        try {
            return getHttpResponseData(request);
        } catch (Exception e) {
            if (e instanceof HttpTimeoutException) {
                System.out.println("Request timed out after " + timeout.toMillis() + " ms");
                return new HttpResultData(408, new HashMap<>(), "Request Timeout");
            }
            throw new RuntimeException("PUT request failed", e);
        }
    }

    public HttpResultData sendDelete(String url, Duration timeout) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(timeout)
                .DELETE()
                .build();

        try {
            return getHttpResponseData(request);
        } catch (Exception e) {
            if (e instanceof HttpTimeoutException) {
                System.out.println("Request timed out after " + timeout.toMillis() + " ms");
                return new HttpResultData(408, new HashMap<>(), "Request Timeout");
            }
            throw new RuntimeException("DELETE request failed", e);
        }
    }


    private HttpResultData getHttpResponseData(HttpRequest request) throws IOException, InterruptedException {
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Map<String, String> headerMap = new HashMap<>();
        response.headers().map().forEach((key, values) -> headerMap.put(key, String.join(", ", values)));
        return new HttpResultData(response.statusCode(), headerMap, response.body());
    }


    public HttpBenchmarkResult sendRequestBenchmark(
            String url, int times, Duration timeout, String name) {

        List<Long> timings = new ArrayList<>();
        int success = 0;

        for (int i = 0; i < times; i++) {
            long iterationStart = System.nanoTime();
            HttpResultData response;

            switch (name) {
                case "GET" -> {
                    response = sendGet(url, timeout);
                }
                case "HEAD" -> {
                    response = sendHead(url, timeout);
                }

                case "DELETE" -> {
                    response = sendDelete(url, timeout);
                }

                default -> {
                    throw new RuntimeException("Unsupported method: " + name);
                }
            }

            long elapsed = (System.nanoTime() - iterationStart) / 1_000_000; // ms
            timings.add(elapsed);

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                success++;
            }
        }

        return getHttpBenchmarkResult(timings, success);
    }

    public HttpBenchmarkResult sendRequestBenchmark(
            String url, int times, Duration timeout, String jsonBody,
            String name) {

        List<Long> timings = new ArrayList<>();
        int success = 0;

        for (int i = 0; i < times; i++) {
            long iterationStart = System.nanoTime();
            HttpResultData response;
            switch (name) {
                case "POST" -> {
                    response = sendPost(url, jsonBody, timeout);
                }

                case "PUT" -> {
                    response = sendPut(url, jsonBody, timeout);
                }
                default -> {
                    throw new RuntimeException("Unsupported method: " + name);
                }
            }
            long elapsed = (System.nanoTime() - iterationStart) / 1_000_000; // ms
            timings.add(elapsed);

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                success++;
            }
        }

        return getHttpBenchmarkResult(timings, success);
    }

    private HttpBenchmarkResult getHttpBenchmarkResult(List<Long> timings, int success) {
        long min = timings.stream().min(Long::compareTo).orElse(0L);
        long max = timings.stream().max(Long::compareTo).orElse(0L);
        double avg = timings.stream().mapToLong(Long::longValue).average().orElse(0.0);

        return new HttpBenchmarkResult(timings.size(), success, min, max, avg);
    }

}