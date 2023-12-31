package ar.edu.itba.models;

import java.util.Objects;

public class Pair<T, V> {
    private T value1;
    private V value2;

    public Pair(T value1, V value2) {
        this.value1 = value1;
        this.value2 = value2;
    }

    public boolean has(Object value) {
        return value.equals(value1) || value.equals(value2);
    }

    public void setOne(T value1) {
        this.value1 = value1;
    }

    public T getOne() {
        return value1;
    }

    public void setOther(V value2) {
        this.value2 = value2;
    }

    public V getOther() {
        return value2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pair<?, ?> pair)) {
            return false;
        }
        return (value1.equals(pair.value1) && value2.equals(pair.value2)) || (value1.equals(pair.value2) && value2.equals(pair.value1));
    }

    @Override
    public int hashCode() {
        return Objects.hash(value1.hashCode() + value2.hashCode());
    }
}
