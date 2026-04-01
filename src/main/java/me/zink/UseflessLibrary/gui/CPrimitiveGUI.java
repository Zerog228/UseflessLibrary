package me.zink.UseflessLibrary.gui;

import io.papermc.paper.datacomponent.item.TooltipDisplay;
import lombok.Getter;
import me.zink.UseflessLibrary.UseflessLibrary;
import me.zink.UseflessLibrary.data.PersistentData;
import me.zink.UseflessLibrary.item.CStackCreator;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public abstract class CPrimitiveGUI implements InventoryHolder, Listener {

    private final String UNTOUCHABLE = "untouchable_item";

    private final Inventory inventory;
    protected final Map<Integer, CPrimitiveButton> buttons = new HashMap<>();
    private ItemStack DEF_EMPTY;

    public CPrimitiveGUI(FixedSize size) {
        this(size.getSize(), Component.empty());
    }

    public CPrimitiveGUI(int size) {
        this(size, Component.empty());
    }

    public CPrimitiveGUI(FixedSize size, Component title) {
        this(size.getSize(), title);
    }

    public CPrimitiveGUI(int size, Component title) {
        this.inventory = UseflessLibrary.getPlugin().getServer().createInventory(this, size, title);

        createDefaultEmpty("", Material.GRAY_STAINED_GLASS_PANE);
        prefillGui(size);
        registerButtons();

        Bukkit.getServer().getPluginManager().registerEvents(this, UseflessLibrary.getPlugin());
    }

    /**
     * Creates default 'empty' item (Item which will usually be used to fill empty spaces in the inventory).
     * @param material Material of the item
     * */
    protected void createDefaultEmpty(String name, Material material){
        DEF_EMPTY = CStackCreator.builder(material)
                .name(name)
                .tooltip(TooltipDisplay.tooltipDisplay().hideTooltip(true).build())
                .data(UNTOUCHABLE, true)
                .build();
    }

    /**
     * Draws a row in the inventory with default empty item
     * @param length Length of the row
     * */
    protected void drawRow(int x, int y, int length) {
        drawRow(DEF_EMPTY, x, y, length);
    }

    /**
     * Draws a row in the inventory on selected coordinates with selected item.
     * Note that if it will not have 'untouchable' tag than player will be able to interact with it
     * @param length Length of the row
     * */
    protected void drawRow(ItemStack stack, int x, int y, int length) {
        int index = y * 9 + x;
        while(length >= 0){
            inventory.setItem(index + length, stack);
            length--;
        }
    }

    /**
     * Draws a column in the inventory with default empty item
     * @param height Height of the column
     * */
    protected void drawColumn(int x, int y, int height) {
        drawColumn(DEF_EMPTY, x, y, height);
    }

    /**
     * Draws a column in the inventory on selected coordinates with selected item.
     * Note that if it will not have 'untouchable' tag than player will be able to interact with it
     * @param height Height of the column
     * */
    protected void drawColumn(ItemStack stack, int x, int y, int height) {
        int index = y * 9 + x;
        while(height >= 0){
            inventory.setItem(index + height * 9, stack);
            height--;
        }
    }

    /**
     * Draws bounds in the inventory with default 'empty' item, coordinates and sizes
     * @param width Width of the bounds
     * @param height Height of the bounds
     * */
    protected void drawBounds(int x, int y, int width, int height){
        drawBounds(DEF_EMPTY, x, y, width, height);
    }

    /**
     * Draws bounds in the inventory with specified item, coordinates and sizes
     * @param stack Stack which will be used to draw bounds
     * @param width Width of the bounds
     * @param height Height of the bounds
     * */
    protected void drawBounds(ItemStack stack, int x, int y, int width, int height){
        drawRow(stack, x, y, width);
        drawRow(stack, x, y + height, width);

        drawColumn(stack, x, y, height);
        drawColumn(stack, x + width, y, height);
    }

    /**Converts coordinates into slot index*/
    public int getSlot(int x, int y){
        return x + y * 9;
    }

    /**Returns item from selected slot*/
    public ItemStack getItem(int slot){
        return inventory.getItem(slot);
    }

    /**Sets item in the slot
     * Note that it can be taken from inventory if it's not marked with 'untouchable' tag
     * */
    protected void setItem(int slot, ItemStack stack){
        setItem(slot, stack, false);
    }

    /**Sets item in the slot
     * @param untouchable Should this item be untouchable or not
     * */
    protected void setItem(int slot, ItemStack stack, boolean untouchable){
        setUntouchable(stack, untouchable);
        inventory.setItem(slot, stack);
    }


    /**
     * Prefills GUI with 'empty' item by default
     * @param size Size of the inventory
     * */
    protected void prefillGui(int size){
        for(int i = 0; i < size; i++){
            inventory.setItem(i, DEF_EMPTY);
        }
    };

    protected void registerButtons(){};

    @EventHandler
    public void onClick(InventoryClickEvent e){
        if(e.getInventory().getHolder(false) == inventory.getHolder()){
            //Default button logic
            defaultBTLogic(e.getSlot(), e);

            //If item is untouchable
            if(isUntouchable(e.getCurrentItem())){
                e.setCancelled(true);
            }
        }
    };

    /**
     * Default button logic. Fires every time player clicks slot in the inventory.
     * Passes 'null' as the argument to 'click()' method.
     * If you want to add custom logic (by adding buttons with not-null consumers) you should probably override this
     * @param slot Clicked slot
     * */
    protected void defaultBTLogic(int slot, InventoryClickEvent e){
        CPrimitiveButton button = buttons.get(slot);
        if(button != null && button.getInTypeParameter().equals(Void.class)){
            buttons.get(slot).click(null);
        }
    }

    /**
     * Adds button to selected slot
     * @param slot Slot in which button will be located
     * @param button Button
     * @param stack ItemStack representing button
     * @param untouchable Should button be marked as untouchable or not
     * */
    protected void addButton(int slot, CPrimitiveButton button, ItemStack stack, boolean untouchable){
        addButton(slot, button);
        setItem(slot, stack, untouchable);
    }

    /**
     * Adds button to selected slot
     * @param slot Slot in which button will be located
     * @param button Button
     * */
    protected void addButton(int slot, CPrimitiveButton button){
        buttons.put(slot, button);
    }

    //TODO Check if 'onDrag' also cancels or not

    @Override
    public @NotNull Inventory getInventory() {
        return this.inventory;
    }

    @Getter
    public enum FixedSize{
        X5(5),
        X9(9),
        X27(27),
        X54(54);

        private final int size;

        FixedSize(int size){
            this.size = size;
        }
    }

    /**Checks if this item is marked with untouchable tag*/
    protected boolean hasUntouchableTag(ItemStack stack){
        return stack != null && PersistentData.hasData(stack.getItemMeta(), UNTOUCHABLE);
    }

    /**Checks if this item is marked with untouchable tag and value of the tag*/
    protected boolean isUntouchable(ItemStack stack){
        return hasUntouchableTag(stack) && PersistentData.getDataBo(stack.getItemMeta(), UNTOUCHABLE);
    }

    /**Marks item with tag and adds tag value*/
    protected void setUntouchable(ItemStack stack, boolean untouchable){
        stack.setItemMeta((ItemMeta) PersistentData.setData(stack.getItemMeta(), UNTOUCHABLE, untouchable));
    }

    /**Removes 'untouchable' tag*/
    protected void removeUntouchable(ItemStack stack){
        stack.setItemMeta((ItemMeta) PersistentData.removeData(stack.getItemMeta(), UNTOUCHABLE));
    }

    /*public boolean isUntouchable(ItemStack stack, boolean checkValue){
        return stack != null && PersistentData.hasData(stack.getItemMeta(), UNTOUCHABLE);
    }*/
}
