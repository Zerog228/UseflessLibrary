package io.github.zerog228.UseflessLibrary.gui;

import org.bukkit.event.inventory.InventoryClickEvent;

public class CButton extends CPrimitiveButton<InventoryClickEvent, Void>{
    public CButton(OnClick<InventoryClickEvent, Void> onClick) {
        super(onClick);
    }
}
