package http;

import java.util.Map;

public record HttpResultData(int statusCode, Map<String, String> headers, String body) {

    @Override
    public String toString() {
        return "HttpResponseData {\n" +
                "  statusCode = " + statusCode + ",\n" +
                "  headers = " + headers + ",\n" +
                "  body = \n" + body + "\n" +
                '}';
    }
}