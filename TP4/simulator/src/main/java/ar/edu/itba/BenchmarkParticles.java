package ar.edu.itba;

import ar.edu.itba.models.Particle;
import ar.edu.itba.simulation.MolecularDynamic;
import ar.edu.itba.utils.CreateStaticAndDynamicFiles;
import ar.edu.itba.utils.ParticlesParserResult;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class BenchmarkParticles {

    final static List<Double> DTS = List.of(0.1, 0.01, 0.001);
    final static Double L = 135.0;
    final static Double MAX_TIME = 180.0;
    final static Double DT2 = 0.1;
    final static List<Integer> Ns = List.of(5, 10, 15, 20, 25, 30);

    public static void main(String[] args) throws IOException {

        final int N = 25;

        final ParticlesParserResult parser = CreateStaticAndDynamicFiles.create(N);

        final Map<Double, Map<Integer, List<Particle>>> particlesWithDts = new HashMap<>();

        for (Double dt : DTS) {

            final String file = "src/main/resources/unidimensional_particles/benchmark/" + dt + ".txt";
            final File outFile = new File(file);

            List<Particle> particles = MolecularDynamic.cloneParticles(parser.getParticlesPerTime().get(0));

            Map<Integer, List<Particle>> aux = MolecularDynamic.run(particles, L, MAX_TIME, dt, DT2, outFile);
            particlesWithDts.put(dt, aux);
        }

        //------------PHI VALUE----------------

        final Map<Integer, List<Double>> phiValues = new HashMap<>();

        for(int i = 0; i < DTS.size() - 1; i++) {
            Double currentDt = DTS.get(i);
            Double nextDt = DTS.get(i + 1);

            Map<Integer, List<Particle>> currentMap = particlesWithDts.get(currentDt);
            Map<Integer, List<Particle>> nextMap = particlesWithDts.get(nextDt);

            List<Double> currentPhiList = new ArrayList<>();

            for(int iter = 0; iter < currentMap.size(); iter++) {

                Double phiValue = 0.0;

                for(int j = 0; j < N; j++) {
                    phiValue += Math.abs(nextMap.get(iter).get(j).getX() - currentMap.get(iter).get(j).getX());
                }

                currentPhiList.add(phiValue);

            }

            phiValues.put(i, currentPhiList);
        }

        final String filePhi = "src/main/resources/unidimensional_particles/benchmark/phiValues.txt";
        final File outPhiFile = new File(filePhi);

        try (PrintWriter pw = new PrintWriter(outPhiFile)) {
            for(Map.Entry<Integer, List<Double>> entry : phiValues.entrySet()) {
                pw.println(String.format(Locale.US, "%d", entry.getKey()));
                entry.getValue().forEach((phi) -> pw.append(String.format(Locale.US, "%.20f ", phi)));
                pw.println();
            }
        }

        System.out.println("Phi calculation finished ...\n");

    }
}
