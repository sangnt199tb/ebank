package onboard.integration.model;

import lombok.Data;

@Data
public class CompareFaceInterRes {
    private boolean isMatch;
    private int confidence;
    private String reasoning;
    private String nameInId;
    private String idNumber;
}
