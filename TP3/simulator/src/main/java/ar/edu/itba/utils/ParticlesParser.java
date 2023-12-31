package ar.edu.itba.utils;

import ar.edu.itba.models.Particle;
import ar.edu.itba.models.Position;

import java.io.File;
import java.io.IOException;
import java.util.*;

import java.lang.Double;
import java.lang.Integer;

public class ParticlesParser {

    public static ParticlesParserResult parseParticlesList(File staticFile, File dynamicFile) throws IOException {
        final Scanner staticScanner = new Scanner(staticFile);
        final Scanner dynamicScanner = new Scanner(dynamicFile);

        final int N = Integer.parseInt(staticScanner.nextLine().split(" ")[0]);
        final double side = Double.parseDouble(staticScanner.nextLine().split(" ")[0]);

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
        final List<Set<Particle>> particlesPerTime = new ArrayList<>();

        while (dynamicScanner.hasNextLine()) {
            List<String> dynamicArray = Arrays.asList(dynamicScanner.nextLine().split(" "));

            //Me fijo si paso al siguiente tiempo
            if (dynamicArray.size() == 1) {
                particleIndex = 0;
                timeIndex++;
                dynamicArray = Arrays.asList(dynamicScanner.nextLine().split(" "));
                particlesPerTime.add(new HashSet<>());
            }

            final Set<Particle> currentParticlesPerTime = particlesPerTime.get(timeIndex);
            final Particle currentParticle = particles.get(particleIndex);

            final double x = Double.parseDouble(dynamicArray.get(0));     //En primera posicion tengo X
            final double y = Double.parseDouble(dynamicArray.get(1));     //En segunda posicion tengo Y
            currentParticle.setPosition(new Position(x, y));

            final double speed = Double.parseDouble(dynamicArray.get(2));     //En tercera posicion tengo la velocidad
            final double angle = Double.parseDouble(dynamicArray.get(3));     //En cuarta posicion tengo el angulo
            currentParticle.setVx(speed * Math.cos(angle));
            currentParticle.setVy(speed * Math.sin(angle));

            currentParticlesPerTime.add(currentParticle);
            particleIndex++;
        }

        dynamicScanner.close();
        return new ParticlesParserResult(N, side, particlesPerTime);
    }

}
