package io.github.zerog228.usefless.util;

@FunctionalInterface
public interface Executable<In, Out> {
    public Out execute(In in);
}
