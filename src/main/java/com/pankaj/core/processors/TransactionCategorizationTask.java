package com.pankaj.core.processors;

import com.pankaj.core.models.Order;
import com.pankaj.core.models.OrderVersion;
import com.pankaj.core.models.Transaction;
import com.pankaj.core.reporters.PositionReporter;
import com.pankaj.core.rules.RulesStore;
import com.pankaj.core.stores.OrderStore;
import com.pankaj.core.stores.TransactionStore;

import java.util.List;
import java.util.Objects;

import static com.pankaj.core.enums.TransactionStatus.*;

public class TransactionCategorizationTask implements Runnable {
    //TODO: Move this some other -- shared data (across threads)
    private final TransactionStore store = TransactionStore.getInstance();
    //TODO: Move this some other part -- shared data (across threads) -- need for reporting --> final position
    private final OrderStore orders = OrderStore.getInstance();
    private final Transaction txn;

    public TransactionCategorizationTask(Transaction txn) {
        this.txn = txn;
    }

    @Override
    public void run() {
        startProcessTxn(txn);
    }

    private void startProcessTxn(Transaction txn) {
        Order order = orders.getOrderById(txn.getOrderId()).orElse(null);
        if (RulesStore.getPendingTransactionOrderRule().test(txn, order)) {
            store.putTransactionWithStatus(txn, PENDING);
        } else if (RulesStore.getRejectedTransactionRule().test(txn, order)) {
            store.putTransactionWithStatus(txn, REJECTED);
        } else if (RulesStore.getTransactionOrderProcessingRule().test(txn, order)) {
            store.putTransactionWithStatus(txn, PROCESSED);
            addTransactionToOrder(order);
            processPendingTransaction();
        }
    }

    private void processPendingTransaction() {
        List<Transaction> pendingTxns = store.getTransactionOfOrdersByStatus(txn.getOrderId(), PENDING);
        store.removeTransactionsOfStatus(pendingTxns, PENDING);
        //TODO: Refactor by events maybe or Q or AOP ?
        PositionReporter.getInstance().prepare();
        pendingTxns.forEach(this::startProcessTxn);
    }

    private void addTransactionToOrder(Order order) {
        OrderVersion version = new OrderVersion(txn.getVersionId(), txn.getSymbol(), txn.getQty(), txn.getTxnType(), txn.getSideType());
        if (Objects.isNull(order)) {
            orders.addOrder(txn.getOrderId(), version);
        } else {
            orders.addOrderVersion(txn.getOrderId(), version);
        }
    }
}
