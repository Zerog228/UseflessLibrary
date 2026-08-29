package io.github.zerog228.usefless.util;

import io.github.zerog228.usefless.annotations.JConvert;
import io.github.zerog228.usefless.item.CStackCreator;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Test {
    public void test(){
        Method[] methods = CStackCreator.Builder.class.getMethods();
        Map<String, Method> methodMap = new HashMap<>();

        Arrays.stream(methods).filter(method -> method.isAnnotationPresent(JConvert.class)).forEach(method -> {
            methodMap.put(method.getDeclaredAnnotation(JConvert.class).key(), method);
        });

        System.out.println(methodMap);
    }
}
