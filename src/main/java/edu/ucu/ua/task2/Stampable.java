package edu.ucu.ua.task2;

public interface Stampable<T> {
    void accept(StampingVisitor<T> visitor);
}
