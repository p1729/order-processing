package com.pankaj.core.processors;

import com.pankaj.core.MockData;
import com.pankaj.core.MyTestExecutor;
import com.pankaj.core.enums.TXN_STATUS;
import com.pankaj.core.models.Transaction;
import com.pankaj.core.rules.RulesStore;
import com.pankaj.core.stores.OrderStore;
import com.pankaj.core.stores.TransactionStore;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.*;


@RunWith(PowerMockRunner.class)
@PrepareForTest({OrderStore.class, TransactionStore.class, RulesStore.class, Executors.class, TransactionProcessor.class})
public class TransactionProcessorTest {

    @Captor
    ArgumentCaptor<Transaction> txnCaptor;

    @Captor
    ArgumentCaptor<TXN_STATUS> txnStatuCaptor;

    @Test
    public void givenTxn_whenPendingRuleApplied_thenItsNotProcessed() throws NoSuchFieldException, IllegalAccessException {
        //Given
        mockStatic(RulesStore.class);
        mockStatic(Executors.class);
        OrderStore orderMock = mock(OrderStore.class);
        TransactionStore txnMock = mock(TransactionStore.class);
        Whitebox.setInternalState(OrderStore.class, "INSTANCE", orderMock);
        Whitebox.setInternalState(TransactionStore.class, "INSTANCE", txnMock);
        when(Executors.newFixedThreadPool(anyInt())).thenReturn(new MyTestExecutor());
        when(orderMock.getOrderById(anyLong())).thenReturn(Optional.empty());
        when(RulesStore.getPendingTransactionOrderRule()).thenReturn((a,b) -> true);
        when(RulesStore.getRejectedTransactionRule()).thenReturn((a,b) -> false);
        when(RulesStore.getTransactionOrderProcessingRule()).thenReturn((a,b) -> false);
        when(txnMock.getTransactionOfOrdersByStatus(anyLong(), anyObject())).thenReturn(List.of(MockData.cancelTransactionV2O1));
        Processor processor = TransactionProcessor.getInstance();

        Field store = TransactionProcessor.class.getDeclaredField("store");
        store.setAccessible(true);
        store.set(processor, txnMock);
        Field orders = TransactionProcessor.class.getDeclaredField("orders");
        orders.setAccessible(true);
        orders.set(processor, orderMock);
        //when
        processor.process(MockData.insertTransactionV1O1);
        //then
        verify(txnMock, times(1)).putTransactionWithStatus(MockData.insertTransactionV1O1, TXN_STATUS.PENDING);
    }

    @Test
    public void givenTxn_whenProcessRuleApplied_thenItsProcessed() throws NoSuchFieldException, IllegalAccessException {
        //given
        mockStatic(RulesStore.class);
        mockStatic(Executors.class);
        OrderStore orderMock = mock(OrderStore.class);
        TransactionStore txnMock = mock(TransactionStore.class);
        Whitebox.setInternalState(OrderStore.class, "INSTANCE", orderMock);
        Whitebox.setInternalState(TransactionStore.class, "INSTANCE", txnMock);
        when(Executors.newFixedThreadPool(anyInt())).thenReturn(new MyTestExecutor());
        when(orderMock.getOrderById(anyLong())).thenReturn(Optional.empty());
        when(RulesStore.getPendingTransactionOrderRule()).thenReturn((a,b) -> false);
        when(RulesStore.getRejectedTransactionRule()).thenReturn((a,b) -> false);
        when(RulesStore.getTransactionOrderProcessingRule()).thenReturn((a,b) -> true);
        when(txnMock.getTransactionOfOrdersByStatus(anyLong(), anyObject())).thenReturn(Collections.emptyList());

        Processor processor = TransactionProcessor.getInstance();
        Field store = TransactionProcessor.class.getDeclaredField("store");
        store.setAccessible(true);
        store.set(processor, txnMock);
        Field orders = TransactionProcessor.class.getDeclaredField("orders");
        orders.setAccessible(true);
        orders.set(processor, orderMock);
        //when
        processor.process(MockData.insertTransactionV1O1);
        //then
        verify(txnMock, times(1)).putTransactionWithStatus(MockData.insertTransactionV1O1, TXN_STATUS.PROCESSED);
    }

