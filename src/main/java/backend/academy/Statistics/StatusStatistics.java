package backend.academy.Statistics;

import backend.academy.LogMapping.Log;
import java.util.HashMap;
import java.util.Map;

/**
 * The StatusStatistics class implements the Statistics interface to track the frequency
 * of HTTP status codes in logs. It stores the status codes and their occurrences.*
 */
public class StatusStatistics implements Statistics {
    public static final Integer FIRST_COLUMN_LENGTH = 11;
    public static final Integer SECOND_COLUMN_LENGTH = 26;
    public static final Integer THIRD_COLUMN_LENGTH = 9;

    Map<Integer, Integer> responseStatusCode = new HashMap<>();

    @Override
    public void update(Log log) {
        responseStatusCode.put(log.status(),
            responseStatusCode.getOrDefault(log.status(), 0) + 1);
    }

    @Override
    @SuppressWarnings("MultipleStringLiterals")
    public String writeStatistics(String fileType) {
        StringBuilder content = new StringBuilder();

        if ("adoc".equals(fileType)) {
            content.append("=== Code statistics\n").append("|===\n");
            content.append("| Status-code |            Name            | Frequency |\n");

            for (Map.Entry<Integer, Integer> entry : responseStatusCode.entrySet()) {
                content.append(formatRowForTable(entry.getKey(), entry.getValue()));
            }

            content.append("|===\n\n");
        } else if ("markdown".equals(fileType)) {
            content.append("#### Code statistics\n\n");
            content.append("| Status-code |            Name            | Frequency |\n");
            content.append("|:-----------:|:--------------------------:|:---------:|\n");

            for (Map.Entry<Integer, Integer> entry : responseStatusCode.entrySet()) {
                content.append(formatRowForTable(entry.getKey(), entry.getValue()));
            }
        }
        return content.toString();
    }

    @SuppressWarnings("MultipleStringLiterals")
    private String formatRowForTable(Integer statusCode, Integer frequency) {
        String name = HttpStatusCodes.getHttpStatusMap().get(statusCode);
        return "| " + centerText(statusCode.toString(), FIRST_COLUMN_LENGTH) + " | "
            + centerText(name, SECOND_COLUMN_LENGTH) + " | "
            + centerText(frequency.toString(), THIRD_COLUMN_LENGTH) + " |\n";
    }
}
