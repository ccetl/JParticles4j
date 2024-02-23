package de.ccetl.jparticles.core;

import de.ccetl.jparticles.core.shape.Shape;
import de.ccetl.jparticles.util.Vec2d;

/**
 * A particle base for systems.
 */
public abstract class Element extends Vec2d {
    /**
     * The radius of the element.
     */
    private final double radius;
    /**
     * Represents the velocity.
     */
    private final Vec2d v;
    /**
     * The color of the element.
     */
    private final int color;
    /**
     * The shape.
     */
    private final Shape shape;

    public Element(double x, double y, double radius, double vx, double vy, int color, Shape shape) {
        super(x, y);
        this.radius = radius;
        this.v = new Vec2d(vx, vy);
        this.color = color;
        this.shape = shape;
    }

    public double getRadius() {
        return radius;
    }

    public double getVx() {
        return v.getX();
    }

    public void setVx(double vx) {
        this.v.setX(vx);
    }

    public double getVy() {
        return v.getY();
    }

    public void setVy(double vy) {
        this.v.setY(vy);
    }

    public Vec2d getV() {
        return v;
    }

    public int getColor() {
        return color;
    }

    public Shape getShape() {
        return shape;
    }
}
