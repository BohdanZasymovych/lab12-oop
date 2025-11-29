package edu.ucu.ua.task1;

public class ATM {
    public Tray firsTray;

    public ATM() {
        firsTray = new Tray20();
        Tray tray10 = new Tray10();
        Tray tray5 = new Tray5();

        firsTray.setNext(tray10);
        tray10.setNext(tray5);
    }

    public void process(int amount) {
        firsTray.process(amount);
    }
}
