package ar.edu.itba.utils;

import ar.edu.itba.models.Particle;

import java.util.List;
import java.util.Set;


public class ParticlesParserResult {
    private final int N;
    private final double L;
    private final double W;
    private final List<List<Particle>> particlesPerTime;

    public ParticlesParserResult(int N, double L, double W, List<List<Particle>> particlesPerTime) {
        this.N = N;
        this.L = L;
        this.W = W;
        this.particlesPerTime = particlesPerTime;
    }

    public int getN() {
        return N;
    }
    public double getL() {
        return L;
    }
    public double getW() {
        return W;
    }
    public List<List<Particle>> getParticlesPerTime() {
        return particlesPerTime;
    }
}
