package ar.edu.itba;

import ar.edu.itba.off_lattice.OffLattice;
import ar.edu.itba.off_lattice.OffLatticeResult;
import ar.edu.itba.utils.CreateStaticAndDynamicFiles;
import ar.edu.itba.utils.ParticlesParserResult;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.*;

public class BenchmarkDensity {
    public static void main(String[] args) throws IOException, ParseException {

        final double Rc = 1.0;
        final double DT = 1.0;
        final int MAX_ITERATIONS = 2000;

        ParticlesParserResult parser;
        double gridCondition;
        long optimalM;

        //-----Calculo de va variando la densidad ----

        double L = 10.0;
        final int MIN_N = 100;
        final int MAX_N = 200;
        final int N_STEP = 100;
        final int SIMULATIONS = 5;
        double MAX_ETA = 4;
        double ETA_STEP = 1;

        gridCondition = L / Rc;
        optimalM = (int) Math.floor(gridCondition);
        if (gridCondition == (int) gridCondition) {
            optimalM = (int) gridCondition - 1;
        }

        for(double eta = 0; eta <= MAX_ETA; eta += ETA_STEP) {

            System.out.printf(Locale.US, "----ETA %.1f------\n", eta);
            final Map<Double, List<Double>> orderParametersDensityMean = new TreeMap<>(Double::compare);

            for (int sim = 0; sim < SIMULATIONS; sim++) {

                System.out.printf(Locale.US, "----SIMULATION %d------\n", sim);
                final Map<Double, List<Double>> orderParametersDensity = new TreeMap<>(Double::compare);

                for (int n = MIN_N; n <= MAX_N; n += N_STEP) {

                    final double density = n / (L * L);

                    System.out.printf(Locale.US, "ρ=%.2f\n", density);

                    parser = CreateStaticAndDynamicFiles.create(n, L);

                    orderParametersDensity.put(density, new ArrayList<>());
                    orderParametersDensityMean.computeIfAbsent(density, k -> new ArrayList<>());

                    OffLatticeResult results = OffLattice.startSimulation(
                            parser.getParticlesPerTime().get(0), L, optimalM,
                            Rc, DT, eta, true, MAX_ITERATIONS);

                    orderParametersDensity.get(density).addAll(results.getOrderParameter());
                    double mean = results.getOrderParameter().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
                    orderParametersDensityMean.get(density).add(mean);
                }

                final String BENCHMARK_DENSITY = "src/main/resources/benchmark_2000/density_variation/eta_" + eta + ".txt";
                final File BENCHMARK_FILE_DENSITY = new File(BENCHMARK_DENSITY);

                try (PrintWriter pwDensity = new PrintWriter(BENCHMARK_FILE_DENSITY)) {
                    pwDensity.printf(Locale.US, "%.1f ", eta);
                    pwDensity.printf(Locale.US, "%.1f ", L);
                    pwDensity.printf(Locale.US, "%.1f ", Rc);
                    pwDensity.printf("%d\n", MAX_ITERATIONS);

                    orderParametersDensity.forEach((den, ops) -> {
                        pwDensity.printf(Locale.US, "%.2f", den);
                        ops.forEach(op -> pwDensity.printf(Locale.US, " %f", op));
                        pwDensity.printf("\n");
                    });
                }
            }

            final String BENCHMARK_DENSITY_MEAN = "src/main/resources/benchmark_2000/density_variation/mean/eta_" + eta + ".txt";
            final File BENCHMARK_FILE_DENSITY_MEAN = new File(BENCHMARK_DENSITY_MEAN);

            try (PrintWriter pwDensityMean = new PrintWriter(BENCHMARK_FILE_DENSITY_MEAN)) {
                pwDensityMean.printf(Locale.US, "%.1f ", eta);
                pwDensityMean.printf(Locale.US, "%.1f ", L);
                pwDensityMean.printf(Locale.US, "%.1f ", Rc);
                pwDensityMean.printf("%d\n", MAX_ITERATIONS);

                orderParametersDensityMean.forEach((den, ops) -> {
                    pwDensityMean.printf(Locale.US, "%.2f", den);
                    ops.forEach(op -> pwDensityMean.printf(Locale.US, " %f", op));
                    pwDensityMean.printf("\n");
                });
            }
        }

    }

}

