package ar.edu.itba.models;

import java.util.ArrayList;
import java.util.List;

public class Container {
    // Constantes
    private static final double A = 0.15;
    private static final double GRAVITY = -5;
    private static final double DIM_X = 20;
    private static final double DIM_Y = 77;
    private static final int COLS = 8;
    private static final int INNER_ROWS = 30;
    private static final int TOTAL_ROWS = 33;
    private static final double CELL_DIMENSION_Y = DIM_Y / (double) TOTAL_ROWS;
    private static final double CELL_DIMENSION_X = DIM_X / (double) COLS;

    public static final double K_NORMAL = 250;
    public static final double GAMMA = 2.5;
    public static final double U = 0.1;
    public static final double K_TAN = 2 * K_NORMAL;

    // Versores
    private static final DoublePair FLOOR_VERSOR = new DoublePair(0.0, -1.0);
    private static final DoublePair TOP_VERSOR = new DoublePair(0.0, 1.0);
    private static final DoublePair LEFT_VERSOR = new DoublePair(-1.0, 0.0);
    private static final DoublePair RIGHT_VERSOR = new DoublePair(1.0, 0.0);

    // Limites del container
    private final double topRightCornerX;
    private double topRightCornerY;
    private final double bottomLeftCornerX;
    private double bottomLeftCornerY;

    private final double topRightLimitInitialY;
    private final double bottomLeftLimitInitialY;
    private final double leftLimitHole;
    private final double rightLimitHole;
    private double movement;

    private final Cell[][] cells;

    public Container(double topRightCornerX, double topRightCornerY, double bottomLeftCornerX, double bottomLeftCornerY, double holeSize) {
        cells = new Cell[TOTAL_ROWS][COLS];
        for (int row = 0; row < TOTAL_ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                cells[row][col] = new Cell();
            }
        }
        this.bottomLeftCornerX = bottomLeftCornerX;
        this.bottomLeftCornerY = bottomLeftCornerY;

        this.topRightCornerX = topRightCornerX;
        this.topRightCornerY = topRightCornerY;

