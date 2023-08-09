package ar.edu.itba.methods;

import ar.edu.itba.models.Particle;

import java.time.Duration;
import java.util.Map;
import java.util.Set;

public class MethodResult {
    private final Duration totalTime;
    private final Map<Integer, Set<Particle>> neighbors;

    public MethodResult(Map<Integer, Set<Particle>> neighbors, Duration totalTime) {
        this.totalTime = totalTime;
        this.neighbors = neighbors;
    }

    public Duration getTotalTime() {
        return totalTime;
    }
    public Map<Integer, Set<Particle>> getNeighbors() {
        return neighbors;
    }

}
