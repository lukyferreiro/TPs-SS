package ar.edu.itba.methods;

import ar.edu.itba.models.Particle;

import java.util.Map;
import java.util.Set;

public class MethodResult {
    private final long totalTime;
    private final Map<Integer, Set<Particle>> neighbors;

    public MethodResult(Map<Integer, Set<Particle>> neighbors, long totalTime) {
        this.totalTime = totalTime;
        this.neighbors = neighbors;
    }

    public long getTotalTime() {
        return totalTime;
    }
    public Map<Integer, Set<Particle>> getNeighbors() {
        return neighbors;
    }

}
