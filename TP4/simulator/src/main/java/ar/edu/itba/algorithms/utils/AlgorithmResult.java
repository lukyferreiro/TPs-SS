package ar.edu.itba.algorithms.utils;

import ar.edu.itba.models.Particle;

import java.util.Map;

public class AlgorithmResult {

    private final long totalTime;
    private final int iterations;
    private final Map<Double, Particle> particles;

    public AlgorithmResult(long totalTime, int iterations, Map<Double, Particle> particles) {
        this.totalTime = totalTime;
        this.iterations = iterations;
        this.particles = particles;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public int getIterations() {
        return iterations;
    }

    public Map<Double, Particle> getParticles() {
        return particles;
    }
}
