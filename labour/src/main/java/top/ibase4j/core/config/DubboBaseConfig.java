package top.ibase4j.core.config;

import org.springframework.context.annotation.Bean;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.RegistryConfig;

import top.ibase4j.core.util.PropertiesUtil;

public class DubboBaseConfig {
    @Bean
    public ApplicationConfig application() {
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setQosPort(PropertiesUtil.getInt("rpc.protocol.port", 22222) + 10);
        applicationConfig.setName(PropertiesUtil.getString("rpc.registry.name"));
        applicationConfig.setLogger("slf4j");
        return applicationConfig;
    }

    @Bean
    public RegistryConfig registry() {
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress(PropertiesUtil.getString("rpc.address"));
        registryConfig.setProtocol(PropertiesUtil.getString("rpc.registry"));
        registryConfig.setFile(PropertiesUtil.getString("rpc.cache.dir") + "/dubbo-"
                + PropertiesUtil.getString("rpc.registry.name") + ".cache");
        return registryConfig;
    }

}