    @Test
    public void givenTxn_whenRejectRuleApplied_thenItsRejected() throws NoSuchFieldException, IllegalAccessException {
        //given
        mockStatic(RulesStore.class);
        mockStatic(Executors.class);
        OrderStore orderMock = mock(OrderStore.class);
        TransactionStore txnMock = mock(TransactionStore.class);
        Whitebox.setInternalState(OrderStore.class, "INSTANCE", orderMock);
        Whitebox.setInternalState(TransactionStore.class, "INSTANCE", txnMock);
        when(Executors.newFixedThreadPool(anyInt())).thenReturn(new MyTestExecutor());
        when(orderMock.getOrderById(anyLong())).thenReturn(Optional.empty());
        when(RulesStore.getPendingTransactionOrderRule()).thenReturn((a,b) -> false);
        when(RulesStore.getRejectedTransactionRule()).thenReturn((a,b) -> true);
        when(RulesStore.getTransactionOrderProcessingRule()).thenReturn((a,b) -> false);
        when(txnMock.getTransactionOfOrdersByStatus(anyLong(), anyObject())).thenReturn(Collections.emptyList());

        Processor processor = TransactionProcessor.getInstance();
        Field store = TransactionProcessor.class.getDeclaredField("store");
        store.setAccessible(true);
        store.set(processor, txnMock);
        Field orders = TransactionProcessor.class.getDeclaredField("orders");
        orders.setAccessible(true);
        orders.set(processor, orderMock);
        //when
        processor.process(MockData.insertTransactionV1O1);
        //then
        verify(txnMock, times(1)).putTransactionWithStatus(MockData.insertTransactionV1O1, TXN_STATUS.REJECTED);
    }

    @Test
    public void givenTxn_whenProcessRuleAppliedAndPendingTransactionPresent_thenBothTransactionProcessed() throws NoSuchFieldException, IllegalAccessException {

        //given
        mockStatic(RulesStore.class);
        mockStatic(Executors.class);
        OrderStore orderMock = mock(OrderStore.class);
        TransactionStore txnMock = mock(TransactionStore.class);
        Whitebox.setInternalState(OrderStore.class, "INSTANCE", orderMock);
        Whitebox.setInternalState(TransactionStore.class, "INSTANCE", txnMock);
        when(Executors.newFixedThreadPool(anyInt())).thenReturn(new MyTestExecutor());
        when(orderMock.getOrderById(anyLong())).thenReturn(Optional.empty());
        when(RulesStore.getPendingTransactionOrderRule()).thenReturn((a,b) -> false);
        when(RulesStore.getRejectedTransactionRule()).thenReturn((a,b) -> false);
        when(RulesStore.getTransactionOrderProcessingRule()).thenReturn((a,b) -> true);
        when(txnMock.getTransactionOfOrdersByStatus(anyLong(), anyObject())).thenReturn(MockData.listofOrderVersion, Collections.emptyList());

        Processor processor = TransactionProcessor.getInstance();
        Field store = TransactionProcessor.class.getDeclaredField("store");
        store.setAccessible(true);
        store.set(processor, txnMock);
        Field orders = TransactionProcessor.class.getDeclaredField("orders");
        orders.setAccessible(true);
        orders.set(processor, orderMock);
        //when
        processor.process(MockData.insertTransactionV1O1);
        //then
        verify(txnMock, times(2)).putTransactionWithStatus(txnCaptor.capture(), txnStatuCaptor.capture());
        Assert.assertEquals(txnCaptor.getAllValues().get(0), MockData.insertTransactionV1O1);
        Assert.assertEquals(txnCaptor.getAllValues().get(1), MockData.updateTransactionV2O1);
        Assert.assertEquals(txnStatuCaptor.getAllValues().get(0), TXN_STATUS.PROCESSED);
        Assert.assertEquals(txnStatuCaptor.getAllValues().get(1), TXN_STATUS.PROCESSED);
    }

}