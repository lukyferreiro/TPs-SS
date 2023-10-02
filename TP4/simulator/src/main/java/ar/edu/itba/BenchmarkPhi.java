package ar.edu.itba;

import ar.edu.itba.models.Particle;
import ar.edu.itba.simulation.MolecularDynamic;
import ar.edu.itba.utils.CreateStaticAndDynamicFiles;
import ar.edu.itba.utils.ParticlesParserResult;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.*;

public class BenchmarkPhi {

    final static List<Double> DTS = List.of(0.1, 0.01, 0.001, 0.0001, 0.00001);
    final static Double DT2 = 0.1;
    final static Double L = 135.0;
    final static Double MAX_TIME = 180.0;

    public static void main(String[] args) throws IOException {

        final int N = 25;

        final ParticlesParserResult parser = CreateStaticAndDynamicFiles.create(N);

        final Map<Double, Map<BigDecimal, List<Particle>>> particlesWithDts = new HashMap<>();

        for (Double dt : DTS) {

            final String file = "src/main/resources/unidimensional_particles/benchmark/phi/" + dt + ".txt";
            final File outFile = new File(file);

            List<Particle> particles = MolecularDynamic.cloneParticles(parser.getParticlesPerTime().get(0));

            Map<BigDecimal, List<Particle>> aux = MolecularDynamic.run(particles, L, MAX_TIME, dt, DT2, outFile);
            particlesWithDts.put(dt, aux);
        }

        //------------PHI VALUE----------------

        final Map<Integer, List<BigDecimal>> phiValues = new HashMap<>();

        for(int i = 0; i < DTS.size() - 1; i++) {
            Double currentDt = DTS.get(i);
            Double nextDt = DTS.get(i + 1);

            Map<BigDecimal, List<Particle>> currentMap = particlesWithDts.get(currentDt);
            Map<BigDecimal, List<Particle>> nextMap = particlesWithDts.get(nextDt);

            List<BigDecimal> currentPhiList = new ArrayList<>();

            for(int iter = 0; iter < currentMap.size(); iter++) {

                BigDecimal time = new BigDecimal(iter).multiply((new BigDecimal(DT2.toString())));
                BigDecimal phiValue = new BigDecimal(0.0);

                for(int j = 0; j < N; j++) {
                    phiValue = phiValue.add(BigDecimal.valueOf(calculatePeriodicDistance(nextMap.get(time).get(j).getX(), currentMap.get(time).get(j).getX())));
                }

                currentPhiList.add(phiValue);
            }
            phiValues.put(i, currentPhiList);
        }

        final String filePhi = "src/main/resources/unidimensional_particles/benchmark/phi/phiValues.txt";
        final File outPhiFile = new File(filePhi);

        try (PrintWriter pw = new PrintWriter(outPhiFile)) {
            for(Map.Entry<Integer, List<BigDecimal>> entry : phiValues.entrySet()) {
                pw.println(String.format(Locale.US, "%d", entry.getKey()));
                entry.getValue().forEach((phi) -> pw.append(String.format(Locale.US, "%.20f ", phi)));
                pw.println();
            }
        }

        System.out.println("Phi calculation finished ...\n");

    }

    private static Double calculatePeriodicDistance(Double posI, Double posJ) {
        Double aux = Math.abs(posI - posJ);
        return Math.min(aux, L - aux);
    }
}