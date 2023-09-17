package ar.edu.itba;

import ar.edu.itba.gasDiffusion.GasDiffusion;
import ar.edu.itba.utils.*;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class BenchmarkL {
    public static void main(String[] args) throws IOException, ParseException {
        final int SIMULATIONS = 3;
        ParticlesParserResult parser;
        List<Integer> listN;
        List<Double> listL;

        listN = List.of(200, 300, 400);
        listL = List.of(0.03, 0.05, 0.07, 0.09);

        for (Double l : listL) {
            for (Integer n : listN) {

                System.out.printf(Locale.US, "N=%d -- L=%.2f\n", n, l);

                for (int sim = 0; sim < SIMULATIONS; sim++) {
                    System.out.printf(Locale.US, "----SIMULATION %d------\n", sim);

                    parser = CreateStaticAndDynamicFiles.create(n);

                    final File outFile = new File("src/main/resources/benchmark/N_" + n + "/N_" + n + "_L_" + l + "_it_" + sim + ".txt");

                    System.out.println("Simulation started ...\n");
                    GasDiffusion.run(parser.getParticlesPerTime(), 150, 0.5, parser.getSide(), l, outFile);
                    System.out.println("Simulation finished ...\n");
                }
            }
        }
    }
}



