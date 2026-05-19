package onboard.presentation.dto;

import lombok.Data;

@Data
public class DownloadFileRes {
    private String status;
    private String errorDesc;
    private String baseStringIc;
    private String baseStringImage;
}
