package de.ccetl.jparticles.types.wave;

import de.ccetl.jparticles.util.Vec2d;

import java.util.List;

public class WaveElement {
    private final List<Vec2d> line;
    private final int fillColor;
    private final int lineColor;

    public WaveElement(List<Vec2d> line, int fillColor, int lineColor) {
        this.line = line;
        this.fillColor = fillColor;
        this.lineColor = lineColor;
    }

    public List<Vec2d> getLine() {
        return line;
    }

    public int getFillColor() {
        return fillColor;
    }

    public int getLineColor() {
        return lineColor;
    }
}
