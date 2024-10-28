package backend.academy.LogMapping;

import backend.academy.Statistics.ResourceStatistics;
import backend.academy.Statistics.ResponseStatistics;
import backend.academy.Statistics.Statistics;
import backend.academy.Statistics.StatusStatistics;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogHandler {

    public static final List<Statistics> STATISTICS_LIST = List.of(new ResponseStatistics(),
        new ResourceStatistics(), new StatusStatistics());
    public static final int MAX_INVALID_STRINGS = 5;
    private static final String LOG_PATTERN = "(.+?) - (\\S+) \\[(.+?)\\] "
        + "\"((POST|GET|PUT|PATCH|DELETE|HEAD) (.+?) (.+?))\" (\\d{3}) (\\d+) \"(\\S+?)\" \"(.+?)\"";
    private static final Pattern PATTERN = Pattern.compile(LOG_PATTERN);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z")
        .withLocale(Locale.ENGLISH);

    private LogHandler() {
    }

    public static void processStream(
        InputStream inputStream, OffsetDateTime from,
        OffsetDateTime to, String filterField, String filterValue
    ) throws IOException {
        List<String> invalidLines = new ArrayList<>();
        int mistakeCounter = 0;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Matcher matcher = PATTERN.matcher(line);
                if (matcher.matches()) {
                    Log log = mapToLog(matcher);
                    if (log.timeLocal().isAfter(to) || log.timeLocal().isBefore(from)
                        || (filterField != null && !filterValidation(log, filterField, filterValue))) {
                        continue;
                    }
                    for (var statistics : STATISTICS_LIST) {
                        statistics.update(log);
                    }
                } else {
                    ++mistakeCounter;
                    invalidLines.add(line);
                    if (mistakeCounter > MAX_INVALID_STRINGS) {
                        throw new IllegalArgumentException(
                            "Most likely, file format is incorrect. Invalid lines: " + invalidLines);

                    }
                }
            }
        }

    }

    private static boolean filterValidation(Log log, String filterField, String filterValue) {
        switch (filterField) {
            case "REMOTE_ADDRESS" -> {
                return log.remoteAddr().equals(filterValue);
            }
            case "STATUS" -> {
                return log.status().equals(Integer.valueOf(filterValue));
            }
            case "METHOD" -> {
                return log.requestType() == RequestType.valueOf(filterValue);
            }
            case "BYTES_SENT" -> {
                return log.bodyBytesSent().equals(Integer.valueOf(filterValue));
            }
            default -> throw new IllegalArgumentException("Filter value is not included in the list");
        }
    }

    @SuppressWarnings("MagicNumber")
    private static Log mapToLog(Matcher matcher) {
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(matcher.group(3), DATE_FORMATTER);
        OffsetDateTime offsetDateTime = zonedDateTime.toOffsetDateTime();
        return new Log.LogBuilder()
            .remoteAddr(matcher.group(1))
            .remoteUser(matcher.group(2))
            .timeLocal(offsetDateTime)
            .requestType(RequestType.valueOf(matcher.group(5)))
            .requestResource(matcher.group(6))
            .httpVersion(matcher.group(7))
            .status(Integer.valueOf(matcher.group(8)))
            .bodyBytesSent(Integer.valueOf(matcher.group(9)))
            .httpReferer(matcher.group(10))
            .httpUserAgent(matcher.group(11))
            .build();
    }
}
