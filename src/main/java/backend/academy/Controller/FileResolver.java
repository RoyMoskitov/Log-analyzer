package backend.academy.Controller;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileResolver {

    private FileResolver() {}

    @SuppressWarnings({"MultipleStringLiterals", "ParameterAssignment"})
    @SuppressFBWarnings({"URLCONNECTION_SSRF_FD", "PATH_TRAVERSAL_IN"})
    public static List<InputStream> resolveFiles(String pathValue, List<String> fileNames) {
        List<InputStream> res = new ArrayList<>();
        try {
            if (pathValue.startsWith("http://") || pathValue.startsWith("https://")) {
                URI uri = new URI(pathValue);
                URL url = uri.toURL();
                res.add(url.openStream());
                fileNames.add(pathValue.split("/", -1)[pathValue.split("/", -1).length - 1]);
            } else {
                if (pathValue.startsWith("file:///")) {
                    URI uri = URI.create(pathValue);
                    pathValue = uri.getPath().substring(1);
                }
                List<Path> paths = findMatchingFiles(pathValue);
                for (var path : paths) {
                    res.add(Files.newInputStream(path));
                    fileNames.add(path.toString().split("\\\\", -1)
                        [path.toString().split("\\\\", -1).length - 1]);

                }
            }
            return res;
        } catch (URISyntaxException | IOException e) {
            throw new IllegalArgumentException("Invalid URI: " + e.getMessage(), e);
        }
    }

    public static List<Path> findMatchingFiles(String pathPattern) throws IOException {
        if (!pathPattern.startsWith("file:///")) {
            pathPattern = "file:///" + pathPattern.replace("\\", "/");
        }

        URI uri = URI.create(pathPattern);
        String localPath = uri.getPath();

        int lastSeparatorIndex = localPath.lastIndexOf('/');
        if (lastSeparatorIndex == -1) {
            throw new IllegalArgumentException("Incorrect path: " + pathPattern);
        }

        Path directory = Paths.get(localPath.substring(0, lastSeparatorIndex));
        String filePattern = localPath.substring(lastSeparatorIndex + 1);

        if (!Files.isDirectory(directory)) {
            throw new IllegalArgumentException("Directory doesn't exist: " + directory);
        }

        try (var fileList = Files.list(directory)) {
            return fileList
                .filter(path -> path.getFileName().toString().startsWith(filePattern.replace("*", "")))
                .toList();
        }
    }


}
