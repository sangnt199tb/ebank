package file.presentaion.service.impl;

import file.persistence.domain.ManageFileEntity;
import file.persistence.repository.ManageFileRepo;
import file.presentaion.model.UploadFileResponse;
import file.presentaion.service.FileService;
import file.presentaion.service.MinioService;
import file.presentaion.validate.Validate;
import io.minio.errors.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Enumeration;
import java.util.Random;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    private static Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);
    private final String alphabet = "zxcvbnmasdfghjklpoiuytrewq";

    private final MinioService minioService;
    private final ManageFileRepo manageFileRepo;

    @Autowired
    public FileServiceImpl(MinioService minioService, ManageFileRepo manageFileRepo) {
        this.minioService = minioService;
        this.manageFileRepo = manageFileRepo;
    }

    @Override
    public UploadFileResponse uploadFile(MultipartFile file, String phoneNumber, String fileType,
                                         String module, HttpServletRequest httpServletRequest)
            throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        try {
            logger.info("FileServiceImpl uploadFile with phoneNumber: {} and module: {} and fileType: {}",
                    phoneNumber, file, module);

            //validate key
            Enumeration<String> key
                    = httpServletRequest.getHeaders("X-Internal-Gateway-Secret");
            logger.info("FileServiceImpl uploadFile key: {}", key);

            // validate file name
            Validate.validateFileName(file.getOriginalFilename());

            // validate file type
            Validate.validateFileType(file.getOriginalFilename());

            String originalFilename = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String newFileName = UUID.randomUUID().toString() + fileExtension;

            // save minio
            String filePath = minioService.uploadFile(file, module);

            //save manageFile
            ManageFileEntity manageFileEntity = new ManageFileEntity();
            manageFileEntity.setId(UUID.randomUUID().toString());
            manageFileEntity.setCreatedBy(phoneNumber);
            manageFileEntity.setCreateDate(new Timestamp(System.currentTimeMillis()));
            manageFileEntity.setFileName(newFileName);
            manageFileEntity.setFilePath(filePath);
            manageFileEntity.setFileStatus("CREATE");
            manageFileEntity.setFileType(fileType);
            manageFileEntity.setFormat(file.getContentType());
            manageFileEntity = manageFileRepo.save(manageFileEntity);

            UploadFileResponse response = new UploadFileResponse();
            response.setFileId(manageFileEntity.getId());

            logger.info("FileServiceImpl uploadFile success with phoneNumber: {}", phoneNumber);

            return response;
        } catch (Exception e){
            logger.error("FileServiceImpl uploadFile with error detail: {}", e);
            throw e;
        }
    }
}
