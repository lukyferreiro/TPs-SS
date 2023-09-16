package ar.edu.itba.models;

import java.util.*;

public class Enclosure{

    private final List<Particle> particles;
    private final double L;
    private final double side;
    private double time = 0;
    private boolean isFirstIteration = true;
    private final Map<Pair<Particle, Particle>, Double> particleCollisionTimes = new HashMap<>();
    private final Map<Pair<Particle, Boundary>, Double> obstacleCollisionTimes = new HashMap<>();
    private final Map<Pair<Particle, CornerParticle>, Double> cornerCollisionTimes = new HashMap<>();
    private final List<Collision<?>> allCollisions = new ArrayList<>();
    private Collision<?> nextCollision;


    public Enclosure(List<Particle> particles, Double side, Double L) {
        this.side = side;
        this.L = L;
        this.particles = particles;
        initializeEnclosure(particles, L);
    }

    public double getTime() {
        return this.time;
    }
    public List<Particle> getParticles() {
        return this.particles;
    }
    public double getL() {
        return this.L;
    }
    public double getSide() {
        return this.side;
    }

    private void initializeEnclosure(List<Particle> particles, double L) {
        this.particleCollisionTimes.putAll(getInitialCollisionTimes(particles));
        Collection<Boundary> obstacles = Arrays.asList(
                // cuadrado de la izquierda
                new Boundary(new Position(0, 0), this.side, BoundaryType.BOTTOM),
                new Boundary(new Position(0, 0), this.side, BoundaryType.LEFT),
                new Boundary(new Position(0, this.side), this.side, BoundaryType.TOP),
                new Boundary(new Position(this.side, 0), (this.side - L) / 2, BoundaryType.RIGHT),
                new Boundary(new Position(this.side, ((this.side - L) / 2) + L), (this.side - L) / 2, BoundaryType.RIGHT),
                // rectangulo de la derecha
                new Boundary(new Position(this.side, (this.side - L) / 2), this.side, BoundaryType.BOTTOM),
                new Boundary(new Position(this.side, ((this.side - L) / 2) + L), this.side, BoundaryType.TOP),
                new Boundary(new Position(2 * this.side, (this.side - L) / 2), L, BoundaryType.RIGHT)
        );
        this.obstacleCollisionTimes.putAll(getInitialWallCollisionTimes(obstacles, particles));
        Collection<CornerParticle> corners = Arrays.asList(
                new CornerParticle(10000, new Position(this.side, (this.side - L) / 2)),
                new CornerParticle(10001, new Position(this.side, ((this.side - L) / 2) + L))
        );
        this.cornerCollisionTimes.putAll(getInitialCornerCollisionTimes(corners, particles));
        setNextCollision();
    }

    private Map<Pair<Particle, Particle>, Double> getInitialCollisionTimes(Collection<Particle> particles) {
        //Guardamos las proximas colision entre todos los posibles pares de particulas
        Map<Pair<Particle, Particle>, Double> collisionTimes = new HashMap<>();
        for (Particle p1 : particles) {
            for (Particle p2 : particles) {
                Pair<Particle, Particle> pair = new Pair<>(p1, p2);
                if (!collisionTimes.containsKey(pair) && !p1.equals(p2)) {
                    collisionTimes.put(pair, pair.getOne().getCollisionTime(pair.getOther()));
                }
            }
        }
        return collisionTimes;
    }

    private Map<Pair<Particle, Boundary>, Double> getInitialWallCollisionTimes(Collection<Boundary> boundaries, Collection<Particle> particles) {
        //Guardamos las proximas colision entre todas las particulas y las paredes
        Map<Pair<Particle, Boundary>, Double> collisionTimes = new HashMap<>();
        for (Particle p : particles) {
            for (Boundary b : boundaries) {
                Pair<Particle, Boundary> pair = new Pair<>(p, b);
                collisionTimes.put(pair, b.getCollisionTime(p));
            }
        }
        return collisionTimes;
    }

    private Map<Pair<Particle, CornerParticle>, Double> getInitialCornerCollisionTimes(Collection<CornerParticle> corners, Collection<Particle> particles) {
        //Guardamos las proximas colision entre todas las particulas y las esquinas
        Map<Pair<Particle, CornerParticle>, Double> collisionTimes = new HashMap<>();
        for (Particle p : particles) {
            for (CornerParticle c : corners) {
                Pair<Particle, CornerParticle> pair = new Pair<>(p, c);
                collisionTimes.put(pair, c.getCollisionTime(p));
            }
        }
        return collisionTimes;
    }

