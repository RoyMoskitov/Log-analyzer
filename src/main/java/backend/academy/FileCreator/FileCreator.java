package backend.academy.FileCreator;

import backend.academy.Statistics.Statistics;
import java.time.OffsetDateTime;
import java.util.List;

public interface FileCreator {
    String DEFAULT_ADOC_PATH = "-log-statistics.adoc";
    String DEFAULT_MARKDOWN_PATH = "-log-statistics.md";
    //this parameter must be equal with one in ResponseStatistics
    Integer GENERAL_INFO_SECOND_COLUMN_LENGTH = 70;

    void createFile(List<Statistics> statisticsList, String fileName, OffsetDateTime from, OffsetDateTime to);

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
