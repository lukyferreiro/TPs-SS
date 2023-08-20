package ar.edu.itba;

import ar.edu.itba.models.Particle;

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

        //Buscamos la particula con radio mas grande para contemplar el peor caso al momento de
        //calcular la distancia entre las particulas y no romper con la condicion L/M > Rc
        final double maxRadius = parser.getParticlesPerTime()
                .get(0)
                .keySet()
                .stream()
                .map(Particle::getRadius)
                .max(Double::compare)
                .orElseThrow();

        final double gridCondition = parser.getL() / config.getRc() + 2 * maxRadius;

        long optimalM = (int) Math.floor(gridCondition);
        if (gridCondition == (int) gridCondition) {
            optimalM = (int) gridCondition - 1;
        }

        System.out.println("Start simulation\n");

        //todo

        System.out.println("Simulation finished\n");
        System.out.println("Writing Results ...\n");

        final File outTimeFile = new File(config.getOutTimeFile());


        try (PrintWriter pw = new PrintWriter(outTimeFile)) {
            final double totalTimeMillis = (double) results.getTotalTime() / 1_000_000; // Convert nanoseconds to milliseconds
            pw.append(String.format(Locale.US, "%s - %.3f%s\n", "Total time", totalTimeMillis, "ms"));
        }

    }
}