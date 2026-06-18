package onboard.presentation.dto;

import lombok.Data;

@Data
public class ReadIcCardRes {
    private String id;
    private String idProb;
    private String name;
    private String nameProb;
    private String dob;
    private String dobProb;
    private String sex;
    private String sexProb;
    private String nationality;
    private String nationalityProb;
    private String overallScore;
    private String numberOfNameLines;
    private String home;
    private String homeProb;
    private String address;
    private String addressProb;
    private String doe;
    private String doeProb;
    private String typeNew;
    private String type;
}
