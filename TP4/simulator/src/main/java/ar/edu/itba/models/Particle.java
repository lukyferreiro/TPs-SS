package ar.edu.itba.models;

import ar.edu.itba.algorithms.utils.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.lang.Math;

import static ar.edu.itba.algorithms.utils.R.values.*;

public class Particle {

    private final int id;
    private final double radius;
    private final double mass;
    private Position position;
    private double vx;
    private double vy;
    private double u;
    private R r;

    public Particle(int id, double radius, double mass) {
        this.id = id;
        this.radius = radius;
        this.mass = mass;
        this.r = new R();
        this.r.add(0.0,0.0);
        this.r.add(0.0,0.0);
        this.r.add(0.0,0.0);
        this.r.add(0.0,0.0);
        this.r.add(0.0,0.0);
        this.r.add(0.0,0.0);
        this.r.add(0.0,0.0);
    }

    public Particle(int id, double radius, double mass, double x, double y, double vx, double vy, double u, R r) {
        this.id = id;
        this.radius = radius;
        this.mass = mass;
        this.position = new Position(x, y);
        this.vx = vx;
        this.vy = vy;
        this.u = u;
        this.r = new R();
        this.r.add(r.get(R0.ordinal()).getOne(), r.get(R0.ordinal()).getOther());
        this.r.add(r.get(R1.ordinal()).getOne(), r.get(R1.ordinal()).getOther());
        this.r.add(r.get(R2.ordinal()).getOne(), r.get(R2.ordinal()).getOther());
        this.r.add(r.get(R3.ordinal()).getOne(), r.get(R3.ordinal()).getOther());
        this.r.add(r.get(R4.ordinal()).getOne(), r.get(R4.ordinal()).getOther());
        this.r.add(r.get(R5.ordinal()).getOne(), r.get(R5.ordinal()).getOther());
        this.r.add(r.get(R6_NO_PERIODIC.ordinal()).getOne(), r.get(R6_NO_PERIODIC.ordinal()).getOther());
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
    public R getR() {
        return this.r;
    }
    public Double getFromR(int index) {
        return this.r.get(index).getOne();
    }
    public void setR(R r) {
        this.r = new R();
        this.r.add(r.get(R0.ordinal()).getOne(), r.get(R0.ordinal()).getOther());
        this.r.add(r.get(R1.ordinal()).getOne(), r.get(R1.ordinal()).getOther());
        this.r.add(r.get(R2.ordinal()).getOne(), r.get(R2.ordinal()).getOther());
        this.r.add(r.get(R3.ordinal()).getOne(), r.get(R3.ordinal()).getOther());
        this.r.add(r.get(R4.ordinal()).getOne(), r.get(R4.ordinal()).getOther());
        this.r.add(r.get(R5.ordinal()).getOne(), r.get(R5.ordinal()).getOther());
        this.r.add(r.get(R6_NO_PERIODIC.ordinal()).getOne(), r.get(R6_NO_PERIODIC.ordinal()).getOther());
    }

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