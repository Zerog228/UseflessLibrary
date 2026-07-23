package io.github.zerog228.usefless.util;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class TTrio<F, S, T> {
    private F first;
    private S second;
    private T third;

    public TTrio(F first, S second, T third){
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public static <F, S, T> TTrio<F, S, T> of(F f, S s, T t){
        return new TTrio<>(f, s, t);
    }

    public static <F, S, T> TTrio<F, S, T> trio(F f, S s, T t){
        return new TTrio<>(f, s, t);
    }

    public TTrio<F, S, T> setFirst(F first){
        this.first = first;
        return this;
    }

    public TTrio<F, S, T> setSecond(S second){
        this.second = second;
        return this;
    }

    public TTrio<F, S, T> setThird(T third){
        this.third = third;
        return this;
    }


}
