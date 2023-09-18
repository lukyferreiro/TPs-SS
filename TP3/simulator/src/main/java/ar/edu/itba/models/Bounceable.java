package ar.edu.itba.models;

public interface Bounceable {
    void collide(Particle particle);
    double getTc(Particle particle);
}
