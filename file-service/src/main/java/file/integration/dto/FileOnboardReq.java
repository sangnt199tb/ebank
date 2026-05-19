package file.integration.dto;

import lombok.Data;

@Data
public class FileOnboardReq {
    private String phoneNumber;
    private String fileIdIc;
    private String fileIdImage;
}
