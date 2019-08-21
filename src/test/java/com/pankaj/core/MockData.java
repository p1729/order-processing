package com.pankaj.core;

import com.pankaj.core.models.Order;
import com.pankaj.core.models.OrderVersion;
import com.pankaj.core.models.Transaction;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.BiPredicate;

import static com.pankaj.core.enums.SIDE_TYPE.*;
import static com.pankaj.core.enums.TXN_TYPE.*;
import static java.util.List.of;

public class MockData {
    public static Order orderWithInsert = new Order(1L, of(
            new OrderVersion(1L, "ABC", 1000L, INSERT, BUY)
    ));

    public static Order orderWithUpdate = new Order(1L, of(
            new OrderVersion(1L, "ABC", 1000L, INSERT, BUY),
            new OrderVersion(2L, "ABC", 1000L, UPDATE, BUY)
    ));

    public static Order orderWithCancel = new Order(1L, of(
            new OrderVersion(1L, "ABC", 1000L, INSERT, BUY),
            new OrderVersion(2L, "ABC", 1000L, CANCEL, BUY)
    ));

    public static Transaction insertTransactionV1O1 =
            new Transaction(1L, 1L, 1L, "ABC", 10L, INSERT, BUY);

    public static Transaction insertTransactionV2O1 =
            new Transaction(1L, 1L, 2L, "ABC", 10L, INSERT, BUY);

    public static Transaction updateTransactionV1O1 =
            new Transaction(1L, 1L, 1L, "ABC", 10L, UPDATE, BUY);

    public static Transaction updateTransactionV2O1 =
            new Transaction(1L, 1L, 2L, "ABC", 10L, UPDATE, BUY);

    public static Transaction updateTransactionV3O1 =
            new Transaction(1L, 1L, 3L, "ABC", 10L, UPDATE, BUY);

    public static Transaction updateTransactionV4O1 =
            new Transaction(1L, 1L, 4L, "ABC", 10L, UPDATE, BUY);

    public static Transaction cancelTransactionV1O1 =
            new Transaction(1L, 1L, 1L, "ABC", 10L, CANCEL, BUY);

    public static Transaction cancelTransactionV2O1 =
            new Transaction(1L, 1L, 2L, "ABC", 10L, CANCEL, BUY);

    public static Transaction cancelTransactionV3O1 =
            new Transaction(1L, 1L, 3L, "ABC", 10L, CANCEL, BUY);

    public static Transaction cancelTransactionV4O1 =
            new Transaction(1L, 1L, 4L, "ABC", 10L, CANCEL, BUY);

    public static List<Transaction> txns = List.of(
            new Transaction(1L, 1L, 1L, "RELIANCE", 1000L, INSERT, BUY),
            new Transaction(2L, 2L, 1L, "TCS", 500L, INSERT, SELL),
            new Transaction(3L, 3L, 2L, "INFOSYS", 1000L, UPDATE, BUY),
            new Transaction(8L, 7L, 1L, "AAAA", 500L, CANCEL, BUY),
            new Transaction(4L, 4L, 1L, "RELIANCE", 500L, INSERT, SELL),
            new Transaction(5L, 3L, 1L, "INFOSYS", 800L, INSERT, BUY),
            new Transaction(9L, 8L, 1L, "BBBB", 500L, CANCEL, BUY),
            new Transaction(6L, 5L, 2L, "IBM", 500L, CANCEL, BUY),
            new Transaction(7L, 5L, 1L, "IBM", 500L, INSERT, BUY)
    );

    public static List<Order> listOfInsertOrders = List.of(
            new Order(1L, List.of(
                new OrderVersion(1L, "ABC", 1000L, INSERT, BUY))
            ),
            new Order(2L, List.of(
                new OrderVersion(1L, "XYZ", 1000L, INSERT, BUY))
            ),
            new Order(4L, List.of(
                    new OrderVersion(1L, "ABC", 1000L, INSERT, SELL))
            )
    );

    public static List<Order> listOfUpdateOrders = List.of(
            new Order(1L, List.of(
                    new OrderVersion(1L, "ABC", 1000L, INSERT, BUY),
                    new OrderVersion(2L, "ABC", 1000L, UPDATE, SELL))
            ),
            new Order(2L, List.of(
                    new OrderVersion(1L, "ABC", 1000L, INSERT, SELL),
                    new OrderVersion(2L, "XYZ", 1000L, UPDATE, BUY))
            ),
            new Order(4L, List.of(
                    new OrderVersion(1L, "XYZ", 1000L, INSERT, BUY),
                    new OrderVersion(2L, "RTS", 1000L, UPDATE, BUY))
            )
    );
    //ABC \t -1000\nXYZ \t 1000\nRTS \t 1000\n

