package com.pankaj.core.rules.impl;

import com.pankaj.core.enums.TXN_TYPE;
import com.pankaj.core.models.Order;
import com.pankaj.core.models.OrderVersion;
import com.pankaj.core.models.Transaction;
import com.pankaj.core.rules.Rule;

public class FirstOrderInsertRule implements Rule<Transaction, Order> {

    @Override
    public boolean apply(Transaction transaction, Order processedOrder) {
        return transaction.getVersionId() == OrderVersion.FIRST_ORDER_VERSION &&
                transaction.getTxnType().equals(TXN_TYPE.INSERT) && processedOrder==null;
    }

}
