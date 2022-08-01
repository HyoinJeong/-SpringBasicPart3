package org.prgms.kdt;

import org.prgms.kdt.order.OrderProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"org.prgms.kdt.voucher", "org.prgms.kdt.order", "org.prgms.kdt.configuration"})
public class KdtSpringOrderApplication {
    private static final Logger logger = LoggerFactory.getLogger(OrderTester.class);

    public static void main(String[] args) {

        var applicationContext = SpringApplication.run(KdtSpringOrderApplication.class, args);
        var orderProperties=applicationContext.getBean(OrderProperties.class);
        logger.warn("logger name => {}", logger.getName());
        logger.error("version -> {}", orderProperties.getVersion());
        logger.info("minimumOrderAmount -> {}", orderProperties.getMinimumOrderAmount());
        logger.info("supportVendors -> {}", orderProperties.getSupportVendors());
        logger.info("description -> {}", orderProperties.getDescription());

//        var springApplicaion = new SpringApplication(KdtSpringOdrderApplication.class);
//        springApplicaion.setAdditionalProfiles("local");
//        var applicationContext=springApplicaion.run(args);
////        var applicationContext = SpringApplication.run(KdtSpringOdrderApplication.class, args);
//
//        var orderProperties = applicationContext.getBean(OrderProperties.class);
//        System.out.println(MessageFormat.format("version -> {0}", orderProperties.getVersion()));
//        System.out.println(MessageFormat.format("minimumOrderAmount -> {0}", orderProperties.getMinimumOrderAmount()));
//        System.out.println(MessageFormat.format("supportVendors -> {0}", orderProperties.getSupportVendors()));
//        System.out.println(MessageFormat.format("description -> {0}", orderProperties.getDescription()));
//
//        var customerId = UUID.randomUUID();
//        var voucherRepository = applicationContext.getBean(VoucherRepository.class);
//        var voucher = voucherRepository.insert(new FixedAmountVoucher(UUID.randomUUID(), 10L));
//
//        System.out.println(MessageFormat.format("is Jdbc Repo -> {0}", voucherRepository instanceof JDBCVoucherRepository));
//        System.out.println(MessageFormat.format("is Jdbc Repo -> {0}", voucherRepository.getClass().getCanonicalName()));
    }

}