    private void setNextCollision() {
        Pair<Particle, Particle> particleCollision = getNextCollision(particleCollisionTimes);
        Pair<Particle, Boundary> wallCollision = getNextCollision(obstacleCollisionTimes);
        Pair<Particle, CornerParticle> cornersCollision = getNextCollision(cornerCollisionTimes);

        Double particleCollisionTime = particleCollisionTimes.getOrDefault(particleCollision, null);
        Double wallCollisionTime = obstacleCollisionTimes.getOrDefault(wallCollision, null);
        Double cornerCollisionTime = cornerCollisionTimes.getOrDefault(cornersCollision, null);

        //Nos fijamos cual es la primera colisión que ocurre: con pared, con particula o con esquina
        //TODO check
        if (particleCollisionTime != null && wallCollisionTime != null && cornerCollisionTime == null
                && particleCollisionTime > 0 && particleCollisionTime.compareTo(wallCollisionTime) < 0) {
            this.nextCollision = new Collision<Particle>(particleCollision.getOne(), particleCollision.getOther(), particleCollisionTime);
        } else if (wallCollisionTime != null && particleCollisionTime != null && cornerCollisionTime == null
                && wallCollisionTime > 0 && wallCollisionTime.compareTo(particleCollisionTime) < 0) {
            this.nextCollision = new Collision<Boundary>(wallCollision.getOne(), wallCollision.getOther(), wallCollisionTime);
        } else {
            this.nextCollision = new Collision<CornerParticle>(cornersCollision.getOne(), cornersCollision.getOther(), cornerCollisionTime);
        }

    }

    private <V> Pair<Particle, V> getNextCollision(Map<Pair<Particle, V>, Double> collisionTimes) {
        //Buscamos el tiempo mas chico en colisionar
        Map.Entry<Pair<Particle, V>, Double> pairWithLowestTime = null;
        for (Map.Entry<Pair<Particle, V>, Double> entry : collisionTimes.entrySet()) {
            Double collisionTime = entry.getValue();
            if ((pairWithLowestTime == null || pairWithLowestTime.getValue() == null)|| (collisionTime != null && collisionTime > 0 && collisionTime.compareTo(pairWithLowestTime.getValue()) < 0)) {
                pairWithLowestTime = entry;
            }
        }
        return pairWithLowestTime.getKey();
    }

    private void updateCollisionTimesAfterCollision(Pair<Particle, Particle> particlesInvolved, double collisionDelta) {
        // Recalculo los tiempos de choque solo para las particulas que chocaron con respecto a las demas
        for (Map.Entry<Pair<Particle, Particle>, Double> entry : particleCollisionTimes.entrySet()) {
            Pair<Particle, Particle> pair = entry.getKey();

            Double nextCollision = pair.getOne().getCollisionTime(pair.getOther());
            entry.setValue(nextCollision);

            // Si tengo un par de una de las particulas involucradas, recalculo el tiempo
//            if (particlesInvolved.has(pair.getOne()) || particlesInvolved.has(pair.getOther())) {
//                Double nextCollision = pair.getOne().getCollisionTime(pair.getOther());
//                entry.setValue(nextCollision);
//            } else {    // Sino, le resto el tiempo que ya tenia con el nuevo
//                Double prevCollisionTime = entry.getValue();
//                if (prevCollisionTime != null) {
//                    entry.setValue(prevCollisionTime - collisionDelta);
//                }
//            }
        }

        // Analago al anterior pero con las paredes
        for (Map.Entry<Pair<Particle, Boundary>, Double> entry : obstacleCollisionTimes.entrySet()) {
            Pair<Particle, Boundary> pair = entry.getKey();
            Double nextCollision = pair.getOther().getCollisionTime(pair.getOne());
            entry.setValue(nextCollision);
//            if (particlesInvolved.has(pair.getOne())) {
//                Double nextCollision = pair.getOther().getCollisionTime(pair.getOne());
//                entry.setValue(nextCollision);
//            } else {
//                Double prevCollisionTime = entry.getValue();
//                if (prevCollisionTime != null) {
//                    entry.setValue(prevCollisionTime - collisionDelta);
//                }
//            }
        }

        // Analago al anterior pero con las esquinas
        for (Map.Entry<Pair<Particle, CornerParticle>, Double> entry : cornerCollisionTimes.entrySet()) {
            Pair<Particle, CornerParticle> pair = entry.getKey();
            Double nextCollision = pair.getOther().getCollisionTime(pair.getOne());
            entry.setValue(nextCollision);
        }
    }

    public void getNextEnclosure() {
        if (isFirstIteration) {
            this.isFirstIteration = false;
            return;
        }
        // calculo que particulas chocan en el proximo evento
        // actualizo la posicion y velocidad de esas dos particulas en base al choque
        // recalculo los tiempos de choque solo para las particulas que chocaron con las demas
        // actualizo todas las otras particulas avanzandolas en el tiempo de forma "normal"
        Particle particle = this.nextCollision.getParticle();
        Object o = this.nextCollision.getOther();
        Double delta = this.nextCollision.getTime();

        if (delta == null) {
            return;
        }

        this.particles.forEach(p -> p.moveForwardInTime(delta));

        allCollisions.add(this.nextCollision);
        this.nextCollision = null;

        if (o instanceof Boundary) {
            Boundary boundary = (Boundary) o;
            boundary.collide(particle);
            updateCollisionTimesAfterCollision(new Pair<>(particle, particle), delta);
        } else if (o instanceof CornerParticle){
            CornerParticle corner = (CornerParticle) o;
            corner.collide(particle);
            updateCollisionTimesAfterCollision(new Pair<>(particle, corner), delta);
        } else {
            Particle particle2 = (Particle) o;
            particle2.collide(particle);
            updateCollisionTimesAfterCollision(new Pair<>(particle, particle2), delta);
        }

        time += delta;
        setNextCollision();
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
