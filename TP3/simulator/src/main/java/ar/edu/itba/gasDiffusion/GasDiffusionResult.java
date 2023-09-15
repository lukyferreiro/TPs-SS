package ar.edu.itba.gasDiffusion;


import ar.edu.itba.models.Particle;

import java.util.List;

public class GasDiffusionResult {

    private final List<List<Particle>> particles;
    private final long totalTime;

    public GasDiffusionResult(List<List<Particle>> particles, long totalTime) {
        this.particles = particles;
        this.totalTime = totalTime;
    }

    public List<List<Particle>> getParticles() {
        return particles;
    }
    public long getTotalTime() {
        return totalTime;
    }
}