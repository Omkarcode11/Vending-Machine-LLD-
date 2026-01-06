package models;

import java.util.UUID;

public class Denomination {
    int id;
    int value;
    int capacity;
    int count;

    public Denomination(int id, int value, int capacity) {
        this.id = UUID.randomUUID().hashCode();
        this.value = value;
        this.capacity = capacity;
        this.count = 0;
    }

    public int getId() {
        return id;
    }

    public int getValue() {
        return value;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getCount() {
        return count;
    }


    public boolean isAvailable() {
        return count > 0;
    }

    public void withdrawMoney(int count) {
        if (isAvailable()) {
            this.count -= count;
        }
    }

    public void addMoney(int count) {
        this.count += count;
    }
}
