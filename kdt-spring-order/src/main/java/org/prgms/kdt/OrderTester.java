package org.prgms.kdt;

import org.prgms.kdt.order.OrderItem;
import org.prgms.kdt.order.OrderProperties;
import org.prgms.kdt.order.OrderService;
import org.prgms.kdt.voucher.FixedAmountVoucher;
import org.prgms.kdt.voucher.VoucherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.util.Assert;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.UUID;

public class OrderTester {
    private static final Logger logger = LoggerFactory.getLogger(OrderTester.class);

    public static void main(String[] args) throws IOException {
        AnsiOutput.setEnabled(AnsiOutput.Enabled.ALWAYS);
        var applicationContext=new AnnotationConfigApplicationContext(AppConfiguration.class);

//        var resource=applicationContext.getResource("application.yaml");
//        var resource2=applicationContext.getResource("file:test/sample.txt");
//        var resource3=applicationContext.getResource("https://stackoverflow.com/");
//
//        System.out.println(MessageFormat.format("Resource -> {0}", resource3.getClass().getCanonicalName()));
//
//        var readableByteChannel=Channels.newChannel(resource3.getURL().openStream());
//        var bufferedReader=new BufferedReader(Channels.newReader(readableByteChannel, StandardCharsets.UTF_8));
//        var contents = bufferedReader.lines().collect(Collectors.joining("\n"));
//        System.out.println(contents);

//        var file = resource.getFile();
//        var strings = Files.readAllLines(file.toPath());
//        System.out.println(strings.stream().reduce("",(a,b)->a+"\n"+b));

//        applicationContext.register(AppConfiguration.class);
//        var enviroment = applicationContext.getEnvironment();
//        enviroment.setActiveProfiles("local");
//        applicationContext.refresh();

        var orderProperties=applicationContext.getBean(OrderProperties.class);
        logger.warn("logger name => {}", logger.getName());
        logger.error("version -> {}", orderProperties.getVersion());
        logger.info("minimumOrderAmount -> {}", orderProperties.getMinimumOrderAmount());
        logger.info("supportVendors -> {}", orderProperties.getSupportVendors());
        logger.info("description -> {}", orderProperties.getDescription());

//        var enviroment = applicationContext.getEnvironment();
//        var version = enviroment.getProperty("kdt.version");
//        var minimumOrderAmount = enviroment.getProperty("kdt.minimum-order-amount", Integer.class);
//        var supportVendors = enviroment.getProperty("kdt.support-vendors", List.class);
//        var description = enviroment.getProperty("kdt.description", List.class);
//        System.out.println(MessageFormat.format("version -> {0}", version));
//        System.out.println(MessageFormat.format("minimumOrderAmount -> {0}", minimumOrderAmount));
//        System.out.println(MessageFormat.format("supportVendors -> {0}", supportVendors));
//        System.out.println(MessageFormat.format("description -> {0}", description));

        var customerId = UUID.randomUUID();

//        var voucherRepository=BeanFactoryAnnotationUtils.qualifiedBeanOfType(applicationContext.getBeanFactory(),VoucherRepository.class,"memory");
//        var voucherRepository2=BeanFactoryAnnotationUtils.qualifiedBeanOfType(applicationContext.getBeanFactory(),VoucherRepository.class,"memory");
//        var voucherRepository=applicationContext.getBean(VoucherRepository.class); // interface를 호출하는

        var voucherRepository=applicationContext.getBean(VoucherRepository.class);

        var voucher = voucherRepository.insert(new FixedAmountVoucher(UUID.randomUUID(),10L));

//        System.out.println(MessageFormat.format("is Jdbc Repo -> {0}", voucherRepository instanceof JDBCVoucherRepository));
//        System.out.println(MessageFormat.format("is Jdbc Repo -> {0}", voucherRepository.getClass().getCanonicalName()));

        var orderService=applicationContext.getBean(OrderService.class);
        var order = orderService.createOrder(customerId,new ArrayList<OrderItem>(){{
            add(new OrderItem(UUID.randomUUID(),100L,1));
        }}, voucher.getVoucherId());

        Assert.isTrue(order.totalAmount()==90L, MessageFormat.format("totalAmount {0} is not 90L", order.totalAmount()));

        applicationContext.close();
    }
}
