package com.pankaj.core.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class Order {
    private final long orderId;
    private final List<OrderVersion> versions;

    public static Order swallowCopyOfAllLists(Order order) {
        return Order.builder()
                .orderId(order.getOrderId())
                .versions(order.getVersionsForRead())
                .build();
    }

    public List<OrderVersion> getVersionsForRead() {
        return Collections.unmodifiableList(getVersions());
    }
}
