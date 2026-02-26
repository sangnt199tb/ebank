package onboard.persistence.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Table(name = "sdk_config")
@Data
public class SdkConfigEntity {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "parter_name")
    private String partnerName;

    @Column(name = "desciption")
    private String description;

    @Column(name = "status_nfc")
    private String statusNfc;

    @Column(name = "priority")
    private String priority;

    @Column(name = "create_date")
    private Timestamp createDate;

    @Column(name = "update_date")
    private Timestamp updateDate;

    @Column(name = "request_limit")
    private String requestLimit;

    @Column(name = "request_quantity")
    private String requestQuantity;

}
