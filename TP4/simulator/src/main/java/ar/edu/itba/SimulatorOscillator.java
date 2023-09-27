package ar.edu.itba;

import ar.edu.itba.algorithms.*;
import ar.edu.itba.algorithms.utils.AlgorithmResult;
import ar.edu.itba.models.Particle;
import ar.edu.itba.models.Position;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

public class SimulatorOscillator {

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
    private static final Algorithm algorithm = Algorithm.VERLET_ORIGINAL;
    private static final Double DT = 0.1;
    private static final Double MAX_TIME = 5.0;


    public static void main(String[] args) throws IOException {
        System.out.println("SimulatorOscillator Starting ...");

        final Particle oscillatorParticle = new Particle(1, RADIUS, MASS);
        oscillatorParticle.setPosition(new Position(X_0, 0));
        oscillatorParticle.setVx(VX_0);
        oscillatorParticle.setVx(0);
        AlgorithmResult result;

        switch (algorithm) {
            case GEAR_PREDICTOR ->
                    result = GearPredictor.execute(oscillatorParticle, K, GAMMA, DT, MAX_TIME);
            case BEEMAN ->
                    result = Beeman.execute(oscillatorParticle, K, GAMMA, DT, MAX_TIME);
            case VERLET_ORIGINAL ->
                    result = VerletOriginal.execute(oscillatorParticle, K, GAMMA, DT, MAX_TIME);
            default -> {
                System.out.println("Invalid algorithm");
                return;
            }
        }


        System.out.printf(Locale.US, "Finished Oscillation In %d Iterations and %.5f ms\n",
                result.getIterations(), (double )result.getTotalTime() / 1_000_000);

        System.out.println("Writing Results ...");

        final File outFile = new File("src/main/resources/oscillator/outOscillatorFile.txt");

        try (PrintWriter pw = new PrintWriter(outFile)) {
            result.getParticles().forEach((time, particle) -> {
                    pw.append(String.format("%f\n", time));
                    pw.printf(Locale.US, "%d %.20f %.20f %.20f %.20f\n",
                            particle.getId(),
                            particle.getX(),
                            particle.getY(),
                            particle.getVx(),
                            particle.getVy()
                    );
            });
        }

        System.out.print("SimulatorOscillator done!");

    }
}
