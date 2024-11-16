package backend.academy.FileCreator;

import backend.academy.Statistics.Statistics;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * The MarkdownFileCreator class is responsible for creating a Markdown file
 * containing statistical data presented in tables. It implements the FileCreator
 * interface and writes the statistics to a file, formatting them in a readable
 * Markdown structure with general information and detailed statistics.
 */
public class MarkdownFileCreator implements FileCreator {

    @SuppressWarnings("MultipleStringLiterals")
    @Override
    public void createFile(List<Statistics> statisticsList, List<String> fileNames,
        OffsetDateTime from, OffsetDateTime to) {

        StringBuilder resultContent = new StringBuilder();
        StringBuilder validFileNames = new StringBuilder();

        //creating string for name of new file & for all file names in one str
        String validFileName = fileNames.get(fileNames.size() - 1).split("/", -1)
            [fileNames.get(fileNames.size() - 1).split("/", -1).length - 1];
        for (var fileName : fileNames) {
            validFileNames.append(fileName.split("/", -1)[fileName.split("/", -1).length - 1]);
            if (!fileName.equals(fileNames.get(fileNames.size()-1))) {
                validFileNames.append(", ");
            }
        }

        String padding = "-".repeat(GENERAL_INFO_SECOND_COLUMN_LENGTH);
        resultContent.append("#### General Info\n\n")
            .append("|         Metric        |  ").append(statisticsList.get(0)
                .centerText("Value", GENERAL_INFO_SECOND_COLUMN_LENGTH)).append("  |\n")
            .append("|:---------------------:|: ").append(padding).append(" :|\n")
            .append(createFirstTablePart(statisticsList, validFileNames.toString(), from, to));
        for (var statistics : statisticsList) {
            resultContent.append(statistics.writeStatisticsInMarkdown());
        }
        try (FileWriter writer = new FileWriter(validFileName + DEFAULT_MARKDOWN_PATH,
            StandardCharsets.UTF_8)) {
            writer.write(resultContent.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
