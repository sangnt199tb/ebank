package file.presentaion.service.impl;

import file.presentaion.service.MinioService;
import io.minio.*;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MinioServiceImpl implements MinioService {
    private static final Logger logger = LoggerFactory.getLogger(MinioServiceImpl.class);

    private final MinioClient minioClient;
    private final String bucketName = "onboard";

    @Override
    public String uploadFile(MultipartFile file, String bucketType) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        try {
            logger.info("MinioServiceImpl uploadFile bucketType: {}", bucketType);
            boolean checkExitBucket = minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(bucketType)
                    .build());
            if(!checkExitBucket){
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(bucketType)
                        .build());
            }
            String datePath = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            String objectName = bucketType + "/" + datePath + "/" + UUID.randomUUID() + "-" + file.getOriginalFilename();
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(StringUtils.equalsIgnoreCase(bucketType, bucketName) ? bucketName : bucketType)
                            .object(objectName)
                            .stream(file.getInputStream(),
                                    file.getSize(),
                                    -1)
                            .contentType(file.getContentType())
                            .build()
            );
            return objectName;
        }  catch (Exception e) {
            logger.error("MinioServiceImpl upload with error detail: {}", e);
            throw e;
        }
    }

    @Override
    public InputStream downloadFile(String path) {
        logger.info("MinioServiceImpl downloadFile with path: {}", path);
        try {
            String bucketNameDownload = path.contains("Onboard") ? bucketName : "file";
            return minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucketNameDownload)
                    .object(path).build());
        } catch (Exception e){
            logger.error("MinioServiceImpl downloadFile with error detail: {}", e);
        }
        return null;
    }
}
