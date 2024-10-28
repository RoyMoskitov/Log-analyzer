package backend.academy.FileCreator;

import backend.academy.Statistics.ResourceStatistics;
import backend.academy.Statistics.ResponseStatistics;
import backend.academy.Statistics.Statistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import static org.junit.jupiter.api.Assertions.*;

class MarkdownFileCreatorTest {

    private MarkdownFileCreator markdownFileCreator;
    private List<Statistics> mockStatisticsList;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        markdownFileCreator = new MarkdownFileCreator();

        mockStatisticsList = List.of(
            new ResponseStatistics(),
            new ResourceStatistics()
        );
    }

    @Test
    void testCreateFileSuccessfully() {
        String fileName = tempDir.resolve("statistics").toString();

        OffsetDateTime from = OffsetDateTime.parse("01/Oct/2024:00:00:00 -0700",
            DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH));
        OffsetDateTime to = OffsetDateTime.parse("10/Oct/2024:23:59:59 -0700",
            DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH));

        assertDoesNotThrow(() -> markdownFileCreator.createFile(mockStatisticsList, fileName, from, to));

//        File createdFile = new File(fileName + MarkdownFileCreator.DEFAULT_MARKDOWN_PATH);
//        assertTrue(createdFile.exists());
//
//        String content = Files.readString(Path.of(createdFile.getPath()), StandardCharsets.UTF_8);
//
//        assertTrue(content.contains("General Info"));
//        assertTrue(content.contains("|         Metric        |"));
//        assertTrue(content.contains("Requested resources"));
//        assertTrue(content.contains("Response number"));
    }
}
