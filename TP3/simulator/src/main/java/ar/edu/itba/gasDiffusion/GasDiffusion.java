package ar.edu.itba.gasDiffusion;

import ar.edu.itba.models.Enclosure;
import ar.edu.itba.models.Particle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GasDiffusion {

    public static GasDiffusionResult run(List<List<Particle>> particlesPerTime, Integer iterations, Double side, Double L, File outFile) {
        long startTime = System.nanoTime();
        Enclosure enclosure = new Enclosure(particlesPerTime.get(0), side, L);

        List<List<Particle>> particlesOverTime = new ArrayList<>();
        particlesOverTime.add(particlesPerTime.get(0));

        try (PrintWriter pw = new PrintWriter(outFile)) {
            for (int i = 0; i < iterations; i++) {
                enclosure.getNextEnclosure();

                List<Particle> particles = enclosure.getParticles();

                pw.printf(Locale.US, "%.20f\n", enclosure.getTime());
                particles.forEach((particle) ->
                        pw.printf(Locale.US, "%d %.20f %.20f %f %f\n",
                                particle.getId(),
                                particle.getPosition().getX(),
                                particle.getPosition().getY(),
                                particle.getVx(),
                                particle.getVy()
                        )
                );
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        long endTime = System.nanoTime();
        long totalTime = endTime - startTime;
        return new GasDiffusionResult(particlesOverTime, totalTime);
    }
}
