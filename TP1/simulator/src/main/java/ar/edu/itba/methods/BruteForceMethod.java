package ar.edu.itba.methods;

import ar.edu.itba.models.Grid;
import ar.edu.itba.models.Particle;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.function.BiFunction;

public class BruteForceMethod {

    public static MethodResult calculateNeighbors(
            Map<Particle, Particle.Position> particles, double L, double Rc, Boolean periodic
    ) {

        final LocalDateTime startTime = LocalDateTime.now();
        final Map<Integer, Set<Particle>> neighbors = new HashMap<>();

        BiFunction<Particle.Position, Particle.Position, Double> computeDistance = periodic
                ? (p1, p2) -> Particle.Position.calculateDistancePeriodic(p1,p2,L)
                : (p1, p2) -> Particle.Position.calculateDistance(p1,p2);

        particles.keySet().forEach(particle -> neighbors.put(particle.getId(), new HashSet<>()));

        // Iterar sobre todas las partículas y comparamos todas con todas
        for (Particle particle : particles.keySet()) {
            final Particle.Position currentPosition = particles.get(particle);
            for (Particle otherParticle : particles.keySet()) {
                if (!particle.equals(otherParticle)) {
                    final Particle.Position otherParticlePosition = particles.get(otherParticle);
                    double distance = computeDistance.apply(currentPosition, otherParticlePosition);
                    double borderDistance = distance - particle.getRadius() - otherParticle.getRadius();
                    if (borderDistance <= Rc) {
                        neighbors.get(particle.getId()).add(otherParticle);
                        neighbors.get(otherParticle.getId()).add(particle);
                    }
                }
            }
        }

        final LocalDateTime endTime = LocalDateTime.now();
        final Duration totalTime = Duration.between(startTime, endTime);
        return new MethodResult(neighbors, totalTime);
    }
}

