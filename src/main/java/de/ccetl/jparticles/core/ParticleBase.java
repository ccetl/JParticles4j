package de.ccetl.jparticles.core;

import de.ccetl.jparticles.event.ResizeEvent;
import de.ccetl.jparticles.types.CommonOptions;

import java.util.LinkedList;
import java.util.List;

public abstract class ParticleBase<T extends CommonOptions, E extends Element> extends Base<T> {
    protected final List<E> elements = new LinkedList<>();

    protected ParticleBase(T defaultConfig, int width, int height) {
        super(defaultConfig, width, height);
    }

    @Override
    public void onResize(ResizeEvent event) {
        for (E element : elements) {
            element.onResize(event, width, height);
        }
        super.onResize(event);
    }
}
