package io.github.zerog228.usefless.item;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.CustomModelData;
import io.papermc.paper.datacomponent.item.ItemLore;
import io.papermc.paper.datacomponent.item.TooltipDisplay;
import io.github.zerog228.usefless.UseflessLibrary;
import io.github.zerog228.usefless.data.PersistentData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import org.bukkit.Material;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class CStackCreator {
    private static final Map<String, Executable> instructions = new HashMap<>();

    private static final MiniMessage serializer = MiniMessage.builder()
            .tags(TagResolver.builder()
                    .resolver(StandardTags.defaults())
                    .build()
            )
            .build();

    public static Builder builder(ItemStack stack){
        return new Builder(stack);
    }

    public static Builder builder(Material material){
        return new Builder(material);
    }

    public static Builder builder(){
        return builder(Material.AIR);
    }

    public static class Builder{

        private ItemStack itemStack;

        public Builder(ItemStack itemStack){
            this.itemStack = itemStack;
        }

        public Builder(Material material){
            this.itemStack = new ItemStack(material);
        }

        public Builder fromMap(Map<String, Object> values){
            for(Map.Entry<String, Object> entry : values.entrySet()){
                instructions.get(entry.getKey()).execute(this, entry.getValue());
            }
            return this;
        }

        public ItemStack build(){
            return itemStack;
        }

        public static Builder attackRange(Builder builder, Object data){
            //AttackRange range = AttackRange.attackRange().minReach().maxReach().minCreativeReach().maxCreativeReach().hitboxMargin().mobFactor().build();
            //ItemStack stack = new ItemStack(Material.GLASS);
            return builder;
        }

        public Builder item(ItemStack stack){
            itemStack = stack;
            return this;
        }

        public Builder material(Material material, boolean preserve){
            if(preserve){
                itemStack = itemStack.withType(material);
            }else{
                itemStack = new ItemStack(material);
            }

            return this;
        }

        public Builder name(Component name){
            itemStack.setData(DataComponentTypes.ITEM_NAME, name);
            return this;
        }

        public Builder name(String name){
            return name(serializer.deserialize(name));
        }

        public Builder lore(List<Component> lore, boolean preserve){
            if(preserve){
                itemStack.setData(DataComponentTypes.LORE, ItemLore.lore().lines(itemStack.getData(DataComponentTypes.LORE).lines()).lines(lore).build());
            }else{
                itemStack.setData(DataComponentTypes.LORE, ItemLore.lore().lines(lore).build());
            }
            return this;
        }

        public Builder loreS(List<String> lore, boolean preserve){
            List<Component> converted_lore = new ArrayList<>(lore.size());
            for(String s : lore){
                converted_lore.add(serializer.deserialize(s));
            }
            return lore(converted_lore, preserve);
        }

        public Builder rarity(ItemRarity rarity){
            itemStack.setData(DataComponentTypes.RARITY, rarity);
            return this;
        }

        public Builder maxStackSize(int maxStackSize){
            itemStack.setData(DataComponentTypes.MAX_STACK_SIZE, maxStackSize);
            return this;
        }

        public Builder amount(int amount){
            itemStack.setAmount(amount);
            return this;
        }

        public Builder tooltip(TooltipDisplay display){
            itemStack.setData(DataComponentTypes.TOOLTIP_DISPLAY, display);
            return this;
        }

        public Builder data(String key, boolean value){
            ItemMeta meta = itemStack.getItemMeta();
            PersistentData.setData(meta, key, value);
            itemStack.setItemMeta(meta);
            return this;
        }

        /**
         * Adds persistent data to item
         * @param data Pairs of 'key - value'. Must be even!
         * */
        public Builder data(String ... data){
            if(data.length % 2 == 0){
               for(int i = 0; i < data.length; i+=2){
                   ItemMeta meta = itemStack.getItemMeta();
                   PersistentData.setData(meta, data[i], data[i+1]);
                   itemStack.setItemMeta(meta);
               }
            }else{
                UseflessLibrary.getPlugin().getLogger().log(Level.WARNING, "Error on adding data to item! Data must come in pairs");
            }
            return this;
        }

        public Builder customModelData(String model){
            itemStack.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData().addString(model).build());
            return this;
        }

        public static void customModelData(ItemStack stack, String model){
            stack.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData().addString(model).build());
        }
    }

    private void initInstructions(){
        instructions.put("minecraft:attack_range", Builder::attackRange);
        /*instructions.put("minecraft:attribute_modifiers", (builder, object) -> {

        });
        instructions.put("minecraft:banner_patterns", (builder, object) -> {

        });
        instructions.put("minecraft:base_color", (builder, object) -> {

        });
        instructions.put("minecraft:bees", (builder, object) -> {

        });
        instructions.put("minecraft:block_entity_data", (builder, object) -> {

        });
        instructions.put("minecraft:block_state", (builder, object) -> {

        });
        instructions.put("minecraft:blocks_attacks", (builder, object) -> {

        });
        instructions.put("minecraft:break_sound", (builder, object) -> {

        });
        instructions.put("minecraft:bucket_entity_data", (builder, object) -> {

        });
        instructions.put("minecraft:bundle_contents", (builder, object) -> {

        });
        instructions.put("minecraft:can_break", (builder, object) -> {

        });
        instructions.put("minecraft:can_place_on", (builder, object) -> {

        });
        instructions.put("minecraft:charged_projectiles", (builder, object) -> {

        });
        instructions.put("minecraft:consumable", (builder, object) -> {

        });
        instructions.put("minecraft:container", (builder, object) -> {

        });
        instructions.put("minecraft:container_loot", (builder, object) -> {

        });
        instructions.put("minecraft:custom_data", (builder, object) -> {

        });
        instructions.put("minecraft:custom_model_data", (builder, object) -> {

        });
        instructions.put("minecraft:custom_name", (builder, object) -> {

        });
        instructions.put("minecraft:damage", (builder, object) -> {

        });
        instructions.put("minecraft:damage_resistant", (builder, object) -> {

        });
        instructions.put("minecraft:damage_type", (builder, object) -> {

        });
        instructions.put("minecraft:death_protection", (builder, object) -> {

        });
        instructions.put("minecraft:debug_stick_state", (builder, object) -> {

        });
        instructions.put("minecraft:dye", (builder, object) -> {

        });*/
    }

    @FunctionalInterface
    private interface Executable{
        Builder execute(Builder builder, Object object);
    }
}
