package ar.edu.itba.simulation;

import ar.edu.itba.algorithms.utils.R;
import ar.edu.itba.models.Pair;
import ar.edu.itba.models.Particle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
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

    private static void writeFile(PrintWriter pw, List<Particle> particles, BigDecimal time) {
        pw.printf(Locale.US, "%.6f\n", time);
        particles.forEach((particle) ->
                pw.printf(Locale.US, "%d %.20f %.1f %.20f %.1f\n",
                    particle.getId(),
                    particle.getPosition().getX(),
                    particle.getPosition().getY(),
                    particle.getVx(),
                    particle.getVy())
        );
    }

    public static List<Particle> cloneParticles(List<Particle> particles) {
        List<Particle> newParticles = new ArrayList<>();
        for(Particle p : particles) {
            Particle particle = new Particle(p.getId(), p.getRadius(), p.getMass(), p.getX(), p.getY(), p.getVx(), p.getVy(), p.getU());
            newParticles.add(particle);
        }
        return newParticles;
    }

    public static Map<BigDecimal, List<Particle>> run(
            List<Particle> particles, Double L, Double maxTime,
            Double dt, Double dt2, File outFile
    ) {
        long startTime = System.nanoTime();

        final Map<BigDecimal, List<Particle>> particlesOverTime = new HashMap<>();

        List<R> currentRs = calculateInitialRs(particles, dt);

        try (PrintWriter pw = new PrintWriter(outFile)) {

            writeFile(pw, particles, new BigDecimal("0.0"));
            particlesOverTime.put(new BigDecimal("0.0"), cloneParticles(particles));

            int iterations = 1;
            int totalIterations = (int) Math.ceil(maxTime / dt);

            BigDecimal dtBig = new BigDecimal(dt.toString());
            BigDecimal dt2Big = new BigDecimal(dt2.toString());
            BigDecimal currentTime = dtBig;

            for (BigDecimal t = dtBig; iterations < totalIterations; iterations += 1) {

                for (Particle particle : particles) {
                    particle.setX(currentRs.get(particle.getId() - 1).get(R0.ordinal()).getOne(), L);
                    particle.setVx(currentRs.get(particle.getId() - 1).get(R1.ordinal()).getOne());
                }

                System.out.println(t);

                if (currentTime.compareTo(dt2Big) >= 0) {
                    writeFile(pw, particles, t);
                    particlesOverTime.put(t.setScale(1, RoundingMode.FLOOR), cloneParticles(particles));
                    currentTime = dtBig;
                } else {
                    currentTime = currentTime.add(dtBig);
                }

                final List<R> predictions = predict(currentRs, dt);
                final List<Double> deltaR2 = getDeltaR2(predictions, particles, dt);

                currentRs = correct(predictions, deltaR2, particles, dt, L);

                t = t.add(dtBig);

            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        long endTime = System.nanoTime();
        long totalTime = endTime - startTime;

        System.out.println("Total time: " + totalTime / 1_000_000);

        return particlesOverTime;

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
            Double r2 = movementEquation(p, particles, dt);
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

    private static List<R> predict(List<R> currentRs, Double dt) {
        List<R> newRs = new ArrayList<>();

        int count = 0;

        for (R currentR : currentRs) {

            final R newPredictions = new R();

            for (int i = 0; i < TOTAL_PREDICTIONS; i++) {
                Double rpx = 0.0;

                for (int j = i; j < TOTAL_PREDICTIONS; j++) {
                    final Pair<Double, Double> rj = currentR.get(j);
                    rpx += ( rj.getOne() * Math.pow(dt, j - i) / factorial(j - i) );
                }

                newPredictions.add(rpx, 0.0);

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

            final Double deltaR2x = ((F - r2x) * Math.pow(dt, 2) / factorial(2));

            deltasR2.add(deltaR2x);
        }

        return deltasR2;
    }

    private static List<R> correct(List<R> predictions, List<Double> deltaR2, List<Particle> particles, Double dt, Double L) {
        List<R> corrections = new ArrayList<>();

        int count = 0;
        for (R prediction : predictions) {

            final R aux = new R();

            for (int i = 0; i < TOTAL_PREDICTIONS; i++) {
                Double rpxi = prediction.get(i).getOne();

                final Double rcx = rpxi + posSpeedCoefficients.get(GEAR_ORDER).get(i) * deltaR2.get(count) * factorial(i) / Math.pow(dt, i);

                if (i == 0) {
                    particles.get(count).setX(rcx, L);
                } else if (i == 1) {
                    particles.get(count).setVx(rcx);
                }

                aux.add(rcx, 0.0);
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

    private static Double getForce(Particle p) {
        return (p.getU() - p.getVx());
    }

    private static Double collisionForce(Particle p1, Particle p2) {
        Double K = 2500.0;
        return K * (Math.abs(p2.getX() - p1.getX()) - 2 * p1.getRadius()) * Math.signum(p2.getX() - p1.getX());
    }

    private static Double movementEquation(Particle p1, List<Particle> particles, Double dt) {
        Double sumForces = 0.0;
        for (Particle p2 : particles) {
            if (!p2.equals(p1) && p2.collidesWith(p1, dt)) {
                sumForces += collisionForce(p1, p2);
            }
        }
        return (getForce(p1) + sumForces) / p1.getMass();
    }

}
