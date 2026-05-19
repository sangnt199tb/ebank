package onboard.presentation.dto;

import lombok.Data;

@Data
public class DownloadFileReq {
    private String phoneNumber;
    private String fileIdIc;
    private String fileIdImage;
}
