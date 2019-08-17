package com.pankaj.core.utils;

import com.pankaj.core.enums.TXN_TYPE;
import com.pankaj.core.models.Order;
import com.pankaj.core.models.OrderVersion;
import com.pankaj.core.models.Transaction;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OrderUtils {

    public static boolean isNotFirstOrderVersion(Transaction txn) {
        return OrderVersion.FIRST_ORDER_VERSION != txn.getVersionId();
    }

    public static boolean isOrderCanceled(Order processedOrder) {
        if(Objects.isNull(processedOrder)) return false;
        return processedOrder.getVersions().stream()
                .anyMatch(ver -> ver.getTxnType().equals(TXN_TYPE.CANCEL));
    }

    public static long getNextOrderVersionInSequence(Order order) {
        if(Objects.isNull(order)) return OrderVersion.FIRST_ORDER_VERSION;
        List<OrderVersion> sortedVers = order.getVersions()
                .stream().sorted().collect(Collectors.toList());
        return sortedVers.get(sortedVers.size()-1).getVersionId() + 1;
    }
}
