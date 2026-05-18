package onboard.presentation.service.impl;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import onboard.presentation.client.FileClient;
import onboard.presentation.controller.OnboardController;
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
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class OnboardServiceImpl implements OnboardService {
    private static Logger logger = LoggerFactory.getLogger(OnboardServiceImpl.class);

    private final DefaultKaptcha defaultKaptcha;
    private final StringRedisTemplate redisTemplate;
    private final FileClient fileClient;

    @Autowired
    public OnboardServiceImpl(DefaultKaptcha defaultKaptcha, StringRedisTemplate redisTemplate, FileClient fileClient) {
        this.defaultKaptcha = defaultKaptcha;
        this.redisTemplate = redisTemplate;
        this.fileClient = fileClient;
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

            CheckPhoneEmailRes checkPhoneEmailRes = new CheckPhoneEmailRes();
            checkPhoneEmailRes.setTransId(UUID.randomUUID().toString());
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
            downloadFileReq.setFileId(request.getIdImageFont());
            DownloadFileRes downloadFileRes = fileClient.callFileClient(downloadFileReq);
            logger.info("OnboardServiceImpl compareFace downloadFileRes: {}", downloadFileRes);

        } catch (Exception e){
            logger.error("OnboardController compareFace with error detail: {}", e);
            throw e;
        }
        return null;
    }
}
