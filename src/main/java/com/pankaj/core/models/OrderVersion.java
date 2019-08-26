package com.pankaj.core.models;

import com.pankaj.core.enums.SideType;
import com.pankaj.core.enums.TransactionType;
import lombok.Data;

@Data
public class OrderVersion implements Comparable<OrderVersion> {

    public static final long FIRST_ORDER_VERSION = 1L;

    private final long versionId;
    private final String sym;
    private final long qty;
    private final Enum<TransactionType> txnType;
    private final Enum<SideType> sideType;

    public int compareTo(OrderVersion second) {
        return Long.compare(this.versionId, second.versionId);
    }
}
