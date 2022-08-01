package org.prgms.kdt.voucher;

import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.UUID;

@Service
public class VoucherService { // getVoucher를 통해 voucherId에 해당하는 voucher가 있는지 확인 후 반환
                                // OrderService에서 이용됨
//    @Autowired
    private  VoucherRepository voucherRepository;

    public VoucherService(VoucherRepository voucherRepository) {
        this.voucherRepository = voucherRepository;
    }

    public Voucher getVoucher(UUID voucherId) {

        return voucherRepository
                .findById(voucherId)
                .orElseThrow(() -> new RuntimeException(MessageFormat.format("Can not find a voucher for {0}",voucherId)));
    }

    public void useVoucher(Voucher voucher) {

    }

    // 미션 1 - commend line
    public Voucher createVoucher(String v_type ,int amount){
        VoucherType voucherType = VoucherType.FIXED;
        Voucher voucher;

        if (v_type.equals("PERCENT")){
            voucherType=VoucherType.PERCENT;
            voucher = new PercentDiscountVoucher(UUID.randomUUID(),amount);
        }
        else {
            voucher = new FixedAmountVoucher(UUID.randomUUID(),amount);
        }
        return voucherRepository.insert(voucher);
    }

//    @Autowired
//    public void setVoucherRepository(VoucherRepository voucherRepository) {
//        this.voucherRepository = voucherRepository;
//    }
}
