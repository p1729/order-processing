package com.pankaj.core.rules.impl;

import com.pankaj.core.models.Order;
import com.pankaj.core.models.Transaction;
import com.pankaj.core.rules.Rule;
import com.pankaj.core.utils.OrderUtils;

public class MiddleOrderUpdateRule implements Rule<Transaction, Order> {

    @Override
    public boolean apply(Transaction txn, Order processedOrder) {
        return OrderUtils.isNotFirstOrderVersion(txn) && !OrderUtils.isOrderCanceled(processedOrder);
    }
}
