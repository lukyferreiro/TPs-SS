package ar.edu.itba;

import ar.edu.itba.off_lattice.OffLattice;
import ar.edu.itba.off_lattice.OffLatticeResult;
import ar.edu.itba.utils.ConfigGeneratorParser;
import ar.edu.itba.utils.ParticlesParser;
import ar.edu.itba.utils.ParticlesParserResult;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.*;

public class Benchmark {
    public static void main(String[] args) throws IOException, ParseException {

        final double Rc = 1.0;
        final double DT = 1.0;
        final int MAX_ITERATIONS = 5000;

        ParticlesParserResult parser;
        double gridCondition;
        long optimalM;
        List<Integer> listN;
        List<Double> listL;

        //-----Calculo va variando el valor de ruido (eta) y misma densidad ----

//        listN = List.of(40, 100, 400, 4000, 10000);
//        listL = List.of(Math.sqrt(10), 5.0, 10.0, Math.sqrt(1000), 50.0);
//
//        for(int iter = 0; iter < listN.size(); iter++){
//
//            System.out.printf(Locale.US, "N=%d -- L=%.2f\n", listN.get(iter), listL.get(iter));
//
//            parser = createStaticAndDynamicFiles(listN.get(iter), listL.get(iter));
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
//            final String BENCHMARK_ETA = "src/main/resources/benchmark/eta_variation_same_density/N_" + listN.get(iter) + ".txt";
//            final File BENCHMARK_FILE_ETA = new File(BENCHMARK_ETA);
//
//            try (PrintWriter pw = new PrintWriter(BENCHMARK_FILE_ETA)) {
//                pw.printf("%d ", listN.get(iter));
//                pw.printf(Locale.US, "%f ", listL.get(iter));
//                pw.printf(Locale.US, "%f ", Rc);
//                pw.printf("%d\n", MAX_ITERATIONS);
//
//                orderParameters.forEach((eta, ops) -> {
//                    pw.printf(Locale.US, "%.1f", eta);
//                    ops.forEach(op -> pw.printf(Locale.US, " %f", op));
//                    pw.printf("\n");
//                });
//            }
//        }

        //-----Calculo va variando el valor de ruido (eta) y distinta densidad (variando N) ----

        double L = 10.0;
        listN = List.of(50, 100, 250, 500, 750, 1000);

        for(int iter = 0; iter < listN.size(); iter++){

            System.out.printf(Locale.US, "N=%d -- L=%.2f\n", listN.get(iter), L);

            parser = createStaticAndDynamicFiles(listN.get(iter), L);

            gridCondition = L / Rc;
            optimalM = (int) Math.floor(gridCondition);
            if (gridCondition == (int) gridCondition) {
                optimalM = (int) gridCondition - 1;
            }

            final double MAX_ETA = 5;
            final double ETA_STEP = 0.1;

            final Map<Double, List<Double>> orderParameters = new TreeMap<>(Double::compare);

            for (double eta = 0.0; eta <= MAX_ETA; eta += ETA_STEP) {
                System.out.printf(Locale.US, "Variando con eta=%.1f\n", eta);
                orderParameters.put(eta, new ArrayList<>());
                OffLatticeResult results = OffLattice.startSimulation(
                        parser.getParticlesPerTime().get(0), L,
                        optimalM, Rc, DT, eta, true, MAX_ITERATIONS
                );
                orderParameters.get(eta).addAll(results.getOrderParameter());
            }

            final String BENCHMARK_ETA_N = "src/main/resources/benchmark_5000/eta_variation_variating_N/N_" + listN.get(iter) + ".txt";
            final File BENCHMARK_FILE_ETA_N = new File(BENCHMARK_ETA_N);

            try (PrintWriter pwEtaN = new PrintWriter(BENCHMARK_FILE_ETA_N)) {
                pwEtaN.printf("%d ", listN.get(iter));
                pwEtaN.printf(Locale.US, "%f ", L);
                pwEtaN.printf(Locale.US, "%f ", Rc);
                pwEtaN.printf("%d\n", MAX_ITERATIONS);

                orderParameters.forEach((eta, ops) -> {
                    pwEtaN.printf(Locale.US, "%.1f", eta);
                    ops.forEach(op -> pwEtaN.printf(Locale.US, " %f", op));
                    pwEtaN.printf("\n");
                });
            }
        }


        //-----Calculo va variando el valor de ruido (eta) y distinta densidad (variando L) ----

        int N = 500;
        listL = List.of(5.0, 7.0, 10.0, 15.0, 25.0, 50.0, 100.0);

        for(int iter = 0; iter < listL.size(); iter++){

            System.out.printf(Locale.US, "N=%d -- L=%.2f\n", N, listL.get(iter));

            parser = createStaticAndDynamicFiles(N, listL.get(iter));

            gridCondition = listL.get(iter) / Rc;
            optimalM = (int) Math.floor(gridCondition);
            if (gridCondition == (int) gridCondition) {
                optimalM = (int) gridCondition - 1;
            }

            final double MAX_ETA = 5;
            final double ETA_STEP = 0.1;

            final Map<Double, List<Double>> orderParameters = new TreeMap<>(Double::compare);

            for (double eta = 0.0; eta <= MAX_ETA; eta += ETA_STEP) {
                System.out.printf(Locale.US, "Variando con eta=%.1f\n", eta);
                orderParameters.put(eta, new ArrayList<>());
                OffLatticeResult results = OffLattice.startSimulation(
                        parser.getParticlesPerTime().get(0), listL.get(iter),
                        optimalM, Rc, DT, eta, true, MAX_ITERATIONS
                );
                orderParameters.get(eta).addAll(results.getOrderParameter());
            }

            final String BENCHMARK_ETA_L = "src/main/resources/benchmark_5000/eta_variation_variating_L/L_" + listL.get(iter) + ".txt";
            final File BENCHMARK_FILE_ETA_L = new File(BENCHMARK_ETA_L);

            try (PrintWriter pwEtaL = new PrintWriter(BENCHMARK_FILE_ETA_L)) {
                pwEtaL.printf("%d ", N);
                pwEtaL.printf(Locale.US, "%f ", listL.get(iter));
                pwEtaL.printf(Locale.US, "%f ", Rc);
                pwEtaL.printf("%d\n", MAX_ITERATIONS);

                orderParameters.forEach((eta, ops) -> {
                    pwEtaL.printf(Locale.US, "%.1f", eta);
                    ops.forEach(op -> pwEtaL.printf(Locale.US, " %f", op));
                    pwEtaL.printf("\n");
                });
            }
        }

    }

