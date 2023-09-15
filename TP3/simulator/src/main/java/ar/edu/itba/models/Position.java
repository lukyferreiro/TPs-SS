package ar.edu.itba.models;

public class Position {

    private double x;
    private double y;

    public Position(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }
    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Position add(Position p) {
        return new Position(this.getX() + p.getX(), this.getY() + p.getY());
    }

    public double dotProduct(Position p) {
        return this.getX() * p.getX() + this.getY() * p.getY();
    }

    public Position substract(Position p) {
        return new Position(this.getX() - p.getX(), this.getY() - p.getY());
    }

    public Position multiply(double scalar) {
        return new Position(this.getX() * scalar, this.getY() * scalar);
    }


    private double wrapAxis(double value, double boardLength) {
        if (value > boardLength) {
            value -= boardLength;
        } else if (value < 0) {
            value += boardLength;
        }

        return value;
    }

    public Position moveForward(Particle particle, double time) {
        double x = getX() + particle.getVx() * time;
        double y = getY() + particle.getVy() * time;
        return new Position(x, y);
    }

    public boolean isAbove(Position p) {
        return getY() > p.getY();
    }

    public boolean isBelow(Position p) {
        return getY() < p.getY();
    }

    public boolean isLeftOf(Position p) {
        return getX() < p.getX();
    }

    public boolean isRightOf(Position p) {
        return getX() > p.getX();
    }

    public double calculateDistance(Position p) {
        return Math.sqrt(Math.pow(this.x - p.getX(), 2) + Math.pow(this.y - p.getY(), 2));
    }

}