        this.bottomLeftLimitInitialY = bottomLeftCornerY;
        this.topRightLimitInitialY = topRightCornerY;
        leftLimitHole = topRightCornerX / 2 - holeSize / 2;
        rightLimitHole = topRightCornerX / 2 + holeSize / 2;
    }

    public void shake(double t, double w) {
        movement = A * Math.sin(w * t);
        bottomLeftCornerY = bottomLeftLimitInitialY + movement;
        topRightCornerY = topRightLimitInitialY + movement;
    }

    public void add(Particle particle) {
        Cell cell = getCell(particle.getPosition().getOne(), particle.getPosition().getOther());
        if (cell != null) {
            cell.add(particle);
        } else throw new IllegalStateException("Cell does not exists");
    }

    public void addAll(List<Particle> particles) {
        particles.forEach(this::add);
    }

    private boolean outsideHole(Particle particle) {
        return particle.getPosition().getOne() < leftLimitHole || particle.getPosition().getOne() > rightLimitHole;
    }

    public void updateForces(double dt) {
        for (int row = 0; row < TOTAL_ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                List<Particle> neighbours = getNeighbours(row, col);
                List<Particle> current = cells[row][col].getParticles();

                current.forEach(
                        p -> {
                            // Add gravity
                            p.addForces(0.0, p.getMass() * GRAVITY);

                            current.forEach(n -> {
                                double diff = p.getPosition().module(n.getPosition());
                                double sumRad = p.getRadius() + n.getRadius();

                                if (diff < sumRad && !n.equals(p)) {

                                    DoublePair normalVersor = n.getPosition().subtract(p.getPosition()).scale(1.0 / diff);
                                    DoublePair auxVelocity = p.getVelocity().subtract(n.getVelocity());
                                    double superpositionB = auxVelocity.getOne() + auxVelocity.getOther();
                                    p.addForces(getNormalForce(sumRad - diff, normalVersor, superpositionB));

                                    DoublePair relativeVelocity = p.getVelocity().subtract(n.getVelocity());
                                    DoublePair aux = p.getAccumRelativeVelocity().get(n);
                                    DoublePair accum;
                                    if ( aux == null){
                                        accum = relativeVelocity;
                                    } else {
                                        accum = aux.sum(relativeVelocity);
                                    }
                                    p.setAccumRelativeVelocity(accum,n) ;
                                    p.addForces(getTangentialForce(sumRad - diff, p.getAccumRelativeVelocity().get(n), normalVersor, superpositionB, dt));
                                } else {
                                    p.setAccumRelativeVelocity(new DoublePair(0.0, 0.0), n);
                                }
                            });

                            // Add particle forces
                            neighbours.forEach(
                                    n -> {

                                        double diff = p.getPosition().module(n.getPosition());
                                        double superposition = p.getRadius() + n.getRadius() - diff;

                                        if (superposition > 0.0 && !n.equals(p)) {

                                            DoublePair normalVersor = n.getPosition().subtract(p.getPosition()).scale(1.0 / diff);

                                            DoublePair auxVelocity = p.getVelocity().subtract(n.getVelocity());
                                            double superpositionB = auxVelocity.getOne() + auxVelocity.getOther();
                                            DoublePair normalForce = getNormalForce(superposition, normalVersor, superpositionB);

                                            p.addForces(normalForce);
                                            n.addForces(normalForce.scale(-1.0));

                                            DoublePair relativeVelocity = p.getVelocity().subtract(n.getVelocity());

                                            DoublePair tangentialForce = getTangentialForce(superposition, relativeVelocity, normalVersor, superpositionB, dt);

                                            p.addForces(tangentialForce);
                                            n.addForces(tangentialForce.scale(-1.0));
                                        } else {
                                            p.setAccumRelativeVelocity(new DoublePair(0.0, 0.0), n);
                                        }
                                    }
                            );
                        }
                );

                if (row <= (TOTAL_ROWS - INNER_ROWS)) //pared inferior con el agujero
                    updateFloorForce(current, dt);

                if (row == TOTAL_ROWS - 1)
                    updateTopForce(current, dt);

                if (col == 0)
                    updateLeftForce(current, dt);

                if (col == COLS - 1)
                    updateRightForce(current, dt);

            }
        }
    }

    private void updateFloorForce(List<Particle> particles, double dt) {
        particles.forEach(p -> {
            if (outsideHole(p) && !p.isGone())
                floorForce(p, dt);
        });
    }
    private void floorForce(Particle p, double dt){
        double superposition = p.getRadius() - (p.getPosition().getOther() - bottomLeftCornerY);
        if (superposition > 0.0) {
            DoublePair auxVelocity = p.getVelocity();
            p.setFloorRelativeVelocity(p.getFloorRelativeVelocity().sum(p.getVelocity()));
            double superpositionB = auxVelocity.getOne() + auxVelocity.getOther();;
            p.addForces(
                    getWallForce(superposition, p.getFloorRelativeVelocity(), FLOOR_VERSOR, superpositionB, dt)
            );
        } else {
            p.setFloorRelativeVelocity(new DoublePair(0.0, 0.0));
        }
    }

    private void updateTopForce(List<Particle> particles, double dt) {
        for (Particle p : particles) {
            topForce(p, dt);
        }
    }

    private void topForce(Particle p, double dt){
        double superposition = p.getRadius() - (topRightCornerY - p.getPosition().getOther());
        if (superposition > 0.0) {
            DoublePair auxVelocity = p.getVelocity();
            double superpositionB = auxVelocity.getOne() + auxVelocity.getOther();
            p.addForces(
                    getWallForce(superposition, p.getVelocity(), TOP_VERSOR, superpositionB, dt)
            );
        }
    }

    private void updateLeftForce(List<Particle> particles, double dt) {
        for (Particle p : particles) {
            leftForce(p, dt);
        }
    }
    private void leftForce(Particle p, double dt){
        double superposition = p.getRadius() - (p.getPosition().getOne() - bottomLeftCornerX);
        if (superposition > 0.0) {
            DoublePair auxVelocity = p.getVelocity();
            p.setLeftRelativeVelocity(p.getLeftRelativeVelocity().sum(p.getVelocity()));
            double superpositionB = auxVelocity.getOne() + auxVelocity.getOther();
            p.addForces(
                    getWallForce(superposition, p.getLeftRelativeVelocity(), LEFT_VERSOR, superpositionB, dt)
            );
        } else {
            p.setLeftRelativeVelocity(new DoublePair(0.0, 0.0));
        }
    }

    private void updateRightForce(List<Particle> particles, double dt) {
        for (Particle p : particles) {
            rightForce(p, dt);
        }
    }
    private void rightForce(Particle p, double dt){
        double superposition = p.getRadius() - (topRightCornerX - p.getPosition().getOne());
        if (superposition > 0.0) {
            DoublePair auxVelocity = p.getVelocity();
            p.setRightRelativeVelocity(p.getRightRelativeVelocity().sum(p.getVelocity()));
            double superpositionB = auxVelocity.getOne() + auxVelocity.getOther();
            p.addForces(
                    getWallForce(superposition, p.getVelocity(), RIGHT_VERSOR, superpositionB, dt)
            );
        } else {
            p.setRightRelativeVelocity(new DoublePair(0.0, 0.0));
        }
    }

    private List<Particle> getNeighbours(int row, int col) {
        List<Particle> particles = new ArrayList<>();

        if (row < TOTAL_ROWS - 1)
            particles.addAll(cells[row + 1][col].getParticles());

        if (row < TOTAL_ROWS - 1 && col < COLS - 1)
            particles.addAll(cells[row + 1][col + 1].getParticles());

        if (col < COLS - 1)
            particles.addAll(cells[row][col + 1].getParticles());

        if (row > 0 && col < COLS - 1)
            particles.addAll(cells[row - 1][col + 1].getParticles());


        return particles;
    }

    private List<Particle> getAllNeighbours(int row, int col) {
        List<Particle> particles = new ArrayList<>();

        int[][] diff = {
                {0, 0}, {0, 1}, {0, -1}, {1, 0}, {1, 1}, {1, -1}, {-1, 0}, {-1, 1}, {-1, -1}
        };

        for (int[] a : diff) {
            try {
                particles.addAll(
                        cells[row + a[0]][col + a[1]].getParticles()
                );
            } catch (IndexOutOfBoundsException ignored) {
            }
        }

        return particles;
    }

    public int update() {
        int goneParticles = 0;
        for (int i = 0; i < TOTAL_ROWS; i++) {
            for (int j = 0; j < COLS; j++) {

                for (int k = 0; k < cells[i][j].getParticles().size(); k++) {
                    if (!updateParticleCell(cells[i][j].getParticles().get(k), i, j)){
                        goneParticles++;
                    }
                }

            }
        }
        return goneParticles;
    }

    private Cell getCell(double x, double y) {
        if (x >= DIM_X || x < 0 || y < 0 || y >= DIM_Y)
            throw new IllegalStateException();
        int row = getIndexY(y);
        int col = getIndexX(x);
        return cells[row][col];
    }

    private int getIndexX(double value) {
        return (int) (value / CELL_DIMENSION_X);
    }

    private int getIndexY(double value) {
        return (int) (value / CELL_DIMENSION_Y);
    }

    private boolean moveFromCell(Particle particle, int row, int col, int newRow, int newCol) {
        try {
            if (newRow < 0) {
                particle.reInject();
                cells[row][col].remove(particle);

                boolean overlap;
                int c, r;
                do {
                    overlap = false;
                    particle.getPosition().setOne(particle.getRadius() + Math.random() * (DIM_X - 2.0 * particle.getRadius()));
                    particle.getPosition().setOther(40 + 70 / 10 + Math.random() * ((70 - 40) - particle.getRadius()));
                    c = getIndexX(particle.getPosition().getOne());
                    r = getIndexY(particle.getPosition().getOther());


                    for (Particle existingParticle : getAllNeighbours(r, c)) {
                        if (particle.isOverlapping(existingParticle))
                            overlap = true;
                    }
                } while (overlap);

                cells[r][c].add(particle);
                particle.setGone(false);

                return false;
            } else {
                cells[newRow][newCol].add(particle);
                cells[row][col].remove(particle);
                return true;
            }
        } catch (IndexOutOfBoundsException e) {
            return true;
        }
    }

    private boolean updateParticleCell(Particle particle, int row, int col) {

        DoublePair inferiorLimit = new DoublePair(((double) col) * CELL_DIMENSION_X, ((double) row) * CELL_DIMENSION_Y + movement);
        DoublePair superiorLimit = new DoublePair(((double) (col + 1)) * CELL_DIMENSION_X, ((double) (row + 1)) * CELL_DIMENSION_Y + movement);

        if(!particle.isGone() && !outsideHole(particle) && particle.getPosition().getOther() < bottomLeftCornerY)
            particle.setGone(true);

        DoublePair inferiorDiff = particle.getPosition().subtract(inferiorLimit);
        DoublePair superiorDiff = particle.getPosition().subtract(superiorLimit);

        return moveFromCell(particle, row, col,
                inferiorDiff.getOther() < 0 ? row - 1 : superiorDiff.getOther() >= 0 ? row + 1 : row,
                inferiorDiff.getOne() < 0 ? col - 1 : superiorDiff.getOne() >= 0 ? col + 1 : col
        );

    }

    public static double getNormalForce(double superpositionA, double superpositionB) {
        return -K_NORMAL * (superpositionA) - GAMMA * (superpositionB);
    }

    public static DoublePair getNormalForce(double superpositionA, DoublePair versor, double superpositionB) {

        double force = getNormalForce(superpositionA, superpositionB);

        return versor.scale(force);
    }

    public static double getTangentialForce(double superpositionA, double relativeTangentialVelocity, double superpositionB, double dt) {
        double res1 = - U * Math.abs(getNormalForce(superpositionA, superpositionB)) * Math.signum(relativeTangentialVelocity);
        double res2 = -K_TAN  * (relativeTangentialVelocity * dt);
        return Math.min(res1, res2);
    }

    public static DoublePair getTangentialForce(double superpositionA, DoublePair relativeTangentialVelocity, DoublePair normalVersor, double superpositionB, double dt) {

        DoublePair tan = new DoublePair(-normalVersor.getOther(), normalVersor.getOne());

        double force = getTangentialForce(superpositionA, relativeTangentialVelocity.dot(tan), superpositionB, dt);

        return tan.scale(force);
    }

    public static DoublePair getWallForce(double superpositionA, DoublePair relativeTangentialVelocity, DoublePair normalVersor, double superpositionB, double dt) {

        DoublePair tan = new DoublePair(-normalVersor.getOther(), normalVersor.getOne());

        double forceT = getTangentialForce(superpositionA, relativeTangentialVelocity.dot(tan), superpositionB, dt);
        double forceN = getNormalForce(superpositionA, superpositionB);
        return normalVersor.scale(forceN).sum(tan.scale(forceT));
    }

}
