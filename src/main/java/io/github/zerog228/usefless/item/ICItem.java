package io.github.zerog228.usefless.item;

import io.github.zerog228.usefless.data.PersistentData;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

public interface ICItem {

    String CITEM_TAG = "custom_item";

    ItemStack getItem();
    NamespacedKey getNamespacedKey();
    boolean equals(ItemStack stack);

    /**Checks if provided item is instance of reference one
     * @param stack Provided item
     * @param reference Reference item. Should be one of the items from CItem list! (There is no manual check for it, at least right now)
     * @return Is 'stack' is instance of 'reference'
     * */
    static boolean equals(ItemStack stack, ItemStack reference){
        return stack != null && stack.hasItemMeta() && PersistentData.hasData(stack.getItemMeta(), CITEM_TAG) && PersistentData.getDataS(stack.getItemMeta(), CITEM_TAG).equals(PersistentData.getDataS(reference.getItemMeta(), CITEM_TAG));
    }

    /**Checks if provided item is instance of reference one
     * @param stack Provided item
     * @param reference Reference item
     * @return Is 'stack' is instance of 'reference'
     * */
    static boolean equals(ItemStack stack, ICItem reference){
        return stack != null && stack.hasItemMeta() && PersistentData.hasData(stack.getItemMeta(), CITEM_TAG) && PersistentData.getDataS(stack.getItemMeta(), CITEM_TAG).equals(PersistentData.getDataS(reference.getItem().getItemMeta(), CITEM_TAG));
    }

    static boolean isCustomItem(ItemStack stack){
        return stack != null && stack.hasItemMeta() && PersistentData.hasData(stack.getItemMeta(), CITEM_TAG);
    }
}
