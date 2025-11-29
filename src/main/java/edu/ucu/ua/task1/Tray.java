package edu.ucu.ua.task1;

public abstract class Tray {
    protected Tray next;

    public abstract void process(int amount);

    public void setNext(Tray tray) { this.next = tray; }
}