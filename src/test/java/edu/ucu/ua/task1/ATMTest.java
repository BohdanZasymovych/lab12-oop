package edu.ucu.ua.task1;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class ATMTest {
    private ATM atm;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        atm = new ATM();
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    void testProcessExactAmount() {
        atm.process(40);
        String output = outputStream.toString();
        assertTrue(output.contains("value 20 coins: 2"));
    }

    @Test
    void testProcessMixedCoins() {
        outputStream.reset();
        atm.process(35);
        String output = outputStream.toString();
        assertTrue(output.contains("value 5 coins: 1"));
        assertTrue(output.contains("value 10 coins: 1"));
        assertTrue(output.contains("value 20 coins: 1"));
    }

    @Test
    void testProcessWithTenAndFive() {
        outputStream.reset();
        atm.process(15);
        String output = outputStream.toString();
        assertTrue(output.contains("value 5 coins: 1"));
        assertTrue(output.contains("value 10 coins: 1"));
        assertTrue(output.contains("value 20 coins: 0"));
    }

    @Test
    void testProcessInvalidAmount() {
        assertThrows(IllegalArgumentException.class, () -> {
            atm.process(7);
        });
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }
}
