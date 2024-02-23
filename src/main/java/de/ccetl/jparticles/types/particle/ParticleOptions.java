package de.ccetl.jparticles.types.particle;

import de.ccetl.jparticles.types.CommonParticleOptions;

public interface ParticleOptions extends CommonParticleOptions {
    // Maximum value of two-point connection
    // If the distance between two points within the range is less than proximity, a connection will be made between the two points
    // (0, 1) represents the number of times the container width, 0 & [1, +∞) represent specific numbers
    double getProximity();

    // Range of anchor points, more connections with larger range
    // When range is 0, no connections are made, and related values are invalid
    double getRange();

    // Line width
    double getLineWidth();

    // Shape of the line
    // spider: scattered spider shape
    // cube: closed cube shape
    LineShape getLineShape();

    // center lines are faster
    boolean isCenterLines();

    // Parallax effect
    boolean isParallax();

    // Define the number of layers and the level size of each layer in the parallax layer similar to z-index in CSS.
    // Range: [0, +∞), the smaller the value, the stronger the parallax effect, 0 means no movement.
    // Example of defining four layers of particles: [1, 3, 5, 10]
    int[] getParallaxLayer();

    // Parallax intensity, the smaller the value, the stronger the parallax effect
    double getParallaxStrength();

    // What happens if two particles bump into each other
    Obstacle getCollisionIntern();

    // Reaction on hitting the window boundaries
    Obstacle getCollisionEdge();

    // The initial direction
    Direction getDirection();

    // Where new particles are created
    // Mismatching with the direction, you might never see particles
    SpawnRegion getSpawnRegion();

    boolean isHoverRepulse();

    double getRepulseRadius();

    // Draws shapes with reduced alpha where the particles have been
    boolean isTrail();

    // How long to wait before setting the point (in MS)
    double getTrailUpdate();

    // How long a point should stay alive (in MS)
    double getTrailAlive();

    // When enabled, the trail gets smaller based on the time alive
    boolean isTrailShrink();
}
