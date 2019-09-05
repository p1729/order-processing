package com.pankaj.core.rules;

import com.pankaj.core.MockData;
import com.pankaj.core.models.Order;
import com.pankaj.core.models.Transaction;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RulesStoreTest {

    @Test
    public void givenNoOrder_whenInsertTxnO1V1_thenTxnProcessed() {
        Order order = null;
        Transaction txn = MockData.insertTransactionV1O1;

        assertTrue(RulesStore.getTransactionOrderProcessingRule().test(txn, order));
        assertFalse(RulesStore.getPendingTransactionOrderRule().test(txn, order));
        assertFalse(RulesStore.getRejectedTransactionRule().test(txn, order));
    }

    @Test
    public void givenNoOrder_whenUpdateTxnO1V1_thenTxnGetRejected() {
        Order order = null;
        Transaction txn = MockData.updateTransactionV1O1;

        assertFalse(RulesStore.getTransactionOrderProcessingRule().test(txn, order));
        assertFalse(RulesStore.getPendingTransactionOrderRule().test(txn, order));
        assertTrue(RulesStore.getRejectedTransactionRule().test(txn, order));
    }

    @Test
    public void givenNoOrder_whenCancelTxnO1V1_thenTxnGetRejected() {
        Order order = null;
        Transaction txn = MockData.cancelTransactionV1O1;

        assertFalse(RulesStore.getTransactionOrderProcessingRule().test(txn, order));
        assertFalse(RulesStore.getPendingTransactionOrderRule().test(txn, order));
        assertTrue(RulesStore.getRejectedTransactionRule().test(txn, order));
    }

    @Test
    public void givenNoOrder_whenUpdateTxnO1V2_thenTxnPending() {
        Order order = null;
        Transaction txn = MockData.updateTransactionV2O1;

        assertFalse(RulesStore.getTransactionOrderProcessingRule().test(txn, order));
        assertTrue(RulesStore.getPendingTransactionOrderRule().test(txn, order));
        assertFalse(RulesStore.getRejectedTransactionRule().test(txn, order));
    }

    @Test
    public void givenNoOrder_whenCancelTxnO1V2_thenTxnPending() {
        Order order = null;
        Transaction txn = MockData.cancelTransactionV2O1;

        assertFalse(RulesStore.getTransactionOrderProcessingRule().test(txn, order));
        assertTrue(RulesStore.getPendingTransactionOrderRule().test(txn, order));
        assertFalse(RulesStore.getRejectedTransactionRule().test(txn, order));
    }

    @Test
    public void givenNoOrder_whenInsertTxnO1V2_thenTxnRejected() {
        Order order = null;
        Transaction txn = MockData.insertTransactionV2O1;

        assertFalse(RulesStore.getTransactionOrderProcessingRule().test(txn, order));
        assertFalse(RulesStore.getPendingTransactionOrderRule().test(txn, order));
        assertTrue(RulesStore.getRejectedTransactionRule().test(txn, order));
    }

    @Test
    public void givenOrderwithInsert_whenUpdate_thenTxnProcessed() {
        Order order = MockData.orderWithInsert;
        Transaction txn = MockData.updateTransactionV2O1;

        assertTrue(RulesStore.getTransactionOrderProcessingRule().test(txn, order));
        assertFalse(RulesStore.getPendingTransactionOrderRule().test(txn, order));
        assertFalse(RulesStore.getRejectedTransactionRule().test(txn, order));
    }

    @Test
    public void givenOrderWithInsert_whenCancel_thenTxnProcessed() {
        Order order = MockData.orderWithInsert;
        Transaction txn = MockData.cancelTransactionV2O1;

        assertTrue(RulesStore.getTransactionOrderProcessingRule().test(txn, order));
        assertFalse(RulesStore.getPendingTransactionOrderRule().test(txn, order));
        assertFalse(RulesStore.getRejectedTransactionRule().test(txn, order));
    }

    @Test
    public void givenOrderWithInsert_whenInsert_thenTxnRejected() {
        Order order = MockData.orderWithInsert;
        Transaction txn = MockData.insertTransactionV2O1;

        assertFalse(RulesStore.getTransactionOrderProcessingRule().test(txn, order));
        assertFalse(RulesStore.getPendingTransactionOrderRule().test(txn, order));
        assertTrue(RulesStore.getRejectedTransactionRule().test(txn, order));
    }

    @Test
    public void givenOrderWithInsert_whenUpdateTxnO1V3_thenTxnPending() {
        Order order = MockData.orderWithInsert;
        Transaction txn = MockData.updateTransactionV3O1;

        assertFalse(RulesStore.getTransactionOrderProcessingRule().test(txn, order));
        assertTrue(RulesStore.getPendingTransactionOrderRule().test(txn, order));
        assertFalse(RulesStore.getRejectedTransactionRule().test(txn, order));
    }

    @Test
    public void givenOrderWithInsert_whenCancelTxnO1V3_thenTxnPending() {
        Order order = MockData.orderWithInsert;
        Transaction txn = MockData.cancelTransactionV3O1;

        assertFalse(RulesStore.getTransactionOrderProcessingRule().test(txn, order));
        assertTrue(RulesStore.getPendingTransactionOrderRule().test(txn, order));
        assertFalse(RulesStore.getRejectedTransactionRule().test(txn, order));
    }

    @Test
    public void givenOrderWithUpdate_whenUpdateTxnO1V3_thenTxnProcessed() {
        Order order = MockData.orderWithUpdate;
        Transaction txn = MockData.updateTransactionV3O1;

        assertTrue(RulesStore.getTransactionOrderProcessingRule().test(txn, order));
        assertFalse(RulesStore.getPendingTransactionOrderRule().test(txn, order));
        assertFalse(RulesStore.getRejectedTransactionRule().test(txn, order));
    }

    @Test
    public void givenOrderWithUpdate_whenCancelTxnO1V3_thenTxnProcessed() {
        Order order = MockData.orderWithUpdate;
        Transaction txn = MockData.cancelTransactionV3O1;

        assertTrue(RulesStore.getTransactionOrderProcessingRule().test(txn, order));
        assertFalse(RulesStore.getPendingTransactionOrderRule().test(txn, order));
        assertFalse(RulesStore.getRejectedTransactionRule().test(txn, order));
    }

    @Test
    public void givenOrderWithCancel_whenCancelTxnO1V4_thenTxnRejected() {
        Order order = MockData.orderWithCancel;
        Transaction txn = MockData.cancelTransactionV4O1;

        assertFalse(RulesStore.getTransactionOrderProcessingRule().test(txn, order));
        assertFalse(RulesStore.getPendingTransactionOrderRule().test(txn, order));
        assertTrue(RulesStore.getRejectedTransactionRule().test(txn, order));
    }

    @Test
    public void givenOrderWithCancel_whenUpdateTxnO1V4_thenTxnRejected() {
        Order order = MockData.orderWithCancel;
        Transaction txn = MockData.updateTransactionV4O1;

        assertFalse(RulesStore.getTransactionOrderProcessingRule().test(txn, order));
        assertFalse(RulesStore.getPendingTransactionOrderRule().test(txn, order));
        assertTrue(RulesStore.getRejectedTransactionRule().test(txn, order));
    }
}