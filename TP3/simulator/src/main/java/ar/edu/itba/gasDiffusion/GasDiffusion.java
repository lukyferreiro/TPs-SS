package ar.edu.itba.gasDiffusion;

import ar.edu.itba.models.Enclosure;
import ar.edu.itba.models.Pair;
import ar.edu.itba.models.Particle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class GasDiffusion {

    public static void run(List<Set<Particle>> particlesPerTime, Integer time, Double deltaT, Double side, Double L, File outFile) throws FileNotFoundException {
        long startTime = System.nanoTime();
        Enclosure enclosure = new Enclosure(particlesPerTime.get(0), side, L);

        List<Double> times = new ArrayList<>();
        Pair<Double, Double> pressures;
        Double totalPressure;

        double timePassed = 0;

        try (PrintWriter pw = new PrintWriter(outFile)) {
            while (enclosure.getTime() < time) {
                if (timePassed > deltaT) {
                    pressures = enclosure.calculatePressure(times);
                    totalPressure = enclosure.calculateTotalPressure();
                    writeFile(pw, enclosure.getParticles(), enclosure.getTime(), pressures, totalPressure);
                    timePassed = 0;
                }

                timePassed += enclosure.getNextEnclosure();
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        long endTime = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println("Total time: " + totalTime / 1_000_000);
    }

    private static void writeFile(PrintWriter pw, Set<Particle> particles, double time, Pair<Double, Double> pressures, Double totalPressure){
        pw.printf(Locale.US, "%.20f\n", time);
        pw.printf(Locale.US, "P1=%.5f P2=%.5f P3=%.5f\n", pressures.getOne(), pressures.getOther(), totalPressure);
        particles.forEach((particle) ->
                pw.printf(Locale.US, "%d %.20f %.20f\n",
                        particle.getId(),
                        particle.getPosition().getX(),
                        particle.getPosition().getY()
                )
        );
    }


}