    private static ParticlesParserResult createStaticAndDynamicFiles(long N, double L) throws IOException, ParseException {
        FileReader fr = new FileReader("src/main/resources/configGenerator.json");
        JSONObject json = (JSONObject) new JSONParser().parse(fr);
        ConfigGeneratorParser config = new ConfigGeneratorParser(json);

        final File staticFile = new File(config.getStaticFile());
        final File dynamicFile = new File(config.getDynamicFile());

        final double MIN_R = 0.0;
        final double MAX_R = 0.0;
        final double PROPERTY = 1.0;
        final double SPEED = 0.03;
        final double MAX_ANGLE = 2 * Math.PI;
        final int TIMES = 1;

        try (PrintWriter pw = new PrintWriter(staticFile)) {
            pw.println(N);
            pw.println(L);
            for (int i = 0; i < N; i++) {
                pw.printf(Locale.US, "%f %f\n", MIN_R + Math.random() * (MAX_R - MIN_R), PROPERTY);
            }
        }

        try (PrintWriter pw = new PrintWriter(dynamicFile)) {
            final Random random = new Random();
            for (int i = 0; i < TIMES; i++) {
                pw.println(i);
                for (int j = 0; j < N; j++) {
                    double x = random.nextDouble() * L;
                    double y = random.nextDouble() * L;
                    double angle = random.nextDouble() * (MAX_ANGLE);
                    pw.printf(Locale.US, "%f %f %f %f\n", x, y, SPEED, angle);
                }
            }
        }

        ParticlesParserResult parser = ParticlesParser.parseParticlesList(staticFile, dynamicFile);

        staticFile.delete();
        dynamicFile.delete();

        return parser;

    }

}

