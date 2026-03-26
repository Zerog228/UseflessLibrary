package me.zink.useflessLibrary;

import lombok.Getter;
import me.zink.useflessLibrary.event.EventListeners;
import org.bukkit.plugin.java.JavaPlugin;

public final class UseflessLibrary extends JavaPlugin {

    // For other classes in our library
    @Getter
    private static JavaPlugin plugin;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new EventListeners(), plugin);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    // This method must not be used anywhere in the library!
    public static void setPlugin(final JavaPlugin plugin) {
        UseflessLibrary.plugin = plugin;
    }
}
