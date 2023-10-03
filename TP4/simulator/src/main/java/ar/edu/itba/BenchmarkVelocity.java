package ar.edu.itba;

import ar.edu.itba.models.Particle;
import ar.edu.itba.simulation.MolecularDynamic;
import ar.edu.itba.utils.CreateStaticAndDynamicFiles;
import ar.edu.itba.utils.ParticlesParserResult;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;

public class BenchmarkVelocity {

    final static Double DT = 0.001;  //TODO sacar el DT segun el punto 1
    final static Double DT2 = 0.1;
    final static Double L = 135.0;
    final static Double MAX_TIME = 180.0;
    final static List<Integer> Ns = List.of(5, 10, 15, 20, 25, 30);

    public static void main(String[] args) throws IOException {

        final Map<Integer, Map<BigDecimal, List<Particle>>> particlesWithN = new HashMap<>();

        for (Integer N : Ns) {

            final ParticlesParserResult parser = CreateStaticAndDynamicFiles.create(N);

            final String file = "src/main/resources/unidimensional_particles/benchmark/velocity/" + N + ".txt";
            final File outFile = new File(file);

            List<Particle> particles = MolecularDynamic.cloneParticles(parser.getParticlesPerTime().get(0));

            Map<BigDecimal, List<Particle>> aux = MolecularDynamic.run(particles, L, MAX_TIME, DT, DT2, outFile);
            particlesWithN.put(N, aux);
        }

        //------------Velocity VALUE----------------

        //Calcular la velocidad promedio del sistema en función del tiempo

        final Map<Integer, List<BigDecimal>> velocityValues = new HashMap<>();
        final Map<Integer, List<BigDecimal>> velocityError = new HashMap<>();

        for(Integer N : Ns) {

            Map<BigDecimal, List<Particle>> currentMap = particlesWithN.get(N);
            List<BigDecimal> currentVelocityList = new ArrayList<>();
            List<BigDecimal> currentErrorList = new ArrayList<>();

            for(int iter = 0; iter < currentMap.size(); iter++) {

                BigDecimal time = new BigDecimal(iter).multiply((new BigDecimal(DT2.toString())));
                BigDecimal meanVelocity = new BigDecimal("0.0");

                for(int j = 0; j < N; j++) {
                    BigDecimal particleVelocity = BigDecimal.valueOf(currentMap.get(time).get(j).getVx());
                    meanVelocity = meanVelocity.add(particleVelocity);
                }

                meanVelocity = meanVelocity.divide(BigDecimal.valueOf(N), RoundingMode.HALF_EVEN);
                currentVelocityList.add(meanVelocity);

                // Calcular el error
                BigDecimal errorSum = new BigDecimal("0.0");

                for (int j = 0; j < N; j++) {
                    BigDecimal particleVelocity = new BigDecimal(currentMap.get(time).get(j).getVx());
                    errorSum = errorSum.add( particleVelocity.subtract(meanVelocity).pow(2) );
                }

                errorSum = errorSum.divide(BigDecimal.valueOf(N), RoundingMode.HALF_EVEN);
                errorSum = errorSum.sqrt(MathContext.DECIMAL32);
                currentErrorList.add(errorSum);
            }
            velocityValues.put(N, currentVelocityList);
            velocityError.put(N, currentErrorList);
        }

        //TODO y luego el promedio temporal en el estacionario como función de N.


        final String fileV = "src/main/resources/unidimensional_particles/benchmark/velocity/velocityValues.txt";
        final File outVFile = new File(fileV);

        try (PrintWriter pw = new PrintWriter(outVFile)) {
            for(Map.Entry<Integer, List<BigDecimal>> entry : velocityValues.entrySet()) {
                pw.println(String.format(Locale.US, "%d", entry.getKey()));
                entry.getValue().forEach((v) -> pw.append(String.format(Locale.US, "%.20f ", v)));
                pw.println();
                velocityError.get(entry.getKey()).forEach((error) -> pw.append(String.format(Locale.US, "%.20f ", error)));
                pw.println();
            }
        }

        System.out.println("Velocity calculation finished ...\n");

    }
}
