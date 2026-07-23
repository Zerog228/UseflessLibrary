package io.github.zerog228.usefless.util;

import java.util.Random;

public class Chance {

    /**
     * Принимает шанс (Например 0.1 для 10% или 0.69 для 69%) и проверяет, сработал ли он
     *
     * @param value Принимает значение в диапазоне [0;1]. При больших или меньших значениях возвращает в диапазон
     * @return Возвращает то, сработал ли шанс
     */
    public static boolean chance100(double value){
        int chance = (int) (map(0, 100, 0, 1, clamp(value, 0, 1)));
        return chance > new Random().nextInt(100);
    }

    /**
     * Возвращает true если шанс сработал, в противном случае false.
     *
     * @param value Входное значение. Может быть в диапазоне [0;1]. При больших или меньших значениях возвращается в диапазон
     * @param lerp_max Максимальное значение линейной интерполяции входного значения
     * @param max_chance Максимальное значение для шанса, сравниваемого со входным знаением
     */
    public static boolean chance(double value, int lerp_max, int max_chance){
        int chance = (int) (map(0, lerp_max, 0, 1, clamp(value, 0, 1)));
        return chance > new Random().nextInt(max_chance);
    }

    private static float clamp(int value, int min, int max){
        return Math.min(Math.max(value, min), max);
    }

    private static double clamp(double value, double min, double max){
        return Math.min(Math.max(value, min), max);
    }

    private static double norm(double min, double max, double value){
        return (value - min) / (max-min);
    }

    private static double lerp(double min, double max, double value){
        return (double) min + value * (max - min);
    }

    private static double map(double min, double max, double value){
        return lerp(min, max, norm(min, max, value));
    }

    private static double map(double l_min, double l_max, double n_min, double n_max, double value){
        return lerp(l_min, l_max, norm(n_min, n_max, value));
    }

    private static double smoothMap(double l_min, double l_max, double n_min, double n_max, double value){
        double normed = norm(n_min, n_max, value);
        return lerp(l_min, l_max, normed*normed);
    }

}
