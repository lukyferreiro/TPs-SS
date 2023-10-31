package ar.edu.itba.models;

public class DoublePair extends Pair<Double, Double>{
    public DoublePair(Double value1, Double value2) {
        super(value1, value2);
    }

    public double dot(DoublePair other) {
        return this.getOne() * other.getOne() + this.getOther() * other.getOther();
    }

    public DoublePair scale(double scalar) {
        return new DoublePair(this.getOne() * scalar, this.getOther() * scalar);
    }

    public DoublePair subtract(DoublePair other) {
        return new DoublePair(this.getOne() - other.getOne(), this.getOther() - other.getOther());
    }

    public DoublePair sum(DoublePair other) {
        return new DoublePair(this.getOne() + other.getOne(), this.getOther() + other.getOther());
    }

    public double module(DoublePair other) {
        return Math.sqrt(Math.pow(this.getOne() - other.getOne(), 2) + Math.pow(this.getOther() - other.getOther(), 2));
    }

    public double calculateDistance(DoublePair other) {
        return Math.sqrt(Math.pow(this.getOne() - other.getOne(), 2) + Math.pow(this.getOther() - other.getOther(), 2));
    }

    @Override
    public String toString() {
        return String.format("x= %.20f y= %.20f", getOne(), getOther());
    }
}
