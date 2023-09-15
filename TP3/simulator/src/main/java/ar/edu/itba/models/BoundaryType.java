package ar.edu.itba.models;

public enum BoundaryType {

    LEFT {
        @Override
        public void collide(Particle particle) {
            particle.setVx(-particle.getVx());
            particle.setVy(particle.getVy());
        }

        // wallPos is bottom side of wall
        @Override
        public double getCollisionTime(Position boundaryPosition, double length, Particle particle) {
            double x = boundaryPosition.getX();
            double vx = particle.getVx();
            if (vx >= 0 || (x + particle.getRadius()) > particle.getPosition().getX()) {
                return Double.MAX_VALUE;
            } else {
                double time = (x + particle.getRadius() - particle.getPosition().getX()) / vx;
                return isInBoundaryVertical(boundaryPosition, length, particle, time) ? time : Double.MAX_VALUE;
            }
        }
    }, RIGHT {
        @Override
        public void collide(Particle particle) {
            particle.setVx(-particle.getVx());
            particle.setVy(particle.getVy());
        }

        // wallPos is bottom side of wall
        @Override
        public double getCollisionTime(Position boundaryPosition, double length, Particle particle) {
            double x = boundaryPosition.getX();
            double vx = particle.getVx();
            if (vx <= 0 || (x - particle.getRadius()) < particle.getPosition().getX()) {
                return Double.MAX_VALUE;
            } else {
                double time = (x - particle.getRadius() - particle.getPosition().getX()) / vx;
                return isInBoundaryVertical(boundaryPosition, length, particle, time) ? time : Double.MAX_VALUE;
            }

        }
    }, TOP {
        @Override
        public void collide(Particle particle) {
            particle.setVx(particle.getVx());
            particle.setVy(-particle.getVy());
        }

        // wallPos is left side of wall
        @Override
        public double getCollisionTime(Position boundaryPosition, double length, Particle particle) {
            double y = boundaryPosition.getY();
            double vy = particle.getVy();
            if (vy <= 0 || (y - particle.getRadius()) < particle.getPosition().getY()) {
                return Double.MAX_VALUE;
            } else {
                double time = (y - particle.getRadius() - particle.getPosition().getY()) / vy;
                return isInBoundaryHorizontal(boundaryPosition, length, particle, time) ? time : Double.MAX_VALUE;
            }
        }
    }, BOTTOM {
        @Override
        public void collide(Particle particle) {
            particle.setVx(particle.getVx());
            particle.setVy(-particle.getVy());
        }

        // wallPos is left side of wall
        @Override
        public double getCollisionTime(Position boundaryPosition, double length, Particle particle) {
            double y = boundaryPosition.getY();
            double vy = particle.getVy();
            if (vy >= 0 || (y + particle.getRadius()) > particle.getPosition().getY()) {
                return Double.MAX_VALUE;
            } else {
                double time = (y + particle.getRadius() - particle.getPosition().getY()) / vy;
                return isInBoundaryHorizontal(boundaryPosition, length, particle, time) ? time : Double.MAX_VALUE;
            }
        }
    };

    private static boolean isInBoundaryVertical(Position boundaryPosition, double length, Particle particle, double time) {
        Position nextPosition = particle.getPosition().moveForward(particle, time);
        Position top = boundaryPosition.add(new Position(0, length));
        Position bottom = boundaryPosition;
        return top.isAbove(nextPosition) && bottom.isBelow(nextPosition);
    }

    private static boolean isInBoundaryHorizontal(Position boundaryPosition, double length, Particle particle, double time) {
        Position nextPosition = particle.getPosition().moveForward(particle, time);
        Position right = boundaryPosition.add(new Position(0, length));
        Position left = boundaryPosition;
        return left.isLeftOf(nextPosition) && right.isRightOf(nextPosition);
    }

    public abstract void collide(Particle particle);

    public abstract double getCollisionTime(Position boundaryPosition, double length, Particle particle);
}
