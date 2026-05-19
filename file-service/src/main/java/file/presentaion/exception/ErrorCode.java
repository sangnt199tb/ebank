package file.presentaion.exception;

public interface ErrorCode {
    String SUCCESS = "0";
    String ERROR = "1";
    String INTERNAL_SERVER_ERROR = "HYD-35-001";
    String FILE_NAME_ERROR = "HYD-35-002";
    String FILE_TYPE_ERROR = "HYD-35-003";
    String FILE_NOT_FOUND="HYD-35-004";
    String FACE_MATCH_FAILED = "HYD-35-005";
    String TIME_OUT = "HYD-35-006";
    String FILE_REQUEST_IN_VALID = "HYD-35-007";
}
