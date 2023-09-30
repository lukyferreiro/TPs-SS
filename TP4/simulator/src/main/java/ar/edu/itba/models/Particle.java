package ar.edu.itba.models;

import java.util.Objects;
import java.lang.Math;

//public class Particle implements Bounceable {
public class Particle {

    private final int id;
    private final double radius;
    private final double mass;
    protected Position position;
    protected double vx;
    protected double vy;
    protected double u;

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
    public void setX(double x, double L){
        double aux = x % L;
        if (aux < 0){
            aux += L;
        }
        this.position.setX(aux);
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
    public double getU() {
        return u;
    }
    public void setU(double u) {
        this.u = u;
    }

//    public void moveForwardInTime(double deltaTime) {
//        double newX = this.getX() + this.vx * deltaTime;
//        double newY = this.getY() + this.vy * deltaTime;
//        this.setPosition(new Position(newX, newY));
//    }
//
//    @Override
//    public void collide(Particle p) {
//        double deltaRx = p.getX() - this.getX();
//        double deltaRy = p.getY() - this.getY();
//        double deltaVx = p.getVx() - this.getVx();
//        double deltaVy = p.getVy() - this.getVy();
//
//        double sigma = this.radius + p.getRadius();
//        double dvdr = deltaRx * deltaVx + deltaRy * deltaVy;
//        double J = (2 * this.mass * p.getMass() * dvdr) / (sigma * (this.mass + p.getMass()));
//        double Jx = (J * deltaRx) / sigma;
//        double Jy = (J * deltaRy) / sigma;
//
//        this.setVx(this.vx + (Jx / this.mass));
//        this.setVy(this.vy + (Jy / this.mass));
//        p.setVx(p.getVx() - (Jx / p.getMass()));
//        p.setVy(p.getVy() - (Jy / p.getMass()));
//    }
//
    public boolean collidesWith(Particle p, Double dt) {
        double deltaRx = this.getX() - p.getX();
        double deltaVx = this.getVx() - p.getVx();

        double sigma = this.radius + p.getRadius();

        double dvdr = (deltaRx * deltaVx);
        if (dvdr >= 0) {
            return false;
        }

        double dvdv = (deltaVx * deltaVx);
        double drdr = (deltaRx * deltaRx);
        double d = Math.pow(dvdr, 2) - dvdv * (drdr - Math.pow(sigma, 2));
        if (d < 0) {
            return false;
        }

        return (-(dvdr + Math.sqrt(d)) / dvdv ) < dt;
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