package org.prgms.kdt.order;

import java.util.UUID;

public class OrderItem {
    public final UUID productId;
    public final long productPrice;
    public final long quantity;

    public OrderItem(UUID productId, long productPrice, int quantituy) {
        this.productId = productId;
        this.productPrice = productPrice;
        this.quantity = quantituy;
    }

    public UUID getProductId() {
        return productId;
    }

    public long getProductPrice() {
        return productPrice;
    }

    public long getQuantity() {
        return quantity;
    }
}
