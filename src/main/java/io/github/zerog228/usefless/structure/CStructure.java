package io.github.zerog228.usefless.structure;

import me.nullicorn.nedit.NBTInputStream;
import me.nullicorn.nedit.type.NBTCompound;
import me.nullicorn.nedit.type.NBTList;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.joml.Vector2i;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.util.*;

/**
 * Main class for interactions with structures.
 * Note that in most cases it better to use CStructureHelper instead of this class
 * to interact with your structures
 * */
public class CStructure {
    private static final Map<String, BlockData[][][]> structures = new HashMap<>();

    /**
     * Creates transposed COPY of a matrix
     * @param structure Array to be transposed
     * @return Transposed COPY
     * */
    private static BlockData[][] transpose(BlockData[][] structure){
        final int M = structure.length;
        final int N = structure[0].length;

        BlockData[][] rotated = new BlockData[N][M];
        for(int i = 0; i < M; i++){
            for(int j = 0; j < N; j++){
                rotated[N - 1 - j][i] = structure[i][j];
            }
        }
        return rotated;
    }

    /**
     * Flips the rows of an existing matrix without creating a copy
     * @param structure Matrix which rows will be flipped
     * @return Optional return of the given matrix
     * */
    private static BlockData[][] reverseRows(BlockData[][] structure){
        BlockData temp;
        for(int i = 0; i < structure.length; i++){
            for(int j = 0; j < structure[0].length / 2; j++){
                temp = structure[i][structure[0].length - 1 - j];
                structure[i][structure[0].length - 1 - j] = structure[i][j];
                structure[i][j] = temp;
            }
        }
        return structure;
    }

    /**
     * Flips the columns of an existing matrix without creating a copy
     * @param structure Matrix which columns will be flipped
     * @return Optional return of the given matrix
     * */
    private static BlockData[][] reverseColumns(BlockData[][] structure){
        BlockData temp;
        for(int i = 0; i < structure.length / 2; i++){
            for(int j = 0; j < structure[0].length; j++){
                temp = structure[i][j];
                structure[i][j] = structure[structure.length - 1 - i][j];
                structure[structure.length - 1 - i][j] = temp;
            }
        }
        return structure;
    }

    /**
     * Rotates given structure by 90 degrees (i.e. Clockwise) WITHOUT CREATING COPY
     * @param structure Structure to be rotated. Note that it WILL NOT create copy but will rotate given matrix
     * @return Optional return of the given matrix
     * */
    private static BlockData[][][] rotate90(BlockData[][][] structure){
        for(int size = 0; size < structure.length; size++){
            structure[size] = reverseRows(transpose(structure[size]));
        }
        return structure;
    }

    /**
     * Rotates given structure by 270 degrees (i.e. Counterclockwise by 90 degrees) WITHOUT CREATING COPY
     * @param structure Structure to be rotated. Note that it WILL NOT create copy but will rotate given matrix
     * @return Optional return of the given matrix
     * */
    private static BlockData[][][] rotate270(BlockData[][][] structure){
        for(int size = 0; size < structure.length; size++){
            structure[size] = reverseColumns(transpose(structure[size]));
        }
        return structure;
    }

    /**
     * Rotates given structure by 180 degrees (i.e. flips) WITHOUT CREATING COPY
     * @param structure Structure to be rotated. Note that it WILL NOT create copy but will rotate given matrix
     * @return Optional return of the given matrix
     * */
    private static BlockData[][][] rotate180(BlockData[][][] structure){
        for(int size = 0; size < structure.length; size++){
            structure[size] = reverseColumns(reverseRows(structure[size]));
        }
        return structure;
    }

    /**
     * Converts a list of blocks into an array of defined size.
     * Notice that list must be properly created and filled (i.e. filled from bottom to top, x to z) in order for this operation to work
     * */
    //Easily breaks if X and Z are swapped
    private static BlockData[][][] listToBlockArray(List<BlockData> blocks, Vector2i length) {
        int layerSize = length.x() * length.y();
        BlockData[][][] structure = new BlockData[blocks.size() / layerSize][length.y()][length.x()];
        for (int y = blocks.size() / layerSize - 1; y >= 0; y--) {
            BlockData[][] subLayer = new BlockData[length.y()][length.x()];
            for (int z = 0; z < length.y(); z++) {
                for (int x = 0; x < length.x(); x++) {
                    subLayer[z][x] = blocks.get(x + z * length.y() + layerSize * y);
                }
            }
            structure[y] = subLayer;
        }
        return structure;
    }

    private static BlockData[][][] listToBlockArray(List<BlockData> blocks, int x, int z) {
        return listToBlockArray(blocks, new Vector2i(x, z));
    }

