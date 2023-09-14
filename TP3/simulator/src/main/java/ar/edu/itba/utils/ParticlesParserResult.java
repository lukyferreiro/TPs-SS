package ar.edu.itba.utils;

import ar.edu.itba.models.Particle;

import java.util.List;


public class ParticlesParserResult {
    private final int N;
    private final double L;
    private final List<List<Particle>> particlesPerTime;

    public ParticlesParserResult(int N, double L, List<List<Particle>> particlesPerTime) {
        this.N = N;
        this.L = L;
        this.particlesPerTime = particlesPerTime;
    }

    public int getN() {
        return N;
    }
    public double getL() {
        return L;
    }
    public List<List<Particle>> getParticlesPerTime() {
        return particlesPerTime;
    }
}
