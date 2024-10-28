package backend.academy.Statistics;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("MagicNumber")
public class HttpStatusCodes {

    private static final Integer INITIAL_CAPACITY = 50;
    private static final Map<Integer, String> HTTP_STATUS_MAP;

    static {
        Map<Integer, String> tempMap = new HashMap<>(INITIAL_CAPACITY);

        // 1xx: Informational
        tempMap.put(100, "Continue");
        tempMap.put(101, "Switching Protocols");
        tempMap.put(102, "Processing");

        // 2xx: Success
        tempMap.put(200, "OK");
        tempMap.put(201, "Created");
        tempMap.put(202, "Accepted");
        tempMap.put(203, "Non-Authoritative Information");
        tempMap.put(204, "No Content");
        tempMap.put(205, "Reset Content");
        tempMap.put(206, "Partial Content");
        tempMap.put(207, "Multi-Status");
        tempMap.put(208, "Already Reported");
        tempMap.put(226, "IM Used");

        // 3xx: Redirection
        tempMap.put(300, "Multiple Choices");
        tempMap.put(301, "Moved Permanently");
        tempMap.put(302, "Found");
        tempMap.put(303, "See Other");
        tempMap.put(304, "Not Modified");
        tempMap.put(305, "Use Proxy");
        tempMap.put(307, "Temporary Redirect");
        tempMap.put(308, "Permanent Redirect");

        // 4xx: Client Error
        tempMap.put(400, "Bad Request");
        tempMap.put(401, "Unauthorized");
        tempMap.put(402, "Payment Required");
        tempMap.put(403, "Forbidden");
        tempMap.put(404, "Not Found");
        tempMap.put(405, "Method Not Allowed");
        tempMap.put(406, "Not Acceptable");
        tempMap.put(407, "Proxy Authentication Required");
        tempMap.put(408, "Request Timeout");
        tempMap.put(409, "Conflict");
        tempMap.put(410, "Gone");
        tempMap.put(411, "Length Required");
        tempMap.put(412, "Precondition Failed");
        tempMap.put(413, "Payload Too Large");
        tempMap.put(414, "URI Too Long");
        tempMap.put(415, "Unsupported Media Type");
        tempMap.put(416, "Range Not Satisfiable");
        tempMap.put(417, "Expectation Failed");
        tempMap.put(418, "I'm a teapot");
        tempMap.put(421, "Misdirected Request");
        tempMap.put(422, "Unprocessable Entity");
        tempMap.put(423, "Locked");
        tempMap.put(424, "Failed Dependency");
        tempMap.put(425, "Too Early");
        tempMap.put(426, "Upgrade Required");
        tempMap.put(428, "Precondition Required");
        tempMap.put(429, "Too Many Requests");
        tempMap.put(431, "Request Header Fields Too Large");
        tempMap.put(451, "Unavailable For Legal Reasons");

        // 5xx: Server Error
        tempMap.put(500, "Internal Server Error");
        tempMap.put(501, "Not Implemented");
        tempMap.put(502, "Bad Gateway");
        tempMap.put(503, "Service Unavailable");
        tempMap.put(504, "Gateway Timeout");
        tempMap.put(505, "HTTP Version Not Supported");
        tempMap.put(506, "Variant Also Negotiates");
        tempMap.put(507, "Insufficient Storage");
        tempMap.put(508, "Loop Detected");
        tempMap.put(510, "Not Extended");
        tempMap.put(511, "Network Authentication Required");

        HTTP_STATUS_MAP = Collections.unmodifiableMap(tempMap);
    }

    private HttpStatusCodes() {}

    public static Map<Integer, String> getHttpStatusMap() {
        return HTTP_STATUS_MAP;
    }
}
