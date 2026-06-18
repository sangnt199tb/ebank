package onboard.presentation.client;

import onboard.config.FeignClientConfig;
import onboard.presentation.dto.DownloadFileReq;
import onboard.presentation.dto.DownloadFileRes;
import onboard.presentation.dto.ReadIcCardReq;
import onboard.presentation.dto.ReadIcCardRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "file-service", configuration = FeignClientConfig.class)
public interface FileClient {
    @PostMapping("/file-service/file/internal/download-file-onboard")
    DownloadFileRes callFileClient(DownloadFileReq downloadFileReq);

    @PostMapping("/file-service/file/internal/ocr-ic-card")
    ReadIcCardRes callFileOcr(ReadIcCardReq readIcCardReq);
}
