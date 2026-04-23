package io.github.zerog228.usefless.util;

import org.bukkit.util.Vector;
import org.joml.Vector2d;

public class CMath {
    private boolean isInAABB(Vector2d vector, Vector2d min, Vector2d max){
        return vector.x() >= min.x() && vector.x() <= max.x() && vector.y() >= min.y() && vector.y() <= max.y();
    }

    /**
     * @return If vector from an origin point falls on given 3D object or not
     * */
    private boolean isFacingObject(Vector origin, Vector movement_direction, Vector plane_min, Vector plane_max){
        //Is player facing up or down
        if(isContainedInPlane(
                new Vector2d(origin.getX(), origin.getZ()),
                origin.getY(),
                new Vector2d(movement_direction.getX(), movement_direction.getZ()),
                movement_direction.getY(),
                new Vector2d(plane_min.getX(), plane_min.getZ()),
                new Vector2d(plane_max.getX(), plane_max.getZ()),
                movement_direction.getY() < 0 ? plane_max.getY() : plane_min.getY())){
            return true;
        }

        //Is player facing west or east
        if(isContainedInPlane(
                new Vector2d(origin.getY(), origin.getZ()),
                origin.getX(),
                new Vector2d(movement_direction.getY(), movement_direction.getZ()),
                movement_direction.getX(),
                new Vector2d(plane_min.getY(), plane_min.getZ()),
                new Vector2d(plane_max.getY(), plane_max.getZ()),
                movement_direction.getX() < 0 ? plane_max.getX() : plane_min.getX())){
            return true;
        }

        //Is player facing north or south
        if(isContainedInPlane(
                new Vector2d(origin.getY(), origin.getX()),
                origin.getZ(),
                new Vector2d(movement_direction.getY(), movement_direction.getX()),
                movement_direction.getZ(),
                new Vector2d(plane_min.getY(), plane_min.getX()),
                new Vector2d(plane_max.getY(), plane_max.getX()),
                movement_direction.getZ() < 0 ? plane_max.getZ() : plane_min.getZ())){
            return true;
        }

        return false;
    }

    /**
     * It's a bit more complicated version of 'vectorAmountBetweenPoints()' method.
     * Instead of just checking overall direction in 2D space and returning multiplier,
     * it checks direction in 2D projection of 3D space and returns 'true' if vector intersects with given plane </br>
     * </br>
     * Because all operations happen on the Z axis, we need to additionally specify all the coordinates of a dimension slices
     * (To calculate distance between origin and given plane) </br>
     * </br>
     * How it works: </br>
     * Because all of it happens on the separate Z planes, first of all it finds distance between origin point (origin_xy)
     * and given plane (plane_min_xy, plane_max_xy) by subtracting their Z coordinates and dividing them
     * by vector in which origin is moving on the Z axis: '(plane_z - origin_z) / movement_z', gaining amount of movement*.
     * By knowing how much our point moved through Z axis we will know how much it will move through X-Y axis.
     * Now we can find X-Y coordinates of our origin point on the same Z slice on which our given plane is located by
     * multiplying movement vector by amount of movement and adding to the origin point:
     * 'origin_xy.add(movement_xy.mul(direction_mult))'.
     * The last thing left is to check if our origin point is located within given plane. Voilà! </br>
     * </br>
     * *The best way to describe it is to imagine two separate parallel Z slices. One contains
     * our origin point and other contains given plane. Only thing that connects them is our vector (The third plane).
     * This vector shows movement of our X-Y point through Z plane. if it has value of (5, -17, 1), then it will mean that by
     * moving our origin point by 1 step on the Z plane, it will also move it by 5 and -17 on X and Y respectively and wise-versa
     * @param origin_xy Origin point on a XY plane
     * @param origin_z Coordinate of the origin point (on the Z axis)
     * @param movement_xy Movement of an origin point through a XY plane
     * @param movement_z Movement of an origin point through a Z plane
     * @param plane_min_xy Minimal corner of a given plane (XY)
     * @param plane_max_xy Maximal corner of a given plane (XY)
     * @param plane_z Coordinate of a given plane (on the Z axis)
     * @return If origin point falls on given plane by moving on given vector
     * */
    private boolean isContainedInPlane(Vector2d origin_xy, double origin_z, Vector2d movement_xy, double movement_z, Vector2d plane_min_xy, Vector2d plane_max_xy, double plane_z){
        double direction_mult = vectorAmountBetweenPoints(plane_z, origin_z, movement_z);

        // This operation splits into two parts:
        // 1. We are checking distance from origin to plane. If it's negative then origin can't be facing that way
        // 2. We are adding multiplied vector to our origin location, effectively setting them on the same plane. And then we check if our new point is in bounds of a given plane
        return direction_mult > 0 && isInAABB(origin_xy.add(movement_xy.mul(direction_mult)), plane_min_xy, plane_max_xy);
    }

    /**
     * Read documentation above 'isContainedInPlane()'
     * */
    private boolean isContainedInPlane(double origin_x, double origin_y, double origin_z, double movement_x, double movement_y, double movement_z, double plane_min_x, double plane_min_y, double plane_max_x, double plane_max_y, double plane_z){
        double direction_mult = vectorAmountBetweenPoints(plane_z, origin_z, movement_z);
        origin_x += movement_x * direction_mult;
        origin_y += movement_y * direction_mult;
        return direction_mult > 0 && origin_x >= plane_min_x && origin_x <= plane_max_x && origin_y >= plane_min_y && origin_y <= plane_max_y;
    }

    /**
     * How it works: </br>
     * We have a separate point (Or a slice), an origin and a vector coming from the origin.
     * This method returns amount of lengths of a given vector between two points.
     * You can see this as a 2D plane with two points and 1 vector OR 1 vertical line, a point and a vector coming from it </br>
     * </br>
     * Note that if returned value is negative then vector is faced in the opposite direction from a point/plane.
     *
     * @param point Point on the plane or just the coordinate of a whole slice
     * @param origin Origin point from which vector if shot towards (or outwards) plane. It should be located on the same coordinate plane
     * @param direction_vector Vector of facing coming from the second plane
     * @return Amount of length of a given vector between two points (Can be negative!)
     * */
    private double vectorAmountBetweenPoints(double point, double origin, double direction_vector){
        return (point - origin) / direction_vector;
    }
}
