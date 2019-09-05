package com.pankaj.core.processors;

import com.pankaj.core.models.Order;
import com.pankaj.core.models.OrderVersion;
import com.pankaj.core.models.Transaction;
import com.pankaj.core.reporters.PositionReporter;
import com.pankaj.core.reporters.Reporter;
import com.pankaj.core.rules.RulesStore;
import com.pankaj.core.stores.OrderStore;
import com.pankaj.core.stores.TransactionStore;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.pankaj.core.enums.TransactionStatus.*;

public enum TransactionProcessor implements Processor {
    INSTANCE;

    //TODO: Move this some other -- shared data (across threads)
    private final TransactionStore store = TransactionStore.getInstance();
    //TODO: Move this some other part -- shared data (across threads) -- need for reporting --> final position
    private final OrderStore orders = OrderStore.getInstance();
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);

    public static Processor getInstance() {
        return INSTANCE;
    }

    @Override
    public void process(Transaction txn) {
        executorService.execute(() -> {
                Order order = orders.getOrderById(txn.getOrderId()).orElse(null);
                if (RulesStore.getPendingTransactionOrderRule().test(txn, order)) {
                    store.putTransactionWithStatus(txn, PENDING);
                } else if (RulesStore.getRejectedTransactionRule().test(txn, order)) {
                    store.putTransactionWithStatus(txn, REJECTED);
                } else if (RulesStore.getTransactionOrderProcessingRule().test(txn, order)) {
                    store.putTransactionWithStatus(txn, PROCESSED);
                    List<Transaction> pendingTxns = store.getTransactionOfOrdersByStatus(txn.getOrderId(), PENDING);
                    OrderVersion version = new OrderVersion(txn.getVersionId(), txn.getSymbol(), txn.getQty(), txn.getTxnType(), txn.getSideType());
                    if (Objects.isNull(order)) {
                        orders.addOrder(txn.getOrderId(), version);
                    } else {
                        orders.addOrderVersion(txn.getOrderId(), version);
                    }
                    store.removeTransactionsOfStatus(pendingTxns, PENDING);
                    //TODO: Refactor by events maybe or Q or AOP ?
                    PositionReporter.getInstance().prepare();
                    pendingTxns.forEach(this::process);
                }
        });
    }

    @Override
    public Reporter getReporter() {
        return PositionReporter.getInstance();
    }
}
