package edu.ucu.ua.task1;

public class Main {
    public static void main(String[] args) {
        ATM atm = new ATM();

        System.out.println("Amount 100:");
        atm.process(100);
        System.out.println("Amount 75:");
        atm.process(75);
        System.out.println("Amount 76:");
        atm.process(76);
    }
}
