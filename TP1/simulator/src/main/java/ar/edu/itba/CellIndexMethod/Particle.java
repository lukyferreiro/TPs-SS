package ar.edu.itba.CellIndexMethod;

import java.util.Objects;
import java.lang.Math;

public class Particle {

    private final int id;
    private final double radius;

    public Particle(int id, double radius) {
        this.id = id;
        this.radius = radius;
    }

    public int getId() {
        return id;
    }
    public double getRadius() {
        return radius;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Particle particle = (Particle) o;
        return id == particle.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static class Position {
        private final double x;
        private final double y;

        public Position(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double getX() {
            return x;
        }
        public double getY() {
            return y;
        }

        public static double calculateDistance(Position p1, Position p2) {
            return Math.sqrt(Math.pow(p1.getX() - p2.getX(), 2) + Math.pow(p1.getY() - p2.getY(), 2));
        }
    }
}