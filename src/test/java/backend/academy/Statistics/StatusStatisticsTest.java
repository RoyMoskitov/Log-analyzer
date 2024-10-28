package backend.academy.Statistics;

import backend.academy.LogMapping.Log;
import backend.academy.LogMapping.RequestType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import static org.junit.jupiter.api.Assertions.*;

class StatusStatisticsTest {

    Log log1;
    Log log2;
    Log log3;
    Statistics statusStatistics = new StatusStatistics();

    @BeforeEach
    void BeforeEach() {
        statusStatistics = new StatusStatistics();
        OffsetDateTime log1Time = OffsetDateTime.parse("09/Oct/2000:13:55:36 -0700",
            DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH));
        OffsetDateTime log2Time = OffsetDateTime.parse("11/Oct/2000:13:55:36 -0700",
            DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH));
        log1 = new Log.LogBuilder()
            .remoteAddr("12.2.2")
            .remoteUser("Matt")
            .timeLocal(log1Time)
            .requestType(RequestType.GET)
            .requestResource("doc/users")
            .httpVersion("HTTP 1.3")
            .status(404)
            .bodyBytesSent(300)
            .httpReferer("-")
            .httpUserAgent("-")
            .build();
        log2 = new Log.LogBuilder()
            .remoteAddr("12.2.3")
            .remoteUser("Dan")
            .timeLocal(log2Time)
            .requestType(RequestType.POST)
            .requestResource("music")
            .httpVersion("HTTP 1.3")
            .status(102)
            .bodyBytesSent(400)
            .httpReferer("-")
            .httpUserAgent("-")
            .build();
        log3 = new Log.LogBuilder()
            .remoteAddr("12.2.4")
            .remoteUser("Ann")
            .timeLocal(log1Time)
            .requestType(RequestType.GET)
            .requestResource("doc/users")
            .httpVersion("HTTP 1.5")
            .status(303)
            .bodyBytesSent(500)
            .httpReferer("-")
            .httpUserAgent("-")
            .build();
    }

    @Test
    void testWritingInAdoc() {
        statusStatistics.update(log1);
        statusStatistics.update(log2);
        statusStatistics.update(log3);

        assertTrue(statusStatistics.writeStatisticsInADoc()
            .contains("|     404     |         Not Found          |     1     |"));
        assertTrue(statusStatistics.writeStatisticsInADoc()
            .contains("|     102     |         Processing         |     1     |"));
        assertTrue(statusStatistics.writeStatisticsInADoc()
            .contains("|     303     |         See Other          |     1     |"));
    }

    @Test
    void testWritingInMarkdown() {
        statusStatistics.update(log1);
        statusStatistics.update(log2);
        statusStatistics.update(log3);

        assertTrue(statusStatistics.writeStatisticsInMarkdown()
            .contains("|     404     |         Not Found          |     1     |"));
        assertTrue(statusStatistics.writeStatisticsInMarkdown()
            .contains("|     102     |         Processing         |     1     |"));
        assertTrue(statusStatistics.writeStatisticsInMarkdown()
            .contains("|     303     |         See Other          |     1     |"));
    }
}
