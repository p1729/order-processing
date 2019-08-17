package com.pankaj.core.rules;

import com.pankaj.core.models.Order;
import com.pankaj.core.models.Transaction;

import static com.pankaj.core.enums.TXN_TYPE.*;
import static com.pankaj.core.utils.OrderUtils.isNotFirstOrderVersion;
import static com.pankaj.core.utils.OrderUtils.isOrderCanceled;
import static com.pankaj.core.utils.OrderUtils.getNextOrderVersionInSequence;
import static com.pankaj.core.models.OrderVersion.FIRST_ORDER_VERSION;

public class RulesStore {

    private static Rule<Transaction, Order> firstOrderInsertRule = (txn, order) ->
            txn.getVersionId() == FIRST_ORDER_VERSION && txn.getTxnType().equals(INSERT) && order == null;

    private static Rule<Transaction, Order> lastOrderCancelRule = (txn, processedOrder) ->
            !isOrderCanceled(processedOrder) && txn.getTxnType().equals(CANCEL) && isNotFirstOrderVersion(txn);

    private static Rule<Transaction, Order> middleOrderUpdateRule = (txn, processedOrder) ->
            isNotFirstOrderVersion(txn) && !isOrderCanceled(processedOrder) && txn.getTxnType().equals(UPDATE);

    private static Rule<Transaction, Order> nextOrderVersionRule = (txn, processedOrder) ->
            getNextOrderVersionInSequence(processedOrder) == txn.getVersionId();

    public static Rule<Transaction, Order> getTransactionOrderProcessingRule() {
        return nextOrderVersionRule.and(firstOrderInsertRule.or(lastOrderCancelRule).or(middleOrderUpdateRule));
    }

    public static Rule<Transaction, Order> getPendingTransactionOrderRule() {
        return nextOrderVersionRule.negate().and(lastOrderCancelRule.or(middleOrderUpdateRule));
    }

    public static Rule<Transaction, Order> getRejectedTransactionRule() {
        return getPendingTransactionOrderRule().negate().and(getTransactionOrderProcessingRule().negate());
    }
}
