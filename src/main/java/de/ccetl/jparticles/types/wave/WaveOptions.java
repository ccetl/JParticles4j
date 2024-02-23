package de.ccetl.jparticles.types.wave;

import de.ccetl.jparticles.types.CommonOptions;

public interface WaveOptions extends CommonOptions {
    // Number of ripples
    int getNumber();

    // Whether to fill the background color, setting to false makes related values invalid
    WaveArgument<Boolean> isFill();

    // Fill color, effective when fill is set to true
    WaveArgument<Integer> getFillColor();

    // Whether to draw the line, setting to false makes related values invalid
    WaveArgument<Boolean> isLine();

    // Border color, effective when line is set to true
    WaveArgument<Integer> getLineColor();

    // Border width, an empty array results in random width [.2, 2).
    WaveArgument<Double> getLineWidth();

    // Horizontal offset of the ripple, offset value from the left edge of the Canvas
    // (0, 1) represents multiple of container width, 0 & [1, +∞) represents specific values
    WaveArgument<Double> getOffsetLeft();

    // Vertical offset of the ripple, distance from the midpoint of the ripple to the top of the Canvas
    // (0, 1) represents multiple of container height, 0 & [1, +∞) represents specific values
    WaveArgument<Double> getOffsetTop();

    // Crest height, (0, 1) represents multiple of container height, 0 & [1, +∞) represents specific values
    WaveArgument<Double> getCrestHeight();

    // Number of crests, i.e., number of sine cycles, default random [1, 0.2 * container width)
    WaveArgument<Double> getCrestCount();

    // Movement speed, default random [.1, .4)
    WaveArgument<Double> getSpeed();
}
