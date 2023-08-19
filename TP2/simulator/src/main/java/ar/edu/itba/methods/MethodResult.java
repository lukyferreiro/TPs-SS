package ar.edu.itba.methods;

import ar.edu.itba.models.Particle;

import java.util.Map;
import java.util.Set;

public class MethodResult {
    private final Map<Particle, Set<Particle>> neighbors;
    private final long totalTime;

    public MethodResult(Map<Particle, Set<Particle>> neighbors, long totalTime) {
        this.neighbors = neighbors;
        this.totalTime = totalTime;

    }

    public Map<Particle, Set<Particle>> getNeighbors() {
        return neighbors;
    }
    public long getTotalTime() {
        return totalTime;
    }


}
