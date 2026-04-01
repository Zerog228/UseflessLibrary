package me.zink.UseflessLibrary.gui;

import org.bukkit.event.inventory.InventoryClickEvent;

public class CButton extends CPrimitiveButton<InventoryClickEvent, Void>{
    public CButton(OnClick<InventoryClickEvent, Void> onClick) {
        super(onClick);
    }
}
