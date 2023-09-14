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

    private static final Double MAX_ANGLE = 2 * Math.PI;

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
            pw.println(config.getSide());
            for (int i = 0; i < config.getN(); i++) {
                final double radius = minR + Math.random() * (maxR - minR);
                pw.printf(Locale.US, "%f %f\n", radius, config.getMass());
                particles.add(new Particle(i, radius, config.getMass()));
            }
        }

        try (PrintWriter pw = new PrintWriter(dynamicFile)) {
            final Random random = new Random();
            for (int i = 0; i < config.getTimes(); i++) {
                pw.println(i);
                for (int j = 0; j < config.getN(); j++) {
                    double speed = config.getSpeed();
                    double angle = random.nextDouble() * (MAX_ANGLE);
                    Particle particle = particles.get(j);

                    double x;
                    double y;
                    boolean superposition;

                    do {
                        superposition = false;
                        x = random.nextDouble() * (config.getSide() - 2 * particle.getRadius());
                        y = random.nextDouble() * (config.getSide() - 2 * particle.getRadius());
                        for (Particle other : particles) {
                            Position otherPosition = new Position(other.getPosition().getX(),other.getPosition().getY());
                            double distance = particle.getPosition().calculateDistance(otherPosition);
                            if (distance < 2 * particle.getRadius()) {
                                superposition = true;
                                break;
                            }
                        }
                    } while (superposition);

                    particle.setAngle(angle);
                    particle.setSpeed(speed);
                    particle.getPosition().setX(x);
                    particle.getPosition().setY(y);

                    pw.printf(Locale.US, "%f %f %f %f\n", x, y, speed, angle);
                }
            }
        }

        System.out.println("Particles generated\n");
    }
}
