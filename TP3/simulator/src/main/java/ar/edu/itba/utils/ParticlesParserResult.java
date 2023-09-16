package ar.edu.itba.utils;

import ar.edu.itba.models.Particle;

import java.util.List;
import java.util.Set;


public class ParticlesParserResult {
    private final int N;
    private final double side;
    private final List<Set<Particle>> particlesPerTime;

    public ParticlesParserResult(int N, double side, List<Set<Particle>> particlesPerTime) {
        this.N = N;
        this.side = side;
        this.particlesPerTime = particlesPerTime;
    }

    public int getN() {
        return N;
    }
    public double getSide() {
        return side;
    }
    public List<Set<Particle>> getParticlesPerTime() {
        return particlesPerTime;
    }
}
