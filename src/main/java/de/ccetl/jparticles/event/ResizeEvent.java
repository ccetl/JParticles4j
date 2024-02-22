package de.ccetl.jparticles.event;

public class ResizeEvent {
    public final int newWidth;
    public final int newHeight;

    public ResizeEvent(int newWidth, int newHeight) {
        this.newWidth = newWidth;
        this.newHeight = newHeight;
    }
}
