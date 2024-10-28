package backend.academy.Statistics;

import backend.academy.LogMapping.Log;
import java.util.HashMap;
import java.util.Map;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ResourceStatistics implements Statistics {
    public static final Integer FIRST_COLUMN_LENGTH = 24;
    public static final Integer SECOND_COLUMN_LENGTH = 9;
    Map<String, Integer> responseNumber = new HashMap<>();

    @Override
    public void update(Log log) {
        responseNumber.put(log.requestResource(),
            responseNumber.getOrDefault(log.requestResource(), 0) + 1);
    }

    @SuppressWarnings("MultipleStringLiterals")
    @Override
    public String writeStatisticsInADoc() {
        StringBuilder content = new StringBuilder();
        content.append("=== Requested resources\n").append("|===\n");
        content.append("|         Resource         | Frequency |\n");
        for (Map.Entry<String, Integer> entry : responseNumber.entrySet()) {
            content.append(formatRowForTable(entry.getKey(), entry.getValue()));
        }
        content.append("|===\n\n");
        return content.toString();
    }

    @Override
    public String writeStatisticsInMarkdown() {
        StringBuilder content = new StringBuilder();
        content.append("#### Requested resources\n\n");
        content.append("|         Resource         | Frequency |\n");
        content.append("|:------------------------:|:---------:|\n");
        for (Map.Entry<String, Integer> entry : responseNumber.entrySet()) {
            content.append(formatRowForTable(entry.getKey(), entry.getValue()));
        }
        return content.toString();
    }

    private String formatRowForTable(String resource, Integer frequency) {
        return "| " + centerText(resource, FIRST_COLUMN_LENGTH) + " | "
            + centerText(frequency.toString(), SECOND_COLUMN_LENGTH) + " |\n";
    }
}
