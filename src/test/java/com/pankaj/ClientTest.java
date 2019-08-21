package com.pankaj;

import com.pankaj.core.MockData;
import com.pankaj.core.processors.Processor;
import com.pankaj.core.processors.TransactionProcessor;
import org.junit.Test;

public class ClientTest {

    @Test
    public void testThread() throws Exception {
        Processor p = TransactionProcessor.getInstance();
        MockData.txns.forEach(txn -> p.process(txn));
        Thread.sleep(1000);
        p.getReporter().print();
    }
}