package ar.edu.itba.utils;

import ar.edu.itba.CellIndexMethod.Particle;

import java.util.List;
import java.util.Map;

public class ParticlesParserResult {
    private final int N;
    private final double L;
    private final List<Map<Particle, Particle.Position>> particlesPerTime;


    public ParticlesParserResult(int N, double L, List<Map<Particle, Particle.Position>> particlesPerTime) {
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
    public List<Map<Particle, Particle.Position>> getParticlesPerTime() {
        return particlesPerTime;
    }
}
