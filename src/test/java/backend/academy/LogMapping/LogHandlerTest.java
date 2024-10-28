package backend.academy.LogMapping;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import static org.junit.jupiter.api.Assertions.*;

class LogHandlerTest {

    @Test
    void testProcessStreamCorrectLogs() {
        String logs = """
            62.75.198.180 - - [17/May/2015:10:05:36 +0000] "GET /downloads/product_2 HTTP/1.1" 304 0 "-" "Debian APT-HTTP/1.3 (0.9.7.9)"
            144.76.137.134 - - [17/May/2015:10:05:03 +0000] "GET /downloads/product_1 HTTP/1.1" 304 0 "-" "Debian APT-HTTP/1.3 (0.9.7.9)"
            80.91.33.133 - - [17/May/2015:10:05:23 +0000] "GET /downloads/product_1 HTTP/1.1" 404 340 "-" "Debian APT-HTTP/1.3 (0.8.16~exp12ubuntu10.16)"
            54.86.157.236 - - [17/May/2015:10:05:11 +0000] "GET /downloads/product_1 HTTP/1.1" 404 339 "-" "Debian APT-HTTP/1.3 (0.8.16~exp12ubuntu10.20.1)"
            80.70.214.71 - - [17/May/2015:10:05:22 +0000] "GET /downloads/product_2 HTTP/1.1" 404 326 "-" "Wget/1.13.4 (linux-gnu)\"""";

        OffsetDateTime from = OffsetDateTime.parse("01/May/2000:00:00:00 -0700",
            DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH));
        OffsetDateTime to = OffsetDateTime.parse("23/May/2000:23:59:59 -0700",
            DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH));

        ByteArrayInputStream inputStream = new ByteArrayInputStream(logs.getBytes(StandardCharsets.UTF_8));

        assertDoesNotThrow(() -> LogHandler.processStream(inputStream, from, to, null, null));
    }

    @Test
    void testProcessStreamMaxInvalidStrings() {
        String invalidLogs = """
            62.75.198.180 - - [17/May/2015:10:05:36 +0000] "MAX /downloads/product_2 HTTP/1.1" 304 0 "-" "Debian APT-HTTP/1.3 (0.9.7.9)"
            144.76.137.134 - - [17/May/2015:10:05:03 +0000 "GET 304 0 "-" "Debian APT-HTTP/1.3 (0.9.7.9)"
            salkfd, fdksmf mds, ds - - [17/May/2015:10:05:23 +0000] "GET /downloads/product_1 HTTP/1.1" 404 340 "-"
            54.86.157.236 - - [17/May/2015:10:05:11 +0000] "GET /downloads/product_1 HTTP/1.1" 404 339 "-" Debian APT-HTTP/1.3 (0.8.16~exp12ubuntu10.20.1)"
            - - [17/May/2015:10:05:22 +0000] "GET /downloads/product_2 HTTP/1.1" 404 326 "-" "Wget/1.13.4 (linux-gnu)"
            salkfd, fdksmf mds, ds - - [17/May/2015:10:05:23 +0000] "GET /downloads/product_1 HTTP/1.1" 404 340 "-"
            """;

        OffsetDateTime from = OffsetDateTime.parse("01/May/2000:00:00:00 -0700",
            DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH));
        OffsetDateTime to = OffsetDateTime.parse("23/May/2000:23:59:59 -0700",
            DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH));

        ByteArrayInputStream inputStream = new ByteArrayInputStream(invalidLogs.getBytes(StandardCharsets.UTF_8));

        assertThrows(IllegalArgumentException.class, () -> LogHandler.processStream(inputStream, from, to, null, null));
    }

    @Test
    void testInvalidFilterField() {
        String logs = "12.34.56.78 - - [10/Oct/2000:13:55:36 -0700] \"GET /index.html HTTP/1.0\" 200 1234 \"-\" \"Mozilla/4.08\"";

        OffsetDateTime from = OffsetDateTime.parse("09/Oct/2000:00:00:00 -0700",
            DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH));
        OffsetDateTime to = OffsetDateTime.parse("12/Oct/2000:23:59:59 -0700",
            DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH));

        ByteArrayInputStream inputStream = new ByteArrayInputStream(logs.getBytes(StandardCharsets.UTF_8));

        assertThrows(IllegalArgumentException.class, () -> LogHandler.processStream(inputStream, from, to, "INVALID_FIELD", "some_value"));
    }
}
