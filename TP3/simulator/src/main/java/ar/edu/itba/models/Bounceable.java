package ar.edu.itba.models;

public interface Bounceable {
    void collide(Particle particle);
    double getCollisionTime(Particle particle);
}
