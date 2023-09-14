package ar.edu.itba.utils;

import ar.edu.itba.models.Particle;

import java.util.List;


public class ParticlesParserResult {
    private final int N;
    private final double side;
    private final List<List<Particle>> particlesPerTime;

    public ParticlesParserResult(int N, double side, List<List<Particle>> particlesPerTime) {
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
    public List<List<Particle>> getParticlesPerTime() {
        return particlesPerTime;
    }
}
