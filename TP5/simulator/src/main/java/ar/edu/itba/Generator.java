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

    public static void main(String[] args) throws IOException, ParseException {

        FileReader fr = new FileReader("src/main/resources/configGenerator.json");
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
            pw.println(config.getW());
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
                for (int j = 0; j < config.getN(); j++) {
                    double speed = 0.0;
                    double angle = 0.0;
                    Particle particle = particles.get(j);

                    System.out.println(j);

                    double x;
                    double y;
                    boolean superposition;

                    do {
                        superposition = false;
                        x = particle.getRadius() + Math.random() * (config.getW() - 2 * particle.getRadius());
                        y = particle.getRadius() + config.getL() / 10 + Math.random() * (config.getL() - 2 * particle.getRadius());

//                        // Comprobar si la partícula está dentro del recinto
//                        if (x < particle.getRadius() || x > config.getW() - particle.getRadius() ||
//                                y < particle.getRadius() || y > config.getL() - particle.getRadius()) {
//                            superposition = true;
//                            continue; // Volver a generar coordenadas si está fuera del recinto
//                        }

                        Position position = new Position(x, y);
                        for (Particle other : particles) {
                            if (other.getPosition() != null) {
                                double distance = position.calculateDistance(other.getPosition());
                                if (distance < particle.getRadius() + other.getRadius()) {
                                    superposition = true;
                                    break;
                                }
                            }
                        }
                    } while (superposition);

                    particle.setPosition(new Position(x, y));
                    pw.printf(Locale.US, "%.15f %.15f %.1f %.1f\n", x, y, speed, angle);
                }
            }
        }

        System.out.println("Particles generated\n");
    }
}
