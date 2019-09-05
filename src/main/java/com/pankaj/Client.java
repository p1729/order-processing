package com.pankaj;

import com.pankaj.core.enums.SideType;
import com.pankaj.core.enums.TransactionType;
import com.pankaj.core.models.Transaction;
import com.pankaj.core.processors.Processor;
import com.pankaj.core.processors.TransactionProcessor;

import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Processor processor = TransactionProcessor.getInstance();

        while(true) {
            System.out.println("Please enter the transaction details..");
            System.out.println("TxnId, ordId, verId, symbol, qty, opn, side");
            Transaction txn = new Transaction(sc.nextLong(), sc.nextLong(), sc.nextLong(), sc.next("[A-Za-z]+"),
                    sc.nextLong(), TransactionType.of(sc.nextInt()), SideType.of(sc.nextInt()));
            processor.process(txn);
        }
    }
}
