package ar.edu.itba.utils;

import ar.edu.itba.models.Particle;

import java.util.List;
import java.util.Set;


public class ParticlesParserResult {
    private final int N;
    private final double L;
    private final List<Set<Particle>> particlesPerTime;

    public ParticlesParserResult(int N, double L, List<Set<Particle>> particlesPerTime) {
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
    public List<Set<Particle>> getParticlesPerTime() {
        return particlesPerTime;
    }
}
