package ar.edu.itba.utils;

import ar.edu.itba.models.DoublePair;
import ar.edu.itba.models.Particle;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class CreateStaticAndDynamicFiles {
    private static final double MIN_RADIUS = 0.85;
    private static final double MAX_RADIUS = 1.15;
    private static final double MASS = 1.0;
    private static final Double W = 20.0;
    private static final Double L = 70.0;

    public static ParticlesParserResult create(int N) throws IOException {

        final File staticFile = new File("src/main/resources/static.txt");
        final File dynamicFile = new File("src/main/resources/dynamic.txt");


        final List<Particle> particles = new ArrayList<>();

        try (PrintWriter pw = new PrintWriter(staticFile)) {
            pw.println(N);
            pw.println(L);
            pw.println(W);
            for (int i = 0; i < N; i++) {
                final double radius = MIN_RADIUS + Math.random() * (MAX_RADIUS - MIN_RADIUS);
                pw.printf(Locale.US, "%.2f %.1f\n", radius, MASS);
                particles.add(new Particle(i, radius, MASS));
            }
        }

        try (PrintWriter pw = new PrintWriter(dynamicFile)) {
            for (int i = 0; i < 1; i++) {
                pw.println(i);
                for (int j = 0; j < N; j++) {
                    double speed = 0.0;
                    double angle = 0.0;
                    Particle particle = particles.get(j);

                    double x;
                    double y;
                    boolean superposition;

                    do {
                        superposition = false;
                        x = particle.getRadius() + Math.random() * (W - 2 * particle.getRadius());
                        y = particle.getRadius() + L / 10 + Math.random() * (L - 2 * particle.getRadius());

                        DoublePair position = new DoublePair(x, y);
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

                    particle.setPosition(new DoublePair(x, y));
                    pw.printf(Locale.US, "%.15f %.15f %.1f %.1f\n", x, y, speed, angle);
                }
            }
        }

        System.out.println("Particles generated\n");
        ParticlesParserResult parser = ParticlesParser.parseParticlesList(staticFile, dynamicFile);

        staticFile.delete();
        dynamicFile.delete();

        return parser;
    }
}