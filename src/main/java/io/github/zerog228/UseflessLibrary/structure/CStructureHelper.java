package io.github.zerog228.UseflessLibrary.structure;

import io.github.zerog228.UseflessLibrary.UseflessLibrary;
import io.github.zerog228.UseflessLibrary.util.Zone;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.joml.Vector2i;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class CStructureHelper {
    private static final String STRUCTURE_TAG = "structure_tag";
    private static final String STRUCTURE_TYPE = "structure_type";

    /**
     * Gives every block in the list Metadata tag with coordinates of a 'structure controller (main block in a structure)'.
     * Note that after reloading a server all tags will be deleted and this operation should be repeated.
     * For now ignores air
     * @param mainBlock Controller of a structure (Usually a crafter or a furnace with which player communicates and which fires events)
     * @param blocks All the blocks of a structure (Usually (*but not always) including the main block)
     * */
    public static void markAsStructure(Block mainBlock, List<Block> blocks, String type){
        markAsStructure(mainBlock, blocks, type, false);
    }

    /**
     * Gives every block in the list Metadata tag with coordinates of a 'structure controller (main block in a structure)'.
     * Note that after reloading a server all tags will be deleted and this operation should be repeated.
     * For now ignores air
     * @param mainBlock Controller of a structure (Usually a crafter or a furnace with which player communicates and which fires events)
     * @param blocks All the blocks of a structure (Usually but not always including the main block)
     * @param markMainAsWell Should it mark main block or not (False by default. In most cases main block is already included in the block list)
     * */
    //For now marks air as well as other blocks
    public static void markAsStructure(Block mainBlock, List<Block> blocks, String type, boolean markMainAsWell){
        final int X = mainBlock.getX(), Y = mainBlock.getY(), Z = mainBlock.getZ();
        String cords = X+":"+Y+":"+Z;
        MetadataValue value = new FixedMetadataValue(UseflessLibrary.getPlugin(), cords);
        MetadataValue typeValue = new FixedMetadataValue(UseflessLibrary.getPlugin(), type);
        for(Block block : blocks){
            if(block != null && block.getBlockData().getMaterial() != Material.AIR){
                block.setMetadata(STRUCTURE_TAG, value);
                block.setMetadata(STRUCTURE_TYPE, typeValue);
            }
        }
        if(markMainAsWell) {//Edgecase in which main block is located far away from the structure
            mainBlock.setMetadata(STRUCTURE_TAG, value);
            mainBlock.setMetadata(STRUCTURE_TYPE, typeValue);
        }
    }

    /**
     * Removes tag from the block of a structure.
     * In most cases it's enough to just remove the tag from the main block of a structure
     * @param block Block which will be checked for containing a tag
     * @param self If set to 'true' then tag will be removed from this particular block. Otherwise, tag will be removed from its parent. 'false' by default
     * */
    public static void removeTag(Block block, boolean self){
        try{
            if (partOfAStructure(block)) {
                if (!self) {
                    String[] cords = block.getMetadata(STRUCTURE_TAG).getFirst().asString().split(":");
                    block.getWorld().getBlockAt(Integer.parseInt(cords[0]), Integer.parseInt(cords[1]), Integer.parseInt(cords[2])).removeMetadata(STRUCTURE_TAG, UseflessLibrary.getPlugin());
                    block.getWorld().getBlockAt(Integer.parseInt(cords[0]), Integer.parseInt(cords[1]), Integer.parseInt(cords[2])).removeMetadata(STRUCTURE_TYPE, UseflessLibrary.getPlugin());
                }

                block.removeMetadata(STRUCTURE_TAG, UseflessLibrary.getPlugin());
                block.removeMetadata(STRUCTURE_TYPE, UseflessLibrary.getPlugin());
            }
        }catch (Exception ignored){}
    }

    /**
     * Removes tag from the main block of a structure
     * In most cases it's enough to just remove the tag from the main block of a structure
     * @param block Block that is assumed to be part of a structure
     * */
    public static void removeTag(Block block){
        removeTag(block, false);
    }

    /**
     * Checks if selected block is a part of a multi-block structure
     * @param block Block to be checked
     * */
    public static boolean partOfAStructure(Block block){
        return block != null && block.hasMetadata(STRUCTURE_TAG);
    }

    /**
     * Gets type of the structure this block is part of
     * @return Returns type of the structure this block relates to
     * */
    public static String getStructureType(Block block){
        if(partOfAStructure(block)){
            try{
                return block.getMetadata(STRUCTURE_TYPE).getFirst().asString();
            }catch (Exception ignored){}
        }
        return null;
    }

    /**
     * Checks if this block is a main block of a given structure AND structure is complete
     * @param block Block to be checked
     * @param structure Structure name against which the block will be checked
     * */
    public static boolean checkIfMainBlock(Block block, String structure){
        return checkIfMainBlock(block, Collections.singletonList(structure));
    }

    /**
     * Checks if this block is a main block of a given structure AND structure is complete
     * @param block Block to be checked
     * @param structure Structure names against which the block will be checked
     * */
    public static boolean checkIfMainBlock(Block block, List<String> structure){
        String type = getStructureType(block);
        if(type != null && structure.contains(type)){
            try{
                String[] cords = block.getMetadata(STRUCTURE_TAG).getFirst().asString().split(":");
                return block.getX() == Integer.parseInt(cords[0]) && block.getY() == Integer.parseInt(cords[1]) && block.getZ() == Integer.parseInt(cords[2]);
            }catch (Exception ignored){}
        }
        return false;
    }

    /**
     * Gets main block of a structure by checking other block that is assumed to be part of a structure
     * @param block Block that is assumed to be part of a structure
     * @return Returns main block of a structure if it was found, 'null' otherwise
     * */
    @Nullable
    public static Block getMainBlock(Block block){
        try{
            String[] cords = block.getMetadata(STRUCTURE_TAG).getFirst().asString().split(":");
            Block related = block.getWorld().getBlockAt(Integer.parseInt(cords[0]), Integer.parseInt(cords[1]), Integer.parseInt(cords[2]));
            if(partOfAStructure(related)){
                return related;
            }
        }catch (Exception ignored){}
        return null;
    }

    /**
     * Compares given structure with reference.
     * In addition to CStructure's 'compare', also marks all blocks of the structure as a part of a structure and uses optimized method for comparison.
     * Can be extremely greedy if there are many arguments passed to 'reference'
     * @param mainBlock Main block of a structure
     * @param structure Structure to be compared
     * @param reference Names of the .nbt reference files (For now should explicitly be with .nbt extension!)
     * @param ignoreData Should data of individual blocks be ignored or not (Comparison only by material)
     * @param ignoreAir Should air blocks be skipped when comparing structures
     * */
    public static boolean compare(Block mainBlock, Zone structure, List<String> reference, boolean ignoreData, boolean ignoreAir){
        if(checkIfMainBlock(mainBlock, reference)){ //If it's a complete structure
            return true;
        }else{
            final int
                    sizeX = structure.getPos2().getBlockX() - structure.getPos1().getBlockX() + 1,
                    sizeZ = structure.getPos2().getBlockZ() - structure.getPos1().getBlockZ() + 1;
            Vector2i size = new Vector2i(sizeX, sizeZ);
            for(String ref : reference){
                if(CStructure.compare(structure.flattenArray(), ref, size, ignoreData, ignoreAir)){
                    markAsStructure(mainBlock, structure.flatten(), ref);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Compares given structure with reference.
     * In addition to CStructure's 'compare', also marks all blocks of the structure as a part of a structure and uses optimized method for comparison.
     * @param mainBlock Main block of a structure
     * @param structure Structure to be compared
     * @param reference Name of the .nbt reference file (For now should explicitly be with .nbt extension!)
     * @param ignoreData Should data of individual blocks be ignored or not (Comparison only by material)
     * @param ignoreAir Should air blocks be skipped when comparing structures
     * */
    public static boolean compare(Block mainBlock, Zone structure, String reference, boolean ignoreData, boolean ignoreAir){
        return compare(mainBlock, structure, Collections.singletonList(reference), ignoreData, ignoreAir);
    }
}
