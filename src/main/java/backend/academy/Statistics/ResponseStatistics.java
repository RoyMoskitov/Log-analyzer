package backend.academy.Statistics;

import backend.academy.FileCreator.FileCreator;
import backend.academy.LogMapping.Log;
import com.tdunning.math.stats.TDigest;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

/**
 * The ResponseStatistics class implements the Statistics interface to track and analyze
 * various response metrics, such as the total number of responses, average response size,
 * 95th percentile of response size, and daily log activity statistics.
 */
@Getter
public class ResponseStatistics implements Statistics {
    public static final Integer TDIGEST_ACCURACY = 100;
    private Integer responseNumber;
    private Double averageResponseSize;
    private final TDigest tDigest;

    private final Map<LocalDate, Integer> logCountMap = new HashMap<>();

    public ResponseStatistics() {
        responseNumber = 0;
        averageResponseSize = 0.0;
        tDigest = TDigest.createDigest(TDIGEST_ACCURACY);
    }

    @Override
    public void update(Log log) {
        ++responseNumber;
        tDigest.add(log.bodyBytesSent());
        averageResponseSize += (log.bodyBytesSent() - averageResponseSize) / responseNumber;
        LocalDate logDate = log.timeLocal().toLocalDate();
        logCountMap.put(log.timeLocal().toLocalDate(),
            logCountMap.getOrDefault(logDate, 0) + 1);
    }

    @Override
    public String writeStatisticsInADoc() {
        return writeStatisticsInMarkdown() + "|===\n\n";
    }

    @SuppressWarnings("MultipleStringLiterals")
    @Override
    public String writeStatisticsInMarkdown() {
        return "|    Response number    |  " + centerText(responseNumber.toString(),
            FileCreator.GENERAL_INFO_SECOND_COLUMN_LENGTH) + "  |\n"
            + "| Average response size |  "
            + centerText(Integer.toString((int) Math.round(averageResponseSize)),
            FileCreator.GENERAL_INFO_SECOND_COLUMN_LENGTH) + "  |\n"
            + "|   95p response size   |  " + centerText(ninetyFifthPercentileSize().toString(),
            FileCreator.GENERAL_INFO_SECOND_COLUMN_LENGTH) + "  |\n"
            + "| Average logs per day  |  " + centerText(getAverageLogCountPerDay().toString(),
            FileCreator.GENERAL_INFO_SECOND_COLUMN_LENGTH) + "  |\n"
            + "|   Peak load per day   |  "
            + centerText(getMaxLogCount().toString(),
            FileCreator.GENERAL_INFO_SECOND_COLUMN_LENGTH) + "  |\n";
    }

    private Integer getAverageLogCountPerDay() {
        int totalLogs = logCountMap.values().stream().mapToInt(Integer::intValue).sum();
        int totalDays = logCountMap.size();
        return totalDays > 0 ? totalLogs / totalDays : 0;
    }

    private Integer getMaxLogCount() {
        return logCountMap.values().stream().mapToInt(Integer::intValue).max().orElse(0);
    }

    @SuppressWarnings("MagicNumber")
    private Integer ninetyFifthPercentileSize() {
        return (int) tDigest.quantile(0.95);
    }
}
