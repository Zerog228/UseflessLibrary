package me.zink.UseflessLibrary.effect;

import me.zink.UseflessLibrary.util.TPair;
import org.apache.commons.lang3.tuple.Triple;
import org.bukkit.Location;
import java.util.ArrayList;
import java.util.List;

public class CEffect {

    /**
     * Creates a list of points in the circle shape on the ground
     * @param radius Circle radius
     * @param pAmount Amount of points the circle will be divided into
     * @param startAngle Angle from which creation starts
     * @param reverse Should be clockwise or counterclockwise
     * @return List of points
     * */
    public static List<TPair<Double, Double>> circle(int radius, int pAmount, int startAngle, boolean reverse){
        List<TPair<Double, Double>> points = new ArrayList<>(pAmount);
        double degree_per_point = 360d /pAmount;
        for(int i = 0; i < pAmount; i++){
            double angle = Math.toRadians(degree_per_point * i + startAngle);
            if(reverse){
                points.add(TPair.of(radius * Math.sin(angle),radius * -Math.cos(angle)));
            }else{
                points.add(TPair.of(radius * -Math.sin(angle), radius * Math.cos(angle)));
            }
        }
        return points;
    }

    /**
     * Creates a list of points in the circle shape on the ground
     * @param radius Circle radius
     * @param pAmount Amount of points the circle will be divided into
     * @return List of points
     * */
    public static List<TPair<Double, Double>> circle(int radius, int pAmount){
        return circle(radius, pAmount, 0, false);
    }

    /**
     * Creates line between two points
     * @param pos1 First position in the world
     * @param pos2 Second position in the world
     * @param distance Distance between placed points
     * */
    public static List<Triple<Double, Double, Double>> line(Location pos1, Location pos2, double distance){
        return line(pos1.x(), pos1.y(), pos1.z(), pos2.x(), pos2.y(), pos2.z(), distance);
    }

    /**
     * Creates line between two points
     * @param pos1 First position in the world
     * @param pos2 Second position in the world
     * @param additional Should it add additional point at the end or not
     * */
    public static List<Triple<Double, Double, Double>> line(Location pos1, Location pos2, int pAmount, boolean additional){
        return line(pos1.x(), pos1.y(), pos1.z(), pos2.x(), pos2.y(), pos2.z(), pAmount, additional);
    }

    /**
     * Creates line between two points
     * @param pAmount Number of points on the line
     * @param additional Should it add additional point at the end or not
     * */
    public static List<Triple<Double, Double, Double>> line(double x1, double y1, double z1, double x2, double y2, double z2, int pAmount, boolean additional){
        double lengthX = (x1 - x2), lengthY = (y1 - y2), lengthZ = (z1 - z2);
        double length = Math.sqrt(lengthX * lengthX + lengthY * lengthY + lengthZ * lengthZ);

        //Adding additional point at the end of the line
        if(additional){
            List<Triple<Double, Double, Double>> points = new ArrayList<>(pAmount + 1);
            points.addAll(lineInnerImpl(x1, y1, z1, length, lengthX, lengthY, lengthZ, length / pAmount));
            return points;
        }else{
            return lineInnerImpl(x1, y1, z1, length, lengthX, lengthY, lengthZ, length / pAmount);
        }
    }


    /**
     * Creates line between two points
     * @param distance Distance between placed points
     * */
    public static List<Triple<Double, Double, Double>> line(double x1, double y1, double z1, double x2, double y2, double z2, double distance){
        double lengthX = (x1 - x2), lengthY = (y1 - y2), lengthZ = (z1 - z2);
        double length = Math.sqrt(lengthX * lengthX + lengthY * lengthY + lengthZ * lengthZ);

        return lineInnerImpl(x1, y1, z1, length, lengthX, lengthY, lengthZ, distance);
    }

    private static List<Triple<Double, Double, Double>> lineInnerImpl(double x, double y, double z, double length, double lengthX, double lengthY, double lengthZ, double distance){
        List<Triple<Double, Double, Double>> points = new ArrayList<>();

        for(double indent = 0; indent <= length; indent += distance){
            double progress = indent / length;
            points.add(Triple.of(x + lengthX * progress, y + lengthY * progress, z + lengthZ * progress));
        }

        return points;
    }

}
