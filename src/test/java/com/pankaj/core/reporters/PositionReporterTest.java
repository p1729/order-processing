package com.pankaj.core.reporters;

import com.pankaj.core.MockData;
import com.pankaj.core.stores.OrderStore;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({OrderStore.class})
public class PositionReporterTest {

    private PrintStream sysOut;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private Reporter report = PositionReporter.getInstance();

    @Before
    public void setUpStreams() throws NoSuchFieldException, IllegalAccessException {
        sysOut = System.out;
        System.setOut(new PrintStream(outContent));

        Field instance = PositionReporter.class.getDeclaredField("positions");
        instance.setAccessible(true);
        instance.set(report, new ConcurrentHashMap<>());
    }

    @After
    public void revertStreams() {
        System.setOut(sysOut);
    }

    @Test
    public void givenOrdersWithInsertVersion_whenPrepareNPrint_thenGivesFinalPositions() throws NoSuchFieldException, IllegalAccessException {
        //Given
        OrderStore mock = mock(OrderStore.class);
        Whitebox.setInternalState(OrderStore.class, "INSTANCE", mock);
        when(mock.getStore()).thenReturn(MockData.listOfInsertOrders);

        Field store = PositionReporter.class.getDeclaredField("store");
        store.setAccessible(true);
        store.set(report, mock);

        //when
        report.prepare();
        report.print();
        //then
        assertThat(outContent.toString(), allOf(containsString("XYZ 1000"), containsString("ABC 0")));
    }

    @Test
    public void givenOrdersWithInsertUpdateVersion_whenPrepareNPrint_thenGivesFinalPositions() throws IllegalAccessException, NoSuchFieldException {
        //Given
        OrderStore mock = mock(OrderStore.class);
        Whitebox.setInternalState(OrderStore.class, "INSTANCE", mock);
        when(mock.getStore()).thenReturn(MockData.listOfUpdateOrders);

        Field store = PositionReporter.class.getDeclaredField("store");
        store.setAccessible(true);
        store.set(report, mock);

        //when
        report.prepare();
        report.print();
        //then
        assertThat(outContent.toString(), allOf(containsString("RTS 1000"),
                containsString("ABC -1000"), containsString("XYZ 1000")));
    }

    @Test
    public void givenOrdersWithInsertUpdateCancelVersion_whenPrepareNPrint_thenGivesFinalPositions() throws NoSuchFieldException, IllegalAccessException {
        //Given
        OrderStore mock = mock(OrderStore.class);
        Whitebox.setInternalState(OrderStore.class, "INSTANCE", mock);
        when(mock.getStore()).thenReturn(MockData.listOfCancelOrders);

        Field store = PositionReporter.class.getDeclaredField("store");
        store.setAccessible(true);
        store.set(report, mock);

        //when
        report.prepare();
        report.print();
        //then
        assertThat(outContent.toString(), allOf( containsString("XYZ 100"), containsString("ABC 10")));
    }

    @Test
    public void givenOrdersWithAllVersion_whenPrepareNPrint_thenGivesFinalPositions() throws NoSuchFieldException, IllegalAccessException {
        //Given
        OrderStore mock = mock(OrderStore.class);
        Whitebox.setInternalState(OrderStore.class, "INSTANCE", mock);
        when(mock.getStore()).thenReturn(MockData.listOfMixOfOrders);

        Field store = PositionReporter.class.getDeclaredField("store");
        store.setAccessible(true);
        store.set(report, mock);

        //when
        report.prepare();
        report.print();
        //then
        assertThat(outContent.toString().trim(), containsString("XYZ 110"));
    }
}