package onboard.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "file-service")
public interface FileClient {
    @GetMapping("/file-service/file/internal/onboard-call-file")
    String callFileClient();
}
