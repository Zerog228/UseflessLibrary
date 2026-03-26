package me.zink.useflessLibrary;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public final class UseflessLibrary extends JavaPlugin {

    // For other classes in our library
    @Getter
    private static JavaPlugin plugin;

    /*@Override
    public void onEnable() {
        // Plugin startup logic
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }*/

    // This method must not be used anywhere in the library!
    public static void setPlugin(final JavaPlugin plugin) {
        UseflessLibrary.plugin = plugin;
    }
}
