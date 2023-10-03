package ar.edu.itba;

import ar.edu.itba.models.Particle;
import ar.edu.itba.simulation.MolecularDynamic;
import ar.edu.itba.utils.CreateStaticAndDynamicFiles;
import ar.edu.itba.utils.ParticlesParserResult;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class BenchmarkVelocity {

    final static Double DT = 0.001;
    final static Double DT2 = 0.1;
    final static Double L = 135.0;
    final static Double MAX_TIME = 180.0;
    final static List<Integer> Ns = List.of(5, 10, 15, 20, 25, 30);

    public static void main(String[] args) throws IOException {

        final Map<Integer, Map<BigDecimal, List<Particle>>> particlesWithN = new HashMap<>();
        final Map<Integer, Map<BigDecimal, List<Particle>>> particlesWithNAscending = new HashMap<>();
        final Map<Integer, Map<BigDecimal, List<Particle>>> particlesWithNDescending = new HashMap<>();

        for (Integer N : Ns) {

            final ParticlesParserResult parser = CreateStaticAndDynamicFiles.create(N);

            String file = "src/main/resources/unidimensional_particles/benchmark/velocity/" + N + ".txt";
            File outFile = new File(file);

            List<Particle> particles = MolecularDynamic.cloneParticles(parser.getParticlesPerTime().get(0));

            Map<BigDecimal, List<Particle>> aux = MolecularDynamic.run(particles, L, MAX_TIME, DT, DT2, outFile);
            particlesWithN.put(N, aux);

            // --------------Orden ascendente--------------

            file = "src/main/resources/unidimensional_particles/benchmark/velocity/ascendingOrder/" + N + ".txt";
            outFile = new File(file);
            List<Particle> particlesAscending = MolecularDynamic.cloneParticles(parser.getParticlesPerTime().get(0));
            List<Double> positions = new ArrayList<>();

            for(Particle p : particlesAscending) {
                positions.add(p.getX());
            }

            positions.sort(Comparator.naturalOrder());
            particlesAscending.sort(Comparator.comparingDouble(Particle::getU));

            int idx = 0;
            for(Particle p : particlesAscending) {
                p.setX(positions.get(idx));
                idx++;
            }

            aux = MolecularDynamic.run(particlesAscending, L, MAX_TIME, DT, DT2, outFile);
            particlesWithNAscending.put(N, aux);

            // --------------Orden descendente--------------

            file = "src/main/resources/unidimensional_particles/benchmark/velocity/descendingOrder/" + N + ".txt";
            outFile = new File(file);
            List<Particle> particlesDescending = MolecularDynamic.cloneParticles(parser.getParticlesPerTime().get(0));
            positions = new ArrayList<>();

            for(Particle p : particlesDescending) {
                positions.add(p.getX());
            }

            positions.sort(Comparator.naturalOrder());
            particlesDescending.sort(Comparator.comparingDouble(Particle::getU).reversed());

            idx = 0;
            for(Particle p : particlesDescending) {
                p.setX(positions.get(idx));
                idx++;
            }

            aux = MolecularDynamic.run(particlesDescending, L, MAX_TIME, DT, DT2, outFile);
            particlesWithNDescending.put(N, aux);
        }

        //-------------------------------------------------------------------------
        //-----------------------PROMEDIO NORMAL------------------------------------
        //--------------------------------------------------------------------------

        final Map<Integer, List<BigDecimal>> velocityValues = new HashMap<>();

        for(Integer N : Ns) {

            Map<BigDecimal, List<Particle>> currentMap = particlesWithN.get(N);
            List<BigDecimal> currentVelocityList = new ArrayList<>();

            for(int iter = 0; iter < currentMap.size(); iter++) {

                BigDecimal time = new BigDecimal(iter).multiply((new BigDecimal(DT2.toString())));
                BigDecimal meanVelocity = new BigDecimal("0.0");

                for(int j = 0; j < N; j++) {
                    BigDecimal particleVelocity = BigDecimal.valueOf(currentMap.get(time).get(j).getVx());
                    meanVelocity = meanVelocity.add(particleVelocity);
                }

                meanVelocity = meanVelocity.divide(BigDecimal.valueOf(N), RoundingMode.HALF_EVEN);
                currentVelocityList.add(meanVelocity);
            }
            velocityValues.put(N, currentVelocityList);
        }

        final String fileV = "src/main/resources/unidimensional_particles/benchmark/velocity/velocityValues.txt";
        final File outVFile = new File(fileV);

        try (PrintWriter pw = new PrintWriter(outVFile)) {
            for(Map.Entry<Integer, List<BigDecimal>> entry : velocityValues.entrySet()) {
                pw.println(String.format(Locale.US, "%d", entry.getKey()));
                entry.getValue().forEach((v) -> pw.append(String.format(Locale.US, "%.20f ", v)));
                pw.println();
            }
        }

        //-------------------------------------------------------------------------
        //-----------------------PROMEDIO ASCENDENTE------------------------------------
        //--------------------------------------------------------------------------

        final Map<Integer, List<BigDecimal>> velocityValuesAscending = new HashMap<>();

        for(Integer N : Ns) {

            Map<BigDecimal, List<Particle>> currentMap = particlesWithNAscending.get(N);
            List<BigDecimal> currentVelocityList = new ArrayList<>();

            for(int iter = 0; iter < currentMap.size(); iter++) {

                BigDecimal time = new BigDecimal(iter).multiply((new BigDecimal(DT2.toString())));
                BigDecimal meanVelocity = new BigDecimal("0.0");

                for(int j = 0; j < N; j++) {
                    BigDecimal particleVelocity = BigDecimal.valueOf(currentMap.get(time).get(j).getVx());
                    meanVelocity = meanVelocity.add(particleVelocity);
                }

                meanVelocity = meanVelocity.divide(BigDecimal.valueOf(N), RoundingMode.HALF_EVEN);
                currentVelocityList.add(meanVelocity);
            }
            velocityValuesAscending.put(N, currentVelocityList);
        }

        final String fileVAscending = "src/main/resources/unidimensional_particles/benchmark/velocity/ascendingOrder/velocityValues.txt";
        final File outVAscendingFile = new File(fileVAscending);

        try (PrintWriter pw = new PrintWriter(outVAscendingFile)) {
            for(Map.Entry<Integer, List<BigDecimal>> entry : velocityValuesAscending.entrySet()) {
                pw.println(String.format(Locale.US, "%d", entry.getKey()));
                entry.getValue().forEach((v) -> pw.append(String.format(Locale.US, "%.20f ", v)));
                pw.println();
            }
        }

        //-------------------------------------------------------------------------
        //-----------------------PROMEDIO DESCENDENTE------------------------------------
        //--------------------------------------------------------------------------

        final Map<Integer, List<BigDecimal>> velocityValuesDescending = new HashMap<>();

        for(Integer N : Ns) {

            Map<BigDecimal, List<Particle>> currentMap = particlesWithNDescending.get(N);
            List<BigDecimal> currentVelocityList = new ArrayList<>();

            for(int iter = 0; iter < currentMap.size(); iter++) {

                BigDecimal time = new BigDecimal(iter).multiply((new BigDecimal(DT2.toString())));
                BigDecimal meanVelocity = new BigDecimal("0.0");

                for(int j = 0; j < N; j++) {
                    BigDecimal particleVelocity = BigDecimal.valueOf(currentMap.get(time).get(j).getVx());
                    meanVelocity = meanVelocity.add(particleVelocity);
                }

                meanVelocity = meanVelocity.divide(BigDecimal.valueOf(N), RoundingMode.HALF_EVEN);
                currentVelocityList.add(meanVelocity);
            }
            velocityValuesDescending.put(N, currentVelocityList);
        }

        final String fileVDescending = "src/main/resources/unidimensional_particles/benchmark/velocity/descendingOrder/velocityValues.txt";
        final File outVDescendingFile = new File(fileVDescending);

        try (PrintWriter pw = new PrintWriter(outVDescendingFile)) {
            for(Map.Entry<Integer, List<BigDecimal>> entry : velocityValuesDescending.entrySet()) {
                pw.println(String.format(Locale.US, "%d", entry.getKey()));
                entry.getValue().forEach((v) -> pw.append(String.format(Locale.US, "%.20f ", v)));
                pw.println();
            }
        }

        System.out.println("Velocity calculation finished ...\n");

    }
}
