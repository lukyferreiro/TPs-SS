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

    private final Collection<Collision> wallCollisions = new HashSet<>();


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
                new Boundary(new Position(0, 0), this.side, BoundaryType.BOTTOM, 0),
                new Boundary(new Position(0, 0), this.side, BoundaryType.LEFT, 0),
                new Boundary(new Position(0, this.side), this.side, BoundaryType.TOP, 0),
                new Boundary(new Position(this.side, 0), (this.side - L) / 2, BoundaryType.RIGHT, 0),
                new Boundary(new Position(this.side, ((this.side - L) / 2) + L), (this.side - L) / 2, BoundaryType.RIGHT, 0),
                // Recinto de la derecha
                new Boundary(new Position(this.side, (this.side - L) / 2), this.side, BoundaryType.BOTTOM, 1),
                new Boundary(new Position(this.side, ((this.side - L) / 2) + L), this.side, BoundaryType.TOP, 1),
                new Boundary(new Position(2 * this.side, (this.side - L) / 2), L, BoundaryType.RIGHT, 1)
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

    public double getNextEnclosure() {
        if (isFirstIteration) {
            this.isFirstIteration = false;
            return 0;
        }
        Pair<Particle,Bounceable> nextCollision = nextCollisions.iterator().next();
        nextCollisions.remove(nextCollision);

        Particle particle = nextCollision.getOne();
        Object o = nextCollision.getOther();
        final double delta = this.nextCollisionDelta;

        particles.forEach(p -> p.moveForwardInTime(delta));

        if (o instanceof Boundary) {
            Boundary boundary = (Boundary) o;
            wallCollisions.add(new Collision(nextCollision.getOne(), boundary, time));
            boundary.collide(particle);
            updateCollisionTimesAfterCollision(new Pair<>(particle, particle), delta);
        }  else {
            Particle particle2 = (Particle) o;
            particle2.collide(particle);
            updateCollisionTimesAfterCollision(new Pair<>(particle, particle2), delta);
        }



        time += delta;
        setNextCollisions();

        return delta;
    }

    public Pair<Double, Double> calculatePressure(double deltaT, List<Double> times) {
        double pressureA = 0;
        double pressureB = 0;
        for (Collision c:wallCollisions) {
            if (c.bouncer.getId() == 0) {
                pressureA += c.getDeltaF();
            }
            else {
                pressureB += c.getDeltaF();
            }
        }

        pressureA/=deltaT;
        pressureB/=deltaT;

        times.add(time);
        Pair<Double, Double> pressures = (new Pair<>((pressureA/(side*side)) , (pressureB/(side*L))));
        wallCollisions.clear();
        return pressures;
    }

    private static class Collision {

        private final Particle particle;
        private final Boundary bouncer;
        private final Double time;
        private final Double deltaF;

        Collision(Particle particle, Boundary bouncer, Double time){
            this.particle = particle;
            this.bouncer = bouncer;
            this.time = time;

            double F = 0;

            if (bouncer.getType().equals(BoundaryType.LEFT) || bouncer.getType().equals(BoundaryType.RIGHT)) {
                F = Math.abs((2 * particle.getMass() * particle.getVx()));
            }
            else {
                F = Math.abs((2 * particle.getMass() * particle.getVy()));
            }

            this.deltaF = F;
        }

        public Particle getParticle() {
            return particle;
        }
        public Bounceable getBouncer() {
            return bouncer;
        }
        public Double getTime() {
            return time;
        }

        @Override
        public int hashCode() {
            return Objects.hash(particle, bouncer, time);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Collision other)) {
                return false;
            }
            return this.particle.equals(other.particle) && this.bouncer.equals(other.bouncer) && this.time.equals(other.time);

        }

        public Double getDeltaF() {
            return deltaF;
        }
    }

}
