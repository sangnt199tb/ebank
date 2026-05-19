package file.integration.dto;

import lombok.Data;

@Data
public class FileOnboardRes {
    private String status;
    private String errorDesc;
    private String baseStringIc;
    private String baseStringImage;
}
