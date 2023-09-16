package ar.edu.itba.gasDiffusion;

import ar.edu.itba.models.Enclosure;
import ar.edu.itba.models.Particle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class GasDiffusion {

    public static void run(List<Set<Particle>> particlesPerTime, Integer time, Double side, Double L, File outFile, File outTimeFile) throws FileNotFoundException {
        long startTime = System.nanoTime();
        Enclosure enclosure = new Enclosure(particlesPerTime.get(0), side, L);

        List<Double> times = new ArrayList<>();
        List<Double> pressures = new ArrayList<>();
        double timePassed = 0;
        boolean calculatePressure = false;

        try (PrintWriter pw = new PrintWriter(outFile)) {
            while (enclosure.getTime() < time) {
                if (timePassed > 1) {
                    calculatePressure = true;
                    timePassed = 0;
                }
                else {
                    calculatePressure = false;
                }

                timePassed += enclosure.getNextEnclosure(calculatePressure, times, pressures);
                writeFile(pw, enclosure.getParticles(), enclosure.getTime());
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        long endTime = System.nanoTime();
        long totalTime = endTime - startTime;

        try (PrintWriter pw = new PrintWriter(outTimeFile)) {
            final double totalTimeMillis = (double) totalTime / 1_000_000; // Convert nanoseconds to milliseconds
            pw.append(String.format(Locale.US, "Total time - %f ms\n", totalTimeMillis));
        }
    }

    private static void writeFile(PrintWriter pw, Set<Particle> particles, double time){
        pw.printf(Locale.US, "%.20f\n", time);
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
}