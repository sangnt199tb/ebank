package file.integration.service.impl;

import file.integration.dto.FptOcrResponse;
import file.integration.dto.ReadIcCardReq;
import file.integration.dto.ReadIcCardRes;
import file.integration.service.FileEkycService;
import file.persistence.domain.ManageFileEntity;
import file.persistence.repository.ManageFileRepo;
import file.presentaion.exception.ErrorCode;
import file.presentaion.exception.FileException;
import file.presentaion.service.MinioService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.InputStream;

import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Objects;

@Service
public class FileEkycServiceImpl implements FileEkycService {
    private static Logger logger = LoggerFactory.getLogger(FileEkycServiceImpl.class);
    private final RestTemplate restTemplate;
    private final MinioService minioService;
    private final ManageFileRepo manageFileRepo;

    @Autowired
    public FileEkycServiceImpl(RestTemplate restTemplate, MinioService minioService, ManageFileRepo manageFileRepo) {
        this.restTemplate = restTemplate;
        this.minioService = minioService;
        this.manageFileRepo = manageFileRepo;
    }

    @Override
    public ReadIcCardRes readIcCard(ReadIcCardReq readIcCardReq) {
        try {
            logger.info("FileEkycServiceImpl readIcCard readIcCardReq: {}", readIcCardReq);
            if(StringUtils.isBlank(readIcCardReq.getPhoneNumber())
                    || StringUtils.isBlank(readIcCardReq.getIcBack())
                    || StringUtils.isBlank(readIcCardReq.getIcBack())){
                throw new FileException(ErrorCode.FILE_REQUEST_IN_VALID);
            }

            // get path ic
            ManageFileEntity manageFileEntity = manageFileRepo.findFirstById(readIcCardReq.getIcFont());
            logger.info("FileEkycServiceImpl readIcCard with file path: {}", manageFileEntity.getFilePath());
            if(Objects.isNull(manageFileEntity)){
                throw new FileException(ErrorCode.FILE_NOT_FOUND);
            }

            String apiUrl = "https://api.fpt.ai/vision/idr/vnm";
            String apiKey = "HoqBnvHQ4MSDaqfvvAmaahJdZGGJqkpL"; // Nên đưa vào file cấu hình

            try {
                InputStream inputStream = minioService.downloadFile(manageFileEntity.getFilePath());
                byte[] bytes = inputStream.readAllBytes();

                String filename = Paths.get(manageFileEntity.getFilePath()).getFileName().toString();

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.MULTIPART_FORM_DATA);
                headers.set("api-key", apiKey);

                ByteArrayResource contentsAsResource = new ByteArrayResource(bytes) {
                    @Override
                    public String getFilename() {
                        return filename;
                    }
                };

                MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
                body.add("image", contentsAsResource);

                HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
                ResponseEntity<FptOcrResponse> response = restTemplate.postForEntity(apiUrl, requestEntity, FptOcrResponse.class);
                logger.info("FileEkycServiceImpl readIcCard response: {}", response.getBody());
                ReadIcCardRes readIcCardRes = new ReadIcCardRes();
                readIcCardRes.setId(response.getBody().getData().get(0).getId());
                return readIcCardRes;
            } catch (Exception e) {
                logger.error("FileEkycServiceImpl readIcCard call fpt with error detail: {}", e);
                e.printStackTrace();
            }
        } catch (Exception e){
            logger.error("FileEkycServiceImpl readIcCard with error detail: {}", e);
            throw e;
        }
        return null;
    }
}
