package onboard.presentation.model;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class ConfigSdkModel {
    private String id;
    private String partnerName;
    private String description;
    private String statusNfc;
    private String priority;
    private Timestamp createDate;
    private Timestamp updateDate;
    private String requestLimit;
    private String requestQuantity;
}
