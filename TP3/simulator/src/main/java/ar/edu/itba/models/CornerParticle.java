package ar.edu.itba.models;

public class CornerParticle extends Particle{
    public CornerParticle(int id, Position position) {
        super(id, 0, 1);
        this.position =  position;
        this.vx = 0;
        this.vy = 0;
    }
    @Override
    public void collide(Particle p) {
        p.setVx(-p.getVx());
        p.setVy(-p.getVy());
    }
}
