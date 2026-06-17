package onboard.presentation.exception;

public interface ErrorCode {
    String INTERNAL_SERVER_ERROR = "HYD-35-001";
    String FILE_NAME_ERROR = "HYD-35-002";
    String FILE_TYPE_ERROR = "HYD-35-003";
    String FILE_NOT_FOUND="HYD-35-004";
    String FACE_MATCH_FAILED = "HYD-35-005";
    String TIME_OUT = "HYD-35-006";
    String CAPTCHA_TIMEOUT = "HYD-35-007";
    String CAPTCHA_FAILED = "HYD-35-008";
    String INVALID_REQUEST = "HYD-35-009";
}
