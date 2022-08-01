package org.prgms.kdt;

import org.prgms.kdt.configuration.YamlPropertiesFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = {"org.prgms.kdt.voucher","org.prgms.kdt.order", "org.prgms.kdt.configuration"})
@PropertySource(value = "application.yaml", factory = YamlPropertiesFactory.class )
@EnableConfigurationProperties
public class AppConfiguration {


//    @Bean(initMethod = "init")
//    public BeanOne beanOne(){
//        return new BeanOne();
//    }
}

//class BeanOne implements InitializingBean {
//
//    private void init() {
//        System.out.println("[BeanOne] init called!");
//    }
//
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        System.out.println("[BeanOne] afterPropertiesSet called!");
//    }
//}

