package org.prgms.kdt.order;

import org.prgms.kdt.voucher.VoucherService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderService {// Voucher를 이용하여 Order객체를 만들어줌
    private final VoucherService voucherService;
    private final OrderRepository orderRepository;

    public OrderService(VoucherService voucherService, OrderRepository orderRepository) {
        this.voucherService = voucherService;
        this.orderRepository = orderRepository;
    }

    public Order createOrder(UUID customerId, List<OrderItem> orderItems){
        var order = new Order(UUID.randomUUID(),customerId,orderItems);
        return orderRepository.insert(order); // order이 영속성 보장
    }

    public Order createOrder(UUID customerId, List<OrderItem> orderItems, UUID voucherId){
        var voucher=voucherService.getVoucher(voucherId);
        var order = new Order(UUID.randomUUID(),customerId,orderItems, voucher);
        orderRepository.insert(order); // order이 영속성 보장
        voucherService.useVoucher(voucher);
        return order;
    }
}
