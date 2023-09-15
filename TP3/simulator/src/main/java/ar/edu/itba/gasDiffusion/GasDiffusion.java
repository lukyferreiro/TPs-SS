package ar.edu.itba.gasDiffusion;

import ar.edu.itba.models.Enclosure;
import ar.edu.itba.models.Particle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GasDiffusion {

    public static GasDiffusionResult run(List<List<Particle>> particlesPerTime, Integer iterations, Double side, Double L) {
        final long startTime = System.nanoTime();
        Enclosure enclosure = new Enclosure(particlesPerTime.get(0), side, L);
        Iterator<Enclosure> it = enclosure.iterator();
        List<List<Particle>> particlesOverTime = new ArrayList<>();
        for (int i = 0; i < iterations; i++) {
//            double time = i * deltaT;
            Enclosure next = it.next();
//            while (next.getTime() < time) {
//                next = it.next();
//            }
            particlesOverTime.add((List<Particle>) next.getParticles());
        }
        final long endTime = System.nanoTime();
        final long totalTime = endTime - startTime;
        return new GasDiffusionResult(particlesOverTime, totalTime);
    }
}
