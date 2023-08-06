package ar.edu.itba.CellIndexMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ar.edu.itba.CellIndexMethod.Particle.Position;

public class Grid {
    private final List<List<Cell>> grid;
    private final Long M;
    private final boolean periodic;

    public Grid(int L, Long M, Map<Particle, Position> particles, Boolean periodic) {
        this.M = M;
        this.periodic = periodic;
        this.grid = new ArrayList<>();
        double increment = L / (double) M;
        buildAndFillGrid(particles, increment);
        setCellNeighbors();
    }

    public List<List<Cell>> getGrid() {
        return grid;
    }

    private Cell getCellInGrid(int y, int x) {
        return this.grid.get(y).get(x);
    }

    private void buildAndFillGrid(Map<Particle, Position> particles, double increment) {
        double leftBoundary;
        double rightBoundary;
        double bottomBoundary = 0;
        double topBoundary = increment;

        for (int y = 0; y < M; y++) {
            leftBoundary = 0;
            rightBoundary = leftBoundary + increment;
            this.grid.add(y, new ArrayList<>());
            for (int x = 0; x < M; x++) {
                this.grid.get(y).add(x, new Cell(x, y, leftBoundary, rightBoundary, topBoundary, bottomBoundary));
                leftBoundary += increment;
                rightBoundary += increment;
            }
            topBoundary += increment;
            bottomBoundary += increment;
        }

        for (Map.Entry<Particle, Position> entry : particles.entrySet()) {
            final int idx = (int) Math.floor(entry.getValue().getX() / increment);
            final int idy = (int) Math.floor(entry.getValue().getY() / increment);
            Cell cell = getCellInGrid(idy, idx);
            cell.addParticle(entry.getKey());
        }

    }

    private void setCellNeighbors() {
        for (int y = 0; y < M; y++) {
            for (int x = 0; x < M; x++) {

                final Cell currentCell = getCellInGrid(y, x);
                Cell topCell = getCellInGrid((int) ((y + 1) % M), x);
                Cell topRightCell = getCellInGrid((int) ((y + 1) % M), (int) ((x + 1) % M));
                Cell bottomRightCell = getCellInGrid((y - 1) < 0 ? (int) (M - 1) : y - 1, (int) ((x + 1) % M));
                Cell rightCell = getCellInGrid(y, (int) ((x + 1) % M));

                if (!this.periodic) {
                    if (y + 1 >= M) {
                        topCell = null;
                        topRightCell = null;
                    }
                    if (y - 1 < 0) {
                        bottomRightCell = null;
                    }
                    if (x + 1 >= M) {
                        topRightCell = null;
                        rightCell = null;
                        bottomRightCell = null;
                    }
                } else {
                    if (y == M - 1 && x == M - 1) {
                        //Esquina arriba a la derecha
                        topRightCell = getCellInGrid(0, 0);
                    } else if (y == 0 && x == M - 1) {
                        //Esquina abajo a la derecha
                        bottomRightCell = getCellInGrid((int) (M - 1), 0);
                    }
                }
                currentCell.setNeighbors(topCell, topRightCell, rightCell, bottomRightCell);
            }
        }
    }

}
