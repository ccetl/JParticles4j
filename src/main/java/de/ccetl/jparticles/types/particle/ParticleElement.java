package de.ccetl.jparticles.types.particle;

import de.ccetl.jparticles.core.Element;
import de.ccetl.jparticles.core.shape.Shape;
import de.ccetl.jparticles.util.Utils;
import de.ccetl.jparticles.util.Vec2d;

import java.util.LinkedList;

public class ParticleElement extends Element {
    private final LinkedList<Trail> trail = new LinkedList<>();
    private final int parallaxLayer;
    private double parallaxOffsetX;
    private double parallaxOffsetY;
    private boolean velocityChanged;

    public ParticleElement(double radius, double x, double y, double vx, double vy, int color, Shape shape, int parallaxLayer, double parallaxOffsetX, double parallaxOffsetY, double rotationSpeed, double rotation) {
        super(x, y, radius, vx, vy, color, shape, rotationSpeed, rotation);
        this.parallaxLayer = parallaxLayer;
        this.parallaxOffsetX = parallaxOffsetX;
        this.parallaxOffsetY = parallaxOffsetY;
    }

    public Vec2d subtract(ParticleElement other) {
        return new Vec2d(Utils.getX(this) - Utils.getX(other), Utils.getY(this) - Utils.getY(other));
    }

    @Override
    public Vec2d normalize() {
        double length = getLength();
        if (length != 0) {
            return new Vec2d(Utils.getX(this) / length, Utils.getY(this) / length);
        } else {
            return ORDINAL.copy();
        }
    }

    @Override
    public double getLength() {
        return Math.sqrt(Utils.sq(Utils.getX(this)) + Utils.sq(Utils.getY(this)));
    }

    public void handleCollision(ParticleElement other) {
        if (this.isVelocityChanged() && other.isVelocityChanged()) {
            return;
        }

        Vec2d collisionNormal = other.subtract(this).normalize();
        Vec2d relativeVelocity = other.getV().copy().subtract(getV());
        double relativeVelocityDotProduct = collisionNormal.dot(new Vec2d(relativeVelocity.getX(), relativeVelocity.getY()));
        if (relativeVelocityDotProduct > 0) {
            return;
        }

        double impulseScalar = -2 * relativeVelocityDotProduct;
        Vec2d impulse = collisionNormal.scale(impulseScalar);
        if (!this.isVelocityChanged()) {
            double oldSpeed = this.getV().getLength();
            this.getV().subtract(impulse);
            double scaleFactor = oldSpeed / this.getV().getLength();
            this.getV().multiply(scaleFactor);
            this.setVelocityChanged(true);
        }
        if (!other.isVelocityChanged()) {
            double oldSpeed = other.getV().getLength();
            other.getV().add(impulse);
            double scaleFactor = oldSpeed / other.getV().getLength();
            other.getV().multiply(scaleFactor);
            other.setVelocityChanged(true);
        }
    }

    public int getParallaxLayer() {
        return parallaxLayer;
    }

    public double getParallaxOffsetX() {
        return parallaxOffsetX;
    }

    public void setParallaxOffsetX(double parallaxOffsetX) {
        this.parallaxOffsetX = parallaxOffsetX;
    }

    public double getParallaxOffsetY() {
        return parallaxOffsetY;
    }

    public void setParallaxOffsetY(double parallaxOffsetY) {
        this.parallaxOffsetY = parallaxOffsetY;
    }

    public boolean isVelocityChanged() {
        return velocityChanged;
    }

    public void setVelocityChanged(boolean velocityChanged) {
        this.velocityChanged = velocityChanged;
    }

    public LinkedList<Trail> getTrail() {
        return trail;
    }
}
