package me.zink.useflessLibrary.item;

import me.zink.useflessLibrary.UseflessLibrary;
import me.zink.useflessLibrary.data.PersistentData;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.List;

import static org.bukkit.Bukkit.getServer;

//TODO Override ItemStack class (Override and pass needed methods, constructors etc.)
public abstract class CItem implements Listener {

    public static final String CITEM_TAG = "custom_item";

    public final NamespacedKey namespaceKey;
    public final ItemStack stack;

    /**
     * Creates custom item with defined item key
     * */
    public CItem(String itemKey) {
        stack = initItem(itemKey);

        //Set model data and tag
        CStackCreator.Builder.customModelData(stack, itemKey);
        stack.setItemMeta((ItemMeta) PersistentData.setData(stack.getItemMeta(), CITEM_TAG, itemKey));

        initRecipe();
        getServer().getPluginManager().registerEvents(this, UseflessLibrary.getPlugin());
        this.namespaceKey = new NamespacedKey(UseflessLibrary.getPlugin(), itemKey);
    }

    public CItem(String itemKey, List<CItem> list) {
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
        return stack != null && stack.hasItemMeta() && PersistentData.hasData(stack.getItemMeta(), CITEM_TAG) && PersistentData.getDataS(stack.getItemMeta(), CITEM_TAG).equals(namespaceKey.value());
    }

    /**Checks if provided item is instance of reference one
     * @param stack Provided item
     * @param reference Reference item. Should be one of the items from CItem list! (There is no manual check for it, at least right now)
     * @return Is 'stack' is instance of 'reference'
     * */
    public static boolean equals(ItemStack stack, ItemStack reference){
        return stack != null && PersistentData.hasData(stack.getItemMeta(), CITEM_TAG) && PersistentData.getDataS(stack.getItemMeta(), CITEM_TAG).equals(PersistentData.getDataS(reference.getItemMeta(), CITEM_TAG));
    }
}
