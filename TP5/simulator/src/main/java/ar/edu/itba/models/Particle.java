package ar.edu.itba.models;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.lang.Math;

public class Particle {
    // Constantes
    private static final double GRAVITY = -5;
    private final static double B = (2.0 / 3.0);
    private final static double C = -(1.0 / 6.0);
    // Velocidades
    private Map<Particle, DoublePair> accumRelativeVelocity = new HashMap<>();
    private DoublePair floorRelativeVelocity = new DoublePair(0.0, 0.0);
    private DoublePair rightRelativeVelocity = new DoublePair(0.0, 0.0);
    private DoublePair leftRelativeVelocity = new DoublePair(0.0, 0.0);
    private DoublePair topRelativeVelocity = new DoublePair(0.0, 0.0);
    // Propiedades de la partícula
    private final Double radius;
    private final Double mass;
    private final int id;
    private DoublePair position;
    private DoublePair velocity;
    // Para estimar
    private Double xForce;
    private Double yForce;
    private DoublePair prevA;
    private DoublePair currentA;
    private DoublePair currentV;
    private boolean gone = false;
    private boolean reInjected = false;

    public Particle(int id, Double radius, Double mass) {
        this.id = id;
        this.radius = radius;
        this.mass = mass;
        this.xForce = 0.0;
        this.yForce = 0.0;
        this.velocity = new DoublePair(0.0, 0.0);
        currentA = new DoublePair(0.0, 0.0);
        prevA = new DoublePair(0.0, GRAVITY);
    }

    public Particle(int id, DoublePair position, Double radius, Double mass) {
        this.id = id;
        this.position = position;
        this.radius = radius;
        this.mass = mass;
        this.xForce = 0.0;
        this.yForce = 0.0;
        this.velocity = new DoublePair(0.0, 0.0);
        currentA = new DoublePair(0.0, 0.0);
        prevA = new DoublePair(0.0, GRAVITY);
    }

    public void resetForces() {
        xForce = 0.0;
        yForce = 0.0;
    }

    public void addForces(double x, double y) {
        xForce = xForce + x;
        yForce = yForce + y;
    }

    public void addForces(DoublePair pair) {
        xForce = xForce + pair.getOne();
        yForce = yForce + pair.getOther();
    }

    public Particle copy() {
        return new Particle(id, position, radius, mass);
    }

    public DoublePair getAcceleration() {
        DoublePair aux = new DoublePair(xForce, yForce);
        return aux.scale(1.0 / mass);
    }

    public void reInject() {
        reInjected = true;
    }

    public int getId() {
        return id;
    }

    public Double getRadius() {
        return radius;
    }

    public Double getMass() {
        return mass;
    }

    public DoublePair getPosition() {
        return position;
    }

    public void setPosition(DoublePair position) {
        this.position = position;
    }

    public DoublePair getVelocity() {
        return velocity;
    }

    public boolean isGone() {
        return gone;
    }

    public void setGone(boolean gone) {
        this.gone = gone;
    }

    public void predict(Double dt) {
        currentA = this.getAcceleration();
        this.position = position.sum(velocity.scale(dt).sum(currentA.scale(B).sum(prevA.scale(C)).scale(Math.pow(dt, 2))));
        this.currentV = velocity;
        this.velocity = this.currentV.sum(this.currentA.scale(1.5 * dt).sum(prevA.scale(-0.5 * dt)));
    }

    public void correct(Double dt) {
        if (reInjected) {
            this.velocity = new DoublePair(0.0, 0.0);
            reInjected = false;
            prevA = new DoublePair(0.0, GRAVITY);
        } else {
            this.velocity = currentV.sum(this.getAcceleration().scale((1.0 / 3.0) * dt).sum(currentA.scale((5.0 / 6.0) * dt).sum(prevA.scale(-(1.0 / 6.0) * dt))));
            prevA = currentA;
        }
    }

    public boolean isOverlapping(Particle other) {
        return position.module(other.getPosition()) < (this.getRadius() + other.getRadius());
    }

    public Map<Particle, DoublePair> getAccumRelativeVelocity() {
        return accumRelativeVelocity;
    }

    public void setAccumRelativeVelocity(DoublePair velocity, Particle particle) {
        this.accumRelativeVelocity.putIfAbsent(particle, velocity);
    }

    public void setFloorRelativeVelocity(DoublePair floorRelativeVelocity) {
        this.floorRelativeVelocity = floorRelativeVelocity;
    }

    public void setRightRelativeVelocity(DoublePair rightRelativeVelocity) {
        this.rightRelativeVelocity = rightRelativeVelocity;
    }

    public void setLeftRelativeVelocity(DoublePair leftRelativeVelocity) {
        this.leftRelativeVelocity = leftRelativeVelocity;
    }

    public void setTopRelativeVelocity(DoublePair topRelativeVelocity) {
        this.topRelativeVelocity = topRelativeVelocity;
    }

    public DoublePair getFloorRelativeVelocity() {
        return floorRelativeVelocity;
    }

    public DoublePair getRightRelativeVelocity() {
        return rightRelativeVelocity;
    }

    public DoublePair getLeftRelativeVelocity() {
        return leftRelativeVelocity;
    }

    public DoublePair getTopRelativeVelocity() {
        return topRelativeVelocity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Particle particle = (Particle) o;
        return id == particle.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Particle{" + "id=" + id + ", radius=" + radius + ", mass=" + mass + ", position=" + position + ", vx=" + velocity.getOne() + ", vy=" + velocity.getOther() + '}';
    }
}