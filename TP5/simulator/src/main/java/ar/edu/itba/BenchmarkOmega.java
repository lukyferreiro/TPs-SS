package ar.edu.itba;

import ar.edu.itba.models.Particle;
import ar.edu.itba.simulation.GranularDynamic;
import ar.edu.itba.utils.CreateStaticAndDynamicFiles;
import ar.edu.itba.utils.ParticlesParserResult;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.*;

public class BenchmarkOmega {
    final static Double DT = 0.001;
    final static Double DT2 = 0.1;
    final static Double L = 70.0;
    final static Double W = 20.0;
    final static Double MAX_TIME = 1000.0;
    final static Double D = 3.0;
    final static Integer N = 200;
    final static List<Double> Omegas = List.of(5.0, 10.0, 15.0, 20.0, 30.0, 50.0);

    public static void main(String[] args) throws IOException {

        final List<Double> caudales = new ArrayList<>();

        for (Double omega : Omegas) {
            System.out.println("Current omega value: " + omega);
            final ParticlesParserResult parser = CreateStaticAndDynamicFiles.create(N);

            String file = "src/main/resources/benchmark/omega/" + omega + ".txt";
            File outFile = new File(file);

            String timeFile = "src/main/resources/benchmark/omega/times_" + omega + ".txt";
            File outTimeFile = new File(timeFile);

            List<Particle> particles = GranularDynamic.cloneParticles(parser.getParticlesPerTime().get(0));

            Double caudal = GranularDynamic.run(particles, L, W, MAX_TIME, omega, D, DT, DT2, outFile, outTimeFile);
            caudales.add(caudal);
        }

        String fileCaudal = "src/main/resources/benchmark/omega/caudales.txt";
        File outFileCaudal = new File(fileCaudal);

        try (PrintWriter pw = new PrintWriter(outFileCaudal)) {
            caudales.forEach((caudal) -> pw.printf(Locale.US, "%.3f\n", caudal));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
