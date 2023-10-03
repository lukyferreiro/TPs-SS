package ar.edu.itba.simulation;

import ar.edu.itba.algorithms.utils.R;
import ar.edu.itba.models.Particle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static ar.edu.itba.algorithms.utils.R.values.*;

public class MolecularDynamic {

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
            Particle particle = new Particle(p.getId(), p.getRadius(), p.getMass(), p.getX(), p.getY(), p.getVx(), p.getVy(), p.getU(), p.getR());
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

        List<Particle> initialParticles = calculateInitialRs(particles, dt);

        try (PrintWriter pw = new PrintWriter(outFile)) {

            writeFile(pw, cloneParticles(initialParticles), new BigDecimal("0.0"));
            particlesOverTime.put(new BigDecimal("0.0"), cloneParticles(initialParticles));

            int iterations = 1;
            int totalIterations = (int) Math.ceil(maxTime / dt);

            BigDecimal dtBig = new BigDecimal(dt.toString());
            BigDecimal dt2Big = new BigDecimal(dt2.toString());
            BigDecimal currentTime = new BigDecimal("0.0");

            List<Particle> prevParticles = cloneParticles(initialParticles);

            for (BigDecimal t = dtBig; iterations < totalIterations; iterations += 1) {
                System.out.println(t);

                List<Particle> newParticles = new ArrayList<>();
                for (Particle p : prevParticles) {
                    Particle newParticle = new Particle(p.getId(), p.getRadius(), p.getMass(), p.getX(), p.getY(), p.getVx(), p.getVy(), p.getU(), p.getR());

                    final R predictedR = predict(p, dt, L);
                    newParticle.setX(predictedR.get(R0.ordinal()).getOne() % L, L);
                    newParticle.setVx(predictedR.get(R1.ordinal()).getOne());

                    final Double deltaR2 = getDeltaR2(predictedR, newParticle, prevParticles, dt);

                    final R correctedR = correct(predictedR, deltaR2, dt, L);

                    newParticle.setX(correctedR.get(R0.ordinal()).getOne(), L);
                    newParticle.setVx(correctedR.get(R1.ordinal()).getOne());
                    newParticle.setR(correctedR);

                    newParticles.add(newParticle);
                }

                prevParticles = cloneParticles(newParticles);

                currentTime = currentTime.add(dtBig);

                if (dtBig.equals(dt2Big) || currentTime.compareTo(dt2Big) >= 0) {
                    writeFile(pw, cloneParticles(newParticles), t);
                    particlesOverTime.put(t.setScale(1, RoundingMode.FLOOR), cloneParticles(newParticles));
                    currentTime = BigDecimal.ZERO;
                }

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

    public static List<Particle> calculateInitialRs(List<Particle> particles, Double dt) {
        List<Particle> newParticles = new ArrayList<>();
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
            //r6 no periodic
            currentR.add(p.getX(), 0.0);

            newParticles.add(new Particle(
                    p.getId(), p.getRadius(), p.getMass(),
                    p.getX(), p.getY(), p.getVx(), p.getVy(),
                    p.getU(), currentR)
            );
        }
        return newParticles;
    }

    private static R predict(Particle p, Double dt, Double L) {

        final R newPredictions = new R();

        double r0 = p.getX() % L;
        double r1 = p.getVx();
        double r2 = p.getFromR(R2.ordinal());
        double r3 = p.getFromR(R3.ordinal());
        double r4 = p.getFromR(R4.ordinal());
        double r5 = p.getFromR(R5.ordinal());
        double rNoPeriodic = p.getFromR(R6_NO_PERIODIC.ordinal());

        Double rp0 = r0 + r1 * dt +
                r2 * Math.pow(dt, 2) / factorial(2) +
                r3 * Math.pow(dt, 3) / factorial(3) +
                r4 * Math.pow(dt, 4) / factorial(4) +
                r5 * Math.pow(dt, 5) / factorial(5);

        Double rp1 = r1 + r2 * dt +
                r3 * Math.pow(dt, 2) / factorial(2) +
                r4 * Math.pow(dt, 3) / factorial(3) +
                r5 * Math.pow(dt, 4) / factorial(4);

        Double rp2 = r2 + r3 * dt +
                r4 * Math.pow(dt, 2) / factorial(2) +
                r5 * Math.pow(dt, 3) / factorial(3);

        Double rp3 = r3 + r4 * dt + r5 * Math.pow(dt, 2) / factorial(2);

        Double rp4 = r4 + r5 * dt;

        Double rp5 = r5;

        Double rpNoPeriodic = rNoPeriodic + r1 * dt +
                r2 * Math.pow(dt, 2) / factorial(2) +
                r3 * Math.pow(dt, 3) / factorial(3) +
                r4 * Math.pow(dt, 4) / factorial(4) +
                r5 * Math.pow(dt, 5) / factorial(5);

        newPredictions.add(rp0 % L, 0);
        newPredictions.add(rp1, 0);
        newPredictions.add(rp2, 0);
        newPredictions.add(rp3, 0);
        newPredictions.add(rp4, 0);
        newPredictions.add(rp5, 0);
        newPredictions.add(rpNoPeriodic, 0);

        return newPredictions;
    }


    private static Double getDeltaR2(R predictions, Particle p, List<Particle> particles, Double dt) {
        Double F = movementEquation(p, particles, dt);
        Double r2p = predictions.get(R2.ordinal()).getOne();    //Aceleración predecida
        return ((F - r2p) * Math.pow(dt, 2) / factorial(2));
    }

    private static R correct(R predictions, Double deltaR2, Double dt, Double L) {

        final R correctedR = new R();

        Double rp0 = predictions.get(R0.ordinal()).getOne();
        Double rp1 = predictions.get(R1.ordinal()).getOne();
        Double rp2 = predictions.get(R2.ordinal()).getOne();
        Double rp3 = predictions.get(R3.ordinal()).getOne();
        Double rp4 = predictions.get(R4.ordinal()).getOne();
        Double rp5 = predictions.get(R5.ordinal()).getOne();
        Double rpNoPeriodic = predictions.get(R6_NO_PERIODIC.ordinal()).getOne();

        List<Double> coefficients = posSpeedCoefficients.get(GEAR_ORDER);

        Double rc0 = rp0 + coefficients.get(R0.ordinal()) * deltaR2;
        Double rc1 = rp1 + coefficients.get(R1.ordinal()) * deltaR2 / dt;
        Double rc2 = rp2 + coefficients.get(R2.ordinal()) * deltaR2 * factorial(2) / Math.pow(dt, 2);
        Double rc3 = rp3 + coefficients.get(R3.ordinal()) * deltaR2 * factorial(3) / Math.pow(dt, 3);
        Double rc4 = rp4 + coefficients.get(R4.ordinal()) * deltaR2 * factorial(4) / Math.pow(dt, 4);
        Double rc5 = rp5 + coefficients.get(R5.ordinal()) * deltaR2 * factorial(5) / Math.pow(dt, 5);
        Double rcNoPeriodic = rpNoPeriodic + coefficients.get(R0.ordinal()) * deltaR2;

        correctedR.add(rc0 % L, 0.0);
        correctedR.add(rc1, 0.0);
        correctedR.add(rc2, 0.0);
        correctedR.add(rc3, 0.0);
        correctedR.add(rc4, 0.0);
        correctedR.add(rc5, 0.0);
        correctedR.add(rcNoPeriodic, 0.0);

        return correctedR;
    }

    private static int factorial(int number) throws IllegalArgumentException {
        if (number == 0) {
            return 1;
        }
        return number * factorial(number-1);
    }

    private static Double getForce(Particle p) {
        return (p.getU() - p.getVx());
    }

    private static Double collisionForce(Particle p1, Particle p2) {
        Double K = 2500.0;
        return K * (Math.abs(p2.getX() - p1.getX()) - 2 * p1.getRadius()) * Math.signum(p1.getX() - p2.getX());
    }

    private static Double movementEquation(Particle p1, List<Particle> particles, Double dt) {
        Double sumForces = 0.0;
        for (Particle p2 : particles) {
            if (!p2.equals(p1) && p1.collidesWith(p2, dt)) {
                sumForces += collisionForce(p2, p1);
            }
        }
        return (getForce(p1) + sumForces) / p1.getMass();
    }

}
