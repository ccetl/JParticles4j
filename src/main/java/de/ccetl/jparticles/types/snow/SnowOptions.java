package de.ccetl.jparticles.types.snow;

import de.ccetl.jparticles.types.CommonParticleOptions;

public interface SnowOptions extends CommonParticleOptions {
    // Duration of particle existence
    double getDuration();

    // Spawns all snowflakes random at the start
    boolean isFirstRandom();

    // Limits the snowflakes to the number
    boolean isStrict();

    // Whether to randomly change the direction of falling
    boolean isSwing();

    // Time interval for changing direction, in milliseconds
    int getSwingInterval();

    // Probability of changing direction (after reaching the time interval), range [0, 1]
    double getSwingProbability();
}
