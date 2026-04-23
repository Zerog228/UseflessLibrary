package io.github.zerog228.usefless.item;

import io.github.zerog228.usefless.util.TAssignedPair;
import io.papermc.paper.datacomponent.DataComponentType;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.*;
import io.github.zerog228.usefless.UseflessLibrary;
import io.github.zerog228.usefless.data.PersistentData;
import io.papermc.paper.datacomponent.item.consumable.ItemUseAnimation;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.keys.tags.BlockTypeTagKeys;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Namespaced;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import net.kyori.adventure.util.TriState;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.block.BlockType;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;

import java.util.*;
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

        protected ItemStack itemStack;

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

        public Builder lore(Component ... lore){
            return lore(Arrays.stream(lore).toList(), false);
        }

        public Builder lore(boolean preserve, Component ... lore){
            return lore(Arrays.stream(lore).toList(), preserve);
        }

        public Builder lore(String ... lore){
            return loreS(Arrays.stream(lore).toList(), false);
        }

        public Builder lore(boolean preserve, String ... lore){
            return loreS(Arrays.stream(lore).toList(), preserve);
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

        public Builder data(String key, int value) {
            ItemMeta meta = itemStack.getItemMeta();
            PersistentData.setData(meta, key, value);
            itemStack.setItemMeta(meta);
            return this;
        }

        public Builder data(String key, float value) {
            ItemMeta meta = itemStack.getItemMeta();
            PersistentData.setData(meta, key, value);
            itemStack.setItemMeta(meta);
            return this;
        }

        public Builder data(String key, double value) {
            ItemMeta meta = itemStack.getItemMeta();
            PersistentData.setData(meta, key, value);
            itemStack.setItemMeta(meta);
            return this;
        }

        public Builder data(String key, byte value) {
            ItemMeta meta = itemStack.getItemMeta();
            PersistentData.setData(meta, key, value);
            itemStack.setItemMeta(meta);
            return this;
        }

        public Builder data(String key, long value) {
            ItemMeta meta = itemStack.getItemMeta();
            PersistentData.setData(meta, key, value);
            itemStack.setItemMeta(meta);
            return this;
        }

        public Builder data(String key, short value) {
            ItemMeta meta = itemStack.getItemMeta();
            PersistentData.setData(meta, key, value);
            itemStack.setItemMeta(meta);
            return this;
        }

        public Builder data(TAssignedPair<?> ... values) {
            ItemMeta meta = itemStack.getItemMeta();
            for(TAssignedPair<?> pair : values){
                Object value = pair.getValue();
                if(value instanceof Integer integer){
                    PersistentData.setData(meta, pair.getKey(), integer);
                    continue;
                }
                if(value instanceof String string){
                    PersistentData.setData(meta, pair.getKey(), string);
                    continue;
                }
                if(value instanceof Double dbl){
                    PersistentData.setData(meta, pair.getKey(), dbl);
                    continue;
                }
                if(value instanceof Float flt){
                    PersistentData.setData(meta, pair.getKey(), flt);
                    continue;
                }
                if(value instanceof Boolean bool){
                    PersistentData.setData(meta, pair.getKey(), bool);
                    continue;
                }
                if(value instanceof Short shrt){
                    PersistentData.setData(meta, pair.getKey(), shrt);
                    continue;
                }
                if(value instanceof Long lng){
                    PersistentData.setData(meta, pair.getKey(), lng);
                    continue;
                }
                if(value instanceof Byte bt){
                    PersistentData.setData(meta, pair.getKey(), bt);
                }
            }
            itemStack.setItemMeta(meta);
            return this;
        }

        public Builder customModelData(String model){
            itemStack.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData().addString(model).build());
            return this;
        }

        public static void customModelData(ItemStack stack, String model){
            stack.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData().addString(model).build());
        }

        public Builder itemModel(String model){
            itemStack.setData(DataComponentTypes.ITEM_MODEL, Key.key(model));
            return this;
        }

        public Builder itemModel(String namespace, String value){
            itemStack.setData(DataComponentTypes.ITEM_MODEL, Key.key(namespace, value));
            return this;
        }

        public Builder dyedColor(Color color){
            itemStack.setData(DataComponentTypes.DYED_COLOR, DyedItemColor.dyedItemColor(color));
            return this;
        }

        public Builder tool(Tool tool){
            itemStack.setData(DataComponentTypes.TOOL, tool);
            return this;
        }

        public Builder tool(DefaultTool tool, float mining_speed){
            itemStack.setData(DataComponentTypes.TOOL, tool.getTool(mining_speed));
            return this;
        }

        public Builder damage(int damage){
            itemStack.setData(DataComponentTypes.DAMAGE, damage);
            return this;
        }

        public Builder maxDamage(int max_damage){
            itemStack.setData(DataComponentTypes.MAX_DAMAGE, max_damage);
            return this;
        }

        public Builder attackRange(AttackRange range){
            itemStack.setData(DataComponentTypes.ATTACK_RANGE, range);
            return this;
        }

        public Builder attackRange(float range){
            itemStack.setData(DataComponentTypes.ATTACK_RANGE, AttackRange.attackRange().maxReach(range).build());
            return this;
        }

        public Builder consumable(float consume_seconds){
            itemStack.setData(DataComponentTypes.CONSUMABLE, Consumable.consumable().consumeSeconds(consume_seconds).animation(ItemUseAnimation.NONE).build());
            return this;
        }

        public Builder consumable(Consumable consumable){
            itemStack.setData(DataComponentTypes.CONSUMABLE, consumable);
            return this;
        }

        public Builder food(){
            itemStack.setData(DataComponentTypes.FOOD, FoodProperties.food().build());
            return this;
        }

        public Builder food(boolean can_always_eat){
            itemStack.setData(DataComponentTypes.FOOD, FoodProperties.food().canAlwaysEat(can_always_eat).build());
            return this;
        }

        public Builder food(FoodProperties properties){
            itemStack.setData(DataComponentTypes.FOOD, properties);
            return this;
        }

        /**
         * @param duration Duration in ticks
         * */
        public Builder swingAnimation(SwingAnimation.Animation animation, int duration){
            itemStack.setData(DataComponentTypes.SWING_ANIMATION, SwingAnimation.swingAnimation().type(animation).duration(duration).build());
            return this;
        }

        public Builder potion(PotionContents potionContents){
            itemStack.setData(DataComponentTypes.POTION_CONTENTS, potionContents);
            return this;
        }

        public Builder potion(PotionType type, String name, Color color, PotionEffect ... effects){
            itemStack.setData(DataComponentTypes.POTION_CONTENTS, PotionContents.potionContents().potion(type).customName(name).customColor(color).addCustomEffects(Arrays.stream(effects).toList()).build());
            return this;
        }

        public Builder potion(PotionType type, Color color, PotionEffect ... effects){
            itemStack.setData(DataComponentTypes.POTION_CONTENTS, PotionContents.potionContents().potion(type).customColor(color).addCustomEffects(Arrays.stream(effects).toList()).build());
            return this;
        }

        public Builder remove(DataComponentType dataComponentType){
            itemStack.unsetData(dataComponentType);
            return this;
        }

        public Builder remove(DataComponentType ... dataComponentTypes){
            for(DataComponentType type : dataComponentTypes){
                itemStack.unsetData(type);
            }
            return this;
        }
    }

    public enum DefaultTool{
        PICKAXE(BlockTypeTagKeys.MINEABLE_PICKAXE),
        AXE(BlockTypeTagKeys.MINEABLE_AXE),
        HOE(BlockTypeTagKeys.MINEABLE_HOE),
        SHOVEL(BlockTypeTagKeys.MINEABLE_SHOVEL),
        SWORD(BlockTypeTagKeys.SWORD_EFFICIENT);

        private TagKey<BlockType> tagKey;
        DefaultTool(TagKey<BlockType> tagKey){
            this.tagKey = tagKey;
        }

        public Tool getTool(float speed){
            return Tool.tool().addRule(
                    Tool.rule(
                            RegistryAccess.registryAccess()
                                    .getRegistry(RegistryKey.BLOCK)
                                    .getTag(tagKey),
                            speed,
                            TriState.TRUE
                    )
            ).build();
        }
    }

    private void initInstructions(){
        //instructions.put("minecraft:attack_range", Builder::attackRange);
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
