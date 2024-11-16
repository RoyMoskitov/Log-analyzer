package backend.academy.FileCreator;

import backend.academy.Statistics.Statistics;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * The FileCreator interface defines methods for creating files
 * that contain statistical information.
 */
public interface FileCreator {
    String DEFAULT_ADOC_PATH = "-log-statistics.adoc";
    String DEFAULT_MARKDOWN_PATH = "-log-statistics.md";
    Integer GENERAL_INFO_SECOND_COLUMN_LENGTH = 70;

    /**
     * Creates a file based on the provided list of statistics.
     *
     * @param statisticsList A list of Statistics objects with data.
     * @param fileName The name of the file to be created.
     * @param from The start date and time of the period for which statistics are collected.
     * @param to The end date and time of the period for which statistics are collected.
     */
    void createFile(List<Statistics> statisticsList, List<String> fileName, OffsetDateTime from, OffsetDateTime to);

    /**
     * Creates the first part of a table with general information about the statistics.
     *
     * @return A string representing the first part of the table with general information.
     */
    @SuppressWarnings("MultipleStringLiterals")
    default String createFirstTablePart(List<Statistics> statisticsList,
        String fileName, OffsetDateTime from, OffsetDateTime to) {
        return "|         File          |  " + statisticsList.get(0)
                .centerText(fileName, GENERAL_INFO_SECOND_COLUMN_LENGTH) + "  |\n"
            + "|      Start date       |  " + statisticsList.get(0)
                .centerText(from.toString(), GENERAL_INFO_SECOND_COLUMN_LENGTH) + "  |\n"
            + "|       End date        |  " + statisticsList.get(0)
                .centerText(to.toString(), GENERAL_INFO_SECOND_COLUMN_LENGTH) + "  |\n";
    }
}
