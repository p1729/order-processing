package com.pankaj.core.processors;

import com.pankaj.core.MockData;
import com.pankaj.core.stores.OrderStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;


class TransactionProcessorTest {

    @Test
    @RepeatedTest(100)
    public void testThread() throws Exception {
        Processor p = TransactionProcessor.getInstance();
        MockData.txns.forEach(txn -> p.process(txn));
        Thread.sleep(1000);
        p.getReporter().print();
    }
}