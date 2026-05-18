package file.integration.listener;

import file.integration.dto.FileOnboardReq;
import file.integration.dto.FileOnboardRes;
import file.persistence.domain.ManageFileEntity;
import file.persistence.repository.ManageFileRepo;
import file.presentaion.service.MinioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@RestController
@RequestMapping("/file/internal")
public class OnboardListener {
    private static Logger logger = LoggerFactory.getLogger(OnboardListener.class);

    private final MinioService minioService;
    private final ManageFileRepo manageFileRepo;

    @Autowired
    public OnboardListener(MinioService minioService, ManageFileRepo manageFileRepo) {
        this.minioService = minioService;
        this.manageFileRepo = manageFileRepo;
    }

    @PostMapping("/download-file-onboard")
    public FileOnboardRes downloadFileOnboard(@RequestBody FileOnboardReq fileOnboardReq) throws IOException {
        try {
            logger.info("OnboardListener downloadFileOnboard fileOnboardReq: {}", fileOnboardReq);
            ManageFileEntity manageFileEntity = manageFileRepo.findFirstById(fileOnboardReq.getFileId());
            InputStream inputStream = minioService.downloadFile(manageFileEntity.getFilePath());

            byte[] bytes = inputStream.readAllBytes();
            String base64String = Base64.getEncoder().encodeToString(bytes);
            FileOnboardRes fileOnboardRes = new FileOnboardRes();
            fileOnboardRes.setBaseString(base64String);
            return fileOnboardRes;
        } catch (Exception e){
            logger.error("OnboardListener downloadFileOnboard with error detail: {}", e);
            throw e;
        }
    }
}
