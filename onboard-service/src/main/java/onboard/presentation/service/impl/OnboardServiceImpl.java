package onboard.presentation.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.nimbusds.jose.shaded.gson.Gson;
import customer.presentation.dto.CustomerDto;
import customer.presentation.dto.RequestUserDto;
import onboard.integration.model.CompareFaceInterReq;
import onboard.integration.model.CompareFaceInterRes;
import onboard.integration.service.EkycService;
import onboard.persistence.service.TransactionService;
import onboard.presentation.client.CommonClient;
import onboard.presentation.client.CustomerClient;
import onboard.presentation.client.FileClient;
import onboard.presentation.dto.*;
import onboard.presentation.exception.ErrorCode;
import onboard.presentation.exception.OnboardingException;
import onboard.presentation.model.*;
import onboard.presentation.service.OnboardService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    private static ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final CommonClient commonClient;
    private final CustomerClient customerClient;

    @Autowired
    public OnboardServiceImpl(DefaultKaptcha defaultKaptcha, StringRedisTemplate redisTemplate, FileClient fileClient, TransactionService transactionService, EkycService ekycService, CommonClient commonClient, CustomerClient customerClient) {
        this.defaultKaptcha = defaultKaptcha;
        this.redisTemplate = redisTemplate;
        this.fileClient = fileClient;
        this.transactionService = transactionService;
        this.ekycService = ekycService;
        this.commonClient = commonClient;
        this.customerClient = customerClient;
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
            OnboardTransactionDto onboardTransactionDto = transactionService.postCreateTransaction(request);

            CheckPhoneEmailRes checkPhoneEmailRes = new CheckPhoneEmailRes();
            checkPhoneEmailRes.setTransId(onboardTransactionDto.getId());

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
            if(Objects.isNull(request)
                    || StringUtils.isBlank(request.getIdImageFace())
                    || StringUtils.isBlank(request.getIdImageFont())){
                throw new OnboardingException(ErrorCode.INVALID_REQUEST);
            }
            OnboardTransactionDto onboardTransaction
                    = transactionService.getTransactionById(request.getId());
            if(Objects.isNull(onboardTransaction)){
                throw new OnboardingException(ErrorCode.INVALID_REQUEST);
            }

            // call module file
            DownloadFileReq downloadFileReq = new DownloadFileReq();
            downloadFileReq.setPhoneNumber(onboardTransaction.getPhoneNumber());
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
            String reasoning = onboard.presentation.util.StringUtils.removeAccentsAndSpaces(compareFaceAi.getReasoning());
            logger.info("OnboardServiceImpl compareFace reasoning: {}", reasoning);
            if(Objects.isNull(compareFaceAi)){
                throw new OnboardingException(ErrorCode.FACE_MATCH_FAILED);
            }

            if(reasoning.contains("tuongdong")
                    || reasoning.contains("cungmotnguoi")
                    || reasoning.contains("trungkhop")
                    || reasoning.contains("khopnhau")){
                CompareFaceRes compareFaceRes = new CompareFaceRes();
                compareFaceRes.setId(request.getId());
                return ResponseEntity.ok(compareFaceRes);
            }
            throw new OnboardingException(ErrorCode.FACE_MATCH_FAILED);
        } catch (Exception e){
            logger.error("OnboardServiceImpl compareFace with error detail: {}", e);
            throw e;
        }
    }

    @Override
    public ResponseEntity<SendOtpToCustomerRes> sendOtpToCustomer(SendOtpToCustomerReq request) {
        try {
            logger.info("OnboardServiceImpl sendOtpToCustomer with request: {}", new Gson().toJson(request));
            if(Objects.isNull(request) || StringUtils.isBlank(request.getTransId())){
                throw new OnboardingException(ErrorCode.INVALID_REQUEST);
            }
            OnboardTransactionDto onboardTransaction
                    = transactionService.getTransactionById(request.getTransId());
            if(Objects.isNull(onboardTransaction)){
                throw new OnboardingException(ErrorCode.INVALID_REQUEST);
            }

            // lấy ra số điện thoại và call common để gửi OTP
            OtpSendRequest otpSendRequest = new OtpSendRequest();
            otpSendRequest.setEmail(onboardTransaction.getEmail());
            otpSendRequest.setPhoneNumber(onboardTransaction.getPhoneNumber());
            OtpSendResponse otpSendResponse = commonClient.sendOtpToCustomer(otpSendRequest);
            logger.info("OnboardServiceImpl sendOtpToCustomer otpSendResponse: {}", toJson(otpSendResponse));

            SendOtpToCustomerRes res = new SendOtpToCustomerRes();
            res.setTransId(onboardTransaction.getId());
            res.setOtpTransactionId(otpSendResponse.getOtpTransactionId());

            return ResponseEntity.ok(res);
        } catch (Exception e){
            logger.error("OnboardServiceImpl sendOtpToCustomer with error detail: {}", e);
            throw e;
        }
    }

    @Override
    public ResponseEntity<ConfirmOtpRes> confirmOtp(ConfirmOtpReq request) {
        try {
            logger.info("OnboardServiceImpl confirmOtp with request: {}", new Gson().toJson(request));
            if(Objects.isNull(request)
                    || StringUtils.isBlank(request.getTransId())
                    || StringUtils.isBlank(request.getOtpCode())
                    || StringUtils.isBlank(request.getOtpTransactionId())){
                throw new OnboardingException(ErrorCode.INVALID_REQUEST);
            }
            OnboardTransactionDto onboardTransaction
                    = transactionService.getTransactionById(request.getTransId());
            if(Objects.isNull(onboardTransaction)){
                throw new OnboardingException(ErrorCode.INVALID_REQUEST);
            }
            ValidateOtpReq validateOtpReq = new ValidateOtpReq();
            validateOtpReq.setOtpCode(request.getOtpCode());
            validateOtpReq.setOtpTransactionId(request.getOtpTransactionId());
            ValidateOtpRes validateOtpRes = commonClient.callCommonValidateOtp(validateOtpReq);
            logger.info("OnboardServiceImpl confirmOtp validateOtpRes: {}", toJson(validateOtpRes));
            if(!validateOtpRes.isStatusValidateOtp()){
                throw new OnboardingException(ErrorCode.OTP_FAIL);
            }
            ConfirmOtpRes res = new ConfirmOtpRes();
            res.setTransId(request.getTransId());
            return ResponseEntity.ok(res);
        } catch (Exception e){
            logger.error("OnboardServiceImpl confirmOtp with error detail: {}", e);
            throw e;
        }
    }

    @Override
    public ResponseEntity<ConfirmInfoRes> confirmInfo(ConfirmInfoReq request) {
        try {
            logger.info("OnboardServiceImpl confirmInfo with request: {}", new Gson().toJson(request));
            if(Objects.isNull(request)
                    || StringUtils.isBlank(request.getTransId())
                    || StringUtils.isBlank(request.getIcNumber())){
                throw new OnboardingException(ErrorCode.INVALID_REQUEST);
            }
            OnboardTransactionDto onboardTransaction
                    = transactionService.getTransactionById(request.getTransId());
            if(Objects.isNull(onboardTransaction)){
                throw new OnboardingException(ErrorCode.INVALID_REQUEST);
            }
            logger.info("OnboardServiceImpl confirmInfo onboardTransaction: {}", toJson(onboardTransaction));

            // check ton tai ic ben customer
            RequestUserDto requestUserDto = new RequestUserDto();
            requestUserDto.setIcNumber(onboardTransaction.getIcNumber());
            CustomerDto customerDto = customerClient.getCustomerByIcNumber(requestUserDto);
            logger.info("OnboardServiceImpl confirmInfo customerDto: {}", toJson(customerDto));
            if(Objects.nonNull(customerDto) && StringUtils.isNotBlank(customerDto.getCifNumber())){
                throw new OnboardingException(ErrorCode.CUSTOMER_EXITS);
            }
            ConfirmInfoRes confirmInfoRes = new ConfirmInfoRes();
            confirmInfoRes.setTransId(request.getTransId());
            return ResponseEntity.ok(confirmInfoRes);
        } catch (Exception e){
            logger.error("OnboardServiceImpl confirmInfo with error detail: {}", e);
            throw e;
        }
    }

    @Override
    public ResponseEntity<RegisterCustomerRes> registerCustomer(RegisterCustomerReq request) {
        try {
            logger.info("OnboardServiceImpl registerCustomer with request: {}", new Gson().toJson(request));
            if(Objects.isNull(request)
                    || StringUtils.isBlank(request.getUserName())
                    || StringUtils.isBlank(request.getPassword())){
                throw new OnboardingException(ErrorCode.INVALID_REQUEST);
            }
            OnboardTransactionDto onboardTransaction
                    = transactionService.getTransactionById(request.getTransId());
            if(Objects.isNull(onboardTransaction)){
                throw new OnboardingException(ErrorCode.INVALID_REQUEST);
            }
            logger.info("OnboardServiceImpl registerCustomer onboardTransaction: {}", toJson(onboardTransaction));

            // call customer create cif
            CustomerDto customerDto = new CustomerDto();
            customerDto.setFullName(onboardTransaction.getFullName());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dob = LocalDate.parse(onboardTransaction.getDob(), formatter);
            customerDto.setDateOfBirth(dob);
            customerDto.setPassword(request.getPassword());
            String cifNumber = customerClient.createCustomer(customerDto);
            logger.info("OnboardServiceImpl registerCustomer success with cif: {}", cifNumber);

            //sent email onboard to customer
            SentEmailCustomerReq sentEmailCustomerReq = new SentEmailCustomerReq();
            sentEmailCustomerReq.setEmail(onboardTransaction.getEmail());
            sentEmailCustomerReq.setFullName(onboardTransaction.getFullName());
            sentEmailCustomerReq.setPhoneNumber(onboardTransaction.getPhoneNumber());
            customerClient.sentEmailSuccessToCustomer(sentEmailCustomerReq);

            RegisterCustomerRes res = new RegisterCustomerRes();
            res.setCifNumber(cifNumber);
            return ResponseEntity.ok(res);
        } catch (Exception e){
            logger.error("OnboardServiceImpl registerCustomer with error detail: {}", e);
            throw e;
        }
    }

    @Override
    public ResponseEntity<OcrCardRes> ocrCard(OcrCardReq request) {
        try {
            logger.info("OnboardServiceImpl ocrCard with request: {}", new Gson().toJson(request));
            if(Objects.isNull(request)
                    || StringUtils.isBlank(request.getIcBack())
                    || StringUtils.isBlank(request.getIcFont())){
                throw new OnboardingException(ErrorCode.INVALID_REQUEST);
            }
            OnboardTransactionDto onboardTransaction
                    = transactionService.getTransactionById(request.getTransId());
            if(Objects.isNull(onboardTransaction)){
                throw new OnboardingException(ErrorCode.INVALID_REQUEST);
            }

            ReadIcCardReq readIcCardReq = new ReadIcCardReq();
            readIcCardReq.setPhoneNumber(onboardTransaction.getPhoneNumber());
            readIcCardReq.setIcBack(request.getIcBack());
            readIcCardReq.setIcFont(request.getIcFont());
            ReadIcCardRes res = fileClient.callFileOcr(readIcCardReq);
            logger.info("OnboardServiceImpl ocrCard res: {}", toJson(res));

            //update data base
            OnboardTransactionDto onboardTransactionDto = new OnboardTransactionDto();
            onboardTransactionDto.setIcNumber(res.getId());
            onboardTransactionDto.setFullName(res.getName());
            onboardTransactionDto.setDob(res.getDob());
            onboardTransactionDto.setId(onboardTransaction.getId());
            transactionService.updateTransactionId(onboardTransactionDto);

            OcrCardRes ocrCardRes = new OcrCardRes();
            ocrCardRes.setTransId(onboardTransaction.getId());
            return ResponseEntity.ok(ocrCardRes);
        } catch (Exception e){
            logger.error("OnboardServiceImpl ocrCard with error detail: {}", e);
            throw e;
        }
    }

    private String toJson(Object obj){
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e){
            return ToStringBuilder.reflectionToString(obj);
        }
    }
}
