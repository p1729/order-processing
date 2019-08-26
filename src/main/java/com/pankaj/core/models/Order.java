package com.pankaj.core.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Collections;
import java.util.List;

import static com.pankaj.core.utils.OrderUtils.copyOfList;

@Data
@Builder
@AllArgsConstructor
public class Order {
    private final long orderId;
    private final List<OrderVersion> versions;

    public static Order shallowCopyOfAllLists(Order order) {
        return Order.builder()
                .orderId(order.getOrderId())
                .versions(copyOfList(order.getVersions()))
                .build();
    }

    public List<OrderVersion> getVersionsForRead() {
        return Collections.unmodifiableList(getVersions());
    }
}