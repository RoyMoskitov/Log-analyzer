package backend.academy.FileCreator;

import backend.academy.Statistics.Statistics;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.List;

public class ADocFileCreator implements FileCreator {
    @Override
    @SuppressWarnings("MultipleStringLiterals")
    public void createFile(List<Statistics> statisticsList, String fileName, OffsetDateTime from, OffsetDateTime to) {
        StringBuilder resultContent = new StringBuilder();
        String validFileName = fileName.split("/", -1)[fileName.split("/", -1).length - 1];
        resultContent.append("=== General Info\n\n")
            .append("|===\n")
            .append("|         Metric        |  ").append(statisticsList.get(0)
                .centerText("Value", GENERAL_INFO_SECOND_COLUMN_LENGTH)).append("  |\n")
            .append(createFirstTablePart(statisticsList, validFileName, from, to));

        for (var statistics : statisticsList) {
            resultContent.append(statistics.writeStatisticsInADoc());
        }
        try (FileWriter writer = new FileWriter(validFileName + DEFAULT_ADOC_PATH,
            StandardCharsets.UTF_8)) {
            writer.write(resultContent.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
