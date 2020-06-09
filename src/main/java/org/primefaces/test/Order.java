package org.primefaces.test;

public class Order implements java.io.Serializable {

    private final int number;

    public Order(int number) {
        this.number = number;

    }

    public int getNumber() {
        return number;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Order order = (Order) o;

        return number == order.number;
    }

    @Override
    public int hashCode() {
        return number;
    }

}