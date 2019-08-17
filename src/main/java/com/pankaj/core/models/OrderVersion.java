package com.pankaj.core.models;

import com.pankaj.core.enums.SIDE_TYPE;
import com.pankaj.core.enums.TXN_TYPE;
import lombok.Data;

@Data
public class OrderVersion implements Comparable<OrderVersion> {

    public static final long FIRST_ORDER_VERSION = 1L;

    private final long versionId;
    private final String sym;
    private final long qty;
    private final Enum<TXN_TYPE> txnType;
    private final Enum<SIDE_TYPE> sideType;

    public int compareTo(OrderVersion second) {
        return Long.compare(this.versionId, second.versionId);
    }
}
