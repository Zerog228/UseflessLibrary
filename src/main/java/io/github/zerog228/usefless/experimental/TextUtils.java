package io.github.zerog228.usefless.experimental;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.text.DecimalFormat;
import java.util.*;

public class TextUtils {

    //TODO Fast lore from setLore(ItemStack, String ... lore)

    public static final DecimalFormat format0p = new DecimalFormat("#.");
    public static final DecimalFormat FORMAT1P = new DecimalFormat("#.#");
    public static final DecimalFormat FORMAT2P = new DecimalFormat("#.##");
    public static final DecimalFormat format2p_0 = new DecimalFormat("0.00");
    public static final DecimalFormat FORMAT3P = new DecimalFormat("#.###");
    public static final DecimalFormat format3p_0 = new DecimalFormat("0.000");
    public static final MiniMessage serializer = MiniMessage.builder()
            .tags(TagResolver.builder()
                    .resolver(StandardTags.defaults())
                    .build()
            )
            .build();
    public static final LegacyComponentSerializer legacySerializer = LegacyComponentSerializer.builder().character('§').hexColors().build();

    public static final Map<String, String> colorToCompressed = new HashMap<>();
    public static final Map<String, String> compressedToColor = new HashMap<>();

    static{
        //Colors
        colorToCompressed.put("<black>", "&0");
        colorToCompressed.put("<dark_blue>", "&1");
        colorToCompressed.put("<dark_green>", "&2");
        colorToCompressed.put("<dark_aqua>", "&3");
        colorToCompressed.put("<dark_red>", "&4");
        colorToCompressed.put("<dark_purple>", "&5");
        colorToCompressed.put("<gold>", "&6");
        colorToCompressed.put("<gray>", "&7");
        colorToCompressed.put("<dark_gray>", "&8");
        colorToCompressed.put("<blue>", "&9");
        colorToCompressed.put("<green>", "&a");
        colorToCompressed.put("<aqua>", "&b");
        colorToCompressed.put("<red>", "&c");
        colorToCompressed.put("<light_purple>", "&d");
        colorToCompressed.put("<yellow>", "&e");
        colorToCompressed.put("<white>", "&f");

        //Modifiers
        colorToCompressed.put("<!italic>", "&!");
        colorToCompressed.put("<italic>", "&I");
        colorToCompressed.put("<bold>", "&B");
        colorToCompressed.put("<strikethrough>", "&S");
        colorToCompressed.put("<reset>", "&R");
        colorToCompressed.put("<obfuscated>", "&O");

        //Fill other map
        colorToCompressed.forEach((key, value) -> compressedToColor.put(value, key));
    }

    public static Component serializeText(String text) {
        text = text.replaceAll("0x", "#");
        return serializer.deserialize(text).decoration(TextDecoration.ITALIC, false);
    }

    public static List<Component> serializeText(String... texts) {
        return serializeText(Arrays.stream(texts).toList());
    }
    public static List<Component> serializeText(List<String> texts) {
        List<Component> components = new ArrayList<>();
        for (String text : texts) {
            if (text != null) {
                components.add(serializeText(text));
            }
        }
        return components;
    }
}
