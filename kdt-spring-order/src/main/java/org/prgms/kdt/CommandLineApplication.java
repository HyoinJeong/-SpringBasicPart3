package org.prgms.kdt;

import org.prgms.kdt.order.OrderService;
import org.prgms.kdt.voucher.VoucherRepository;
import org.prgms.kdt.voucher.VoucherService;
import org.springframework.beans.factory.annotation.BeanFactoryAnnotationUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Scanner;
import java.util.UUID;

public class CommandLineApplication {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        var applicationContext = new AnnotationConfigApplicationContext(AppConfiguration.class);

        var customerId= UUID.randomUUID();

        var orderService=applicationContext.getBean(OrderService.class);

        var voucherRepository = BeanFactoryAnnotationUtils.qualifiedBeanOfType(applicationContext.getBeanFactory(), VoucherRepository.class,"memory");

        var voucherService = applicationContext.getBean(VoucherService.class);

        while (true){
            System.out.println("=== Voucher Program ===");
            System.out.println("Type exit to exit the program.");
            System.out.println("Type create to create a new voucher.");
            System.out.println("Type list to list all vouchers.");

            String type = scanner.nextLine();

            switch (type){
                case "exit":
                    System.out.println("exit the program");
                    System.exit(0);
                    break;
                case "create":
                    System.out.println("create a new voucher");
                    System.out.println("select what kind of voucher : FIXED, PERCENT");
                    String v_type = scanner.nextLine();// 받을 때 정규표현식으로 받는게 나을수도?
                    System.out.println("enter amount");
                    int amount = scanner.nextInt();

                    // VoucherType 생성(FIXED, PERCENT) -> 꼭 필요한가?
                    // VoucherService의 createVoucher(v_type, amount) 실행


//                    VoucherType voucherType = VoucherType.FIXED;
//
//                    if (v_type.equals("FixedAmountVoucher")){
//                        voucherRepository.insert()
//                    }
//                    else if(v_type.equals("PercentDiscountVoucher")){
//
//                    }
//                    else {
//                        System.out.println("잘못된 voucher를 선택했습니다. 처음부터 다시 시작해주세요");
//                    }
                    break;
                case "list":
                    System.out.println("list all vouchers");
                    break;
                default:
                    System.out.println("형식에 맞지 않은 입력입니다. 다시 입력해주세요");
                    break;
            }

        }
    }
}
