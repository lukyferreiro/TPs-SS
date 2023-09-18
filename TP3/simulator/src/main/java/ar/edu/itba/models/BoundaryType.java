package ar.edu.itba.models;

public enum BoundaryType {

    LEFT {
        @Override
        public void collide(Particle particle) {
            particle.setVx(-particle.getVx());
            particle.setVy(particle.getVy());
        }

        // boundaryPosition is bottom side of wall
        @Override
        public Double getTc(Position boundaryPosition, double length, Particle particle) {
            double x = boundaryPosition.getX();
            double vx = particle.getVx();
            if (vx >= 0 || (x + particle.getRadius()) > particle.getX()) {
                return Double.MAX_VALUE;
            } else {
                double time = (x + particle.getRadius() - particle.getX()) / vx;
                return checkInnerCornerVertical(boundaryPosition, length, particle, time) ? time : Double.MAX_VALUE;
            }
        }
    }, RIGHT {
        @Override
        public void collide(Particle particle) {
            particle.setVx(-particle.getVx());
            particle.setVy(particle.getVy());
        }

        // boundaryPosition is bottom side of wall
        @Override
        public Double getTc(Position boundaryPosition, double length, Particle particle) {
            double x = boundaryPosition.getX();
            double vx = particle.getVx();
            if (vx <= 0 || (x - particle.getRadius()) < particle.getX()) {
                return Double.MAX_VALUE;
            } else {
                double time = (x - particle.getRadius() - particle.getX()) / vx;
                return checkInnerCornerVertical(boundaryPosition, length, particle, time) ? time : Double.MAX_VALUE;
            }

        }
    }, TOP {
        @Override
        public void collide(Particle particle) {
            particle.setVx(particle.getVx());
            particle.setVy(-particle.getVy());
        }

        // boundaryPosition is left side of wall
        @Override
        public Double getTc(Position boundaryPosition, double length, Particle particle) {
            double y = boundaryPosition.getY();
            double vy = particle.getVy();
            if (vy <= 0 || (y - particle.getRadius()) < particle.getY()) {
                return Double.MAX_VALUE;
            } else {
                double time = (y - particle.getRadius() - particle.getY()) / vy;
                return checkInnerCornerHorizontal(boundaryPosition, length, particle, time) ? time : Double.MAX_VALUE;
            }
        }
    }, BOTTOM {
        @Override
        public void collide(Particle particle) {
            particle.setVx(particle.getVx());
            particle.setVy(-particle.getVy());
        }

        // boundaryPosition is left side of wall
        @Override
        public Double getTc(Position boundaryPosition, double length, Particle particle) {
            double y = boundaryPosition.getY();
            double vy = particle.getVy();
            if (vy >= 0 || (y + particle.getRadius()) > particle.getY()) {
                return Double.MAX_VALUE;
            } else {
                double time = (y + particle.getRadius() - particle.getY()) / vy;
                return checkInnerCornerHorizontal(boundaryPosition, length, particle, time) ? time : Double.MAX_VALUE;
            }
        }
    };

    private static boolean checkInnerCornerVertical(Position boundaryPosition, double length, Particle particle, double time) {
        Position nextPosition = particle.getPosition().moveForward(particle, time);
        Position top = boundaryPosition.add(new Position(0, length));
        return top.isAbove(nextPosition) && boundaryPosition.isBelow(nextPosition);
    }

    private static boolean checkInnerCornerHorizontal(Position boundaryPosition, double length, Particle particle, double time) {
        Position nextPosition = particle.getPosition().moveForward(particle, time);
        Position right = boundaryPosition.add(new Position(length, 0));
        return boundaryPosition.isLeftOf(nextPosition) && right.isRightOf(nextPosition);
    }

    public abstract void collide(Particle particle);

    public abstract Double getTc(Position boundaryPosition, double length, Particle particle);
}
