package ar.edu.itba.models;

import java.util.Objects;
import java.lang.Math;

public class Particle implements Bounceable {

    private final int id;
    private final double radius;
    private final double mass;
    protected Position position;
    protected double vx;
    protected double vy;

    public Particle(int id, double radius, double mass) {
        this.id = id;
        this.radius = radius;
        this.mass = mass;
    }

    public int getId() {
        return this.id;
    }
    public double getRadius() {
        return this.radius;
    }
    public double getMass() {
        return this.mass;
    }
    public Position getPosition() {
        return this.position;
    }
    public void setPosition(Position position) {
        this.position = position;
    }
    public double getX() {
        return this.position.getX();
    }
    public void setX(double x) {
        this.position.setX(x);
    }
    public double getY() {
        return this.position.getY();
    }
    public void setY(double y) {
        this.position.setY(y);
    }
    public double getVx() {
        return vx;
    }
    public void setVx(double vx) {
        this.vx = vx;
    }
    public double getVy() {
        return vy;
    }
    public void setVy(double vy) {
        this.vy = vy;
    }

    public void moveForwardInTime(double deltaTime) {
        double newX = this.getX() + this.vx * deltaTime;
        double newY = this.getY() + this.vy * deltaTime;
        this.setPosition(new Position(newX, newY));
    }

    @Override
    public void collide(Particle p) {
        double deltaRx = p.getX() - this.getX();
        double deltaRy = p.getY() - this.getY();
        double deltaVx = p.getVx() - this.getVx();
        double deltaVy = p.getVy() - this.getVy();

        double sigma = this.radius + p.getRadius();
        double dvdr = deltaRx * deltaVx + deltaRy * deltaVy;
        double J = (2 * this.mass * p.getMass() * dvdr) / (sigma * (this.mass + p.getMass()));
        double Jx = (J * deltaRx) / sigma;
        double Jy = (J * deltaRy) / sigma;

        this.setVx(this.vx + (Jx / this.mass));
        this.setVy(this.vy + (Jy / this.mass));
        p.setVx(p.getVx() - (Jx / p.getMass()));
        p.setVy(p.getVy() - (Jy / p.getMass()));
    }

    @Override
    public double getCollisionTime(Particle p) {
        double deltaRx = this.getX() - p.getX();
        double deltaRy = this.getY() - p.getY();
        double deltaVx = this.getVx() - p.getVx();
        double deltaVy = this.getVy() - p.getVy();

        double sigma = this.radius + p.getRadius();

        double dvdr = (deltaRx * deltaVx) + (deltaRy * deltaVy);
        if (dvdr >= 0) {
            return Double.MAX_VALUE;
        }

        double dvdv = (deltaVx * deltaVx) + (deltaVy * deltaVy);
        double drdr = (deltaRx * deltaRx) + (deltaRy * deltaRy);
        double d = Math.pow(dvdr, 2) - dvdv * (drdr - Math.pow(sigma, 2));
        if (d < 0) {
            return Double.MAX_VALUE;
        }

        return - (dvdr + Math.sqrt(d)) / dvdv;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Particle particle = (Particle) o;
        return id == particle.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Particle{" + "id=" + id + ", radius=" + radius + ", mass=" + mass + ", position=" + position + ", vx=" + vx + ", vy=" + vy + '}';
    }
}