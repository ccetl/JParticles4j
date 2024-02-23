package de.ccetl.jparticles.types.wave;

import de.ccetl.jparticles.util.Utils;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class WaveArgument<T> {
    private final List<T> list;
    private final Supplier<T> supplier;

    public WaveArgument(List<T> list) {
        this.list = list;
        this.supplier = null;
    }

    public WaveArgument(Supplier<T> supplier) {
        this.list = null;
        this.supplier = supplier;
    }

    public T getRandom() {
        return get(list != null ? (int) Math.floor(Utils.getRandomInRange(0, list.size() - 1)) : 0);
    }

    public T get(int i) {
        if (list != null) {
            return list.get(i);
        } else {
            return Objects.requireNonNull(supplier).get();
        }
    }
}
