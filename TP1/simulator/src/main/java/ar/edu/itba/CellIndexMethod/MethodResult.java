package ar.edu.itba.CellIndexMethod;

import java.time.LocalTime;
import java.util.Map;
import java.util.Set;

public class MethodResult {
    private final LocalTime totalTime;
    private final Map<Integer, Set<Particle>> neighbors;

    public MethodResult(Map<Integer, Set<Particle>> neighbors, LocalTime totalTime) {
        this.totalTime = totalTime;
        this.neighbors = neighbors;
    }

    public LocalTime getTotalTime() {
        return totalTime;
    }
    public Map<Integer, Set<Particle>> getNeighbors() {
        return neighbors;
    }

}
