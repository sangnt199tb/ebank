package file.integration.dto;

import lombok.Data;
import java.util.List;

@Data
public class FptOcrResponse {
    private int errorCode;
    private String errorMessage;
    private List<OcrData> data;
}
