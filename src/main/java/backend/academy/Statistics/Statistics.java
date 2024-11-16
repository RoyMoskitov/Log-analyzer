package backend.academy.Statistics;

import backend.academy.LogMapping.Log;

/**
 * The Statistics interface defines methods for updating statistics based on log entries
 * and writing the results in AsciiDoc or Markdown format.
 */
public interface Statistics {
    void update(Log log);

    String writeStatistics(String fileType);

    default String centerText(String text, int length) {
        if (text.length() >= length) return  " " + text + " ";
        int padding = (length - text.length()) / 2;
        String paddingLeft = " ".repeat(padding);
        String paddingRight = " ".repeat(length - padding - text.length());
        return paddingLeft + text + paddingRight;
    }
}
