package ar.edu.itba.methods;

import ar.edu.itba.models.Cell;
import ar.edu.itba.models.Grid;
import ar.edu.itba.models.Particle;

import java.util.*;

import static ar.edu.itba.models.Particle.Position;

public class CellIndexMethod {

    public static MethodResult calculateNeighbors(
            Map<Particle, Position> particles, double L, Long M, double Rc, Boolean periodic
    ) {

        final long startTime = System.nanoTime();
        final Grid grid = new Grid(L, M, particles, periodic);
        final Map<Particle, Set<Particle>> neighbors = new HashMap<>();

        particles.forEach((key, value) -> neighbors.put(key, new HashSet<>()));

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

        final long endTime = System.nanoTime();
        final long totalTime = endTime - startTime;
        return new MethodResult(neighbors, totalTime);
    }

    private static void checkNeighbors(
            Particle particle, Map<Particle, Position> particles,
            Cell cell, Map<Particle, Set<Particle>> neighbors,
            double Rc, double L, Long M, boolean periodic
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
            Particle particle, Position particlePosition, Particle otherParticle,
            Position otherParticlePosition, Map<Particle, Set<Particle>> neighbors, double Rc
    ) {
        //Distancia Borde-borde = Distancia centros de masa - Ri -Rj.
        final double distance = Position.calculateDistance(particlePosition, otherParticlePosition);
        final double borderDistance = distance - particle.getRadius() - otherParticle.getRadius();
        if (borderDistance <= Rc) {
            neighbors.get(particle).add(otherParticle);
            neighbors.get(otherParticle).add(particle);
        }
    }

}
