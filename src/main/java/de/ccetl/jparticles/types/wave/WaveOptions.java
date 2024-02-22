package de.ccetl.jparticles.types.wave;

import de.ccetl.jparticles.types.CommonOptions;

import java.util.List;

public interface WaveOptions extends CommonOptions {
    // Number of ripples
    int getNumber();

    // Whether to fill the background color, setting to false makes related values invalid
    boolean isFill();

    // Fill color, effective when fill is set to true
    List<String> getFillColor();

    // Whether to draw the border, setting to false makes related values invalid
    boolean isLine();

    // Border color, effective when line is set to true
    List<String> getLineColor();

    // Border width, an empty array results in random width [.2, 2).
    List<Double> getLineWidth();

    // Horizontal offset of the ripple, offset value from the left edge of the Canvas
    // (0, 1) represents multiple of container width, 0 & [1, +∞) represents specific values
    List<Double> getOffsetLeft();

    // Vertical offset of the ripple, distance from the midpoint of the ripple to the top of the Canvas
    // (0, 1) represents multiple of container height, 0 & [1, +∞) represents specific values
    List<Double> getOffsetTop();

    // Crest height, (0, 1) represents multiple of container height, 0 & [1, +∞) represents specific values
    List<Double> getCrestHeight();

    // Number of crests, i.e., number of sine cycles, default random [1, 0.2 * container width)
    List<Double> getCrestCount();

    // Movement speed, default random [.1, .4)
    List<Double> getSpeed();

    // Mask: image URL address, Base64 format, canvas image source
    String getMask();

    // Mask mode, default normal
    String getMaskMode();
}
