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
            if(StringUtils.isBlank(fileOnboardReq.getFileIdIc())
                    || StringUtils.isBlank(fileOnboardReq.getPhoneNumber())
                    || StringUtils.isBlank(fileOnboardReq.getFileIdImage())){
                throw new FileException(ErrorCode.FILE_REQUEST_IN_VALID);
            }

            // get path ic
            ManageFileEntity manageFileEntity = manageFileRepo.findFirstById(fileOnboardReq.getFileIdIc());
            if(Objects.isNull(manageFileEntity)){
                throw new FileException(ErrorCode.FILE_NOT_FOUND);
            }

            String prefix = "data:image/png;base64,";

            InputStream inputStream = minioService.downloadFile(manageFileEntity.getFilePath());
            byte[] bytes = inputStream.readAllBytes();
            String base64StringIc = Base64.getEncoder().encodeToString(bytes);
            fileOnboardRes.setBaseStringIc(prefix + base64StringIc);

            // get path image
            ManageFileEntity manageFileImage = manageFileRepo.findFirstById(fileOnboardReq.getFileIdImage());
            if(Objects.isNull(manageFileImage)){
                throw new FileException(ErrorCode.FILE_NOT_FOUND);
            }

            InputStream iSImage = minioService.downloadFile(manageFileImage.getFilePath());
            byte[] bytesImage = iSImage.readAllBytes();
            String base64StringImage = Base64.getEncoder().encodeToString(bytesImage);
            fileOnboardRes.setBaseStringImage(prefix + base64StringImage);

            //set status
            fileOnboardRes.setStatus(ErrorCode.SUCCESS);

            return fileOnboardRes;
        } catch (Exception e){
            logger.error("OnboardListener downloadFileOnboard with error detail: {}", e);

            if(e instanceof FileException){
                logger.error("Loi file vao day");
                FileException fileException = (FileException) e;
                ErrorObject errorObject = fileException.getResponse().getErrorMessage();
                fileOnboardRes.setStatus(ErrorCode.ERROR);
                fileOnboardRes.setErrorDesc(errorObject.getMessages().getEn());
                return fileOnboardRes;
            }

            fileOnboardRes.setStatus(ErrorCode.INTERNAL_SERVER_ERROR);
            return fileOnboardRes;
        }
    }
}
