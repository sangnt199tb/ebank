package onboard.presentation.service;

import onboard.presentation.model.ConfigSdkModel;

import java.util.List;

public interface ConfigService {
    List<ConfigSdkModel> getAllConfigSdk();

    ConfigSdkModel getAllConfigSdkById();

    String getOnboardCallFile();
}
