package me.zink.useflessLibrary.experimental;

import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

public class CIMetaCreator{

    private static final Map<String, Executable> instructions = new HashMap<>();

    public static class Builder<Meta extends ItemMeta>{

        private final Meta itemMeta;

        public Builder(Meta itemMeta){
            this.itemMeta = itemMeta;
        }

        public Builder<Meta> fromMap(Map<String, Object> values){
            for(Map.Entry<String, Object> entry : values.entrySet()){
                instructions.get(entry.getKey()).execute(this, entry.getValue());
            }
            return this;
        }

        public Meta build(){
            return itemMeta;
        }

        public static Builder<? extends ItemMeta> attackRange(Builder<? extends ItemMeta> builder, Object data){
            //AttackRange range = AttackRange.attackRange().minReach().maxReach().minCreativeReach().maxCreativeReach().hitboxMargin().mobFactor().build();
            //ItemStack stack = new ItemStack(Material.GLASS);



            return builder;
        }

        public static Builder<? extends ItemMeta> customModelData(Builder<? extends ItemMeta> builder, Object data){
            try{
                builder.itemMeta.setCustomModelData((int) data);
            }catch (Exception ignored){
                //TODO Set custom model data
                //builder.itemMeta.setCustomModelDataComponent(builder.itemMeta.getCustomModelDataComponent());
            }

            return builder;
        }
    }

    private void initInstructions(){
        /*instructions.put("minecraft:attack_range", (object) -> {

        });
        instructions.put("minecraft:attribute_modifiers", (object) -> {

        });
        instructions.put("minecraft:banner_patterns", (object) -> {

        });
        instructions.put("minecraft:base_color", (object) -> {

        });
        instructions.put("minecraft:bees", (object) -> {

        });
        instructions.put("minecraft:block_entity_data", (object) -> {

        });
        instructions.put("minecraft:block_state", (object) -> {

        });
        instructions.put("minecraft:blocks_attacks", (object) -> {

        });
        instructions.put("minecraft:break_sound", (object) -> {

        });
        instructions.put("minecraft:bucket_entity_data", (object) -> {

        });
        instructions.put("minecraft:bundle_contents", (object) -> {

        });
        instructions.put("minecraft:can_break", (object) -> {

        });
        instructions.put("minecraft:can_place_on", (object) -> {

        });
        instructions.put("minecraft:charged_projectiles", (object) -> {

        });
        instructions.put("minecraft:consumable", (object) -> {

        });
        instructions.put("minecraft:container", (object) -> {

        });
        instructions.put("minecraft:container_loot", (object) -> {

        });
        instructions.put("minecraft:custom_data", (object) -> {

        });
        instructions.put("minecraft:custom_model_data", Builder::customModelData);
        instructions.put("minecraft:custom_name", (object) -> {

        });
        instructions.put("minecraft:damage", (object) -> {

        });
        instructions.put("minecraft:damage_resistant", (object) -> {

        });
        instructions.put("minecraft:damage_type", (object) -> {

        });
        instructions.put("minecraft:death_protection", (object) -> {

        });
        instructions.put("minecraft:debug_stick_state", (object) -> {

        });
        instructions.put("minecraft:dye", (object) -> {

        });*/
    }

    @FunctionalInterface
    private interface Executable{
        void execute(Builder<? extends ItemMeta> builder, Object object);
    }

}
