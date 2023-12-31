package ar.edu.itba;

import ar.edu.itba.algorithms.*;
import ar.edu.itba.algorithms.utils.AlgorithmResult;
import ar.edu.itba.models.Particle;
import ar.edu.itba.models.Position;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Locale;

public class BenchmarkOscillator {
    // PARAMETERS
    private static final int MASS = 70;
    private static final int RADIUS = 1;
    private static final double K = Math.pow(10, 4);
    private static final int GAMMA = 100;
    // INITIAL CONDITIONS
    private static final double X_0 = 1;
    private static final double A = 1;
    private static final double VX_0 = -A * GAMMA / (2 * MASS);
    //VARIABLES
    private static final Double MAX_TIME = 5.0;
    //BENCHMARk
    private static final String BEEMAN_DIR = "Beeman";
    private static final String GEAR_PREDICTOR_DIR = "GearPredictor";
    private static final String VERLET_ORIGINAL_DIR = "VerletOriginal";
    private static final List<Double> DTS = List.of(0.01, 0.001, 0.0001, 0.00001);

    public static void main(String[] args) throws IOException {

        final Particle oscillatorParticle = new Particle(1, RADIUS, MASS);
        oscillatorParticle.setPosition(new Position(X_0, 0));
        oscillatorParticle.setVx(VX_0);
        oscillatorParticle.setVx(0);

        for(Double dt : DTS) {
            System.out.print("--------------------------\n");
            System.out.printf(Locale.US, "Running simulator with dt %.6f\n", dt);

            for (Algorithm algorithm : Algorithm.values()) {
                AlgorithmResult result;

                switch (algorithm) {
                    case GEAR_PREDICTOR -> {
                        System.out.print("Running Gear Predictor\n");
                        result = GearPredictor.execute(oscillatorParticle, K, GAMMA, dt, MAX_TIME);
                        writeToFile(GEAR_PREDICTOR_DIR, result, dt);
                    }
                    case BEEMAN -> {
                        System.out.print("Running Beeman\n");
                        result = Beeman.execute(oscillatorParticle, K, GAMMA, dt, MAX_TIME);
                        writeToFile(BEEMAN_DIR, result, dt);
                    }
                    case VERLET_ORIGINAL -> {
                        System.out.print("Running Verlet\n");
                        result = VerletOriginal.execute(oscillatorParticle, K, GAMMA, dt, MAX_TIME);
                        writeToFile(VERLET_ORIGINAL_DIR, result, dt);
                    }
                    default -> {
                        System.out.println("Invalid algorithm");
                        return;
                    }
                }
            }
        }

        System.out.println("Done!");

    }

    private static void writeToFile(String algorithm, AlgorithmResult result, double dt) throws IOException {
        System.out.printf(Locale.US, "Finished Oscillation In %d Iterations and %.5f ms\n",
                result.getIterations(), (double) result.getTotalTime() / 1_000_000);
        String filePath = String.format(Locale.US, "src/main/resources/oscillator/benchmark/%s_%f.txt", algorithm, dt);
        final File outResultsFile = new File(filePath);
        try (PrintWriter pw = new PrintWriter(outResultsFile)) {
            result.getParticles().forEach((time, particle) -> {
                pw.printf(Locale.US, "%.6f\n", time);
                pw.printf(Locale.US, "%d %.20f %.1f %.20f %.1f\n",
                        particle.getId(),
                        particle.getX(),
                        particle.getY(),
                        particle.getVx(),
                        particle.getVy()
                );
            });
        }
    }
}
