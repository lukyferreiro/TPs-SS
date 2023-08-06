package ar.edu.itba;

import ar.edu.itba.utils.ConfigParser;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.Random;


public class Main {
    public static void main(String[] args) throws IOException, ParseException {


        FileReader fr = new FileReader("src/main/resources/config.json");
        JSONObject json = (JSONObject) new JSONParser().parse(fr);
        ConfigParser config = new ConfigParser(json);

        final File staticFile = new File(config.getStaticFile());
        final File dynamicFile = new File(config.getDynamicFile());

        System.out.println("Generating particles ...\n");

        final double minR = config.getMinR();
        final double maxR = config.getMaxR();
        try (PrintWriter pw = new PrintWriter(staticFile)) {
            pw.println(config.getN());
            pw.println(config.getL());
            for (int i = 0; i < config.getN(); i++) {
                pw.printf(Locale.US, "%f %f\n", minR + Math.random() * (maxR - minR), 1.0000);
            }
        }

        try (PrintWriter pw = new PrintWriter(dynamicFile)) {
            final Random random = new Random();
            for (int i = 0; i < config.getTimes(); i++) {
                pw.println(i);
                for (int j = 0; j < config.getN(); j++) {
                    double x = random.nextDouble() * (config.getL());
                    double y = random.nextDouble() * (config.getL());
                    pw.printf(Locale.US, "%f %f\n", x, y);
                }

            }
        }

        System.out.println("Particles generated\n");

    }
}