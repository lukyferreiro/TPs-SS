package ar.edu.itba.CellIndexMethod;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static ar.edu.itba.CellIndexMethod.Particle.Position;

public class CellIndexMethod {

    public static MethodResult calculateNeighbors(
            Map<Particle, Position> particles, double L, Long M, double Rc, Boolean periodic
    ) {

        final LocalDateTime startTime = LocalDateTime.now();
        final Grid grid = new Grid(L, M, particles, periodic);
        final Map<Integer, Set<Particle>> neighbors = new HashMap<>();

        particles.forEach((key, value) -> neighbors.put(key.getId(), new HashSet<>()));

        for (int y = 0; y < M; y++) {
            for (int x = 0; x < M; x++) {
                final Cell currentCell = grid.getCellInGrid(y,x);
                if (!currentCell.isEmpty()) {
                    for (Particle p : currentCell.getParticles()) {
                        checkNeighbors(p, particles, currentCell, neighbors, Rc, L, M, periodic);
                    }
                }
            }
        }

        final LocalDateTime endTime = LocalDateTime.now();
        final LocalTime totalTime = LocalTime.ofNanoOfDay(endTime.toLocalTime().toNanoOfDay() - startTime.toLocalTime().toNanoOfDay());
        return new MethodResult(neighbors, totalTime);
    }

    private static void checkNeighbors(
            final Particle particle, final Map<Particle, Position> particles,
            final Cell cell, final Map<Integer, Set<Particle>> neighbors,
            final double Rc, final double L, final Long M, final boolean periodic
    ) {
        final Cell topCell = cell.getTopCell();
        final Cell rightCell = cell.getRightCell();
        final Cell topRightCell = cell.getTopRightCell();
        final Cell bottomRightCell = cell.getBottomRightCell();

        final Position particlePosition = particles.get(particle);

        for (Particle otherParticle : cell.getParticles()) {
            Position otherParticlePosition = particles.get(otherParticle);
            if (!particle.equals(otherParticle)) {
                addIfInRadius(particle, particlePosition, otherParticle, otherParticlePosition, neighbors, Rc);
            }
        }

        if (topCell != null && !topCell.isEmpty()) {
            for (Particle otherParticle : topCell.getParticles()) {
                Position otherParticlePosition = particles.get(otherParticle);

                if (periodic && M != 1) {
                    if (otherParticlePosition.getY() < particlePosition.getY()) {
                        otherParticlePosition = new Position(
                                otherParticlePosition.getX(),
                                otherParticlePosition.getY() + L);
                    }
                }

                addIfInRadius(particle, particlePosition, otherParticle, otherParticlePosition, neighbors, Rc);
            }
        }

        if (topRightCell != null && !topRightCell.isEmpty()) {
            for (Particle otherParticle : topRightCell.getParticles()) {
                Position otherParticlePosition = particles.get(otherParticle);

                if (periodic && M != 1) {
                    if (otherParticlePosition.getY() < particlePosition.getY()) {
                        otherParticlePosition = new Position(
                                otherParticlePosition.getX() + (otherParticlePosition.getX() < particlePosition.getX() ? L : 0),
                                otherParticlePosition.getY() + L);
                    } else if (otherParticlePosition.getX() < particlePosition.getX()) {
                        otherParticlePosition = new Position(
                                otherParticlePosition.getX() + L,
                                otherParticlePosition.getY());
                    }
                }

                addIfInRadius(particle, particlePosition, otherParticle, otherParticlePosition, neighbors, Rc);
            }
        }

        if (rightCell != null && !rightCell.isEmpty()) {
            for (Particle otherParticle : rightCell.getParticles()) {
                Position otherParticlePosition = particles.get(otherParticle);

                if (periodic && M != 1) {
                    if (otherParticlePosition.getX() < particlePosition.getX()) {
                        otherParticlePosition = new Position(
                                otherParticlePosition.getX() + L,
                                otherParticlePosition.getY());
                    }
                }

                addIfInRadius(particle, particlePosition, otherParticle, otherParticlePosition, neighbors, Rc);
            }
        }

        if (bottomRightCell != null && !bottomRightCell.isEmpty()) {
            for (Particle otherParticle : bottomRightCell.getParticles()) {
                Position otherParticlePosition = particles.get(otherParticle);

                if (periodic && M != 1) {
                    if (otherParticlePosition.getY() > particlePosition.getY()) {
                        otherParticlePosition = new Position(
                                otherParticlePosition.getX() + (otherParticlePosition.getX() < particlePosition.getX() ? L : 0),
                                otherParticlePosition.getY() - L);
                    } else if (otherParticlePosition.getX() < particlePosition.getX()) {
                        otherParticlePosition = new Position(
                                otherParticlePosition.getX() + L,
                                otherParticlePosition.getY());
                    }
                }

                addIfInRadius(particle, particlePosition, otherParticle, otherParticlePosition, neighbors, Rc);
            }
        }

    }

    private static void addIfInRadius(
            final Particle particle, final Position particlePosition, final Particle otherParticle,
            final Position otherParticlePosition, final Map<Integer, Set<Particle>> neighbors, final double Rc
    ) {
        //Distancia Borde-borde = Distancia centros de masa - Ri -Rj.
        final double distance = Position.calculateDistance(particlePosition, otherParticlePosition);
        final double borderDistance = distance - particle.getRadius() - otherParticle.getRadius();
        if (borderDistance <= Rc) {
            neighbors.get(particle.getId()).add(otherParticle);
            neighbors.get(otherParticle.getId()).add(particle);
        }
    }

}
