package edu.ucu.ua.task1;

public class Tray10 extends Tray{
    final int coinValue = 10;

    @Override
    public void process(int amount) {
        int curCoinCount = amount / coinValue;
        amount %= coinValue;
        if (amount != 0 && next != null) {
            next.process(amount);
        } else if (amount != 0 && next == null) {
            throw new IllegalArgumentException("For given amount rest cannot be given");
        }
        System.out.printf("value %d coins: %d\n", coinValue, curCoinCount);
    }
}
