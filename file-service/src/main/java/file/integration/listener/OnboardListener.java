package file.integration.listener;

import file.integration.dto.FileOnboardReq;
import file.integration.dto.FileOnboardRes;
import file.persistence.domain.ManageFileEntity;
import file.persistence.repository.ManageFileRepo;
import file.presentaion.exception.ErrorCode;
import file.presentaion.exception.ErrorMessageLoader;
import file.presentaion.exception.ErrorObject;
import file.presentaion.exception.FileException;
import file.presentaion.service.MinioService;
import org.apache.commons.lang3.StringUtils;
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
import java.util.Objects;

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
        FileOnboardRes fileOnboardRes = new FileOnboardRes();
        try {
            logger.info("OnboardListener downloadFileOnboard fileOnboardReq: {}", fileOnboardReq);
            if(StringUtils.isBlank(fileOnboardReq.getFileId()) ||
                StringUtils.isBlank(fileOnboardReq.getPhoneNumber())){
                throw new FileException(ErrorCode.FILE_REQUEST_IN_VALID);
            }
            ManageFileEntity manageFileEntity = manageFileRepo.findFirstById(fileOnboardReq.getFileId());

            if(Objects.isNull(manageFileEntity)){
                throw new FileException(ErrorCode.FILE_NOT_FOUND);
            }

            InputStream inputStream = minioService.downloadFile(manageFileEntity.getFilePath());

            byte[] bytes = inputStream.readAllBytes();
            String base64String = Base64.getEncoder().encodeToString(bytes);
            fileOnboardRes.setBaseString(base64String);
            return fileOnboardRes;
        } catch (Exception e){
            if(e instanceof FileException){
                logger.error("Loi file vao day");
                FileException fileException = (FileException) e;
                ErrorObject errorObject = fileException.getResponse().getErrorMessage();
                fileOnboardRes.setBaseString(errorObject.getErrorCode() +
                        "_" + errorObject.getMessages().getVn());
                return fileOnboardRes;
            }

            logger.error("OnboardListener downloadFileOnboard with error detail: {}", e);
            throw e;
        }
    }
}
