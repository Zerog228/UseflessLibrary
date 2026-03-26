package me.zink.useflessLibrary.gui;

import net.kyori.adventure.text.Component;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class CGUI extends CPrimitiveGUI {
    protected final Map<Integer, CButton> buttons = new HashMap<>();

    public CGUI(Plugin plugin, FixedSize size) {
        super(plugin, size);
    }

    public CGUI(Plugin plugin, int size) {
        super(plugin, size);
    }

    public CGUI(Plugin plugin, FixedSize size, Component title) {
        super(plugin, size, title);
    }

    public CGUI(Plugin plugin, int size, Component title) {
        super(plugin, size, title);
    }

    @Override
    protected void defaultBTLogic(int slot, InventoryClickEvent e) {
        if(buttons.containsKey(slot)){
            buttons.get(slot).click(e);
        }
    }

    /**
     * Adds button to selected slot
     * @param slot Slot in which button will be located
     * @param button Button
     * */
    protected void addButton(int slot, CButton button) {
        buttons.put(slot, button);
    }

    /**
     * Adds button to selected slot
     * @param slot Slot in which button will be located
     * @param button Button
     * @param stack ItemStack representing button
     * @param untouchable Should button be marked as untouchable or not
     * */
    protected void addButton(int slot, CButton button, ItemStack stack, boolean untouchable) {
        addButton(slot, button);
        setItem(slot, stack, untouchable);
    }
}
