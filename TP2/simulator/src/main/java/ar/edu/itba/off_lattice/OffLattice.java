package ar.edu.itba.off_lattice;

import ar.edu.itba.methods.CellIndexMethod;
import ar.edu.itba.models.Particle;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import static ar.edu.itba.models.Particle.Position;
import static ar.edu.itba.models.Particle.State;

public class OffLattice {

    public static OffLatticeResult startSimulation(
            Map<Particle, State> initialParticlesStates, double L, Long M, double R,
            double dt, double eta, boolean periodic, long maxIterations
    ) {

        final long startTime = System.nanoTime();
        final List<Map<Particle, State>> particlesStates = new ArrayList<>(List.of(initialParticlesStates));
        final List<Double> orderParameters = new ArrayList<>();
        int iterations = 0;
        double currentOrderParameter;

        while (iterations < maxIterations) {
            final Map<Particle, State> currentParticlesStates = particlesStates.get(particlesStates.size() - 1);

            //Calculate order parameter
            currentOrderParameter = generateOrderParameterVa(currentParticlesStates.values());
            orderParameters.add(currentOrderParameter);

            //Update particles positions
            final Map<Particle, Position> currentParticlesPositions = currentParticlesStates
                    .entrySet()
                    .stream()
                    .collect(Collectors.toMap(Entry::getKey, e -> e.getValue().getPosition()));

            final Map<Particle, Set<Particle>> currentNeighbors = CellIndexMethod
                    .calculateNeighbors(currentParticlesPositions, L, M, R, periodic).getNeighbors();

            final Map<Particle, State> nextParticlesState = new HashMap<>();

            currentNeighbors.forEach((particle, neighbors) ->
                    nextParticlesState.put(particle, getNextState(particle, neighbors, currentParticlesStates, dt, eta, L))
            );

            particlesStates.add(nextParticlesState);
            iterations++;
        }

        final long endTime = System.nanoTime();
        final long totalTime = endTime - startTime;
        return new OffLatticeResult(particlesStates, orderParameters, totalTime);
    }

    private static State getNextState(
            Particle currentParticle, Set<Particle> neighbors, Map<Particle, State> particlesState,
            double dt, double eta, double L
    ) {

        final List<State> neighborsStates = neighbors.stream().map(particlesState::get).collect(Collectors.toList());
        final State currentParticleState = particlesState.get(currentParticle);

        // Calculate next position
        double nextX = (currentParticleState.getPosition().getX() + currentParticleState.getXVelocity() * dt) % L;
        if (nextX < 0) {
            nextX += L;
        }
        double nextY = (currentParticleState.getPosition().getY() + currentParticleState.getYVelocity() * dt) % L;
        if (nextY < 0) {
            nextY += L;
        }

        final Position nextPosition = new Position(nextX, nextY);

        // Calculate next angle
        final List<State> surroundingParticlesStates = new ArrayList<>(neighborsStates);
        surroundingParticlesStates.add(currentParticleState);

        final double avgCos = surroundingParticlesStates.stream()
                .mapToDouble(p -> Math.cos(p.getAngle())).average().orElseThrow(RuntimeException::new);
        final double avgSin = surroundingParticlesStates.stream()
                .mapToDouble(p -> Math.sin(p.getAngle())).average().orElseThrow(RuntimeException::new);

        final double nextAngle = Math.atan2(avgSin, avgCos) + generateNoise(eta);

        return new State(nextPosition, currentParticleState.getSpeed(), nextAngle);
    }

    private static double generateNoise(double eta) {
        return -eta / 2 + eta * new Random().nextDouble();
    }

    private static double generateOrderParameterVa(Collection<State> particlesStates) {
        final double speed = particlesStates.stream().findFirst().orElseThrow().getSpeed();
        final double vx = particlesStates.stream().mapToDouble(State::getXVelocity).sum();
        final double vy = particlesStates.stream().mapToDouble(State::getYVelocity).sum();
        final double norm = Math.sqrt(Math.pow(vx, 2) + Math.pow(vy, 2));
        return norm / (particlesStates.size() * speed);
    }

}
