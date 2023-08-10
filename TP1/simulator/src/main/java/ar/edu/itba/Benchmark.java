package ar.edu.itba;

import ar.edu.itba.methods.BruteForceMethod;
import ar.edu.itba.methods.CellIndexMethod;
import ar.edu.itba.methods.MethodResult;
import ar.edu.itba.utils.ConfigGeneratorParser;
import ar.edu.itba.utils.ParticlesParser;
import ar.edu.itba.utils.ParticlesParserResult;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.Locale;
import java.util.Random;

public class Benchmark {
    public static void main(String[] args) throws IOException, ParseException {

        final double Rc = 1.0;
        final double L = 20.0;
        long M = 5;
        long N;


        final String BENCHMARK_N = "src/main/resources/benchmarkN.txt";
        final File BENCHMARK_FILE_N = new File(BENCHMARK_N);

        MethodResult resultsCim;
        MethodResult resultsBrute;

        try (PrintWriter pwN = new PrintWriter(BENCHMARK_FILE_N)) {
            pwN.format("Variando el numero de particulas con M:%d\n",M);
            pwN.println("N ---- CIM ---- BRUTE");
            for (int i = 0; i < 20; i++) {
                N = (i + 1) * 50;
                ParticlesParserResult parser = createStaticAndDynamicFiles(N, L);
                resultsCim = CellIndexMethod.calculateNeighbors(parser.getParticlesPerTime().get(0), L, M, Rc, true);
                resultsBrute = BruteForceMethod.calculateNeighbors(parser.getParticlesPerTime().get(0), L, Rc, true);
                final double totalTimeCim = (double) resultsCim.getTotalTime() / 1_000_000;
                final double totalTimeBrute = (double) resultsBrute.getTotalTime() / 1_000_000;
                pwN.printf(Locale.US, String.format("%d  %.3f%s  %.3f%s\n", N, totalTimeCim, "ms", totalTimeBrute, "ms"));

                //----------------------------------------

                final String BENCHMARK_M = "src/main/resources/benchmarkM_" + N + ".txt";
                final File BENCHMARK_FILE_M = new File(BENCHMARK_M);

                MethodResult results;
                try (PrintWriter pwM = new PrintWriter(BENCHMARK_FILE_M)) {
                    pwM.println("Variando el numero de celdas M");
                    for (long m = 1; m <= 13; m++) {
                        results = CellIndexMethod.calculateNeighbors(parser.getParticlesPerTime().get(0), L, m, Rc, true);
                        final double totalTime = (double) results.getTotalTime() / 1_000_000;
                        pwM.printf(Locale.US, String.format("%d - %.3f%s\n", m, totalTime, "ms"));
                    }
                }
            }
        }

    }

    private static ParticlesParserResult createStaticAndDynamicFiles(long N, double L) throws IOException, ParseException {
        FileReader fr = new FileReader("src/main/resources/configGenerator.json");
        JSONObject json = (JSONObject) new JSONParser().parse(fr);
        ConfigGeneratorParser config = new ConfigGeneratorParser(json);

        final File staticFile = new File(config.getStaticFile());
        final File dynamicFile = new File(config.getDynamicFile());

        final double minR = 0.25;
        final double maxR = 0.25;

        try (PrintWriter pw = new PrintWriter(staticFile)) {
            pw.println(N);
            pw.println(L);
            for (int i = 0; i < N; i++) {
                pw.printf(Locale.US, "%f %f\n", minR + Math.random() * (maxR - minR), 1.0000);
            }
        }

        try (PrintWriter pw = new PrintWriter(dynamicFile)) {
            final Random random = new Random();
            for (int i = 0; i < config.getTimes(); i++) {
                pw.println(i);
                for (int j = 0; j < N; j++) {
                    double x = random.nextDouble() * (L);
                    double y = random.nextDouble() * (L);
                    pw.printf(Locale.US, "%f %f\n", x, y);
                }
            }
        }

        return ParticlesParser.parseParticlesList(staticFile, dynamicFile);

    }

}
