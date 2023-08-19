package ar.edu.itba.models;

import java.util.Objects;
import java.lang.Math;

public class Particle {

    private final int id;
    private final double radius;
    private final double property;

    public Particle(int id, double radius, double property) {
        this.id = id;
        this.radius = radius;
        this.property = property;
    }

    public int getId() {
        return id;
    }
    public double getRadius() {
        return radius;
    }
    public double getProperty() {
        return property;
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
        public static double calculateDistancePeriodic(Position p1, Position p2, double L) {
            double dx = Math.abs(p1.getX() - p2.getX());
            double dy = Math.abs(p1.getY() - p2.getY());

            // Aplicar condiciones periódicas en ambas direcciones
            dx = Math.min(dx, L - dx);
            dy = Math.min(dy, L - dy);

            return Math.sqrt(Math.pow(dx,2) + Math.pow(dy,2));
        }

    }
}