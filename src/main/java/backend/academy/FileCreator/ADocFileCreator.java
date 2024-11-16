package backend.academy.FileCreator;

import backend.academy.Statistics.Statistics;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * The ADocFileCreator class is responsible for creating a ADoc file
 * containing statistical data presented in tables. It implements the FileCreator
 * interface and writes the statistics to a file, formatting them in a readable
 * ADoc structure with general information and detailed statistics.
 */
public class ADocFileCreator implements FileCreator {
    @Override
    @SuppressWarnings("MultipleStringLiterals")
    public void createFile(
        List<Statistics> statisticsList, List<String> fileNames,
        OffsetDateTime from, OffsetDateTime to
    ) {

        StringBuilder resultContent = new StringBuilder();
        StringBuilder validFileNames = new StringBuilder();

        //creating string for name of new file & for all file names in one str
        String validFileName = fileNames.get(fileNames.size() - 1).split("/", -1)
            [fileNames.get(fileNames.size() - 1).split("/", -1).length - 1];
        for (var fileName : fileNames) {
            validFileNames.append(fileName.split("/", -1)[fileName.split("/", -1).length - 1]);
            if (!fileName.equals(fileNames.get(fileNames.size() - 1))) {
                validFileNames.append(", ");
            }
        }

        resultContent.append("=== General Info\n\n")
            .append("|===\n")
            .append("|         Metric        |  ").append(statisticsList.get(0)
                .centerText("Value", GENERAL_INFO_SECOND_COLUMN_LENGTH)).append("  |\n")
            .append(createFirstTablePart(statisticsList, validFileNames.toString(), from, to));

        for (var statistics : statisticsList) {
            resultContent.append(statistics.writeStatistics("adoc"));
        }
        try (FileWriter writer = new FileWriter(validFileName + DEFAULT_ADOC_PATH,
            StandardCharsets.UTF_8)) {
            writer.write(resultContent.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
