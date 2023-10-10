package ar.edu.itba.utils;

import ar.edu.itba.models.Particle;
import ar.edu.itba.models.Position;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class CreateStaticAndDynamicFiles {

    private static final double MIN_UI = 9.0;
    private static final double MAX_UI = 12.0;
    private static final double L = 135.0;
    private static final double RADIUS = 2.25;
    private static final double MASS = 25.0;

    public static ParticlesParserResult create(int N) throws IOException {

        final File staticFile = new File("src/main/resources/unidimensional_particles/static.txt");
        final File dynamicFile = new File("src/main/resources/unidimensional_particles/dynamic.txt");


        final List<Particle> particles = new ArrayList<>();

        try (PrintWriter pw = new PrintWriter(staticFile)) {
            pw.println(N);
            pw.println(L);
            for (int i = 0; i < N; i++) {
                pw.printf(Locale.US, "%.2f %.1f\n", RADIUS, MASS);
                particles.add(new Particle(i, RADIUS, MASS));
            }
        }

        try (PrintWriter pw = new PrintWriter(dynamicFile)) {
            final Random random = new Random();
            for (int i = 0; i < 1; i++) {
                pw.println(i);
                if (N == 25 || N == 30) {
                    int count = 0;
                    for (Particle p : particles) {
                        double requiredSpacing = 2 * RADIUS;
                        double unusedSpace = L - (requiredSpacing * N);
                        double spacing = unusedSpace > 0 ? unusedSpace / (N - 1) : 0;
                        double speed = MIN_UI + Math.random() * (MAX_UI - MIN_UI);
                        double x = count * (requiredSpacing + spacing);
                        pw.printf(Locale.US, "%.15f %.1f %.15f %.1f\n", x, 0.0, speed, 0.0);
                        count++;
                    }
                } else {
                    for (int j = 0; j < N; j++) {
                        double speed = MIN_UI + Math.random() * (MAX_UI - MIN_UI);
                        Particle particle = particles.get(j);

                        double x;
                        boolean superposition;

                        do {
                            superposition = false;
                            x = random.nextDouble() * (L - 2 * RADIUS);

                            Position position = new Position(x, 0.0);
                            for (Particle other : particles) {
                                if (other.getPosition() != null) {
                                    double distance = position.calculateDistance(other.getPosition());
                                    if (distance < 2 * RADIUS) {
                                        superposition = true;
                                        break;
                                    }
                                }
                            }
                        } while (superposition);

                        particle.setPosition(new Position(x, 0.0));
                        pw.printf(Locale.US, "%.15f %.1f %.15f %.1f\n", x, 0.0, speed, 0.0);
                    }
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
