package onboard.presentation.client;

import onboard.presentation.dto.DownloadFileReq;
import onboard.presentation.dto.DownloadFileRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "file-service")
public interface FileClient {
    @PostMapping("/file-service/file/internal/download-file-onboard")
    DownloadFileRes callFileClient(DownloadFileReq downloadFileReq);
}
