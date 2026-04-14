package io.github.zerog228.usefless.item;

import io.github.zerog228.usefless.UseflessLibrary;
import io.github.zerog228.usefless.data.PersistentData;
import lombok.Getter;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

//TODO Override ItemStack class (Override and pass needed methods, constructors etc.)
public abstract class CBasicItem implements ICItem {
    @Getter
    public final NamespacedKey namespacedKey;
    public final ItemStack stack;

    /**
     * Creates custom item with defined item key
     * */
    public CBasicItem(String itemKey) {
        stack = initItem(itemKey);

        //Set model data and tag
        CStackCreator.Builder.customModelData(stack, itemKey);
        stack.setItemMeta((ItemMeta) PersistentData.setData(stack.getItemMeta(), CITEM_TAG, itemKey));

        initRecipe();
        this.namespacedKey = new NamespacedKey(UseflessLibrary.getPlugin(), itemKey);
        CItemUtils.addItem(this);
    }

    public CBasicItem(String itemKey, List<ICItem> list) {
        this(itemKey);
        list.add(this);
    }

    public abstract ItemStack initItem(String itemKey);
    public abstract void initRecipe();
    public ItemStack getItem(){
        return stack;
    };

    /**Checks if provided item is instance of this CustomItem. As a bonus checks if provided stack is 'null'
     * @param stack Provided item stack
     * @return Whether provided stack is instance of this item or not
     * */
    public boolean equals(ItemStack stack) {
        return stack != null && stack.hasItemMeta() && PersistentData.hasData(stack.getItemMeta(), CITEM_TAG) && PersistentData.getDataS(stack.getItemMeta(), CITEM_TAG).equals(namespacedKey.value());
    }
}
