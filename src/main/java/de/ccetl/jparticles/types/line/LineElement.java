package de.ccetl.jparticles.types.line;

public class LineElement {
    private double x;
    private double width;
    private int color;
    private double speed;
    private int degree;

    public LineElement(double x, double width, int color, double speed, int degree) {
        this.x = x;
        this.width = width;
        this.color = color;
        this.speed = speed;
        this.degree = degree;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public int getColor() {
        return color;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public int getDegree() {
        return degree;
    }
}
