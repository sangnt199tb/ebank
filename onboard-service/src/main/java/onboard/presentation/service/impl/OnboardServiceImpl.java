package onboard.presentation.service.impl;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import onboard.integration.model.CompareFaceInterReq;
import onboard.integration.model.CompareFaceInterRes;
import onboard.integration.service.EkycService;
import onboard.persistence.domain.OnboardingTransactionEntity;
import onboard.persistence.service.TransactionService;
import onboard.presentation.client.FileClient;
import onboard.presentation.dto.DownloadFileReq;
import onboard.presentation.dto.DownloadFileRes;
import onboard.presentation.exception.ErrorCode;
import onboard.presentation.exception.OnboardingException;
import onboard.presentation.model.*;
import onboard.presentation.service.OnboardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class OnboardServiceImpl implements OnboardService {
    private static Logger logger = LoggerFactory.getLogger(OnboardServiceImpl.class);

    private final DefaultKaptcha defaultKaptcha;
    private final StringRedisTemplate redisTemplate;
    private final FileClient fileClient;
    private final TransactionService transactionService;
    private final EkycService ekycService;

    @Autowired
    public OnboardServiceImpl(DefaultKaptcha defaultKaptcha, StringRedisTemplate redisTemplate, FileClient fileClient, TransactionService transactionService, EkycService ekycService) {
        this.defaultKaptcha = defaultKaptcha;
        this.redisTemplate = redisTemplate;
        this.fileClient = fileClient;
        this.transactionService = transactionService;
        this.ekycService = ekycService;
    }

    @Override
    public ResponseEntity<CaptchaResponse> getCaptcha() throws IOException {
        try {
            String captchaText = defaultKaptcha.createText();
            BufferedImage bufferedImage = defaultKaptcha.createImage(captchaText);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", outputStream);
            String base64Image = Base64.getEncoder().encodeToString(outputStream.toByteArray());
            String formatBase64Img = "data:image/jpeg;base64," + base64Image;

            String captchaId = UUID.randomUUID().toString();
            redisTemplate.opsForValue().set("captcha:" + captchaId, captchaText, 2, TimeUnit.MINUTES);

            CaptchaResponse response = new CaptchaResponse(captchaId, formatBase64Img);

            return ResponseEntity.ok(response);
        } catch (Exception e){
            logger.error("OnboardController getCaptcha with error detail: {}", e);
            throw e;
        }
    }

    @Override
    public ResponseEntity<CheckPhoneEmailRes> checkPhoneAndEmail(CheckPhoneEmailReq request) {
        try {
            String redisKey = "captcha:" + request.getCaptchaId();
            String serverCaptcha = redisTemplate.opsForValue().get(redisKey);

            if (serverCaptcha == null) {
                throw new OnboardingException(ErrorCode.CAPTCHA_TIMEOUT);
            }

            if (!serverCaptcha.equalsIgnoreCase(request.getCaptcha())) {
                throw new OnboardingException(ErrorCode.CAPTCHA_FAILED);
            }

            redisTemplate.delete(redisKey);

            // create transaction
            OnboardingTransactionEntity entity = transactionService.postCreateTransaction(request);

            CheckPhoneEmailRes checkPhoneEmailRes = new CheckPhoneEmailRes();
            checkPhoneEmailRes.setTransId(entity.getId());

            return ResponseEntity.ok(checkPhoneEmailRes);
        } catch (Exception e){
            logger.error("OnboardController checkPhoneAndEmail with error detail: {}", e);
            throw e;
        }
    }

    @Override
    public ResponseEntity<CompareFaceRes> compareFace(CompareFaceReq request) {
        try {
            logger.info("OnboardServiceImpl compareFace with request: {}", request);
            // validate id transaction

            // call module file
            DownloadFileReq downloadFileReq = new DownloadFileReq();
            downloadFileReq.setPhoneNumber("0387501614");
            downloadFileReq.setFileIdIc(request.getIdImageFont());
            downloadFileReq.setFileIdImage(request.getIdImageFace());
            DownloadFileRes downloadFileRes = fileClient.callFileClient(downloadFileReq);
            logger.info("OnboardServiceImpl compareFace downloadFileRes: {}", downloadFileRes);

            // call compare face
            CompareFaceInterReq compareFaceInterReq = CompareFaceInterReq
                    .builder()
                    .idImage(downloadFileRes.getBaseStringIc().replaceAll("[\\n\\r\\s]", ""))
                    .selfieImage(downloadFileRes.getBaseStringImage().replaceAll("[\\n\\r\\s]", ""))
                    .build();
            CompareFaceInterRes compareFaceAi = ekycService.compareFaceAi(compareFaceInterReq);
            logger.info("OnboardServiceImpl compareFace compareFaceAi: {}", compareFaceAi);

            if(Objects.isNull(compareFaceAi)){
                throw new OnboardingException(ErrorCode.FACE_MATCH_FAILED);
            }

            if(!compareFaceAi.isMatch()){
                throw new OnboardingException(ErrorCode.FACE_MATCH_FAILED);
            }

            CompareFaceRes compareFaceRes = new CompareFaceRes();
            compareFaceRes.setId(request.getId());
            return ResponseEntity.ok(compareFaceRes);

        } catch (Exception e){
            logger.error("OnboardController compareFace with error detail: {}", e);
            throw e;
        }
    }
}
