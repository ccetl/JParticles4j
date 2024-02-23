package de.ccetl.jparticles.types.particle;

import de.ccetl.jparticles.util.Vec2d;

public class Trail extends Vec2d {
    private final long creation;

    public Trail(double x, double y, long creation) {
        super(x, y);
        this.creation = creation;
    }

    public long getCreation() {
        return creation;
    }
}
