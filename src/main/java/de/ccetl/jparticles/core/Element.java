package de.ccetl.jparticles.core;

import de.ccetl.jparticles.core.shape.Shape;
import de.ccetl.jparticles.event.ResizeEvent;
import de.ccetl.jparticles.util.Vec2d;

/**
 * A particle base for systems.
 */
public abstract class Element extends Vec2d {
    /**
     * The radius of the element.
     */
    private double radius;
    /**
     * Represents the velocity.
     */
    private Vec2d v;
    /**
     * The color of the element.
     */
    private int color;
    /**
     * The shape.
     */
    private Shape shape;

    public Element(double x, double y, double radius, double vx, double vy, int color, Shape shape) {
        super(x, y);
        this.radius = radius;
        this.v = new Vec2d(vx, vy);
        this.color = color;
        this.shape = shape;
    }

    public void onResize(ResizeEvent event, int oldWidth, int oldHeight) {
        double ax = getX() / oldWidth;
        double ay = getY() / oldHeight;
        setX(ax * event.newWidth);
        setY(ay * event.newHeight);
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
