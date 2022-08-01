package org.prgms.kdt.order;

import org.prgms.kdt.voucher.Voucher;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class Order {
    private final UUID orderid;
    private final UUID customerId;
    private final List<OrderItem> orderItems;
    private Optional<Voucher> voucher;
    private OrderStatus orderStatus =  OrderStatus.ACCEPTED;

    public Order(UUID orderid, UUID customerId, List<OrderItem> orderItems) {
        this.orderid = orderid;
        this.customerId = customerId;
        this.orderItems = orderItems;
        this.voucher=Optional.empty();
    }

    public Order(UUID orderid, UUID customerId, List<OrderItem> orderItems, Voucher voucher) {
        this.orderid = orderid;
        this.customerId = customerId;
        this.orderItems = orderItems;
        this.voucher=Optional.of(voucher);
    }

    public long totalAmount(){
        Long beforeDiscount = orderItems.stream()
                .map(v -> v.getProductPrice() * v.getQuantity())
                .reduce(0L, Long::sum);
        if(voucher.isPresent()){
            return voucher.get().discount(beforeDiscount);
        }
        else {
            return beforeDiscount;
        }
    }

    public void setDiscountAmount(long discountAmount) {
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public UUID getOrderid() {
        return orderid;
    }

    public Optional<Voucher> getVoucher() {
        return voucher;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public UUID getCustomerId() {
        return customerId;
    }
}
