package de.ccetl.jparticles.types;

import de.ccetl.jparticles.core.Renderer;

import java.util.function.Supplier;

public interface CommonOptions {
    Renderer getRenderer();

    Supplier<Integer> getColorSupplier();
}
