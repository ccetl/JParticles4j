package de.ccetl.jparticles.types.line;

import de.ccetl.jparticles.types.CommonOptions;

public interface LineOptions extends CommonOptions {
    // Number of lines
    int getNumber();

    // Maximum width
    double getMaxWidth();

    // Minimum width
    double getMinWidth();

    // Maximum speed
    double getMaxSpeed();

    // Minimum speed
    double getMinSpeed();

    // Maximum inclination angle of the line [0, 180]
    double getMaxDegree();

    // Minimum inclination angle
    double getMinDegree();

    // Create line on click
    boolean isCreateOnClick();

    // Number of lines to create
    int getNumberOfCreations();

    // Remove lines on overflow
    boolean isRemoveOnOverflow();

    // Overflow compensation, to let the line overflow the container by a certain distance (unit: PX), value range: [0, +∞)
    double getOverflowCompensation();

    // Number of lines to be retained to avoid all being removed, effective when removeOnOverflow is true
    int getReservedLines();
}