    /**
     * Converts nbt compound to an array of blocks
     * @param structureCompound Compound taken from .nbt file
     * @param ignoreData Will block properties from NBT scheme be ignored or not
     * */
    private static BlockData[][][] nbtToBlockArray(NBTCompound structureCompound, boolean ignoreData) throws Exception{
        //Init structure size
        NBTList size = structureCompound.getList("size");
        final int X = size.getInt(0), Y = size.getInt(1), Z = size.getInt(2);
        BlockData[][][] structure = new BlockData[Y][Z][X];

        //Init block palette
        NBTList palette = structureCompound.getList("palette");
        Map<Integer, BlockData> stateToBlockMap = new HashMap<>(palette.size());
        for(int index = 0; index < palette.size(); index++){
            NBTCompound entry = (NBTCompound) palette.get(index);
            if(entry.containsKey("Properties")  && !ignoreData){
                NBTCompound properties = entry.getCompound("Properties");
                List<String> propertyList = new ArrayList<>();
                properties.forEach((key, value) -> propertyList.add(key+"="+value));
                stateToBlockMap.put(index, Material.getMaterial(entry.getString("Name").split(":")[1].toUpperCase()).createBlockData(propertyList.toString()));
            }else{
                stateToBlockMap.put(index, Material.getMaterial(entry.getString("Name").split(":")[1].toUpperCase()).createBlockData());
            }
        }

        //Fill array with blocks
        structureCompound.getList("blocks").forEach(blockCompound -> {
            NBTList coordinates = ((NBTCompound) blockCompound).getList("pos");
            structure[coordinates.getInt(1)][coordinates.getInt(2)][coordinates.getInt(0)] = stateToBlockMap.get(((NBTCompound) blockCompound).getInt("state", -1));
        });

        return structure;
    }

    /**
     * Initializes all structures from given path and puts them in structure map
     * @param path Path in which structures are located
     * @param ignoreData Should properties of the blocks in the structure be ignored
     * @param deapSearch Should it also search in subfolders or not
     * */
    public static void initAllFromPath(Path path, boolean ignoreData, boolean deapSearch){
        if(path != null && path.toFile().exists() && path.toFile().listFiles() != null){
            for (File file : path.toFile().listFiles()) {
                if(file.isDirectory()){
                    if(deapSearch){
                        initAllFromPath(file.toPath(), ignoreData, deapSearch);
                    }
                }else{
                    initStructure(file.toPath(), ignoreData);
                }
            }
        }
    }

