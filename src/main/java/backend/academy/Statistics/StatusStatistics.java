package backend.academy.Statistics;

import backend.academy.LogMapping.Log;
import java.util.HashMap;
import java.util.Map;

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
    public String writeStatisticsInADoc() {
        StringBuilder content = new StringBuilder();
        content.append("=== Code statistics\n").append("|===\n");
        content.append("| Status-code |            Name            | Frequency |\n");

        for (Map.Entry<Integer, Integer> entry : responseStatusCode.entrySet()) {
            content.append(formatRowForTable(entry.getKey(), entry.getValue()));
        }

        content.append("|===\n\n");
        return content.toString();
    }

    @Override
    public String writeStatisticsInMarkdown() {
        StringBuilder content = new StringBuilder();
        content.append("#### Code statistics\n\n");
        content.append("| Status-code |            Name            | Frequency |\n");
        content.append("|:-----------:|:--------------------------:|:---------:|\n");

        for (Map.Entry<Integer, Integer> entry : responseStatusCode.entrySet()) {
            content.append(formatRowForTable(entry.getKey(), entry.getValue()));
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
