package io.github.zerog228.usefless.gui;

import javax.annotation.Nullable;
import java.util.Objects;

public class CPrimitiveButton<In, Out>{

    @Nullable
    protected final OnClick<In, Out> onClick;
    protected final Class<In> inTypeParameter;
    protected final Class<Out> outTypeParameter;

    public CPrimitiveButton(@org.jetbrains.annotations.Nullable OnClick<In, Out> onClick){
        this.onClick = onClick;
        inTypeParameter = null;
        outTypeParameter = null;
    }

    public CPrimitiveButton(Class<In> inTypeParameter, Class<Out> outTypeParameter, @org.jetbrains.annotations.Nullable OnClick<In, Out> onClick){
        this.onClick = onClick;
        this.inTypeParameter = inTypeParameter;
        this.outTypeParameter = outTypeParameter;
    }

    public Out click(In in){
        if(onClick == null){
            return null;
        }
        return onClick.perform(in);
    }

    public Class<?> getInTypeParameter(){
        return Objects.requireNonNullElse(inTypeParameter, Void.class);
    }

    public Class<?> getOutTypeParameter(){
        return Objects.requireNonNullElse(outTypeParameter, Void.class);
    }

    @FunctionalInterface
    public interface OnClick<In, Out>{
        Out perform(In in);
    }
}
