package com.pankaj.core.models;

import com.pankaj.core.enums.SideType;
import com.pankaj.core.enums.TransactionType;
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
    private final Enum<TransactionType> txnType;
    private final Enum<SideType> sideType;
}
