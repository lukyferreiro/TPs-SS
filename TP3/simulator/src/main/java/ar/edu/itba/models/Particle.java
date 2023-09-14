package ar.edu.itba.models;

import java.util.Objects;
import java.lang.Math;

public class Particle {

    private final int id;
    private final double radius;
    private final double mass;
    private double x;
    private double y;
    private double speed;
    private double angle;

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
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public void setX(double x) {
        this.x = x;
    }
    public void setY(double y) {
        this.y = y;
    }
    public double getSpeed() {
        return speed;
    }
    public void setSpeed(double speed) {
        this.speed = speed;
    }
    public double getAngle() {
        return angle;
    }
    public void setAngle(double angle) {
        this.angle = angle;
    }
    public double getVelocityX() {
        return Math.cos(angle) * speed;
    }
    public double getVelocityY() {
        return Math.sin(angle) * speed;
    }

    public static double calculateDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
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
        return "Particle{" + "id=" + id + ", radius=" + radius + ", mass=" + mass + ", x=" + x + ", y=" + y + ", speed=" + speed + ", angle=" + angle + '}';
    }

    //        public static State nextInstant(State currentState, double time) {
//            Position position = new Position(
//                    currentState.position.getX() + currentState.getVelocityX() * time,
//                    currentState.position.getY() + currentState.getVelocityY() * time
//            );
//
//            return new State(position, currentState.getVelocityX(), currentState.getVelocityY());
//        }

}