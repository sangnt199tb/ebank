package onboard.presentation.service.impl;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import onboard.presentation.controller.OnboardController;
import onboard.presentation.exception.ErrorCode;
import onboard.presentation.exception.OnboardingException;
import onboard.presentation.model.CaptchaResponse;
import onboard.presentation.model.CheckPhoneEmailReq;
import onboard.presentation.model.CheckPhoneEmailRes;
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
    private static Logger logger = LoggerFactory.getLogger(OnboardController.class);

    private final DefaultKaptcha defaultKaptcha;
    private final StringRedisTemplate redisTemplate;

    @Autowired
    public OnboardServiceImpl(DefaultKaptcha defaultKaptcha, StringRedisTemplate redisTemplate) {
        this.defaultKaptcha = defaultKaptcha;
        this.redisTemplate = redisTemplate;
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
}
