package onboard.integration.service;

import onboard.integration.model.CompareFaceInterReq;
import onboard.integration.model.CompareFaceInterRes;

public interface EkycService {
    CompareFaceInterRes compareFaceAi(CompareFaceInterReq req);
}
