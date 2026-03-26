package me.zink.useflessLibrary.gui;

import java.util.Objects;

public class CPrimitiveButton<In, Out>{

    private final OnClick<In, Out> onClick;
    private final Class<In> inTypeParameter;
    private final Class<Out> outTypeParameter;

    public CPrimitiveButton(OnClick<In, Out> onClick){
        this.onClick = onClick;
        inTypeParameter = null;
        outTypeParameter = null;
    }

    public CPrimitiveButton(Class<In> inTypeParameter, Class<Out> outTypeParameter, OnClick<In, Out> onClick){
        this.onClick = onClick;
        this.inTypeParameter = inTypeParameter;
        this.outTypeParameter = outTypeParameter;
    }

    public Out click(In in){
        return onClick.perform(in);
    }

    public Class<?> getInTypeParameter(){
        return Objects.requireNonNullElse(inTypeParameter, Void.class);
    }

    @FunctionalInterface
    public interface OnClick<In, Out>{
        Out perform(In in);
    }
}
