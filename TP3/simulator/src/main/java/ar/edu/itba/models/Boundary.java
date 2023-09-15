package ar.edu.itba.models;

public class Boundary {
    private final Position boundaryPosition;
    private final BoundaryType type;
    private final double length;

    public Boundary(Position boundaryPosition, double length, BoundaryType type) {
        this.boundaryPosition = boundaryPosition;
        this.length = length;
        this.type = type;
    }

    public double getCollisionTime(Particle particle) {
        return type.getCollisionTime(boundaryPosition, length, particle);
    }

    public void collide(Particle particle) {
        type.collide(particle);
    }

}