    /**
     * Initializes structure from given path and puts it in structure map
     * @param path Path in which structures are located
     * @param ignoreData Should properties of the blocks in the structure be ignored
     * */
    public static void initStructure(Path path, boolean ignoreData){
        File structureFile = path.toFile();
        if(structureFile.exists()){
            try{
                structures.put(structureFile.getName(), nbtToBlockArray(new NBTInputStream(new BufferedInputStream(new FileInputStream(structureFile))).readFully(), ignoreData));
                //System.out.println("Initialized structure "+structureFile.getName()+"!");
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            System.err.println("Structure with path "+path.toString()+" does not exist!");
        }
    }

    //TODO Comparison with a list of references
    /**
     * Compares given structure with reference. This operation is pretty greedy. Air is NOT ignored
     * @param structure Structure to be compared
     * @param reference Name of the .nbt reference file (For now should explicitly be with .nbt extension!)
     * @param size Dimensions of our structure
     * @param ignoreData Should data of individual blocks be ignored or not (Comparison only by material)
     * */
    public static boolean compare(BlockData[][][] structure, String reference, Vector2i size, boolean ignoreData){
        return compare(structure, reference, size, ignoreData, false);
    }

    /**
     * Compares given structure with reference. This operation is pretty greedy
     * @param structureArray Structure to be compared
     * @param reference Name of the .nbt reference file (For now should explicitly be with .nbt extension!)
     * @param size Dimensions of our structure
     * @param ignoreData Should data of individual blocks be ignored or not (Comparison only by material)
     * @param ignoreAir Should air blocks be skipped when comparing structures
     * */
    public static boolean compare(BlockData[][][] structureArray, String reference, Vector2i size, boolean ignoreData, boolean ignoreAir){
        if(structures.containsKey(reference) && structureArray != null && structureArray.length != 0){
            try{
                BlockData[][][] referenceArray = structures.get(reference);

                if(size.x() == size.y()){ //If sides of a structure are same length
                    if (compare(structureArray, referenceArray, ignoreData, ignoreAir)) {
                        return true;
                    }
                    if (compare(rotate90(structureArray.clone()), referenceArray, ignoreData, ignoreAir)) {
                        return true;
                    }
                    if (compare(rotate270(structureArray.clone()), referenceArray, ignoreData, ignoreAir)) {
                        return true;
                    }
                    if (compare(rotate180(structureArray.clone()), referenceArray, ignoreData, ignoreAir)) {
                        return true;
                    }
                }else{ //More efficient algorithm in cases where sides are different. But I'm not sure is it even more efficient and for how much
                    if(structureArray[0].length == referenceArray[0].length){ //If rotated the same way
                        if (compare(structureArray, referenceArray, ignoreData, ignoreAir)) {
                            return true;
                        }
                        if (compare(rotate180(structureArray.clone()), referenceArray, ignoreData, ignoreAir)) {
                            return true;
                        }
                    }else{
                        if (compare(rotate90(structureArray.clone()), referenceArray, ignoreData, ignoreAir)) {
                            return true;
                        }
                        if (compare(rotate270(structureArray.clone()), referenceArray, ignoreData, ignoreAir)) {
                            return true;
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        return false;
    }

    /**
     * Compares given structure with reference by comparing material of the blocks. This operation is pretty greedy and should be used only in edge cases.
     * This method should probably be replaced with comparison by block tags
     * @param structureArray Structure to be compared
     * @param reference Name of the .nbt reference file (For now should explicitly be with .nbt extension!)
     * @param size Dimensions of our structure
     * @param material Part of the material name for comparison
     * @param ignoreAir Should air blocks be skipped when comparing structures
     * */
    public static boolean compare(BlockData[][][] structureArray, String reference, Vector2i size, String material, boolean ignoreAir){
        if(structures.containsKey(reference) && structureArray != null && structureArray.length != 0){
            try{
                BlockData[][][] referenceArray = structures.get(reference);

                if(size.x() == size.y()){ //If sides of a structure are same length
                    if (compare(structureArray, referenceArray, material, ignoreAir)) {
                        return true;
                    }
                    if (compare(rotate90(structureArray.clone()), referenceArray, material, ignoreAir)) {
                        return true;
                    }
                    if (compare(rotate270(structureArray.clone()), referenceArray, material, ignoreAir)) {
                        return true;
                    }
                    if (compare(rotate180(structureArray.clone()), referenceArray, material, ignoreAir)) {
                        return true;
                    }
                }else{ //More efficient algorithm in cases where sides are different. But I'm not sure is it even more efficient and for how much
                    if(structureArray[0].length == referenceArray[0].length){ //If rotated the same way
                        if (compare(structureArray, referenceArray, material, ignoreAir)) {
                            return true;
                        }
                        if (compare(rotate180(structureArray.clone()), referenceArray, material, ignoreAir)) {
                            return true;
                        }
                    }else{
                        if (compare(rotate90(structureArray.clone()), referenceArray, material, ignoreAir)) {
                            return true;
                        }
                        if (compare(rotate270(structureArray.clone()), referenceArray, material, ignoreAir)) {
                            return true;
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        return false;
    }

    private static boolean compare(BlockData[][][] structure, BlockData[][][] reference, boolean ignoreData, boolean ignoreAir){
        for(int y = 0; y < structure.length; y++){
            for(int z = 0; z < structure[y].length; z++){
                for(int x = 0; x < structure[y][z].length; x++){
                    if(ignoreAir && reference[y][z][x].getMaterial() == Material.AIR) continue;

                    if(ignoreData){
                        if(structure[y][z][x].getMaterial() != reference[y][z][x].getMaterial()){
                            return false;
                        }
                    }else{
                        if(structure[y][z][x] != reference[y][z][x]){
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private static boolean compare(BlockData[][][] structure, BlockData[][][] reference, String material, boolean ignoreAir){
        for(int y = 0; y < structure.length; y++){
            for(int z = 0; z < structure[y].length; z++){
                for(int x = 0; x < structure[y][z].length; x++){
                    if(ignoreAir && reference[y][z][x].getMaterial() == Material.AIR) continue;

                    if(reference[y][z][x].getMaterial().toString().contains(material)){ //Comparison by material. If block in reference falls under this condition
                        if(!structure[y][z][x].getMaterial().toString().contains(material)){ //...but structure doesn't
                            return false;
                        }
                    }else{
                        if(structure[y][z][x].getMaterial() != reference[y][z][x].getMaterial()){
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /*public static void init(){
        File structure = new File(TetsUltima.getPlugin().getDataFolder(), "clean_room.nbt");
        try{
            initStructure(structure.toPath(), false);
            //System.out.println("Room - "+arrayToList(structures.get("clean_room.nbt")));
        }catch (Exception e){
            e.printStackTrace();
        }
    }*/

    //Temporary
    private static List<BlockData> arrayToList(BlockData[][][] array){
        List<BlockData> list = new ArrayList<>();
        for(int y = 0; y < array.length; y++){
            for(int z = 0; z < array[y].length; z++) {
                list.addAll(Arrays.asList(array[y][z]));
            }
        }
        return list;
    }
}
