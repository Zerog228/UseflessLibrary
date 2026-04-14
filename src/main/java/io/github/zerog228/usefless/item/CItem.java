package io.github.zerog228.usefless.item;

import io.github.zerog228.usefless.UseflessLibrary;
import org.bukkit.event.Listener;

import java.util.List;

import static org.bukkit.Bukkit.getServer;

public abstract class CItem extends CBasicItem implements Listener {
    /**
     * Creates custom item with defined item key
     * */
    public CItem(String itemKey) {
        super(itemKey);
        getServer().getPluginManager().registerEvents(this, UseflessLibrary.getPlugin());
    }

    public CItem(String itemKey, List<ICItem> list) {
        this(itemKey);
        list.add(this);
    }
}
