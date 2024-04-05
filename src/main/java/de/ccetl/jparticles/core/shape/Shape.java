package de.ccetl.jparticles.core.shape;

import de.ccetl.jparticles.core.Renderer;
import de.ccetl.jparticles.util.Vec2d;

public class Shape {
    private final ShapeType type;
    /**
     * The image id.
     */
    private int id;
    private int sides;
    private double dent;

    public Shape(ShapeType type) {
        this.type = type;
    }

    public void render(Renderer renderer, Vec2d vec2d, double radius, double degrees, int color) {
        switch (type) {
            case CIRCLE:
                renderer.drawCircle(vec2d.getX(), vec2d.getY(), radius, degrees, color);
                break;
            case STAR:
                renderer.drawStar(vec2d.getX(), vec2d.getY(), radius, sides, dent, degrees, color);
                break;
            case TRIANGLE:
                renderer.drawTriangle(vec2d.getX(), vec2d.getY(), radius, degrees, color);
                break;
            case IMAGE:
                renderer.drawImage(vec2d.getX(), vec2d.getY(), radius, degrees, id);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    public ShapeType getType() {
        return type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSides(int sides) {
        this.sides = sides;
    }

    public void setDent(double dent) {
        this.dent = dent;
    }
}
