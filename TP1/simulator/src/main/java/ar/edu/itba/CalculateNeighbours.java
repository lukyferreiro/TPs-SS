package ar.edu.itba;

import ar.edu.itba.methods.BruteForceMethod;
import ar.edu.itba.methods.CellIndexMethod;
import ar.edu.itba.models.Particle;
import ar.edu.itba.methods.MethodResult;
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
import java.time.Duration;
import java.util.Locale;
import java.util.Objects;

public class CalculateNeighbours {
    private final static String CIM = "CIM";
    private final static String BRUTE = "BRUTE";
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
                .orElse(0.0);

        if (parser.getL() / (double) config.getM() <= config.getRc() + 2 * maxRadius) {
            System.out.printf(Locale.US,"Invalid condition L/M>R (L=%f, M=%d, R=%f and maxR=%f)\n",
                    parser.getL(), config.getM(), config.getRc(), maxRadius);
            return;
        }

        System.out.println("Start simulation\n");
        System.out.println("Calculating Neighbours ...\n");

        MethodResult results;
        if(Objects.equals(config.getMethod(), CIM)){
            results = CellIndexMethod.calculateNeighbors(
                    parser.getParticlesPerTime().get(0),
                    parser.getL(), config.getM(),
                    config.getRc(), config.getPeriodic()
            );
        } else if (Objects.equals(config.getMethod(), BRUTE)) {
            results = BruteForceMethod.calculateNeighbors(
                    parser.getParticlesPerTime().get(0),
                    parser.getL(), config.getRc(),
                    config.getPeriodic()
            );
        } else {
            return;
        }

        System.out.println("Simulation finished\n");
        System.out.println("Writing Results ...\n");

        final File outNeighborsFile = new File(config.getOutNeighborsFile());
        final File outTimeFile = new File(config.getOutTimeFile());

        try (PrintWriter pw = new PrintWriter(outNeighborsFile)) {
            results.getNeighbors().forEach((key, value) -> {
                pw.append(String.format("%d ", key));
                value.forEach(particle -> pw.append(String.format("%d ", particle.getId())));
                pw.println();
            });
        }

        try (PrintWriter pw = new PrintWriter(outTimeFile)) {
            Duration duration = results.getTotalTime();
            long hours = duration.toHours();
            long minutes = duration.toMinutesPart();
            long seconds = duration.toSecondsPart();
            long millis = duration.toMillisPart();
            pw.append(String.format("%s - %02d:%02d:%02d.%03d\n", "Total time", hours, minutes, seconds, millis));
        }

    }
}