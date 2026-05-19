package onboard.integration.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import onboard.integration.model.CompareFaceInterReq;
import onboard.integration.model.CompareFaceInterRes;
import onboard.integration.service.EkycService;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EkycServiceImpl implements EkycService {

    private static Logger logger = LoggerFactory.getLogger(EkycServiceImpl.class);
    private final RestTemplate restTemplate;
    private static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    public EkycServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public CompareFaceInterRes compareFaceAi(CompareFaceInterReq req) {
        logger.info("EkycServiceImpl compareFaceAi request: {}", toJson(req));
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<CompareFaceInterReq> httpEntity = new HttpEntity<>(req, headers);
            ResponseEntity<CompareFaceInterRes> response = restTemplate.postForEntity(
                    "https://face-matcher-cccd-237625122667.asia-southeast1.run.app/api/match-faces",
                    httpEntity, CompareFaceInterRes.class);

            CompareFaceInterRes compareFaceRes = response.getBody();
            return compareFaceRes;

        } catch (Exception e){
            logger.error("EkycServiceImpl compareFaceAi with error detail: {}", e);
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
