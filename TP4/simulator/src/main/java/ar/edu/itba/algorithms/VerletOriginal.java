package ar.edu.itba.algorithms;

import ar.edu.itba.algorithms.utils.AlgorithmResult;
import ar.edu.itba.algorithms.utils.AlgorithmsUtils;
import ar.edu.itba.algorithms.utils.R;
import ar.edu.itba.models.Pair;
import ar.edu.itba.models.Particle;
import ar.edu.itba.models.Position;

import java.util.*;

import static ar.edu.itba.algorithms.utils.R.values.*;

public class VerletOriginal {

    public static AlgorithmResult execute(Particle particle, double k, double gamma, double dt, double tf) {

        long startTime = System.nanoTime();
        final Map<Double, Particle> particles = new TreeMap<>();

        final R initialR = AlgorithmsUtils.calculateInitialR(particle, k, gamma);
        final List<R> RStates = new ArrayList<>();
        RStates.add(initialR);

        final Pair<Double, Double> initialr0 = initialR.get(R0.ordinal());
        final Pair<Double, Double> initialr1 = initialR.get(R1.ordinal());

        Particle p = new Particle(particle.getId(), particle.getRadius(), particle.getMass());
        p.setPosition(new Position(initialr0.getOne(), initialr0.getOther()));
        p.setVx(initialr1.getOne());
        p.setVy(initialr1.getOther());
        particles.put(0.0, p);

        R prevR = AlgorithmsUtils.euler(initialR, -dt, particle.getMass(), k, gamma);

        int iterations = 0;
        int totalIterations = (int) Math.ceil(tf / dt);
        for (double t = dt; iterations < totalIterations; t += dt, iterations += 1) {

            final R currentR = RStates.get(iterations);
            final R nextR = calculateNextR(prevR, currentR, particle.getMass(), k, gamma, dt);

            final Pair<Double, Double> r0 = nextR.get(R0.ordinal());
            final Pair<Double, Double> r1 = nextR.get(R1.ordinal());

            Particle auxP = new Particle(particle.getId(), particle.getRadius(), particle.getMass());
            auxP.setPosition(new Position(r0.getOne(), r0.getOther()));
            auxP.setVx(r1.getOne());
            auxP.setVy(r1.getOther());
            particles.put(t, auxP);

            RStates.add(nextR);
            prevR = currentR;
        }

        long endTime = System.nanoTime();
        long totalTime = endTime - startTime;

        return new AlgorithmResult(totalTime, iterations, particles);

    }

    private static R calculateNextR(R prevR, R currentR, double mass, double k, double gamma, double dt) {

        final R nextR = new R();

        final Pair<Double, Double> r2 = AlgorithmsUtils.calculateAcceleration(
                mass, currentR.get(R0.ordinal()), currentR.get(R1.ordinal()), k, gamma
        );

        final double r0x = 2 * currentR.get(R0.ordinal()).getOne() - prevR.get(R0.ordinal()).getOne() +
                (Math.pow(dt, 2) / mass) * mass * r2.getOne();
        final double r0y = 2 * currentR.get(R0.ordinal()).getOther() - prevR.get(R0.ordinal()).getOther() +
                (Math.pow(dt, 2) / mass) * mass * r2.getOther();
        nextR.add(r0x, r0y);

        final double r1x = (r0x - prevR.get(R0.ordinal()).getOne()) / (2 * dt);
        final double r1y = (r0y - prevR.get(R0.ordinal()).getOther()) / (2 * dt);
        nextR.add(r1x, r1y);

        return nextR;
    }
}
