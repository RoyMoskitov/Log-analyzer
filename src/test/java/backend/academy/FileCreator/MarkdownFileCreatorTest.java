package backend.academy.FileCreator;

import backend.academy.Statistics.ResourceStatistics;
import backend.academy.Statistics.ResponseStatistics;
import backend.academy.Statistics.Statistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
        List<String> fileNames = new ArrayList<>();
        String fileName = tempDir.resolve("statistics").toString();
        fileNames.add(fileName);

        OffsetDateTime from = OffsetDateTime.parse("01/Oct/2024:00:00:00 -0700",
            DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH));
        OffsetDateTime to = OffsetDateTime.parse("10/Oct/2024:23:59:59 -0700",
            DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH));

        assertDoesNotThrow(() -> markdownFileCreator.createFile(mockStatisticsList, fileNames, from, to));
    }
}
