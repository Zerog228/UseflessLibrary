package io.github.zerog228.usefless.item;

import io.github.zerog228.usefless.UseflessLibrary;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

public class CItemUtils {

    protected static final LinkedHashSet<ICItem> items = new LinkedHashSet<>();

    public static boolean addItem(ICItem item){
        if(!items.add(item)){
            UseflessLibrary.getPlugin().getLogger().log(Level.WARNING, "Custom item with key "+item.getNamespacedKey()+" already existed!");
            return false;
        }
        return true;
    }

    public static Set<ICItem> getItems(){
        return items;
    }

    public static CBasicItem basic(String itemKey, ItemStack itemStack){
        return new CBasicItem(itemKey) {
            @Override
            public ItemStack initItem(String itemKey) {
                return itemStack;
            }

            @Override
            public void initRecipe() {}
        };
    }

    public static CBasicItem basic(String itemKey, ItemStack itemStack, List<ICItem> list){
        return new CBasicItem(itemKey, list) {
            @Override
            public ItemStack initItem(String itemKey) {
                return itemStack;
            }

            @Override
            public void initRecipe() {}
        };
    }
}
