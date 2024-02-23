package de.ccetl.jparticles.core;

import de.ccetl.jparticles.util.Vec2d;

public interface Renderer {
    void drawLine(double x, double y, double x1, double y1, double width, int color);
    void drawLine(Vec2d[] vertices, double width, int color);
    void drawLineRotated(double x, double y, double x1, double y1, double translationX, double translationY, double width, double radians, int color);
    void drawCircle(double x, double y, double radius, int color);
    void drawTriangle(double x, double y, double radius, int color);
    void drawStar(double x, double y, double radius, int sides, double dent, int color);
    void drawImage(double x, double y, double radius, int id);
    void drawPolygon(Vec2d[] vertices, int color);
}
