package file.integration.service;

import file.integration.dto.ReadIcCardReq;
import file.integration.dto.ReadIcCardRes;

public interface FileEkycService {
    ReadIcCardRes readIcCard(ReadIcCardReq readIcCardReq);
}
