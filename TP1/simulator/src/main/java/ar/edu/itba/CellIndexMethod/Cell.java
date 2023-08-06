package ar.edu.itba.CellIndexMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Cell {
    private final int cellX;
    private final int cellY;
    private final double leftBoundary;
    private final double rightBoundary;
    private final double topBoundary;
    private final double bottomBoundary;
    private Cell topCell;
    private Cell topRightCell;
    private Cell rightCell;
    private Cell bottomRightCell;
    private final List<Particle> particles = new ArrayList<>();

    public Cell(int cellX, int cellY, double leftBoundary, double rightBoundary, double topBoundary, double bottomBoundary) {
        this.cellX = cellX;
        this.cellY = cellY;
        this.leftBoundary = leftBoundary;
        this.rightBoundary = rightBoundary;
        this.topBoundary = topBoundary;
        this.bottomBoundary = bottomBoundary;
    }

    public boolean isEmpty() {
        return particles.isEmpty();
    }

    public List<Particle> getParticles() {
        return particles;
    }

    public void addParticle(final Particle particle) {
        particles.add(particle);
    }

    public double getLeftBoundary() {
        return leftBoundary;
    }

    public double getRightBoundary() {
        return rightBoundary;
    }

    public double getTopBoundary() {
        return topBoundary;
    }

    public double getBottomBoundary() {
        return bottomBoundary;
    }

    public int getCellX() {
        return cellX;
    }

    public int getCellY() {
        return cellY;
    }

    public Cell getTopCell() {
        return topCell;
    }

    public Cell getTopRightCell() {
        return topRightCell;
    }

    public Cell getRightCell() {
        return rightCell;
    }

    public Cell getBottomRightCell() {
        return bottomRightCell;
    }

    public void setNeighbors(Cell topCell, Cell topRightCell, Cell rightCell, Cell bottomRightCell) {
        this.topCell = topCell;
        this.topRightCell = topRightCell;
        this.rightCell = rightCell;
        this.bottomRightCell = bottomRightCell;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return cellX == cell.cellX && cellY == cell.cellY;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cellX, cellY);
    }
}
