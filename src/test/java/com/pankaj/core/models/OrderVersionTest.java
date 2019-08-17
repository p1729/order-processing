package com.pankaj.core.models;

import com.pankaj.core.enums.SIDE_TYPE;
import com.pankaj.core.enums.TXN_TYPE;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

class OrderVersionTest {

    @Test
    public void testOrder() {
        OrderVersion[] vers = {new OrderVersion(31L, "ABC", 1000L, TXN_TYPE.INSERT, SIDE_TYPE.BUY),
                new OrderVersion(3L, "ABC", 1000L, TXN_TYPE.INSERT, SIDE_TYPE.BUY),
                new OrderVersion(2L, "ABC", 1000L, TXN_TYPE.INSERT, SIDE_TYPE.BUY)};
        Arrays.sort(vers);
        for (OrderVersion ver : vers) {
            System.out.println(ver);
        }
    }

}