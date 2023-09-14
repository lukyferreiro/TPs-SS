package ar.edu.itba.utils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.Random;

public class CreateStaticAndDynamicFiles {

    public static ParticlesParserResult create(long N, double L) throws IOException, ParseException {
        FileReader fr = new FileReader("src/main/resources/configGenerator.json");
        JSONObject json = (JSONObject) new JSONParser().parse(fr);
        ConfigGeneratorParser config = new ConfigGeneratorParser(json);

        final File staticFile = new File(config.getStaticFile());
        final File dynamicFile = new File(config.getDynamicFile());

        final double MIN_R = 0.0;
        final double MAX_R = 0.0;
        final double MASS = 1.0;
        final double SPEED = 0.03;
        final double MAX_ANGLE = 2 * Math.PI;
        final int TIMES = 1;

        try (PrintWriter pw = new PrintWriter(staticFile)) {
            pw.println(N);
            pw.println(L);
            for (int i = 0; i < N; i++) {
                pw.printf(Locale.US, "%f %f\n", MIN_R + Math.random() * (MAX_R - MIN_R), MASS);
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
