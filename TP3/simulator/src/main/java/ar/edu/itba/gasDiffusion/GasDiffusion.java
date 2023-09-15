package ar.edu.itba.gasDiffusion;

import ar.edu.itba.models.Enclosure;
import ar.edu.itba.models.Particle;

import java.util.ArrayList;
import java.util.List;

public class GasDiffusion {

    public static GasDiffusionResult run(List<List<Particle>> particlesPerTime, Integer iterations, Double side, Double L) {
        long startTime = System.nanoTime();
        Enclosure enclosure = new Enclosure(particlesPerTime.get(0), side, L);

        List<List<Particle>> particlesOverTime = new ArrayList<>();
        particlesOverTime.add(particlesPerTime.get(0));

        for (int i = 0; i < iterations; i++) {
            Enclosure nextEnclosure = enclosure.getNextEnclosure();
            particlesOverTime.add(nextEnclosure.getParticles());
            enclosure = nextEnclosure;
        }

        long endTime = System.nanoTime();
        long totalTime = endTime - startTime;
        return new GasDiffusionResult(particlesOverTime, totalTime);
    }
}