    public static List<Order> listOfCancelOrders = List.of(
            new Order(1L, List.of(
                    new OrderVersion(1L, "ABC", 1000L, INSERT, BUY),
                    new OrderVersion(2L, "ABC", 1000L, UPDATE, BUY),
                    new OrderVersion(3L, "ABC", 1000L, CANCEL, SELL))
            ),
            new Order(2L, List.of(
                    new OrderVersion(1L, "ABC", 1000L, INSERT, SELL),
                    new OrderVersion(2L, "XYZ", 100L, UPDATE, BUY))
            ),
            new Order(4L, List.of(
                    new OrderVersion(1L, "XYZ", 1000L, INSERT, BUY),
                    new OrderVersion(2L, "RTS", 0L, CANCEL, BUY))
            ),
            new Order(5L, List.of(
                    new OrderVersion(1L, "XYZ", 1000L, INSERT, SELL),
                    new OrderVersion(2L, "ABC", 10L, UPDATE, BUY))
            )
    );
    //ABC \t 10\nXYZ \t 100\n

    public static List<Order> listOfMixOfOrders = List.of(
            new Order(1L, List.of(
                    new OrderVersion(1L, "ABC", 1000L, INSERT, BUY),
                    new OrderVersion(2L, "ABC", 1000L, UPDATE, BUY),
                    new OrderVersion(3L, "XYZ", 1000L, UPDATE, BUY),
                    new OrderVersion(4L, "XYZ", 1000L, CANCEL, SELL))
            ),
            new Order(2L, List.of(
                    new OrderVersion(1L, "ABC", 1000L, INSERT, SELL),
                    new OrderVersion(2L, "XYZ", 100L, UPDATE, BUY),
                    new OrderVersion(3L, "XYZ", 100L, UPDATE, BUY),
                    new OrderVersion(4L, "XYZ", 100L, UPDATE, SELL),
                    new OrderVersion(5L, "XYZ", 100L, UPDATE, BUY))
            ),
            new Order(4L, List.of(
                    new OrderVersion(1L, "XYZ", 1000L, INSERT, BUY),
                    new OrderVersion(2L, "XYZ", 1000L, UPDATE, BUY),
                    new OrderVersion(3L, "XYZ", 1000L, UPDATE, BUY),
                    new OrderVersion(4L, "XYZ", 1000L, UPDATE, BUY),
                    new OrderVersion(5L, "AZX", 0L, CANCEL, BUY))
            ),
            new Order(5L, List.of(
                    new OrderVersion(1L, "XYZ", 1000L, INSERT, SELL),
                    new OrderVersion(2L, "XYZ", 10L, UPDATE, BUY))
            )
    );
    //XYZ \t 110\n   RTS \t 0\n
    public static List<Transaction> listofOrderVersion = Arrays.asList(updateTransactionV2O1);


//    private BiPredicate<Transaction, Order> pendingBiPredicate() {
//        Map<Long, Integer> map = new HashMap<>();
//        return (transaction, order) -> {
//            if(pendingStatusOnMultipleRuns.containsKey(transaction.getTxnId())) {
//                if(map.containsKey(transaction.getTxnId())) {
//                    map.put(transaction.getTxnId(), map.get(transaction.getTxnId()) + 1);
//                } else {
//                    map.put(transaction.getTxnId(), 1);
//                }
//
//                return pendingStatusOnMultipleRuns.get(transaction.getTxnId()).get(map.get(transaction.getTxnId()));
//            }
//            return false;
//        };
//    }
//
//    private BiPredicate<Transaction, Order> rejectBiPredicate() {
//        Map<Long, Integer> map = new HashMap<>();
//        return (transaction, order) -> {
//            if(rejectStatusOnMultipleRuns.containsKey(transaction.getTxnId())) {
//                if(map.containsKey(transaction.getTxnId())) {
//                    map.put(transaction.getTxnId(), map.get(transaction.getTxnId()) + 1);
//                } else {
//                    map.put(transaction.getTxnId(), 1);
//                }
//
//                return rejectStatusOnMultipleRuns.get(transaction.getTxnId()).get(map.get(transaction.getTxnId()));
//            }
//            return false;
//        };
//    }
//
//    private BiPredicate<Transaction, Order> processBiPredicate() {
//        Map<Long, Integer> map = new HashMap<>();
//        return (transaction, order) -> {
//            if(processStatusOnMultipleRuns.containsKey(transaction.getTxnId())) {
//                if(map.containsKey(transaction.getTxnId())) {
//                    map.put(transaction.getTxnId(), map.get(transaction.getTxnId()) + 1);
//                } else {
//                    map.put(transaction.getTxnId(), 1);
//                }
//
//                return processStatusOnMultipleRuns.get(transaction.getTxnId()).get(map.get(transaction.getTxnId()));
//            }
//            return false;
//        };
//    }
}
