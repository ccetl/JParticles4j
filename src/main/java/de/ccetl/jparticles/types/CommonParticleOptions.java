package de.ccetl.jparticles.types;

import de.ccetl.jparticles.core.shape.Shape;

import java.util.function.Supplier;

public interface CommonParticleOptions extends CommonOptions {
    int getNumber();

    // Maximum radius of particles
    // (0, +∞)
    double getMaxRadius();

    // Minimum radius of particles
    // (0, +∞)
    double getMinRadius();

    // Minimum speed of particles
    // (0, +∞)
    double getMinSpeed();

    // Maximum speed of particles
    // (0, +∞)
    double getMaxSpeed();

    boolean isSpin();

    // (0, 360)
    double getMaxSpinSpeed();

    // (0, 360)
    double getMinSpinSpeed();

    Supplier<Shape> getShapeSupplier();
}
