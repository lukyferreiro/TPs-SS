package ar.edu.itba.models;

public class CornerParticle extends Particle{
    public CornerParticle(int id, Position position) {
        super(id, 0.0, 1.0);
        this.position =  position;
        this.vx = 0;
        this.vy = 0;
    }
    @Override
    public void collide(Particle p) {
        double deltaRx = p.getX() - this.getX();
        double deltaRy = p.getY() - this.getY();
        double deltaVx = p.getVx() - this.getVx();
        double deltaVy = p.getVy() - this.getVy();

        double sigma = this.getRadius() + p.getRadius();
        double dvdr = deltaRx * deltaVx + deltaRy * deltaVy;
        double J = (2 * dvdr) / (sigma);
        double Jx = (J * deltaRx) / sigma;
        double Jy = (J * deltaRy) / sigma;

        p.setVx(p.getVx() - Jx);
        p.setVy(p.getVy() - Jy);
    }
}
