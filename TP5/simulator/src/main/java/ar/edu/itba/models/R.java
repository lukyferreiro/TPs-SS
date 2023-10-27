package ar.edu.itba.models;
import ar.edu.itba.models.Pair;

import java.util.ArrayList;
import java.util.List;

public class R {
    private final List<Pair<Double, Double>> ri;

    public R() {
        this.ri = new ArrayList<>();
    }

    public void add(double x, double y) {
        ri.add(new Pair<>(x, y));
    }

    public void set(int index, double x, double y) {
        ri.set(index, new Pair<>(x, y));
    }

    public Pair<Double, Double> get(int index) {
        return ri.get(index);
    }

    public enum values {
        R0, R1, R2, R3, R4, R5, R6_NO_PERIODIC
    }

}
