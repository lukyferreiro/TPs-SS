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
        double time = type.getCollisionTime(boundaryPosition, length, particle);
        if (time < 0) {
            System.out.println((String.format("Negative value for wall %s\n", type)));
        }
        return time;
    }

    public void collide(Particle particle) {
        type.collide(particle);
    }

}
