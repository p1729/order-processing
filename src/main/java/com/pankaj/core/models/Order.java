package com.pankaj.core.models;

import lombok.Data;

import java.util.List;

@Data
public class Order {
    private final long orderId;
    private final List<OrderVersion> versions;
}
