package ar.edu.itba;

import ar.edu.itba.models.Pair;
import ar.edu.itba.models.Particle;
import ar.edu.itba.simulation.MolecularDynamic;
import ar.edu.itba.utils.CreateStaticAndDynamicFiles;
import ar.edu.itba.utils.ParticlesParserResult;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.*;


public class BenchmarkDensity {

    final static Double DT = 0.001;
    final static Double DT2 = 0.1;
    final static Double L = 135.0;
    final static Double MAX_TIME = 180.0;
    final static List<Integer> Ns = List.of(5, 10, 15, 20, 25, 30);

    public static void main(String[] args) throws IOException {

        final Map<Integer, Map<BigDecimal, List<Particle>>> particlesWithN = new HashMap<>();

        for (Integer N : Ns) {

            final ParticlesParserResult parser = CreateStaticAndDynamicFiles.create(N);

            String file = "src/main/resources/unidimensional_particles/benchmark/density/" + N + ".txt";
            File outFile = new File(file);

            List<Particle> particles = MolecularDynamic.cloneParticles(parser.getParticlesPerTime().get(0));

            Map<BigDecimal, List<Particle>> aux = MolecularDynamic.run(particles, L, MAX_TIME, DT, DT2, outFile);
            particlesWithN.put(N, aux);
        }

        List<Double> densityList = new ArrayList<>();
        List<Double> velocityList = new ArrayList<>();

        for (Integer N : Ns) {

            Map<BigDecimal, List<Particle>> currentMap = particlesWithN.get(N);

            for (int iter = 0; iter < currentMap.size(); iter++) {

                BigDecimal time = new BigDecimal(iter).multiply(new BigDecimal(DT2.toString()));
                List<Particle> particlesAtTime = currentMap.get(time);

                for (int j = 0; j < N; j++) {
                    Particle particle = particlesAtTime.get(j);
                    densityList.add(particle.getDensity());
                    velocityList.add(particle.getVx());
                }
            }
        }

        List<Pair<Double, Double>> pairedList = new ArrayList<>();
        for (int i = 0; i < densityList.size(); i++) {
            pairedList.add(new Pair<>(densityList.get(i), velocityList.get(i)));
        }

        pairedList.sort(Comparator.comparingDouble(Pair::getOne));

        List<Double> sortedDensityList = new ArrayList<>();
        List<Double> sortedVelocityList = new ArrayList<>();
        for (Pair<Double, Double> pair : pairedList) {
            sortedDensityList.add(pair.getOne());
            sortedVelocityList.add(pair.getOther());
        }

        int window = 1000;

        List<Double> smoothedDensity = movingAverage(sortedDensityList, window);
        List<Double> smoothedVelocity = movingAverage(sortedVelocityList, window);

        final String fileDensity = "src/main/resources/unidimensional_particles/benchmark/density/density.txt";
        final File outDensityFile = new File(fileDensity);

        try (PrintWriter pw = new PrintWriter(outDensityFile)) {
            smoothedDensity.forEach((d) -> pw.append(String.format(Locale.US,"%.20f ", d)));
            pw.println();
            smoothedVelocity.forEach((d) -> pw.append(String.format(Locale.US,"%.20f ", d)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("Density calculation finished ...\n");

    }

    private static List<Double> movingAverage(List<Double> list, int window) {
        List<Double> smoothedData = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            int start = Math.max(0, i - window / 2);
            int end = Math.min(list.size(), i + window / 2 + 1);
            List<Double> windowData = list.subList(start, end);

            double sum = 0;
            for (double value : windowData) {
                sum += value;
            }
            double average = sum / windowData.size();
            smoothedData.add(average);
        }

        return smoothedData;
    }

}
