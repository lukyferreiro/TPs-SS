package ar.edu.itba.CellIndexMethod;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class BruteForceMethod {

    public static MethodResult calculateNeighbors(
            Map<Particle, Particle.Position> particles, double L, Long M, double Rc, Boolean periodic
    ) {

        final LocalDateTime startTime = LocalDateTime.now();
        final Grid grid = new Grid(L, M, particles, periodic);
        final Map<Integer, Set<Particle>> neighbors = new HashMap<>();

        particles.forEach((key, value) -> neighbors.put(key.getId(), new HashSet<>()));

        //TODO check si anda y que onda con periodic
        //En Brute Force chequeamos todas las particulas contra todas la particulas
        for(Particle particle : particles.keySet()){
            final Particle.Position currentPosition = particles.get(particle);
            for(Particle otherParticle : particles.keySet()){
                if(!particle.equals(otherParticle)){
                    final Particle.Position otherParticlePosition = particles.get(otherParticle);
                    final double distance = Particle.Position.calculateDistance(currentPosition, otherParticlePosition);
                    final double borderDistance = distance - particle.getRadius() - otherParticle.getRadius();
                    if (borderDistance <= Rc) {
                        neighbors.get(particle.getId()).add(otherParticle);
                        neighbors.get(otherParticle.getId()).add(particle);
                    }
                }
            }
        }

        final LocalDateTime endTime = LocalDateTime.now();
        final LocalTime totalTime = LocalTime.ofNanoOfDay(endTime.toLocalTime().toNanoOfDay() - startTime.toLocalTime().toNanoOfDay());
        return new MethodResult(neighbors, totalTime);
    }
}
