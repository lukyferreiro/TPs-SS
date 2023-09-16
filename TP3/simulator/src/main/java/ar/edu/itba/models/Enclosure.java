package ar.edu.itba.models;

import java.util.*;

public class Enclosure {

    private final Set<Particle> particles;
    private final double L;
    private final double side;
    private double time = 0;
    private boolean isFirstIteration = true;
    private double nextCollisionDelta;
    private final Map<Pair<Particle,Bounceable>, Double> allCollisions = new HashMap<>();
    private final Collection<Pair<Particle,Bounceable>> nextCollisions = new HashSet<>();


    public Enclosure(Set<Particle> particles, Double side, Double L) {
        this.side = side;
        this.L = L;
        this.particles = particles;
        initializeEnclosure(particles, L);
    }

    public double getTime() {
        return this.time;
    }
    public Set<Particle> getParticles() {
        return this.particles;
    }
    public double getL() {
        return this.L;
    }
    public double getSide() {
        return this.side;
    }

    private void initializeEnclosure(Set<Particle> particles, double L) {
        Collection<Bounceable> obstacles = Arrays.asList(
                // Esquinas de union de los recintos
                new CornerParticle(particles.size()+1, new Position(this.side, (this.side - L) / 2)),
                new CornerParticle(particles.size()+2, new Position(this.side, ((this.side - L) / 2) + L)),
                // Recinto de la izquierda
                new Boundary(new Position(0, 0), this.side, BoundaryType.BOTTOM),
                new Boundary(new Position(0, 0), this.side, BoundaryType.LEFT),
                new Boundary(new Position(0, this.side), this.side, BoundaryType.TOP),
                new Boundary(new Position(this.side, 0), (this.side - L) / 2, BoundaryType.RIGHT),
                new Boundary(new Position(this.side, ((this.side - L) / 2) + L), (this.side - L) / 2, BoundaryType.RIGHT),
                // Recinto de la derecha
                new Boundary(new Position(this.side, (this.side - L) / 2), this.side, BoundaryType.BOTTOM),
                new Boundary(new Position(this.side, ((this.side - L) / 2) + L), this.side, BoundaryType.TOP),
                new Boundary(new Position(2 * this.side, (this.side - L) / 2), L, BoundaryType.RIGHT)
        );
        setInitialCollisionTimes(obstacles,particles);
        setNextCollisions();
    }

    private void setInitialCollisionTimes(Collection<Bounceable> bounceables, Collection<Particle> particles) {
        Set<Bounceable> allBounceables = new HashSet<>(bounceables);
        allBounceables.addAll(particles);
        for (Particle p : particles) {
            for (Bounceable b : allBounceables) {
                Pair<Particle, Bounceable> pair = new Pair<>(p, b);
                Double time = pair.getOther().getCollisionTime(pair.getOne());
                if (!allCollisions.containsKey(pair)) {
                    allCollisions.put(pair, time);
                }
            }
        }
    }

    private void setNextCollisions() {
        if (!nextCollisions.isEmpty()) {
            nextCollisionDelta = 0;
            return;
        }
        double minTime = Integer.MAX_VALUE;
        for (Map.Entry<Pair<Particle, Bounceable>, Double> entry : allCollisions.entrySet()) {
            double collisionTime = entry.getValue();
            if (collisionTime < minTime) {
                nextCollisions.clear();
                minTime = collisionTime;
                nextCollisions.add(entry.getKey());
            } else if (collisionTime == minTime) {
                nextCollisions.add(entry.getKey());
            }
        }
        nextCollisionDelta = minTime;
    }

    private void updateCollisionTimesAfterCollision(Pair<Particle, Particle> particlesInvolved, Double collisionDelta) {
        for (Map.Entry<Pair<Particle, Bounceable>, Double> entry : allCollisions.entrySet()) {
            Pair<Particle, Bounceable> pair = entry.getKey();
            double newTime = Double.MAX_VALUE;
            // Si tomamos el par contiene una particula que colisiono, recalculamos el tc
            if (particlesInvolved.has(pair.getOne()) || particlesInvolved.has(pair.getOther())) {
                newTime = pair.getOther().getCollisionTime(pair.getOne());
            } else {    // Sino, solo le restamos el tiempo que ya teniamos de antes
                Double prevCollisionTime = entry.getValue();
                if (prevCollisionTime.compareTo(Double.MAX_VALUE) < 0) {
                    newTime = prevCollisionTime - collisionDelta;
                }
            }
            entry.setValue(newTime);
        }
    }

    public void getNextEnclosure() {
        if (isFirstIteration) {
            this.isFirstIteration = false;
            return;
        }
        Pair<Particle,Bounceable> nextCollision = nextCollisions.iterator().next();
        nextCollisions.remove(nextCollision);

        Particle particle = nextCollision.getOne();
        Object o = nextCollision.getOther();
        final double delta = this.nextCollisionDelta;

        particles.forEach(p -> p.moveForwardInTime(delta));

        if (o instanceof Boundary) {
            Boundary boundary = (Boundary) o;
            boundary.collide(particle);
            updateCollisionTimesAfterCollision(new Pair<>(particle, particle), delta);
        }  else {
            Particle particle2 = (Particle) o;
            particle2.collide(particle);
            updateCollisionTimesAfterCollision(new Pair<>(particle, particle2), delta);
        }

        System.out.printf("Time: %f\n",time);
        time += delta;
        setNextCollisions();
    }

    private static class Collision<T> {

        private final Particle particle;
        private final T other;
        private final Double time;

        Collision(Particle particle, T other, Double time){
            this.particle = particle;
            this.other = other;
            this.time = time;
        }

        public Particle getParticle() {
            return particle;
        }
        public T getOther() {
            return other;
        }
        public Double getTime() {
            return time;
        }
    }

}
