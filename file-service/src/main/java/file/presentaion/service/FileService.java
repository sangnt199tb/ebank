package file.presentaion.service;

import file.presentaion.model.UploadFileResponse;
import io.minio.errors.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface FileService {
    UploadFileResponse uploadFile(MultipartFile file, String phoneNumber, String fileType, String bucketType, HttpServletRequest httpServletRequest) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;
}
