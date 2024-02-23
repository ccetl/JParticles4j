package de.ccetl.jparticles.util;

import de.ccetl.jparticles.event.ResizeEvent;

/**
 * A point on the screen.
 */
public class Vec2d {
    public static final Vec2d ORDINAL = new Vec2d(0, 0);

    private double x;
    private double y;

    public Vec2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vec2d add(Vec2d other) {
        this.x += other.x;
        this.y += other.y;
        return this;
    }

    public Vec2d subtract(Vec2d other) {
        this.x -= other.x;
        this.y -= other.y;
        return this;
    }

    public Vec2d multiply(double d) {
        x *= d;
        y *= d;
        return this;
    }

    public Vec2d divide(double d) {
        x /= d;
        y /= d;
        return this;
    }

    public double dot(Vec2d other) {
        return this.x * other.x + this.y * other.y;
    }

    public Vec2d normalize() {
        double length = getLength();
        if (length != 0) {
            x /= length;
            y /= length;
            return this;
        } else {
            return set(ORDINAL);
        }
    }

    public double getLength() {
        return Math.sqrt(Utils.sq(x) + Utils.sq(y));
    }

    public Vec2d scale(double scalar) {
        return new Vec2d(this.x * scalar, this.y * scalar);
    }

    public Vec2d set(Vec2d other) {
        this.x = other.x;
        this.y = other.y;
        return this;
    }

    public void onResize(ResizeEvent event, int oldWidth, int oldHeight) {
        double ax = getX() / oldWidth;
        double ay = getY() / oldHeight;
        setX(ax * event.newWidth);
        setY(ay * event.newHeight);
    }

    public Vec2d copy() {
        return new Vec2d(x, y);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
