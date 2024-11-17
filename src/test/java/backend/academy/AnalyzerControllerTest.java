package backend.academy;

import backend.academy.Controller.AnalyzerController;
import backend.academy.FileCreator.FileCreator;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AnalyzerControllerTest {

    @Test
    void testInvalidDateFormatThrowsException() {
        String[] args = {
            "--path", "file:///path/to/log.txt",
            "--from", "invalid_date",
            "--to", "10/Oct/2024:23:59:59 -0700",
            "--format", "adoc"
        };

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(output);

        AnalyzerController.processAnalysis(args, printStream);
        assertTrue(output.toString().contains("Parsing failed. Reason: Invalid date format"));
        assertTrue(output.toString().contains("usage: LogAnalyzer"));
    }

    @Test
    void testInvalidFilterFieldThrowsException() {
        String[] args = {
            "--path", "file:///path/to/log.txt",
            "--from", "01/Oct/2024:00:00:00 -0700",
            "--to", "10/Oct/2024:23:59:59 -0700",
            "--format", "adoc",
            "--filter-field", "INVALID_FIELD",
            "--filter-value", "some_value"
        };

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(output);

        AnalyzerController.processAnalysis(args, printStream);

        assertTrue(output.toString().contains("Invalid filter field"));
        assertTrue(output.toString().contains("usage: LogAnalyzer"));
    }

    @Test
    void testInvalidFilePathThrowsException() {
        String[] args = {
            "--path", "file:///invalid/path/to/log.txt",
            "--from", "01/Oct/2024:00:00:00 -0700",
            "--to", "10/Oct/2024:23:59:59 -0700",
            "--format", "adoc"
        };

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(output);

        AnalyzerController.processAnalysis(args, printStream);

        assertTrue(output.toString().contains("usage: LogAnalyzer"));
    }

    @Test
    void testProcessAnalysisSuccessfully() throws IOException {
        Path tempFile = Files.createTempFile("log", ".txt");
        Files.write(tempFile, List.of(
            "127.0.0.1 - - [01/Oct/2024:12:00:00 -0700] \"GET /index.html HTTP/1.1\" 200 1234 \"-\" \"Mozilla/5.0\""
        ), StandardCharsets.UTF_8);

        String[] args = {
            "--path", tempFile.toUri().toString(),
            "--from", "01/Oct/2024:00:00:00 -0700",
            "--to", "10/Oct/2024:23:59:59 -0700",
            "--format", "adoc"
        };

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(output);

        assertDoesNotThrow(() -> AnalyzerController.processAnalysis(args, printStream));
        assertTrue(output.toString().contains("Logs analyzed, file was created in your working directory"));

        String validFileName = tempFile.toString().split("\\\\", -1)[tempFile.toString().split("\\\\", -1).length - 1];
        String currentDirectory = System.getProperty("user.dir");
        Path filePath = Paths.get(currentDirectory, validFileName + FileCreator.DEFAULT_ADOC_PATH);
        Files.deleteIfExists(filePath);
    }

    @Test
    void testUrlProcessing() throws IOException {

        WireMockServer wireMockServer = new WireMockServer();
        wireMockServer.start();

        wireMockServer.stubFor(WireMock.get("/test")
            .willReturn(WireMock.aResponse()
                .withStatus(200)
                .withBody("Test response")));

        URL url = new URL(wireMockServer.baseUrl() + "/test");

        String[] args = {
            "--path", url.toString(),
            "--from", "01/Oct/2024:00:00:00 -0700",
            "--to", "10/Oct/2024:23:59:59 -0700",
            "--format", "adoc"
        };

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(output);

        assertDoesNotThrow(() -> AnalyzerController.processAnalysis(args, printStream));
        assertTrue(output.toString().contains("Logs analyzed, file was created in your working directory"));

        wireMockServer.stop();
    }

}
