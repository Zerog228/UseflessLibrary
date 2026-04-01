package io.github.zerog228.usefless.experimental;

import io.github.zerog228.usefless.experimental.base91.Base91;
import io.github.zerog228.usefless.util.TPair;
import net.kyori.adventure.text.Component;
import org.apache.commons.lang3.SerializationUtils;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nullable;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;

public class CISerializer {

    //Material maps
    private static final Map<Material, Integer> matToInt = new HashMap<>();
    private static final Map<Integer, Material> intToMat = new HashMap<>();

    //Utility prefixes
    private static final String
            TYPE = ",t:",
            DISPLAY = ",d:",
            LORE = ",l:",
            META = ",m:",
            FLAGS = ",f:",

            //Tags for text
            LITERATE = "l",
            TRANSLATABLE = "t",

            //Tags for compression type
            CLASSIC = "c",
            BASE91 = "9",
            BASE64 = "6",
            ZIP = "z"
                    ;

    //Prefixes list
    private static List<String> prefixes = new ArrayList<>();

    //Init all data
    static {
        int i = 0;
        for(Material material : Material.values()){
            matToInt.put(material, i);
            intToMat.put(i, material);
            i++;
        }
        prefixes.add(TYPE);
        prefixes.add(DISPLAY);
        prefixes.add(LORE);
        prefixes.add(META);
        prefixes.add(FLAGS);
    }

    /**
     * Converts inventory contents into an array of bytes
     * */
    @Nullable
    public static byte[] serialize(Inventory inventory){
        return serialize(inventory.getContents());
    }

    @Nullable
    public static byte[] serialize(ItemStack[] contents){
        String[] stacks = new String[contents.length];

        for(int i = 0; i < contents.length; i++){
            stacks[i] = contents[i].toString();
        }

        try{
            return SerializationUtils.serialize(stacks);
        }catch (Exception ignored){
            ignored.printStackTrace();
            return null;
        }
    }

    @Nullable
    public static TPair<byte[], byte[][]> serializeOptimized(Inventory inventory){
        return serializeOptimized(inventory.getContents());
    }

    @Nullable
    public static TPair<byte[], byte[][]> serializeOptimized(ItemStack[] contents){

        //Populate item type collection
        Map<ItemStack, Byte> variantsMap = new HashMap<>();
        byte index = 0; //Index related to certain item
        byte[] keys = new byte[contents.length]; //Keys for the map
        for(int i = 0; i < contents.length; i++){
            if(!variantsMap.containsKey(contents[i])){
                variantsMap.put(contents[i], index++);
            }
            keys[i] = variantsMap.get(contents[i]);
        }

        //Convert item type collection to byte array
        byte[][] codedItems = new byte[index][];
        int i = 0;
        for(ItemStack stack : variantsMap.keySet()){
            codedItems[i] = stack.toString().getBytes(StandardCharsets.UTF_8);
            i++;
        }

        return TPair.of(keys, codedItems);
    }

    public static String extremeCompression(ItemStack[] contents){
        int slot = 0; //Relative position of item from another item
        StringBuilder builder = new StringBuilder();
        for(ItemStack stack : contents){
            if (stack == null || stack.getType() == Material.AIR) {
                slot++; //Adding indent
                continue;
            }
            //Beginning of a stack. Appending stack type and slot
            builder.append(TYPE).append(compressType(stack, slot));

            //If has custom name. Otherwise, vanilla translatable name will be given on deserialization
            if(stack.getItemMeta().hasCustomName()){
                builder.append(DISPLAY).append(compressText(stack.effectiveName()));
            }

            //Add lore
            if(stack.lore() != null){
                for (Component lore : stack.lore()) {
                    builder.append(LORE).append(compressText(lore));
                }
            }

            //Не легче ли будет генерировать ванильную мету,
            // после чего сравнивать с метой от текущего предмета и записывать только значения, что были изменены?

            //Add item meta
            ItemMeta meta = stack.getItemMeta();
            ItemMeta testMeta = new ItemStack(Material.GLASS).getItemMeta();
            // There are 25 types of item meta (+ default value)
            // Common data:

            // Lore and name (Already implemented)
            // BukkitValues
            // CustomModelData
            // Enchants
            // itemFlag
            // isUnbreakable
            // showTooltip
            // customItemModel
            // hasEnchantmentGlintOverride
            // isGlider
            // isFireResistant
            // hasDamageResistant
            // maxStackSize
            // rarity
            // useReminder
            // useCooldown
            // hasFood
            // hasTool
            // hasEquipable
            // hasJukeboxPlayable
            // hasAttributeModifiers

            // BOOK
            // generation
            // author

            slot = 0; //Setting indent to 0
        }

        return builder.toString();
    }

    private static String compressType(ItemStack stack, int slot){
        return matToInt.get(stack.getType()) + ":" + slot + ":" + stack.getAmount();
    }

    public static String compressText(Component component){
        String preProcessed = TextUtils.legacySerializer.serialize(component);
        preProcessed = Pattern.compile("§#[a-f0-9]{6}").matcher(preProcessed).replaceAll(matchResult -> hexToShort(matchResult.group()));
        //TODO Convert hex to known vanilla colors if possible
        //TODO Store hex colors that occur multiple times in separate tag
        return preProcessed;
    }

    public static String encode91(byte[] bytes){
        return new String(Base91.encode(bytes));
    }

    public static String encode64(byte[] bytes){
        return Base64.getEncoder().encodeToString(bytes);
    }

    private static String hexToShort(String hex) {
        return "§#" + hex.charAt(2) + hex.charAt(4) + hex.charAt(6);
    }
}
