package ar.edu.itba.algorithms;

import ar.edu.itba.algorithms.utils.AlgorithmResult;
import ar.edu.itba.algorithms.utils.AlgorithmsUtils;
import ar.edu.itba.algorithms.utils.R;
import ar.edu.itba.models.Pair;
import ar.edu.itba.models.Particle;
import ar.edu.itba.models.Position;

import java.util.*;

import static ar.edu.itba.algorithms.utils.R.values.*;

public class Beeman {

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
            final R nextR = calculateNextR(prevR, currentR, dt, particle.getMass(), k, gamma);

            final Pair<Double, Double> r0 = nextR.get(R0.ordinal());
            final Pair<Double, Double> r1 = nextR.get(R1.ordinal());

            if (Double.compare(t, tf - 2 * dt) == 0) {
                System.out.println();
            }

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

    private static R calculateNextR(R prevR, R currentR, double dt, double mass, double k, double gamma) {
        final R nextR = new R();

        final double r0x = currentR.get(R0.ordinal()).getOne() + currentR.get(R1.ordinal()).getOne() * dt +
                ((2.0 / 3) * currentR.get(R2.ordinal()).getOne() - (1.0 / 6) * prevR.get(R2.ordinal()).getOne()) * Math.pow(dt, 2);
        final double r0y = currentR.get(R0.ordinal()).getOther() + currentR.get(R1.ordinal()).getOther() * dt +
                ((2.0 / 3) * currentR.get(R2.ordinal()).getOther() - (1.0 / 6) * prevR.get(R2.ordinal()).getOther()) * Math.pow(dt, 2);
        nextR.add(r0x, r0y);

        //Velocity predictions
        final double r1Px = currentR.get(R1.ordinal()).getOne() +
                ((3.0 / 2) * currentR.get(R2.ordinal()).getOne() - (1.0 / 2) * prevR.get(R2.ordinal()).getOne()) * dt;
        final double r1Py = currentR.get(R1.ordinal()).getOther() +
                ((3.0 / 2) * currentR.get(R2.ordinal()).getOther() - (1.0 / 2) * prevR.get(R2.ordinal()).getOther()) * dt;

        final Pair<Double, Double> r2P = AlgorithmsUtils.calculateAcceleration(mass, new Pair<>(r0x, r0y), new Pair<>(r1Px, r1Py), k, gamma);

        //Velocity correction
        final double r1Cx = currentR.get(R1.ordinal()).getOne() +
                ((1.0 / 3) * r2P.getOne() + (5.0 / 6) * currentR.get(R2.ordinal()).getOne() -
                        (1.0 / 6) * prevR.get(R2.ordinal()).getOne()) * dt;
        final double r1Cy = currentR.get(R1.ordinal()).getOther() +
                ((1.0 / 3) * r2P.getOther() + (5.0 / 6) * currentR.get(R2.ordinal()).getOther() -
                        (1.0 / 6) * prevR.get(R2.ordinal()).getOther()) * dt;
        nextR.add(r1Cx, r1Cy);

        final Pair<Double, Double> r2C = AlgorithmsUtils.calculateAcceleration(mass, new Pair<>(r0x, r0y), new Pair<>(r1Cx, r1Cy), k, gamma);
        nextR.add(r2C.getOne(), r2C.getOther());

        return nextR;

    }
}