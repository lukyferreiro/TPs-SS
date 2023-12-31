package ar.edu.itba;

import ar.edu.itba.models.Particle;
import ar.edu.itba.models.Position;
import ar.edu.itba.utils.ConfigGeneratorParser;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Generator {

    private static final double MIN_UI = 9.0;
    private static final double MAX_UI = 12.0;

    public static void main(String[] args) throws IOException, ParseException {

        FileReader fr = new FileReader("src/main/resources/unidimensional_particles/configGenerator.json");
        JSONObject json = (JSONObject) new JSONParser().parse(fr);
        ConfigGeneratorParser config = new ConfigGeneratorParser(json);

        final File staticFile = new File(config.getStaticFile());
        final File dynamicFile = new File(config.getDynamicFile());

        System.out.println("Generating particles ...\n");

        final List<Particle> particles = new ArrayList<>();
        final double minR = config.getMinR();
        final double maxR = config.getMaxR();

        try (PrintWriter pw = new PrintWriter(staticFile)) {
            pw.println(config.getN());
            pw.println(config.getL());
            for (int i = 0; i < config.getN(); i++) {
                final double radius = minR + Math.random() * (maxR - minR);
                pw.printf(Locale.US, "%.2f %.1f\n", radius, config.getMass());
                particles.add(new Particle(i, radius, config.getMass()));
            }
        }

        try (PrintWriter pw = new PrintWriter(dynamicFile)) {
            final Random random = new Random();
            for (int i = 0; i < config.getTimes(); i++) {
                pw.println(i);
                if (config.getN() == 25 || config.getN() == 30) {
                    int count = 0;
                    for (Particle p : particles) {
                        double requiredSpacing = 2 * p.getRadius();
                        double unusedSpace = config.getL() - (requiredSpacing * config.getN());
                        double spacing = unusedSpace > 0 ? unusedSpace / (config.getN() - 1) : 0;
                        double speed = MIN_UI + Math.random() * (MAX_UI - MIN_UI);
                        double x = count * (requiredSpacing + spacing);
                        pw.printf(Locale.US, "%.15f %.1f %.15f %.1f\n", x, 0.0, speed, 0.0);
                        count++;
                    }
                } else {
                    for (int j = 0; j < config.getN(); j++) {
                        double speed = MIN_UI + Math.random() * (MAX_UI - MIN_UI);
                        double angle = 0.0;
                        Particle particle = particles.get(j);

                        System.out.println(j);

                        double x;
                        double y;
                        boolean superposition;

                        do {
                            superposition = false;
                            x = random.nextDouble() * (config.getL() - 2 * particle.getRadius());
                            y = 0.0;

                            Position position = new Position(x, y);
                            for (Particle other : particles) {
                                if (other.getPosition() != null) {
                                    double distance = position.calculateDistance(other.getPosition());
                                    if (distance < 2 * particle.getRadius()) {
                                        superposition = true;
                                        break;
                                    }
                                }
                            }
                        } while (superposition);

                        particle.setPosition(new Position(x, y));
                        pw.printf(Locale.US, "%.15f %.1f %.15f %.1f\n", x, y, speed, angle);
                    }
                }
            }
        }

        System.out.println("Particles generated\n");
    }
}
