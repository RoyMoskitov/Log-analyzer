package backend.academy.LogMapping;

import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Log {
    private String remoteAddr;
    private String remoteUser;
    private OffsetDateTime timeLocal;
    private RequestType requestType;
    private String requestResource;
    private String httpVersion;
    private Integer status;
    private Integer bodyBytesSent;
    private String httpReferer;

    @Override public String toString() {
        return "Log{"
            + "remoteAddr='" + remoteAddr + '\''
            + ", remoteUser='" + remoteUser + '\''
            + ", timeLocal=" + timeLocal
            + ", requestType=" + requestType
            + ", requestResource='" + requestResource + '\''
            + ", httpVersion='" + httpVersion + '\''
            + ", status=" + status
            + ", bodyBytesSent=" + bodyBytesSent
            + ", httpReferer='" + httpReferer + '\''
            + ", httpUserAgent='" + httpUserAgent + '\'' + '}';
    }

    private String httpUserAgent;

    private Log(LogBuilder builder) {
        this.remoteAddr = builder.remoteAddr;
        this.remoteUser = builder.remoteUser;
        this.timeLocal = builder.timeLocal;
        this.requestType = builder.requestType;
        this.requestResource = builder.requestResource;
        this.httpVersion = builder.httpVersion;
        this.status = builder.status;
        this.bodyBytesSent = builder.bodyBytesSent;
        this.httpReferer = builder.httpReferer;
        this.httpUserAgent = builder.httpUserAgent;
    }

    @Setter
    public static class LogBuilder {
        private String remoteAddr;
        private String remoteUser;
        private OffsetDateTime timeLocal;
        private RequestType requestType;
        private String requestResource;
        private String httpVersion;
        private Integer status;
        private Integer bodyBytesSent;
        private String httpReferer;
        private String httpUserAgent;

        public Log build() {
            return new Log(this);
        }
    }
}
