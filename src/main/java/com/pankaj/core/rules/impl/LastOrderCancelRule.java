package com.pankaj.core.rules.impl;

import com.pankaj.core.enums.TXN_TYPE;
import com.pankaj.core.models.Order;
import com.pankaj.core.models.Transaction;
import com.pankaj.core.rules.Rule;

import static com.pankaj.core.utils.OrderUtils.isNotFirstOrderVersion;
import static com.pankaj.core.utils.OrderUtils.isOrderCanceled;

public class LastOrderCancelRule implements Rule<Transaction, Order> {

    @Override
    public boolean apply(Transaction txn, Order processedOrder) {
        return isOrderCanceled(processedOrder) &&
                txn.getTxnType().equals(TXN_TYPE.CANCEL) && isNotFirstOrderVersion(txn);
    }


}