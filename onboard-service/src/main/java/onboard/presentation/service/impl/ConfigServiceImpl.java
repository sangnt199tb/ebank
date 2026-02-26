package onboard.presentation.service.impl;

import onboard.persistence.domain.SdkConfigEntity;
import onboard.persistence.repository.SdkConfigRepo;
import onboard.presentation.model.ConfigSdkModel;
import onboard.presentation.service.ConfigService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ConfigServiceImpl implements ConfigService {

    private final SdkConfigRepo sdkConfigRepo;

    @Autowired
    public ConfigServiceImpl(SdkConfigRepo sdkConfigRepo) {
        this.sdkConfigRepo = sdkConfigRepo;
    }

    @Override
    public List<ConfigSdkModel> getAllConfigSdk() {
        List<SdkConfigEntity> list = sdkConfigRepo.findAll();
        System.out.println("ConfigServiceImpl list: " + list.toString());
        System.out.println("ConfigServiceImpl list size: " + list.size());
        return list.stream().map(
                e -> {
                 ConfigSdkModel configSdkModel = new ConfigSdkModel();
                 configSdkModel.setId(e.getId());
                 configSdkModel.setPartnerName(e.getPartnerName());
                 configSdkModel.setDescription(e.getDescription());
                 configSdkModel.setStatusNfc(e.getPriority());
                 configSdkModel.setPriority(e.getPriority());
                 configSdkModel.setCreateDate(e.getCreateDate());
                 configSdkModel.setUpdateDate(e.getUpdateDate());
                 configSdkModel.setRequestLimit(e.getRequestLimit());
                 configSdkModel.setRequestQuantity(e.getRequestQuantity());
                 return configSdkModel;
                }
        ).collect(Collectors.toList());
    }

    @Override
    public ConfigSdkModel getAllConfigSdkById() {
        Optional<SdkConfigEntity> config =  sdkConfigRepo.findById("SDK001");
        System.out.println("KQ:" + config.get().getPartnerName());
        List<SdkConfigEntity> list = sdkConfigRepo.findAll();
        SdkConfigEntity configEntity = list.stream()
                .filter(e -> StringUtils.equalsIgnoreCase("SDK001", e.getId()))
                .findFirst().orElse(null);
        ConfigSdkModel configSdkModel = new ConfigSdkModel();
        configSdkModel.setId(configEntity.getId());
        configSdkModel.setPartnerName(configEntity.getPartnerName());
        return configSdkModel;

    }
}
