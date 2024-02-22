package de.ccetl.jparticles.types.snow;

import de.ccetl.jparticles.core.Element;
import de.ccetl.jparticles.core.shape.Shape;

public class SnowElement extends Element {
    private long swingAt;

    public SnowElement(double radius, double x, double y, double vx, double vy, int color, long swingAt, Shape shape) {
        super(x, y, radius, vx, vy, color, shape);
        this.swingAt = swingAt;
    }

    public long getSwingAt() {
        return swingAt;
    }

    public void setSwingAt(long swingAt) {
        this.swingAt = swingAt;
    }
}
