package onboard.presentation.service.impl;

import onboard.client.CustomerClient;
import onboard.client.FileClient;
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
    private final FileClient fileClient;
    private final CustomerClient customerClient;

    @Autowired
    public ConfigServiceImpl(SdkConfigRepo sdkConfigRepo, FileClient fileClient, CustomerClient customerClient) {
        this.sdkConfigRepo = sdkConfigRepo;
        this.fileClient = fileClient;
        this.customerClient = customerClient;
    }

    @Override
    public List<ConfigSdkModel> getAllConfigSdk() {
       try {
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
       } catch (Exception e){
           System.out.println("ConfigServiceImpl getAllConfigSdk with error detail:"  + e.toString());
           throw e;
       }
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

    @Override
    public String getOnboardCallFile() {
        String callFile = fileClient.callFileClient();
        System.out.println("getOnboardCallFile callFile kq: " + callFile);
        String kqCallCustomer = customerClient.callCustomerClient();
        System.out.println("getOnboardCallFile kqCallCustomer: " + kqCallCustomer);
        return callFile + " and " + kqCallCustomer;
    }
}
