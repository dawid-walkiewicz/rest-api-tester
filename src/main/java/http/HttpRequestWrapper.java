package http;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.time.Duration;
import java.util.HashMap;
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

}