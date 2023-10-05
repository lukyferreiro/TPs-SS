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

        //-------------------------------------------------------------------------
        //-----------------------PROMEDIO NORMAL------------------------------------
        //--------------------------------------------------------------------------

        // N -> ID -> [(v(0),p(0)), ...]
        Map<Integer, Map<Integer, List<Pair<Double, Double>>>> data = new HashMap<>();

        for (Integer N : Ns) {

            Map<Integer, List<Pair<Double, Double>>> particleData = new HashMap<>();
            Map<BigDecimal, List<Particle>> currentMap = particlesWithN.get(N);

            for (int iter = 0; iter < currentMap.size(); iter++) {

                BigDecimal time = new BigDecimal(iter).multiply(new BigDecimal(DT2.toString()));
                List<Particle> particlesAtTime = currentMap.get(time);

                for (int j = 0; j < N; j++) {
                    Particle particle = particlesAtTime.get(j);

                    double particleVelocity = particle.getVx();
                    double particleDensity = particle.getDensity();
                    int particleId = particle.getId();

                    particleData.computeIfAbsent(particleId, k -> new ArrayList<>())
                            .add(new Pair<>(particleVelocity, particleDensity));
                }
            }

            data.put(N, particleData);
        }

        int window = 1000;

        for (Map<Integer, List<Pair<Double, Double>>> particleData : data.values()) {
            for (List<Pair<Double, Double>> points : particleData.values()) {
                List<Pair<Double, Double>> smoothedPoints = new ArrayList<>();

                int size = points.size();
                for (int i = 0; i < size; i++) {
                    double sumVelocity = 0.0;
                    double sumDensity = 0.0;
                    int count = 0;

                    // Calcular el promedio en la ventana móvil
                    for (int j = Math.max(0, i - window / 2); j <= Math.min(size - 1, i + window / 2); j++) {
                        sumVelocity += points.get(j).getOne();
                        sumDensity += points.get(j).getOther();
                        count++;
                    }

                    // Calcular el promedio y agregar el punto suavizado a la lista
                    double averageVelocity = sumVelocity / count;
                    double averageDensity = sumDensity / count;
                    smoothedPoints.add(new Pair<>(averageVelocity, averageDensity));
                }

                // Reemplazar los puntos originales con los puntos suavizados
                points.clear();
                points.addAll(smoothedPoints);
            }
        }


        for(Map.Entry<Integer, Map<Integer, List<Pair<Double, Double>>>> entry : data.entrySet()) {
            final String fileDensity = "src/main/resources/unidimensional_particles/benchmark/density/" + entry.getKey() + ".txt";
            final File outDensityFile = new File(fileDensity);

            try (PrintWriter pw = new PrintWriter(outDensityFile)) {
                for (Map.Entry<Integer, List<Pair<Double, Double>>> innerEntry : entry.getValue().entrySet()) {
                    int particleId = innerEntry.getKey();
                    List<Pair<Double, Double>> velocityDensityPairs = innerEntry.getValue();

                    pw.println(String.format(Locale.US, "%d", particleId));

                    for (Pair<Double, Double> pair : velocityDensityPairs) {
                        pw.println(String.format(Locale.US, "%.20f %.20f", pair.getOne(), pair.getOther()));
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Density calculation finished ...\n");

    }

}
