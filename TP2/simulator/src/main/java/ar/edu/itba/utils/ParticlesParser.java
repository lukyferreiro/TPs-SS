package ar.edu.itba.utils;

import ar.edu.itba.models.Particle;

import java.io.File;
import java.io.IOException;
import java.util.*;

import java.lang.Double;
import java.lang.Integer;
import static ar.edu.itba.models.Particle.Position;
import static ar.edu.itba.models.Particle.State;

public class ParticlesParser {

    public static ParticlesParserResult parseParticlesList(File staticFile, File dynamicFile) throws IOException {
        final Scanner staticScanner = new Scanner(staticFile);
        final Scanner dynamicScanner = new Scanner(dynamicFile);

        final int N = Integer.parseInt(staticScanner.nextLine().split(" ")[0]);
        final double L = Double.parseDouble(staticScanner.nextLine().split(" ")[0]);

        final List<Particle> particles = new ArrayList<>();

        while (staticScanner.hasNextLine()) {
            final List<String> staticArray = Arrays.asList(staticScanner.nextLine().split(" "));
            particles.add(new Particle(
                    particles.size() + 1,
                    Double.parseDouble(staticArray.get(0)), //En primera posicion tengo el radio
                    Double.parseDouble(staticArray.get(1))  //En segunda posicion tengo la propiedad
            ));
        }

        staticScanner.close();

        int particleIndex = 0;
        int timeIndex = -1;
        final List<Map<Particle, State>> particlesPerTime = new ArrayList<>();

        while (dynamicScanner.hasNextLine()) {
            List<String> dynamicArray = Arrays.asList(dynamicScanner.nextLine().split(" "));

            //Me fijo si paso al siguiente tiempo
            if (dynamicArray.size() == 1) {
                particleIndex = 0;
                timeIndex++;
                dynamicArray = Arrays.asList(dynamicScanner.nextLine().split(" "));
                particlesPerTime.add(new HashMap<>());
            }

            final Map<Particle, State> currentParticlesPerTime = particlesPerTime.get(timeIndex);
            final Particle currentParticle = particles.get(particleIndex);

            final State currentParticleState = new State(
                    new Position(
                            Double.parseDouble(dynamicArray.get(0)),    //En primera posicion tengo X
                            Double.parseDouble(dynamicArray.get(1))     //En segunda posicion tengo Y
                    ),
                    Double.parseDouble(dynamicArray.get(2)),    //En tercera posicion tengo la velocidad
                    Double.parseDouble(dynamicArray.get(3))     //En cuarta posicion tengo el angulo
            );

            currentParticlesPerTime.put(currentParticle, currentParticleState);
            particleIndex++;
        }

        dynamicScanner.close();
        return new ParticlesParserResult(N, L, particlesPerTime);
    }

}
