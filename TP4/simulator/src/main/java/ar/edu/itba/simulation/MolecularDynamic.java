package ar.edu.itba.simulation;

import ar.edu.itba.algorithms.utils.R;
import ar.edu.itba.models.Pair;
import ar.edu.itba.models.Particle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

import static ar.edu.itba.algorithms.utils.R.values.*;

public class MolecularDynamic {

    private static final int TOTAL_PREDICTIONS = 6;
    private static final int GEAR_ORDER = 5;
    private static final Map<Integer, List<Double>> posSpeedCoefficients = Map.ofEntries(
            Map.entry(2, Arrays.asList(0.0, 1.0, 1.0)),
            Map.entry(3, Arrays.asList(1.0 / 6, 5.0 / 6, 1.0, 1.0 / 3)),
            Map.entry(4, Arrays.asList(19.0 / 90, 3.0 / 4, 1.0, 1.0 / 2, 1.0 / 12)),
            Map.entry(5, Arrays.asList(3.0 / 16, 251.0 / 360, 1.0, 11.0 / 18, 1.0 / 6, 1.0 / 60))
    );

    private static void writeFile(PrintWriter pw, List<Particle> particles, Double time) {
        pw.printf(Locale.US, "%.20f\n", time);
        particles.forEach((particle) ->
                pw.printf(Locale.US, "%d %.20f %.1f %.20f %.1f\n",
                    particle.getId(),
                    particle.getPosition().getX(),
                    particle.getPosition().getY(),
                    particle.getVx(),
                    particle.getVy())
        );
    }

    public static void run(List<List<Particle>> particlesPerTime, Double L, Double maxTime, Double dt, Double dt2, File outFile) {
        long startTime = System.nanoTime();

        List<Particle> particles = particlesPerTime.get(0);

        List<R> currentRs = calculateInitialRs(particles, dt);

        int iterations = 0;
        int totalIterations = (int) Math.ceil(maxTime / dt);

        double currentTime = 0;

        try (PrintWriter pw = new PrintWriter(outFile)) {
            for (double t = dt; iterations < totalIterations; t += dt, iterations += 1) {

                for (Particle particle : particles) {
                    particle.setX(currentRs.get(particle.getId() - 1).get(R0.ordinal()).getOne(), L);
                    particle.setVx(currentRs.get(particle.getId() - 1).get(R1.ordinal()).getOne());
//                particle.setAx(currentRs.get(particle.getId()).get(R2.ordinal()).getOne());
                }

                if (currentTime > dt2) {
                    writeFile(pw, particles, t);
                    currentTime = 0;
                }
                else {
                    currentTime += dt;
                }

                final List<R> predictions = predict(currentRs, particles, dt, L);

                final List<Double> deltaR2 = getDeltaR2(predictions, particles, dt);

                currentRs = correct(predictions, deltaR2, dt, L);

            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        long endTime = System.nanoTime();
        long totalTime = endTime - startTime;

        System.out.println("Total time: " + totalTime / 1_000_000);

    }

    public static List<R> calculateInitialRs(List<Particle> particles, Double dt) {
        List<R> initialRs = new ArrayList<>();
        for (Particle p : particles) {
            final R currentR = new R();

            //r0
            currentR.add(p.getX(), 0.0);
            //r1
            currentR.add(p.getVx(), 0.0);
            //r2
            double r2 = movementEquation(p, particles, dt);
            currentR.add(r2, 0.0);

            //r3
            currentR.add(0.0, 0.0);
            //r4
            currentR.add(0.0, 0.0);
            //r5
            currentR.add(0.0, 0.0);

            initialRs.add(currentR);
        }

        return initialRs;
    }

    private static List<R> predict(List<R> currentRs, List<Particle> particles, Double dt, Double L) {
        List<R> newRs = new ArrayList<>();

        int count = 0;

        for (R currentR : currentRs) {

            final R newPredictions = new R();

            for (int i = 0; i < TOTAL_PREDICTIONS; i++) {
                double rpx = 0;

                for (int j = i; j < TOTAL_PREDICTIONS; j++) {
                    final Pair<Double, Double> rj = currentR.get(j);
                    rpx += rj.getOne() * Math.pow(dt, j - i) / factorial(j - i);
                }

                if (i == 0) {
                    particles.get(count).setX(rpx, L);
                    newPredictions.add(rpx % L, 0.0);

                } else if (i == 1) {
                    particles.get(count).setVx(rpx);
                    newPredictions.add(rpx, 0.0);
                }
//                } else if (i == 2) {
//                    particles.get(i).setAx(rpx);
//                    newPredictions.add(rpx, 0.0);
//                }
                else {
                    newPredictions.add(rpx, 0.0);
                }
            }
            count++;
            newRs.add(newPredictions);
        }

        return newRs;
    }


    private static List<Double> getDeltaR2(List<R> predictions, List<Particle> particles, Double dt) {
        List<Double> deltasR2 = new ArrayList<>();

        for (Particle p : particles) {

            Double F = movementEquation(p, particles, dt);

            //Aceleración predecida
            Double r2x = predictions.get(p.getId() - 1).get(R2.ordinal()).getOne();

            final double deltaR2x = (F - r2x) * Math.pow(dt, 2) / factorial(2);

            deltasR2.add(deltaR2x);
        }

        return deltasR2;
    }

    private static List<R> correct(List<R> predictions, List<Double> deltaR2, Double dt, Double L) {
        List<R> corrections = new ArrayList<>();

        int count = 0;
        for (R prediction : predictions) {

            final R aux = new R();

            for (int i = 0; i < TOTAL_PREDICTIONS; i++) {
                Double rpxi = prediction.get(i).getOne();

                final double rcx = rpxi + posSpeedCoefficients.get(GEAR_ORDER).get(i) * deltaR2.get(count) * factorial(i) / Math.pow(dt, i);

                if (i == 0) {
                    aux.add(rcx % L, 0.0);
                } else {
                    aux.add(rcx, 0.0);
                }
            }

            corrections.add(aux);
            count++;
        }

        return corrections;
    }

    private static int factorial(int number) throws IllegalArgumentException {

        if (number < 0) {
            throw new IllegalArgumentException();
        }

        int factorial = 1;
        while (number != 0) {
            factorial = factorial * number;
            number--;
        }

        return factorial;
    }

    private static double getForce(Particle p) {
        return (p.getU() - p.getVx());
    }

    //fuerza que le ejerce p1 sobre p2
    private static double collisionForce(Particle p1, Particle p2) {
        double K = 2500.0;
        return K * (Math.abs(p1.getX() - p2.getX()) - 2 * p1.getRadius()) * Math.signum(p2.getX() - p1.getX());
    }

    private static double movementEquation(Particle p1, List<Particle> particles, Double dt) {
        double sumForces = 0;
        for (Particle p2 : particles) {
            if (!p2.equals(p1) && p2.collidesWith(p1, dt)) {
                sumForces += collisionForce(p1, p2);
            }
        }
        return (getForce(p1) + sumForces) / p1.getMass();
    }

}
