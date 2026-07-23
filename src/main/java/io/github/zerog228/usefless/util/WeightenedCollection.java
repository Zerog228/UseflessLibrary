package io.github.zerog228.usefless.util;

import lombok.Getter;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

// Source - https://stackoverflow.com/a/6409791
// Posted by Peter Lawrey, modified by community. See post 'Timeline' for change history
// Retrieved 2026-06-30, License - CC BY-SA 3.0

public class WeightenedCollection<E> {
    private final NavigableMap<Double, E> map = new TreeMap<>();
    @Getter
    private final Random random;
    private double total = 0;

    public WeightenedCollection() {
        this(new Random());
    }

    public WeightenedCollection(Random random) {
        this.random = random;
    }

    public WeightenedCollection<E> add(double weight, E result) {
        if (weight <= 0) return this;
        total += weight;
        map.put(total, result);
        return this;
    }

    public E next() {
        double value = random.nextDouble() * total;
        return map.higherEntry(value).getValue();
    }
}
