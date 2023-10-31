package ar.edu.itba.simulation;

import ar.edu.itba.models.Container;
import ar.edu.itba.models.Particle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class GranularDynamic {
    private static void writeFile(PrintWriter pw, List<Particle> particles, BigDecimal time) {
        pw.printf(Locale.US, "%.6f\n", time);
        particles.forEach((particle) ->
                pw.printf(Locale.US, "%d %.20f %.20f\n",
                        particle.getId(),
                        particle.getPosition().getOne(),
                        particle.getPosition().getOther()
        ));
    }

    public static List<Particle> cloneParticles(List<Particle> particles) {
        List<Particle> newParticles = new ArrayList<>();
        for(Particle p : particles) {
            Particle particle = p.copy();
            newParticles.add(particle);
        }
        return newParticles;
    }

    public static Map<BigDecimal, List<Particle>> run(
            List<Particle> particles, Double l, Double w, Double maxTime, Double frequency, Double holeSize,
            Double dt, Double dt2, File outFile
    ) {
        long startTime = System.nanoTime();

        final Map<BigDecimal, List<Particle>> particlesOverTime = new HashMap<>();

        List<Particle> initialParticles = cloneParticles(particles);

        List<Double> times = new ArrayList<>();

        try (PrintWriter pw = new PrintWriter(outFile)) {

            writeFile(pw, cloneParticles(initialParticles), new BigDecimal("0.0"));
            particlesOverTime.put(new BigDecimal("0.0"), cloneParticles(initialParticles));

            int iterations = 1;
            int totalIterations = (int) Math.ceil(maxTime / dt);

            BigDecimal dtBig = new BigDecimal(dt.toString());
            BigDecimal dt2Big = new BigDecimal(dt2.toString());
            BigDecimal currentTime = new BigDecimal("0.0");

            List<Particle> prevParticles = cloneParticles(particles);

            Container container = new Container(w, (l + l) / 10, 0.0, l / 10, holeSize);
            container.addAll(prevParticles);


            for (BigDecimal t = dtBig; iterations < totalIterations; iterations += 1) {
                System.out.println(t);

                List<Particle> newParticles = new ArrayList<>();

                container.shake(t.doubleValue(), frequency);

                for (Particle particle : particles) {
                    particle.predict(dt);
                }
                particles.forEach(Particle::resetForces);

                for (int j = 0; j < container.update(); j++)
                    times.add(t.doubleValue());

                container.updateForces(dt);

                for (Particle particle : particles) {
                    particle.correct(dt);
                }
                particles.forEach(Particle::resetForces);

                container.updateForces(dt);

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
}
