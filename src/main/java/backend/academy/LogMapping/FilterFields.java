package backend.academy.LogMapping;

public enum FilterFields {
    METHOD, STATUS, REMOTE_ADDRESS, BYTES_SENT;

    public String getLogFieldName() {
        return switch (this) {
            case METHOD -> "requestType";
            case STATUS -> "status";
            case REMOTE_ADDRESS -> "remoteAddr";
            case BYTES_SENT -> "bodyBytesSent";
        };
    }

    public static boolean isValid(String field) {
        try {
            FilterFields.valueOf(field.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
