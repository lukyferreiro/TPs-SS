package ar.edu.itba;

import ar.edu.itba.gasDiffusion.GasDiffusion;
import ar.edu.itba.models.Particle;
import ar.edu.itba.models.Position;
import ar.edu.itba.utils.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class BenchmarkL {
    public static void main(String[] args) throws IOException, ParseException {
        final int SIMULATIONS = 1;
        ParticlesParserResult parser;
        List<Integer> listN;
        List<Double> listL;

        listN = List.of(200, 300, 400);
        listL = List.of(0.03, 0.05, 0.07, 0.09);

        for(int j = 0; j < listL.size() ; j++) {
            for (int iter = 0; iter < listN.size(); iter++) {

                System.out.printf(Locale.US, "N=%d -- L=%.2f\n", listN.get(iter), listL.get(j));

                for (int sim = 0; sim < SIMULATIONS; sim++) {
                    System.out.printf(Locale.US, "----SIMULATION %d------\n", sim);

                    parser = CreateStaticAndDynamicFiles.create(listN.get(iter));

                    final File outFile = new File("src/main/resources/benchmark/N_" + listN.get(iter) + "_L_" + listL.get(j) + "_it_" + sim + ".txt");

                    System.out.println("Simulation started ...\n");
                    GasDiffusion.run(parser.getParticlesPerTime(), 1, 0.5, parser.getSide(),listL.get(j), outFile);
                    System.out.println("Simulation finished ...\n");
                }
            }
        }
    }
}



