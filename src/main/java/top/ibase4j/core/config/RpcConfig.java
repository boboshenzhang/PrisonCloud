package top.ibase4j.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.type.AnnotatedTypeMetadata;

import com.alibaba.dubbo.config.ConsumerConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.ProviderConfig;

import top.ibase4j.core.util.PropertiesUtil;

/**
 * RPC服务配置
 * @author ShenHuaJie
 * @since 2017年8月14日 上午10:16:18
 */
public class RpcConfig {
    public static class EnableDubboService implements Condition {
        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            return "dubbo".equals(PropertiesUtil.getString("rpc.type"))
                    && "Y".equals(PropertiesUtil.getString("rpc.dubbo.service"));
        }
    }

    public static class EnableDubboReference implements Condition {
        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            return "dubbo".equals(PropertiesUtil.getString("rpc.type"))
                    && "Y".equals(PropertiesUtil.getString("rpc.dubbo.reference"));
        }
    }

    public static class EnableMotan implements Condition {
        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            return "motan".equals(PropertiesUtil.getString("rpc.type"));
        }
    }

    @Configuration
    @Conditional(RpcConfig.EnableDubboService.class)
    static class DubboServiceConfig extends DubboBaseConfig {
        @Bean
        public ProviderConfig provider() {
            ProviderConfig providerConfig = new ProviderConfig();
            providerConfig.setFilter("dataSourceAspect,default");
            return providerConfig;
        }

        @Bean
        public ProtocolConfig protocol() {
            ProtocolConfig protocolConfig = new ProtocolConfig();
            protocolConfig.setPort(PropertiesUtil.getInt("rpc.protocol.port", 20880));
            protocolConfig.setThreadpool("cached");
            protocolConfig.setThreads(PropertiesUtil.getInt("rpc.protocol.maxThread", 100));
            protocolConfig.setPayload(PropertiesUtil.getInt("rpc.protocol.maxContentLength", 1048576));
            return protocolConfig;
        }
    }

    @Configuration
    @Conditional(RpcConfig.EnableDubboReference.class)
    static class DubboConsumerConfig extends DubboBaseConfig {
        @Bean
        public ConsumerConfig consumer() {
            ConsumerConfig consumerConfig = new ConsumerConfig();
            consumerConfig.setLoadbalance("leastactive");
            consumerConfig.setTimeout(PropertiesUtil.getInt("rpc.request.timeout", 20000));
            consumerConfig.setRetries(PropertiesUtil.getInt("rpc.consumer.retries", 0));
            consumerConfig.setCheck(false);
            return consumerConfig;
        }
    }

    @Configuration
    @Conditional(RpcConfig.EnableMotan.class)
    @ImportResource({"classpath*:spring/motan.xml"})
    static class MotanConfig {
    }
}
