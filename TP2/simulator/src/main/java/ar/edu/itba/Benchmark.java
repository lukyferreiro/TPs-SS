package ar.edu.itba;

import ar.edu.itba.models.Particle;
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
        final int MAX_ITERATIONS = 3000;

        final int ITERATIONS = 1000;

        ParticlesParserResult parser;
        double gridCondition;
        long optimalM;
        double maxRadius;

        double L = 100.0;

        //-----Calculo de parametro de orden variando el valor de ruido (eta) ----

        long N = 100;

        parser = createStaticAndDynamicFiles(N, L);

        maxRadius = parser.getParticlesPerTime()
                .get(0)
                .keySet()
                .stream()
                .map(Particle::getRadius)
                .max(Double::compare)
                .orElseThrow(RuntimeException::new);

        gridCondition = parser.getL() / Rc + 2 * maxRadius;
        optimalM = (int) Math.floor(gridCondition);
        if (gridCondition == (int) gridCondition) {
            optimalM = (int) gridCondition - 1;
        }

        final double MAX_ETA = 5;
        final double ETA_STEP = 0.5;

        final Map<Double, List<Double>> orderParameters = new TreeMap<>(Double::compare);

        for (double eta = 0.0; eta <= MAX_ETA; eta += ETA_STEP) {
            System.out.printf(Locale.US, "Variando con eta=%.1f\n", eta);
            orderParameters.put(eta, new ArrayList<>());
            OffLatticeResult results = OffLattice.startSimulation(
                    parser.getParticlesPerTime().get(0), parser.getL(),
                    optimalM, Rc, DT, eta, true, MAX_ITERATIONS
            );
            orderParameters.get(eta).addAll(results.getOrderParameter());
        }

        final String BENCHMARK_ETA = "src/main/resources/benchmark/eta_variation.txt";
        final File BENCHMARK_FILE_ETA = new File(BENCHMARK_ETA);

        try (PrintWriter pw = new PrintWriter(BENCHMARK_FILE_ETA)) {
            pw.printf("%d ", parser.getN());
            pw.printf(Locale.US, "%f ", parser.getL());
            pw.printf(Locale.US, "%f ", Rc);
            pw.printf("%d\n", MAX_ITERATIONS);

            orderParameters.forEach((eta, ops) -> {
                pw.printf(Locale.US, "%.1f", eta);
                ops.forEach(op -> pw.printf(Locale.US, " %f", op));
                pw.printf("\n");
            });
        }

        //-----Calculo de parametro de orden variando la cantidad de particulas  ----

        double ETA = 1.0;
        final int MAX_N = 1000;
        final int N_STEP = 200;

        final Map<Integer, List<Double>> orderParametersByN = new TreeMap<>(Integer::compare);

        for (int n = 100; n <= MAX_N; n += N_STEP) {
            System.out.printf(Locale.US, "Variando con N=%d\n", n);

            orderParametersByN.put(n, new ArrayList<>());

            parser = createStaticAndDynamicFiles(N, L);

            maxRadius = parser.getParticlesPerTime().get(0).keySet().stream().map(Particle::getRadius).max(Double::compare).orElseThrow(RuntimeException::new);

            gridCondition = parser.getL() / Rc + 2 * maxRadius;
            optimalM = (int) Math.floor(gridCondition);
            if (gridCondition == (int) gridCondition) {
                optimalM = (int) gridCondition - 1;
            }

            OffLatticeResult results = OffLattice.startSimulation(
                    parser.getParticlesPerTime().get(0), parser.getL(),
                    optimalM, Rc, DT, ETA, true, MAX_ITERATIONS
            );
            orderParametersByN.get(n).addAll(results.getOrderParameter());

        }

        final String BENCHMARK_N = "src/main/resources/benchmark/n_variation.txt";
        final File BENCHMARK_FILE_N = new File(BENCHMARK_N);

        try (PrintWriter pw = new PrintWriter(BENCHMARK_FILE_N)) {
            pw.printf(Locale.US, "%f ", L);
            pw.printf(Locale.US, "%f ", Rc);
            pw.printf("%d\n", MAX_ITERATIONS);

            orderParametersByN.forEach((n, ops) -> {
                pw.printf(Locale.US, "%d", n);
                ops.forEach(op -> pw.printf(Locale.US, " %f", op));
                pw.printf("\n");
            });
        }

        //-----Calculo de parametro de orden variando la longitud de la grilla  ----

        ETA = 1.0;
        N = 100;
        final double MAX_L = 300.0;
        final double L_STEP = 50.0;

        final Map<Double, List<Double>> orderParametersByL = new TreeMap<>(Double::compare);

        for (double l = 50; l <= MAX_L; l += L_STEP) {
            System.out.printf(Locale.US, "Variando con L=%.0f\n", l);

            orderParametersByL.put(l, new ArrayList<>());

            parser = createStaticAndDynamicFiles(N, l);

            maxRadius = parser.getParticlesPerTime().get(0).keySet().stream().map(Particle::getRadius).max(Double::compare).orElseThrow(RuntimeException::new);

            gridCondition = parser.getL() / Rc + 2 * maxRadius;
            optimalM = (int) Math.floor(gridCondition);
            if (gridCondition == (int) gridCondition) {
                optimalM = (int) gridCondition - 1;
            }

            OffLatticeResult results = OffLattice.startSimulation(
                    parser.getParticlesPerTime().get(0), parser.getL(),
                    optimalM, Rc, DT, ETA, true, MAX_ITERATIONS
            );
            orderParametersByL.get(l).addAll(results.getOrderParameter());

        }

        final String BENCHMARK_L = "src/main/resources/benchmark/l_variation.txt";
        final File BENCHMARK_FILE_L = new File(BENCHMARK_L);

        try (PrintWriter pw = new PrintWriter(BENCHMARK_FILE_L)) {
            pw.printf(Locale.US, "%d ", N);
            pw.printf(Locale.US, "%f ", Rc);
            pw.printf("%d\n", MAX_ITERATIONS);

            orderParametersByL.forEach((l, ops) -> {
                pw.printf(Locale.US, "%.0f", l);
                ops.forEach(op -> pw.printf(Locale.US, " %f", op));
                pw.printf("\n");
            });
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

