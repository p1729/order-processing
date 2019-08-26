package com.pankaj.core.processors;

import com.pankaj.core.reporters.PositionReporter;
import com.pankaj.core.reporters.Reporter;
import com.pankaj.core.models.*;
import com.pankaj.core.rules.RulesStore;
import com.pankaj.core.stores.OrderStore;
import com.pankaj.core.stores.TransactionStore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.pankaj.core.enums.TransactionStatus.*;
import static com.pankaj.core.models.Transaction.ORDER_VERSION_COMPARATOR;

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
                    pendingTxns.sort(ORDER_VERSION_COMPARATOR);
                    if (Objects.isNull(order)) {
                        order = new Order(txn.getOrderId(), new ArrayList<>());
                        orders.addOrder(order);
                    }
                    orders.addOrderVersion(txn.getOrderId(), new OrderVersion(txn.getVersionId(), txn.getSymbol(), txn.getQty(), txn.getTxnType(), txn.getSideType()));
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
