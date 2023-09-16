package ar.edu.itba.models;

public class Boundary implements Bounceable {
    private final Position boundaryPosition;
    private final BoundaryType type;
    private final double length;

    public Boundary(Position boundaryPosition, double length, BoundaryType type) {
        this.boundaryPosition = boundaryPosition;
        this.length = length;
        this.type = type;
    }

    public BoundaryType getType() {
        return type;
    }

    @Override
    public double getCollisionTime(Particle particle) {
        return type.getCollisionTime(boundaryPosition, length, particle);
    }

    @Override
    public void collide(Particle particle) {
        type.collide(particle);
    }

}
