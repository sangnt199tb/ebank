package file.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@ConfigurationProperties(prefix = "tpb.hydro.file")
@Data
public class FileAppConfiguration {
    private String ocrUrl;
    private String apiKey;
}
