package io.github.zerog228.usefless.util;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class TAssignedPair<V> extends TPair<String, V> {
    private String key;
    private V value;

    public TAssignedPair(String key, V value){
        super(key, value);
    }

    public static <V> TAssignedPair<V> of(String key, V value){
        return new TAssignedPair<>(key, value);
    }

    public TAssignedPair<V> setFirst(String first){
        this.key = first;
        return this;
    }

    public TAssignedPair<V> setSecond(V second){
        this.value = second;
        return this;
    }

    public TAssignedPair<V> setKey(String key){
        this.key = key;
        return this;
    }

    public TAssignedPair<V> setValue(V value){
        this.value = value;
        return this;
    }
}
