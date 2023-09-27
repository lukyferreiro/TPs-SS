package ar.edu.itba.algorithms;

import ar.edu.itba.algorithms.utils.AlgorithmResult;
import ar.edu.itba.algorithms.utils.AlgorithmsUtils;
import ar.edu.itba.algorithms.utils.R;
import ar.edu.itba.models.Pair;
import ar.edu.itba.models.Particle;
import ar.edu.itba.models.Position;

import java.util.*;

import static ar.edu.itba.algorithms.utils.R.values.*;

public class GearPredictor {

    private static final Map<Integer, List<Double>> posSpeedCoefficients = Map.ofEntries(
            Map.entry(2, Arrays.asList(0.0, 1.0, 1.0)),
            Map.entry(3, Arrays.asList(1.0 / 6, 5.0 / 6, 1.0, 1.0 / 3)),
            Map.entry(4, Arrays.asList(19.0 / 90, 3.0 / 4, 1.0, 1.0 / 2, 1.0 / 12)),
            Map.entry(5, Arrays.asList(3.0 / 16, 251.0 / 360, 1.0, 11.0 / 18, 1.0 / 6, 1.0 / 60)));

    private static final int TOTAL_PREDICTIONS = 6;

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

        int iterations = 0;
        int totalIterations = (int) Math.ceil(tf / dt);
        for (double t = dt; iterations < totalIterations; t += dt, iterations += 1) {

            final R currentR = RStates.get(iterations);
            final R predictions = predict(currentR, dt);

            final Pair<Double, Double> deltaR2 = getDeltaR2(
                    particle.getMass(), k, gamma,
                    predictions.get(R0.ordinal()), predictions.get(R1.ordinal()),
                    predictions.get(R2.ordinal()), dt
            );

            final R corrections = correct(predictions, deltaR2, dt);

            final Pair<Double, Double> r0 = corrections.get(R0.ordinal());
            final Pair<Double, Double> r1 = corrections.get(R1.ordinal());

            RStates.add(corrections);

            Particle auxP = new Particle(particle.getId(), particle.getRadius(), particle.getMass());
            auxP.setPosition(new Position(r0.getOne(), r0.getOther()));
            auxP.setVx(r1.getOne());
            auxP.setVy(r1.getOther());
            particles.put(t, auxP);
        }

        long endTime = System.nanoTime();
        long totalTime = endTime - startTime;

        return new AlgorithmResult(totalTime, iterations, particles);
    }

    public static R predict(final R currentR, final double dt) {

        final R newPredictions = new R();

        for (int i = 0; i < TOTAL_PREDICTIONS; i++) {
            double rpx = 0;
            double rpy = 0;

            for (int j = i; j < TOTAL_PREDICTIONS; j++) {
                final Pair<Double, Double> rj = currentR.get(j);
                rpx += rj.getOne() * Math.pow(dt, j - i) / factorial(j - i);
                rpy += rj.getOther() * Math.pow(dt, j - i) / factorial(j - i);
            }
            newPredictions.add(rpx, rpy);
        }

        return newPredictions;
    }


    private static Pair<Double, Double> getDeltaR2(
            double mass, double k, double gamma, Pair<Double, Double> r0p,
            Pair<Double, Double> r1p, Pair<Double, Double> r2p, double dt
    ) {
        //Evalúo la aceleracion con la posicion y velocidad predecida
        final double r2x = (-k * r0p.getOne() - gamma * r1p.getOne()) / mass;
        final double r2y = (-k * r0p.getOther() - gamma * r1p.getOther()) / mass;

        //Aceleración predecida
        final double r2px = r2p.getOne();
        final double r2py = r2p.getOther();

        final double deltar2x = r2x - r2px;
        final double deltar2y = r2y - r2py;

        final double deltaR2x = deltar2x * Math.pow(dt, 2) / factorial(2);
        final double deltaR2y = deltar2y * Math.pow(dt, 2) / factorial(2);

        return new Pair<>(deltaR2x, deltaR2y);
    }

    private static R correct(R predictions, Pair<Double, Double> deltaR2, double dt) {
        final R corrections = new R();

        for (int i = 0; i < TOTAL_PREDICTIONS; i++) {
            Pair<Double, Double> rpi = predictions.get(i);

            final double rcx = rpi.getOne() + GearPredictor.posSpeedCoefficients.get(5).get(i) * deltaR2.getOne()
                    * factorial(i) / Math.pow(dt, i);
            final double rcy = rpi.getOther() + GearPredictor.posSpeedCoefficients.get(5).get(i) * deltaR2.getOther()
                    * factorial(i) / Math.pow(dt, i);

            corrections.add(rcx, rcy);
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

}
