package com.pankaj.core.reporters;

import com.pankaj.core.models.Order;
import com.pankaj.core.models.OrderVersion;
import com.pankaj.core.models.Position;
import com.pankaj.core.stores.OrderStore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

import static com.pankaj.core.enums.SIDE_TYPE.*;
import static com.pankaj.core.enums.TXN_TYPE.*;
import static com.pankaj.core.utils.OrderUtils.*;

public enum PositionReporter implements Reporter {
    INSTANCE;

    private final ConcurrentHashMap<String, Boolean> reportedOrderVersions = new ConcurrentHashMap<>();
    private final OrderStore store = OrderStore.getInstance();
    private final ConcurrentHashMap<String, Long> positions = new ConcurrentHashMap<>();
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public synchronized void print() {
        positions.forEach((key, value) -> System.out.println(String.format("%s \t %d", key, value)));
    }

    //TODO: Refactor for process only new orders
    public void prepare() {
        Map<String, Long> newPositions = this.store.getStore().stream()
                .filter(order -> order.getVersions().size() > 0)
                .map(order -> {
                    String sym = getUpdatedSymbolOfOrder(order);
                    long valueOfOrder = getNewValueOfOrder(order);
                    return new Position(sym, valueOfOrder);
                }).collect(Collectors.toConcurrentMap(Position::getSym, Position::getValue, Long::sum));

        positions.putAll(newPositions);
    }

    private String getUpdatedSymbolOfOrder(Order order) {
        List<OrderVersion> vers = order.getVersionsForRead().stream()
                .filter(ov -> !ov.getTxnType().equals(CANCEL))
                .sorted().collect(Collectors.toList());
        return vers.get(vers.size() - 1).getSym();
    }

    public static Reporter getInstance() {
        return INSTANCE;
    }

    private long getNewValueOfOrder(Order order) {
        if (isOrderCanceled(order)) return 0;

        List<OrderVersion> newVersions = order.getVersionsForRead().stream().sorted().collect(Collectors.toList());
        int len = newVersions.size();
        long sum = 0;
        if (len > 0) {
            OrderVersion ver = newVersions.get(len - 1);
            sum = (ver.getSideType().equals(SELL) ? ver.getQty() * -1 : ver.getQty());
        }
        return sum;
    }
}