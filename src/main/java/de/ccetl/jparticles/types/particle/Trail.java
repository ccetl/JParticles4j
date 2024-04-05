package de.ccetl.jparticles.types.particle;

import de.ccetl.jparticles.util.Vec2d;

public class Trail extends Vec2d {
    private final long creation;
    private final double rotation;

    public Trail(double x, double y, long creation, double rotation) {
        super(x, y);
        this.creation = creation;
        this.rotation = rotation;
    }

    public long getCreation() {
        return creation;
    }

    public double getRotation() {
        return rotation;
    }
}
