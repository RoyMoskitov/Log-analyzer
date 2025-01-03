package backend.academy.Controller;

import backend.academy.FileCreator.ADocFileCreator;
import backend.academy.FileCreator.FileCreator;
import backend.academy.FileCreator.MarkdownFileCreator;
import backend.academy.LogMapping.FilterFields;
import backend.academy.LogMapping.LogHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.tuple.Pair;

/**
 * The AnalyzerController class handles the command-line arguments for log analysis,
 * processes log files (either from local paths or URLs), applies filters, and
 * generates output files in Markdown or AsciiDoc format.
 */
public class AnalyzerController {
    private static final Option PATH = new Option(null, "path", true,
        "local path or URL to NGINX log file(s), last file name will be used to name file with statistics");
    private static final Option FROM = new Option(null, "from", true,
        "start time of logs that should be analysed (ISO-8601 format)");
    private static final Option TO = new Option(null, "to", true,
        "end time of logs that should be analysed (ISO-8601 format)");
    private static final Option FILE_FORMAT = new Option(null, "format", true,
        "output file format, only adoc and markdown are supported");
    private static final Option FILTER_FIELD = new Option(null, "filter-field", true,
        "field by which logs will be filtered, only works in combination with filter-value"
            + ", Possible values: " + Arrays.toString(FilterFields.values()));
    private static final Option FILTER_VALUE = new Option(null, "filter-value", true,
        "field value by which logs will be filtered");

    private static final OffsetDateTime MIN_DATE = OffsetDateTime.MIN;
    private static final OffsetDateTime MAX_DATE = OffsetDateTime.MAX;

    private static final Integer LEFT_PAD = 2;
    private static final Integer DESC_PAD = 4;
    private static final Integer WIDTH_FORMATTER = 80;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter
        .ofPattern("dd/MMM/yyyy:HH:mm:ss Z").withLocale(Locale.ENGLISH);

    private AnalyzerController() {
    }

    public static void printHelp(PrintStream output, Options options) {
        HelpFormatter formatter = new HelpFormatter();
        PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));
        formatter.printHelp(printWriter, WIDTH_FORMATTER, "LogAnalyzer",
            null, options, LEFT_PAD, DESC_PAD, null, true);
        printWriter.flush();
    }

    private static Options setupOptions() {
        Options options = new Options();
        PATH.setArgs(Option.UNLIMITED_VALUES);
        options.addOption(PATH);
        options.addOption(FROM);
        options.addOption(TO);
        options.addOption(FILE_FORMAT);
        options.addOption(FILTER_VALUE);
        options.addOption(FILTER_FIELD);
        return options;
    }

    @SuppressWarnings("MultipleStringLiterals")
    public static void processAnalysis(String[] args, PrintStream output) {
        Options options = setupOptions();

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine line = parser.parse(options, args);

            OffsetDateTime fromDate = getDateOption(line, FROM, MIN_DATE);
            OffsetDateTime toDate = getDateOption(line, TO, MAX_DATE);
            String fileFormat = getStringOption(line, FILE_FORMAT);
            Pair<String, String> filter = validateFilterOptions(line);

            if (fromDate.isAfter(toDate) || !("markdown".equals(fileFormat) || "adoc".equals(fileFormat))) {
                printHelp(output, options);
                return;
            }

            processPaths(line, fromDate, toDate, filter, fileFormat, output, options);

        } catch (ParseException | IllegalArgumentException e) {
            output.println("Parsing failed. Reason: " + e.getMessage());
            printHelp(output, options);
        }
    }

    private static Pair<String, String> validateFilterOptions(CommandLine line) {
        String filterField = getStringOption(line, FILTER_FIELD);
        String filterValue = getStringOption(line, FILTER_VALUE);

        if (filterField == null && filterValue == null) {
            return Pair.of(null, null);
        }

        if (filterField == null || filterValue == null) {
            throw new IllegalArgumentException("Both filter-field and filter-value must be provided together.");
        }

        String logField = getLogFieldIfValid(filterField);
        return Pair.of(logField, filterValue);
    }

    private static void processPaths(
        CommandLine line, OffsetDateTime fromDate, OffsetDateTime toDate,
        Pair<String, String> filter, String fileFormat, PrintStream output, Options options
    ) {
        if (line.hasOption(PATH)) {
            String[] paths = line.getOptionValues(PATH);
            List<String> fileNames = new ArrayList<>();
            for (var path : paths) {

                List<InputStream> inputStreams = FileResolver.resolveFiles(path, fileNames);

                for (InputStream inputStream : inputStreams) {
                    try (inputStream) {
                        LogHandler.processStream(inputStream, fromDate, toDate, filter.getLeft(), filter.getRight());
                    } catch (IOException | IllegalArgumentException e) {
                        output.println("Something went wrong during the file parsing: " + e.getMessage());
                        printHelp(output, options);
                    }
                }
            }
            FileCreator fileCreator = createFileCreator(fileFormat);
            fileCreator.createFile(LogHandler.STATISTICS_LIST, fileNames, fromDate, toDate);
            output.println("Logs analyzed, file was created in your working directory\n");
        } else {
            printHelp(output, options);
        }
    }

    private static FileCreator createFileCreator(String fileFormat) {
        return switch (fileFormat) {
            case "adoc" -> new ADocFileCreator();
            case "markdown" -> new MarkdownFileCreator();
            default -> throw new RuntimeException("Unsupported format: " + fileFormat);
        };
    }

    private static String getLogFieldIfValid(String filterField) {
        if (!FilterFields.isValid(filterField)) {
            throw new IllegalArgumentException("Invalid filter field: " + filterField);
        }
        return filterField;
    }

    private static String getStringOption(CommandLine line, Option option) {
        return line.getOptionValue(option.getLongOpt());
    }

    private static OffsetDateTime getDateOption(CommandLine line, Option option, OffsetDateTime defaultValue) {
        return Optional.ofNullable(line.getOptionValue(option.getLongOpt()))
            .map(dateString -> {
                try {
                    ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateString, DATE_FORMATTER);
                    return zonedDateTime.toOffsetDateTime();
                } catch (DateTimeParseException e) {
                    throw new IllegalArgumentException("Invalid date format: " + dateString, e);
                }
            })
            .orElse(defaultValue);
    }
}
