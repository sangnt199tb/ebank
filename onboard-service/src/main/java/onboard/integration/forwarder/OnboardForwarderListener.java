package onboard.integration.forwarder;

import onboard.presentation.model.CompareFaceReq;
import onboard.presentation.model.CompareFaceRes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/fwd/onboard")
public class OnboardForwarderListener {

    private final String xInternalGatewaySecret = "X-Internal-Gateway-Secret";

    @PostMapping("/check-info-customer")
    public ResponseEntity<CompareFaceRes> checkPhoneAndEmailTest(
            @RequestHeader(value = xInternalGatewaySecret, required = false) String gatewaySecret,
            @RequestBody CompareFaceReq request) {
        System.out.println("get header: " + gatewaySecret);
        CompareFaceRes compareFaceRes = new CompareFaceRes();
        compareFaceRes.setId("SUCCESS ROI BAN OI");
        return ResponseEntity.ok(compareFaceRes);
    }
}
