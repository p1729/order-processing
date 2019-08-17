package com.pankaj.core.models;

import com.pankaj.core.enums.SIDE_TYPE;
import com.pankaj.core.enums.TXN_TYPE;
import lombok.Data;

import java.util.Comparator;

@Data
public class Transaction {

    public static final Comparator<Transaction> ORDER_VERSION_COMPARATOR = Comparator.comparingLong(Transaction::getVersionId);

    private final long txnId;
    private final long orderId;
    private final long versionId;
    private final String symbol;
    private final long qty;
    private final Enum<TXN_TYPE> txnType;
    private final Enum<SIDE_TYPE> sideType;
}
