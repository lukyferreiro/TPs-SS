package ar.edu.itba;

import ar.edu.itba.models.Particle;
import ar.edu.itba.simulation.GranularDynamic;
import ar.edu.itba.utils.CreateStaticAndDynamicFiles;
import ar.edu.itba.utils.ParticlesParserResult;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BenchmarkOmega {
    final static Double DT = 0.001;
    final static Double DT2 = 0.1;
    final static Double L = 70.0;
    final static Double W = 20.0;
    final static Double MAX_TIME = 5.0;
    final static Double D = 3.0;
    final static Integer N = 200;
    final static List<Double> Omegas = List.of(5.0, 10.0, 15.0, 20.0, 30.0, 50.0);

    public static void main(String[] args) throws IOException {

        final Map<Double, Map<BigDecimal, List<Particle>>> particlesWithOmega = new HashMap<>();

        for (Double omega : Omegas) {
            System.out.println("Current omega value: " + omega);
            final ParticlesParserResult parser = CreateStaticAndDynamicFiles.create(N);

            String file = "src/main/resources/benchmark/omega/" + omega + ".txt";
            File outFile = new File(file);

            String timeFile = "src/main/resources/benchmark/omega/times_" + omega + ".txt";
            File outTimeFile = new File(timeFile);

            List<Particle> particles = GranularDynamic.cloneParticles(parser.getParticlesPerTime().get(0));

            Map<BigDecimal, List<Particle>> aux = GranularDynamic.run(particles, L, W, MAX_TIME, omega, D, DT, DT2, outFile, outTimeFile);
            particlesWithOmega.put(omega, aux);
        }

        //TODO: check postprocessing
    }
}
