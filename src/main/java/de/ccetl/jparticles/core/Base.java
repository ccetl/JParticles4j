package de.ccetl.jparticles.core;

import de.ccetl.jparticles.event.ResizeEvent;
import de.ccetl.jparticles.types.CommonOptions;

public abstract class Base<T extends CommonOptions> {
    protected final T options;

    protected int width;
    protected int height;

    protected boolean paused = true;

    protected long lastUpdate;

    protected Base(T config, int width, int height) {
        this.options = config;
        this.width = width;
        this.height = height;
    }

    /**
     * Bootstrapping
     */
    public void bootstrap() {
        init();
    }

    /**
     * Initializes data or method calls
     */
    public abstract void init();

    /**
     * Drawing entry
     */
    public abstract void draw(double mouseX, double mouseY);

    /**
     * Pauses motion
     */
    public void pause() {
        if (!paused) {
            paused = true;
        }
    }

    /**
     * Starts motion
     */
    public void start() {
        if (paused) {
            lastUpdate = System.currentTimeMillis();
            paused = false;
        }
    }

    protected double getDelta() {
        long time = System.currentTimeMillis();
        long timeDelta = time - lastUpdate;
        lastUpdate = time;
        return timeDelta / 16.67;
    }

    public boolean isPaused() {
        return paused;
    }

    public void onResize(ResizeEvent event) {
        this.width = event.newWidth;
        this.height = event.newHeight;
    }
}
