package com.pankaj;

import com.pankaj.core.processors.Processor;
import com.pankaj.core.processors.TransactionProcessor;
import com.pankaj.core.enums.SIDE_TYPE;
import com.pankaj.core.enums.TXN_TYPE;
import com.pankaj.core.models.Transaction;

import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Processor processor = TransactionProcessor.getInstance();

        while(true) {
            System.out.println("Please enter the transaction details..");
            System.out.println("TxnId, ordId, verId, symbol, qty, opn, side");
            Transaction txn = new Transaction(sc.nextLong(), sc.nextLong(), sc.nextLong(), sc.next("[A-Za-z]+"),
                    sc.nextLong(), TXN_TYPE.of(sc.nextInt()), SIDE_TYPE.of(sc.nextInt()));
            processor.process(txn);
        }
    }
}
