package ar.edu.itba.models;

import java.util.*;

public class Enclosure implements Iterable<Enclosure>{

    private final List<Particle> particles;
    private final double L;
    private final double side;
    private double nextCollisionDelta;
    private double time = 0;
    private boolean isFirstIteration = true;
    private final Map<Pair<Particle, Particle>, Double> particleCollisionTimes = new HashMap<>();
    private final Map<Pair<Particle, Boundary>, Double> obstacleCollisionTimes = new HashMap<>();
    private Pair<Particle, ?> nextCollision;

    public Enclosure(List<Particle> particles, Double side, Double L) {
        this.side = side;
        this.L = L;
        this.particles = particles;
        initializeEnclosure(particles, L);
    }

    public double getTime() {
        return time;
    }
    public Collection<Particle> getParticles() {
        return particles;
    }
    public double getL() {
        return L;
    }
    public double getSide() {
        return side;
    }

    private void initializeEnclosure(Collection<Particle> particles, double L) {
        this.particleCollisionTimes.putAll(getInitialCollisionTimes(particles));
        Collection<Boundary> obstacles = Arrays.asList(
                // cuadrado de la izquierda
                new Boundary(new Position(0, 0), this.side, BoundaryType.BOTTOM),
                new Boundary(new Position(0, 0), this.side, BoundaryType.LEFT),
                new Boundary(new Position(0, this.side), this.side, BoundaryType.TOP),
                new Boundary(new Position(0, 0), (this.side - L) / 2, BoundaryType.RIGHT),
                new Boundary(new Position(0, this.side - (this.side - L) / 2), (this.side - L) / 2, BoundaryType.RIGHT),
                // rectangulo de la derecha
                new Boundary(new Position(this.side, (this.side - L) / 2), this.side, BoundaryType.BOTTOM),
                new Boundary(new Position(this.side, this.side - (this.side - L) / 2), this.side, BoundaryType.TOP),
                new Boundary(new Position(this.side, (this.side - L) / 2), L, BoundaryType.RIGHT)
        );
        this.obstacleCollisionTimes.putAll(getInitialWallCollisionTimes(obstacles, particles));
        setNextCollision();
    }

    private Map<Pair<Particle, Particle>, Double> getInitialCollisionTimes(Collection<Particle> particles) {
        final Map<Pair<Particle, Particle>, Double> collisionTimes = new HashMap<>();
        for (Particle p1 : particles) {
            for (Particle p2 : particles) {
                final Pair<Particle, Particle> pair = new Pair<>(p1, p2);
                if (!collisionTimes.containsKey(pair)) {
                    collisionTimes.put(pair, pair.getOne().getCollisionTime(pair.getOther()));
                }
            }
        }
        return collisionTimes;
    }

    private <V> Pair<Particle, V> getNextCollision(Map<Pair<Particle, V>, Double> wallCollisionTimes) {
        Map.Entry<Pair<Particle, V>, Double> maxEntry = null;
        for (Map.Entry<Pair<Particle, V>, Double> entry : wallCollisionTimes.entrySet()) {
            double collisionTime = entry.getValue();
            if (maxEntry == null || collisionTime < maxEntry.getValue()) {
                maxEntry = entry;
            }
        }
        return maxEntry.getKey();
    }


    private Map<Pair<Particle, Boundary>, Double> getInitialWallCollisionTimes(Collection<Boundary> boundaries, Collection<Particle> particles) {
        final Map<Pair<Particle, Boundary>, Double> collisionTimes = new HashMap<>();
        for (Particle p : particles) {
            for (Boundary b : boundaries) {
                Pair<Particle, Boundary> pair = new Pair<>(p, b);
                collisionTimes.put(pair, b.getCollisionTime(p));
            }
        }
        return collisionTimes;
    }


    private void updateCollisionTimesAfterCollision(Pair<Particle, Particle> particlesInvolved, double collisionDelta) {
        for (Map.Entry<Pair<Particle, Particle>, Double> entry : particleCollisionTimes.entrySet()) {
            final Pair<Particle, Particle> pair = entry.getKey();
            if (particlesInvolved.has(pair.getOne()) || particlesInvolved.has(pair.getOther())) {
                double nextCollision = pair.getOne().getCollisionTime(pair.getOther());
                entry.setValue(nextCollision);
            } else {
                double prevCollisionTime = entry.getValue();
                if (prevCollisionTime != Double.MAX_VALUE) {
                    entry.setValue(prevCollisionTime - collisionDelta);
                }
            }
        }
        for (Map.Entry<Pair<Particle, Boundary>, Double> entry : obstacleCollisionTimes.entrySet()) {
            final Pair<Particle, Boundary> pair = entry.getKey();
            if (particlesInvolved.has(pair.getOne())) {
                double nextCollision = pair.getOther().getCollisionTime(pair.getOne());
                entry.setValue(nextCollision);
            } else {
                double prevCollisionTime = entry.getValue();
                if (prevCollisionTime != Double.MAX_VALUE) {
                    entry.setValue(prevCollisionTime - collisionDelta);
                }
            }
        }
    }

    private void setNextCollision() {
        final Pair<Particle, Particle> particleCollision = getNextCollision(particleCollisionTimes);
        final Pair<Particle, Boundary> wallCollision = getNextCollision(obstacleCollisionTimes);

        double particleCollisionTime = particleCollisionTimes.getOrDefault(particleCollision, Double.MAX_VALUE);
        double wallCollisionTime = obstacleCollisionTimes.getOrDefault(wallCollision, Double.MAX_VALUE);
        if (particleCollisionTime < wallCollisionTime) {
            nextCollision = particleCollision;
            nextCollisionDelta = particleCollisionTime;
        } else {
            nextCollision = wallCollision;
            nextCollisionDelta = wallCollisionTime;
        }
    }

    private Enclosure getNextEnclosure() {
        if (isFirstIteration) {
            isFirstIteration = false;
            return this;
        }
        // calculo que particulas chocan en el proximo evento
        // actualizo la posicion y velocidad de esas dos particulas en base al choque
        // recalculo los tiempos de choque solo para las particulas que chocaron con las demas
        // actualizo todas las otras particulas avanzandolas en el tiempo de forma "normal"
        final Object o = nextCollision.getOther();
        final Particle particle = nextCollision.getOne();
        final double delta = nextCollisionDelta;
        particles.forEach(p -> p.moveForwardInTime(delta));
        if (o instanceof Boundary boundary) {
            boundary.collide(nextCollision.getOne());
            updateCollisionTimesAfterCollision(new Pair<>(particle, particle), delta);
        } else {
            final Particle particle2 = (Particle) o;
            particle.collide(particle2);
            updateCollisionTimesAfterCollision(new Pair<>(particle, particle2), delta);
        }

        time += delta;
        setNextCollision();

        return this;
    }

    @Override
    public Iterator<Enclosure> iterator() {
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Enclosure next() {
                return getNextEnclosure();
            }
        };
    }

}
