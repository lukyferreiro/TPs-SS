package ar.edu.itba;

import ar.edu.itba.off_lattice.OffLattice;
import ar.edu.itba.off_lattice.OffLatticeResult;
import ar.edu.itba.utils.CreateStaticAndDynamicFiles;
import ar.edu.itba.utils.ParticlesParserResult;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.*;

public class BenchmarkEta {
    public static void main(String[] args) throws IOException, ParseException {

        final double Rc = 1.0;
        final double DT = 1.0;
        final int MAX_ITERATIONS = 2000;
        final int SIMULATIONS = 5;

        ParticlesParserResult parser;
        double gridCondition;
        long optimalM;
        List<Integer> listN;
        List<Double> listL;
        double L;
        int N;

        final double MAX_ETA = 5;
        final double ETA_STEP = 0.1;

        //-----Calculo va variando el valor de ruido (eta) y misma densidad = 4 ----

        listN = List.of(40, 100, 400, 4000, 10000);
        listL = List.of(Math.sqrt(10), 5.0, 10.0, Math.sqrt(1000), 50.0);

        for (int iter = 0; iter < listN.size(); iter++) {

            System.out.printf(Locale.US, "N=%d -- L=%.2f\n", listN.get(iter), listL.get(iter));
            final Map<Double, List<Double>> orderParametersEtaMean = new TreeMap<>(Double::compare);

            for(int sim = 0; sim < SIMULATIONS; sim++) {

                System.out.printf(Locale.US, "----SIMULATION %d------\n", sim);

                parser = CreateStaticAndDynamicFiles.create(listN.get(iter), listL.get(iter));

                gridCondition = listL.get(iter) / Rc;
                optimalM = (int) Math.floor(gridCondition);
                if (gridCondition == (int) gridCondition) {
                    optimalM = (int) gridCondition - 1;
                }

                final Map<Double, List<Double>> orderParametersEta = new TreeMap<>(Double::compare);

                for (double eta = 0.0; eta <= MAX_ETA; eta += ETA_STEP) {
                    System.out.printf(Locale.US, "Variando con eta=%.1f\n", eta);

                    orderParametersEta.put(eta, new ArrayList<>());
                    orderParametersEtaMean.computeIfAbsent(eta, k -> new ArrayList<>());

                    OffLatticeResult results = OffLattice.startSimulation(
                            parser.getParticlesPerTime().get(0), listL.get(iter),
                            optimalM, Rc, DT, eta, true, MAX_ITERATIONS
                    );

                    orderParametersEta.get(eta).addAll(results.getOrderParameter());

                    double mean = results.getOrderParameter().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
                    orderParametersEtaMean.get(eta).add(mean);
                }

                final String BENCHMARK_ETA = "src/main/resources/benchmark_2000/eta_variation/N_" + listN.get(iter) + ".txt";
                final File BENCHMARK_FILE_ETA = new File(BENCHMARK_ETA);

                try (PrintWriter pw = new PrintWriter(BENCHMARK_FILE_ETA)) {
                    pw.printf("%d ", listN.get(iter));
                    pw.printf(Locale.US, "%f ", listL.get(iter));
                    pw.printf(Locale.US, "%f ", Rc);
                    pw.printf("%d\n", MAX_ITERATIONS);

                    orderParametersEta.forEach((eta, ops) -> {
                        pw.printf(Locale.US, "%.1f", eta);
                        ops.forEach(op -> pw.printf(Locale.US, " %f", op));
                        pw.printf("\n");
                    });
                }
            }

            final String BENCHMARK_ETA_MEAN = "src/main/resources/benchmark_2000/eta_variation/mean/N_" + listN.get(iter) + ".txt";
            final File BENCHMARK_FILE_ETA_MEAN = new File(BENCHMARK_ETA_MEAN);

            try (PrintWriter pwEtaMean = new PrintWriter(BENCHMARK_FILE_ETA_MEAN)) {
                pwEtaMean.printf("%d ", listN.get(iter));
                pwEtaMean.printf(Locale.US, "%f ", listL.get(iter));
                pwEtaMean.printf(Locale.US, "%f ", Rc);
                pwEtaMean.printf("%d\n", MAX_ITERATIONS);

                orderParametersEtaMean.forEach((mean, ops) -> {
                    pwEtaMean.printf(Locale.US, "%.1f", mean);
                    ops.forEach(op -> pwEtaMean.printf(Locale.US, " %f", op));
                    pwEtaMean.printf("\n");
                });
            }
        }


        //-----Calculo va variando el valor de ruido (eta) y distinta densidad (variando N) ----

//        L = 10.0;
//        listN = List.of(50, 100, 250, 500, 750, 1000);
//
//        for(int iter = 0; iter < listN.size(); iter++){
//
//            System.out.printf(Locale.US, "N=%d -- L=%.2f\n", listN.get(iter), L);
//
//            parser = CreateStaticAndDynamicFiles.create(listN.get(iter), L);
//
//            gridCondition = L / Rc;
//            optimalM = (int) Math.floor(gridCondition);
//            if (gridCondition == (int) gridCondition) {
//                optimalM = (int) gridCondition - 1;
//            }
//
//            final double MAX_ETA = 5;
//            final double ETA_STEP = 0.1;
//
//            final Map<Double, List<Double>> orderParameters = new TreeMap<>(Double::compare);
//
//            for (double eta = 0.0; eta <= MAX_ETA; eta += ETA_STEP) {
//                System.out.printf(Locale.US, "Variando con eta=%.1f\n", eta);
//                orderParameters.put(eta, new ArrayList<>());
//                OffLatticeResult results = OffLattice.startSimulation(
//                        parser.getParticlesPerTime().get(0), L,
//                        optimalM, Rc, DT, eta, true, MAX_ITERATIONS
//                );
//                orderParameters.get(eta).addAll(results.getOrderParameter());
//            }
//
//            final String BENCHMARK_ETA_N = "src/main/resources/benchmark_5000/eta_variation_variating_N/N_" + listN.get(iter) + ".txt";
//            final File BENCHMARK_FILE_ETA_N = new File(BENCHMARK_ETA_N);
//
//            try (PrintWriter pwEtaN = new PrintWriter(BENCHMARK_FILE_ETA_N)) {
//                pwEtaN.printf("%d ", listN.get(iter));
//                pwEtaN.printf(Locale.US, "%f ", L);
//                pwEtaN.printf(Locale.US, "%f ", Rc);
//                pwEtaN.printf("%d\n", MAX_ITERATIONS);
//
//                orderParameters.forEach((eta, ops) -> {
//                    pwEtaN.printf(Locale.US, "%.1f", eta);
//                    ops.forEach(op -> pwEtaN.printf(Locale.US, " %f", op));
//                    pwEtaN.printf("\n");
//                });
//            }
//        }


        //-----Calculo va variando el valor de ruido (eta) y distinta densidad (variando L) ----

//        N = 500;
//        listL = List.of(5.0, 7.0, 10.0, 15.0, 25.0, 50.0, 100.0);
//
//        for(int iter = 0; iter < listL.size(); iter++){
//
//            System.out.printf(Locale.US, "N=%d -- L=%.2f\n", N, listL.get(iter));
//
//            parser = CreateStaticAndDynamicFiles.create(N, listL.get(iter));
//
//            gridCondition = listL.get(iter) / Rc;
//            optimalM = (int) Math.floor(gridCondition);
//            if (gridCondition == (int) gridCondition) {
//                optimalM = (int) gridCondition - 1;
//            }
//
//            final double MAX_ETA = 5;
//            final double ETA_STEP = 0.1;
//
//            final Map<Double, List<Double>> orderParameters = new TreeMap<>(Double::compare);
//
//            for (double eta = 0.0; eta <= MAX_ETA; eta += ETA_STEP) {
//                System.out.printf(Locale.US, "Variando con eta=%.1f\n", eta);
//                orderParameters.put(eta, new ArrayList<>());
//                OffLatticeResult results = OffLattice.startSimulation(
//                        parser.getParticlesPerTime().get(0), listL.get(iter),
//                        optimalM, Rc, DT, eta, true, MAX_ITERATIONS
//                );
//                orderParameters.get(eta).addAll(results.getOrderParameter());
//            }
//
//            final String BENCHMARK_ETA_L = "src/main/resources/benchmark_5000/eta_variation_variating_L/L_" + listL.get(iter) + ".txt";
//            final File BENCHMARK_FILE_ETA_L = new File(BENCHMARK_ETA_L);
//
//            try (PrintWriter pwEtaL = new PrintWriter(BENCHMARK_FILE_ETA_L)) {
//                pwEtaL.printf("%d ", N);
//                pwEtaL.printf(Locale.US, "%f ", listL.get(iter));
//                pwEtaL.printf(Locale.US, "%f ", Rc);
//                pwEtaL.printf("%d\n", MAX_ITERATIONS);
//
//                orderParameters.forEach((eta, ops) -> {
//                    pwEtaL.printf(Locale.US, "%.1f", eta);
//                    ops.forEach(op -> pwEtaL.printf(Locale.US, " %f", op));
//                    pwEtaL.printf("\n");
//                });
//            }
//        }

    }

}

