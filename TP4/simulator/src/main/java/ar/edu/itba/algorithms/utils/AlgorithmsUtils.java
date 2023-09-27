package ar.edu.itba.algorithms.utils;

import ar.edu.itba.models.Pair;
import ar.edu.itba.models.Particle;
import static ar.edu.itba.algorithms.utils.R.values.*;

public class AlgorithmsUtils {

    public static Pair<Double, Double> calculateAcceleration(
            double mass, Pair<Double, Double> r0, Pair<Double, Double> r1, double k, double gamma
    ) {
        final double r2x = (-k * r0.getOne() - gamma * r1.getOne()) / mass;
        final double r2y = (-k * r0.getOther() - gamma * r1.getOther()) / mass;

        return new Pair<>(r2x, r2y);
    }

    public static R calculateInitialR(Particle particle, double k, double gamma) {
        final R initialR = new R();
        final double mass = particle.getMass();

        //r0
        initialR.add(particle.getX(), particle.getY());
        //r1
        initialR.add(particle.getVx(), particle.getVy());
        //r2
        final Pair<Double, Double> r2 = calculateAcceleration(mass, initialR.get(R0.ordinal()), initialR.get(R1.ordinal()), k, gamma);
        initialR.add(r2.getOne(), r2.getOther());
        //r3
        initialR.add((-k * initialR.get(R1.ordinal()).getOne() - gamma * initialR.get(R2.ordinal()).getOne()) / mass,
                (-k * initialR.get(R1.ordinal()).getOther() - gamma * initialR.get(R2.ordinal()).getOther()) / mass);
        //r4
        initialR.add((-k * initialR.get(R2.ordinal()).getOne() - gamma * initialR.get(R3.ordinal()).getOne()) / mass,
                (-k * initialR.get(R2.ordinal()).getOther() - gamma * initialR.get(R3.ordinal()).getOther()) / mass);

        //r5
        initialR.add((-k * initialR.get(R3.ordinal()).getOne() - gamma * initialR.get(R4.ordinal()).getOne()) / mass,
                (-k * initialR.get(R3.ordinal()).getOther() - gamma * initialR.get(R4.ordinal()).getOther()) / mass);

        return initialR;
    }

    public static R euler(R r, double dt, double mass, double k, double gamma) {

        final R eulerR = new R();
        final Pair<Double, Double> r0 = r.get(R0.ordinal());
        final Pair<Double, Double> r1 = r.get(R1.ordinal());
        final Pair<Double, Double> r2 = r.get(R2.ordinal());

        // r0
        double r0x = r0.getOne() + dt * r1.getOne() + (Math.pow(dt, 2) / (2 * mass)) * r2.getOne() * mass;
        double r0y = r0.getOther() + dt * r1.getOther() + (Math.pow(dt, 2) / (2 * mass)) * r2.getOther() * mass;
        eulerR.add(r0x, r0y);

        // r1
        double r1x = r1.getOne() + (dt / mass) * r2.getOne() * mass;
        double r1y = r1.getOther() + (dt / mass) * r2.getOther() * mass;
        eulerR.add(r1x, r1y);

        final Pair<Double, Double>  eulerR2 = calculateAcceleration(mass, eulerR.get(R0.ordinal()), eulerR.get(R1.ordinal()), k, gamma);
        eulerR.add(eulerR2.getOne(), eulerR2.getOther());

        return eulerR;
    }

}
