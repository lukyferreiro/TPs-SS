package ar.edu.itba.models;

public class Position {

    private double x;
    private double y;

    public Position(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return this.x;
    }
    public void setX(double x) {
        this.x = x;
    }
    public double getY() {
        return this.y;
    }
    public void setY(double y) {
        this.y = y;
    }

    public Position add(Position p) {
        return new Position(this.getX() + p.getX(), this.getY() + p.getY());
    }

    public double calculateDistance(Position p) {
        return Math.sqrt(Math.pow(this.x - p.getX(), 2) + Math.pow(this.y - p.getY(), 2));
    }

    @Override
    public String toString() {
        return "x=" + x + ", y=" + y;
    }
}
