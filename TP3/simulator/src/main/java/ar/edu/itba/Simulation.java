package ar.edu.itba;

import ar.edu.itba.gasDiffusion.GasDiffusion;
import ar.edu.itba.gasDiffusion.GasDiffusionResult;
import ar.edu.itba.utils.ConfigMethodParser;
import ar.edu.itba.utils.ParticlesParser;
import ar.edu.itba.utils.ParticlesParserResult;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

public class Simulation {
    public static void main(String[] args) throws IOException, ParseException {

        FileReader fr = new FileReader("src/main/resources/configMethod.json");
        JSONObject json = (JSONObject) new JSONParser().parse(fr);
        ConfigMethodParser config = new ConfigMethodParser(json);

        System.out.println("Parsing Particles from files ...\n");

        final File staticFile = new File(config.getStaticFile());
        final File dynamicFile = new File(config.getDynamicFile());

        final ParticlesParserResult parser = ParticlesParser.parseParticlesList(staticFile, dynamicFile);

        final File outFile = new File(config.getOutFile());
        GasDiffusionResult results = GasDiffusion.run(parser.getParticlesPerTime(), 1000, parser.getSide(), config.getL(), outFile);

        System.out.println("Simulation finished ...\n");
        System.out.println("Writing Results ...\n");

        final File outTimeFile = new File(config.getOutTimeFile());

        try (PrintWriter pw = new PrintWriter(outTimeFile)) {
            final double totalTimeMillis = (double) results.getTotalTime() / 1_000_000; // Convert nanoseconds to milliseconds
            pw.append(String.format(Locale.US, "Total time - %f ms\n", totalTimeMillis));
        }
    }
}