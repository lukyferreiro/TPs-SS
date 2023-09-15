package ar.edu.itba.models;

import java.util.Objects;
import java.lang.Math;

public class Particle {

    private final int id;
    private final double radius;
    private final double mass;
    private Position position;
    private double vx;
    private double vy;

    public Particle(int id, double radius, double mass) {
        this.id = id;
        this.radius = radius;
        this.mass = mass;
    }

    public int getId() {
        return id;
    }
    public double getRadius() {
        return radius;
    }
    public double getMass() {
        return mass;
    }
    public Position getPosition() {
        return position;
    }
    public void setPosition(Position position) {
        this.position = position;
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

    public void moveForwardInTime(double delta) {
        double newX = this.getPosition().getX() + this.vx * delta;
        double newY = this.getPosition().getY() + this.vy * delta;
        this.setPosition(new Position(newX, newY));
    }

    public void collide(Particle p) {
        double deltaRx = p.getPosition().getX() - this.getPosition().getX();
        double deltaRy = p.getPosition().getY() - this.getPosition().getY();
        double deltaVx = p.getVx() - this.getVx();
        double deltaVy = p.getVx() - this.getVy();

        double omega = this.radius + p.getRadius();
        double J = (2 * this.mass * p.getMass() * (deltaRx * deltaVx + deltaRy * deltaVy)) / (omega * 2);
        double Jx = J * deltaRx / omega;
        double Jy = J * deltaRy / omega;

        this.setVx(this.vx + (Jx / this.mass));
        this.setVy(this.vy + (Jy / this.mass));
        p.setVx(p.vx + (Jx / p.mass));
        p.setVy(p.vy + (Jy / p.mass));
    }


    public double getCollisionTime(Particle p) {
        double deltaRx = this.getPosition().getX() - p.getPosition().getX();
        double deltaRy = this.getPosition().getY() - p.getPosition().getY();
        double deltaVx = this.getVx() - p.getVx();
        double deltaVy = this.getVx() - p.getVy();

        double omega = this.radius + p.getRadius();

        double dotProductDvDr = deltaRx * deltaVx + deltaRy * deltaVy;
        if (dotProductDvDr >= 0) {
            return Double.MAX_VALUE;
        }
        double dotProductDvDv = deltaVx * deltaVx + deltaVy * deltaVy;
        double dotProductDrDr = deltaRx * deltaRx + deltaRy * deltaRy;
        double d = Math.pow(dotProductDvDr, 2) - dotProductDvDv * (dotProductDrDr - Math.pow(omega, 2));
        if (d < 0) {
            return Double.MAX_VALUE;
        }

        return - (dotProductDvDr + Math.sqrt(d))/dotProductDvDv;
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


}