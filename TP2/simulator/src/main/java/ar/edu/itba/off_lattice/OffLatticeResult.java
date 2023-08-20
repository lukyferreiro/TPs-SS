package ar.edu.itba.off_lattice;

import ar.edu.itba.models.Particle;

import java.util.List;
import java.util.Map;

import static ar.edu.itba.models.Particle.State;

public class OffLatticeResult {
    private final List<Map<Particle, State>> particlesStates;
    private final List<Double> orderParameter;
    private final long totalTime;

    public OffLatticeResult(List<Map<Particle, State>> particlesStates, List<Double> orderParameter, long totalTime) {
        this.particlesStates = particlesStates;
        this.orderParameter = orderParameter;
        this.totalTime = totalTime;
    }

    public List<Map<Particle, State>> getParticlesStates() {
        return particlesStates;
    }
    public List<Double> getOrderParameter() {
        return orderParameter;
    }
    public long getTotalTime() {
        return totalTime;
    }
}
