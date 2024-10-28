package backend.academy.Statistics;

import backend.academy.LogMapping.Log;

public interface Statistics {
    void update(Log log);

    String writeStatisticsInADoc();

    String writeStatisticsInMarkdown();

    default String centerText(String text, int length) {
        int padding = (length - text.length()) / 2;
        String paddingLeft = " ".repeat(padding);
        String paddingRight = " ".repeat(length - padding - text.length());
        return paddingLeft + text + paddingRight;
    }
}
