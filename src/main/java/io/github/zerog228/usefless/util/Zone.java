package io.github.zerog228.usefless.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.util.Vector;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class Zone {

    protected World world;

    protected Vector pos1;
    protected Vector pos2;
    protected double radius = 0;
    protected int minHeight = 0;
    protected int maxHeight = 0;
    protected Type type;

    public Zone() {
        this.pos1 = new Vector(0, 0, 0);
        this.pos2 = new Vector(0, 0, 0);
        this.radius = 0;
        this.type = Type.CUBOID;
    }

    public Zone(int x1, int y1, int z1, int x2, int y2, int z2) {
        this.pos1 = new Vector(Math.min(x1, x2), Math.min(y1, y2), Math.min(z1, z2));
        this.pos2 = new Vector(Math.max(x1, x2), Math.max(y1, y2), Math.max(z1, z2));
        this.maxHeight = Math.max(y1, y2);
        this.minHeight = Math.min(y1, y2);
        this.type = Type.CUBOID;
    }

    public Zone(int x1, int y1, int z1, int x2, int y2, int z2, World world) {
        this.pos1 = new Vector(Math.min(x1, x2), Math.min(y1, y2), Math.min(z1, z2));
        this.pos2 = new Vector(Math.max(x1, x2), Math.max(y1, y2), Math.max(z1, z2));
        this.maxHeight = Math.max(y1, y2);
        this.minHeight = Math.min(y1, y2);
        this.type = Type.CUBOID;
        setWorld(world);
    }

    public Zone(Block block1, Block block2, World world) {
        this.pos1 = new Vector(Math.min(block1.getX(), block2.getX()), Math.min(block1.getY(), block2.getY()), Math.min(block1.getZ(), block2.getZ()));
        this.pos2 = new Vector(Math.max(block1.getX(), block2.getX()), Math.max(block1.getY(), block2.getY()), Math.max(block1.getZ(), block2.getZ()));
        this.maxHeight = Math.max(block1.getY(), block2.getY());
        this.minHeight = Math.min(block1.getY(), block2.getY());
        this.type = Type.CUBOID;
        setWorld(world);
    }

    public Zone(Block block1, Block block2) {
        this(block1, block2, block1.getWorld());
    }

    public Zone(int x, int y, int z, double radius) {
        this.pos1 = new Vector(x, y, z);
        this.radius = radius;
        this.type = Type.SPHERE;
    }

    public Zone(int x, int y, int z, double radius, int maxHeight, int minHeight) {
        this.pos1 = new Vector(x, y, z);
        this.radius = radius;
        this.maxHeight = maxHeight;
        this.minHeight = minHeight;
        this.type = Type.CYLINDER;
    }

    public boolean inZone(Location loc) {
        switch (type) {
            case CUBOID -> {
                return inCuboidZone(loc);
            }
            case SPHERE -> {
                return inSphereZone(loc);
            }
            case CYLINDER -> {
                return inCylinderZone(loc);
            }
        }
        return false;
    }

    public boolean inZone(int x, int y, int z, World world) {
        return inZone(new Location(world, x, y, z));
    }

    private boolean inCuboidZone(Location loc) {
        if (world != null && loc.getWorld() != null) {
            return loc.toVector().isInAABB(pos1, pos2) && world == loc.getWorld();
        }
        return loc.toVector().isInAABB(pos1, pos2);
    }

    private boolean inSphereZone(Location loc) {
        if (world != null && loc.getWorld() != null)
            return loc.toVector().isInSphere(pos1, radius) && world == loc.getWorld();
        return loc.toVector().isInSphere(pos1, radius);
    }

    private boolean inCylinderZone(Location loc) {
        if (world != null && loc.getWorld() != null) return loc.toVector().getY() <= maxHeight
                && loc.toVector().getY() >= minHeight && loc.toVector().isInSphere(pos1, radius) && world == loc.getWorld();
        return loc.toVector().getY() <= maxHeight && loc.toVector().getY() >= minHeight && loc.toVector().isInSphere(pos1, radius);
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public void setWorld(String world) {
        this.world = Bukkit.getWorld(world);
    }

    public Vector getPos1() {
        return pos1;
    }

    public Zone setPos1(Vector pos1) {
        Vector min = Vector.getMinimum(pos1, pos2);
        Vector max = Vector.getMaximum(pos1, pos2);
        this.pos1 = min;
        this.pos2 = max;
        return this;
    }

    public Vector getPos2() {
        return pos2;
    }

    public Zone setPos2(Vector pos2) {
        Vector min = Vector.getMinimum(pos1, pos2);
        Vector max = Vector.getMaximum(pos1, pos2);
        this.pos1 = min;
        this.pos2 = max;
        return this;
    }

    /**
     * Generates list of blocks that are in the zone. From bottom to top, x to z
     * @param zone Zone which will be used
     * @return List of blocks in the zone
     * */
    public static List<Block> flatten(Zone zone) {
        List<Block> blocks = new ArrayList<>();
        for (int y = zone.getMinHeight(); y <= zone.getMaxHeight(); y++) {
            for (int z = zone.getPos1().getBlockZ(); z <= zone.getPos2().getBlockZ(); z++) {
                for (int x = zone.getPos1().getBlockX(); x <= zone.getPos2().getBlockX(); x++) {
                    blocks.add(zone.getWorld().getBlockAt(x, y, z));
                }
            }
        }
        return blocks;
    }

    /**
     * Generates list of blocks that are in the zone. From bottom to top, x to z
     * @return List of blocks in the zone
     * */
    public List<Block> flatten() {
        return flatten(this);
    }

    /**
     * Generates list of blocks with respective blockData that are in the zone. From bottom to top, x to z
     * @param zone Zone which will be used
     * @return List of blocks in the zone
     * */
    @Nullable
    public static List<BlockData> flattenBD(Zone zone) {
        if(zone == null){
            return null;
        }
        List<BlockData> blocks = new ArrayList<>();
        for (int y = zone.getMinHeight(); y <= zone.getMaxHeight(); y++) {
            for (int z = zone.getPos1().getBlockZ(); z <= zone.getPos2().getBlockZ(); z++) {
                for (int x = zone.getPos1().getBlockX(); x <= zone.getPos2().getBlockX(); x++) {
                    blocks.add(zone.getWorld().getBlockAt(x, y, z).getBlockData());
                }
            }
        }
        return blocks;
    }

    /**
     * Generates list of blocks with respective blockData that are in the zone. From bottom to top, x to z
     * @return List of blocks in the zone
     * */
    @Nullable
    public List<BlockData> flattenBD() {
        return flattenBD(this);
    }

    /**
     * Generates list of blocks with respective blockData that are in the zone. From bottom to top, x to z
     * @param zone Zone which will be used
     * @return List of blocks in the zone
     * */
    @Nullable
    public static BlockData[][][] flattenArray(Zone zone) {
        if(zone == null){
            return null;
        }
        BlockData[][][] blocks = new BlockData[zone.getMaxHeight() - zone.getMinHeight() + 1][zone.getPos2().getBlockZ() - zone.getPos1().getBlockZ() + 1][zone.getPos2().getBlockX() - zone.getPos1().getBlockX() + 1];
        for (int y = zone.getMinHeight(); y <= zone.getMaxHeight(); y++) {
            for (int z = zone.getPos1().getBlockZ(); z <= zone.getPos2().getBlockZ(); z++) {
                for (int x = zone.getPos1().getBlockX(); x <= zone.getPos2().getBlockX(); x++) {
                    blocks[y - zone.getMinHeight()][z - zone.getPos1().getBlockZ()][x - zone.getPos1().getBlockX()] = (zone.getWorld().getBlockAt(x, y, z).getBlockData());
                }
            }
        }
        return blocks;
    }

    /**
     * Generates list of blocks with respective blockData that are in the zone. From bottom to top, x to z
     * @return List of blocks in the zone
     * */
    @Nullable
    public BlockData[][][] flattenArray() {
        return flattenArray(this);
    }

    public double getRadius() {
        return radius;
    }

    public Zone setRadius(double radius) {
        this.radius = Math.abs(radius);
        return this;
    }

    public int getMinHeight() {
        return minHeight;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public Zone setHeight(int height) {
        this.minHeight = Math.min(minHeight, height);
        this.maxHeight = Math.max(maxHeight, height);
        return this;
    }

    public Type getType() {
        return type;
    }

    public Zone setType(Type type) {
        this.type = type;
        switch (type) {
            case CUBOID -> {
                if (pos2 == null) pos2 = pos1;
            }
            case SPHERE -> {
                if (radius <= 0) radius = 1;
            }
            case CYLINDER -> {
                if (radius <= 0) radius = 1;
                if (minHeight > maxHeight) {
                    int max = minHeight;
                    minHeight = maxHeight;
                    maxHeight = max;
                }
            }
        }
        return this;
    }


    public enum Type {
        CUBOID,
        SPHERE,
        CYLINDER
    }
}
