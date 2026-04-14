package io.github.zerog228.usefless.util;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class TPair<L /*extends Number*/, R /*extends Number*/> {
    private L first;
    private R second;

    public TPair(L left, R right){
        this.first = left;
        this.second = right;
    }

    public static <F /*extends Number*/, S /*extends Number*/> TPair<F, S> of(F l, S r){
        return new TPair<>(l, r);
    }

    public TPair<L, R> setFirst(L first){
        return of(first, getSecond());
    }

    public TPair<L, R> setSecond(R second){
        return of(getFirst(), second);
    }

    public L getKey(){
        return first;
    }

    public R getValue(){
        return second;
    }

    /*public <F extends Number, S extends Number> NPair<Number, Number> add(F first, S second) throws Exception{
        if(this.first.getClass() == first.getClass() && this.second.getClass() == second.getClass()){
            return new NPair<>(sum(this.first, first), sum(this.second, second));
        }
        throw new Exception("Pair values are not the same type as provided ones!");
    }

    private Number sum(Number num1, Number num2) throws Exception{
        if(num1 instanceof Integer){
            return num1.intValue() + num2.intValue();
        }
        if(num1 instanceof Float){
            return num1.floatValue() + num2.floatValue();
        }
        if(num1 instanceof Double){
            return num1.doubleValue() + num2.doubleValue();
        }
        if(num1 instanceof Long){
            return num1.longValue() + num2.longValue();
        }
        if(num1 instanceof Byte){
            return num1.byteValue() + num2.byteValue();
        }
        if(num1 instanceof Short){
            return num1.shortValue() + num2.shortValue();
        }
        throw new Exception("Number format not found for "+num1+" | "+num2+"!");
    }*/
}
