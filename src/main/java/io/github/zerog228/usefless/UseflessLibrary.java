package io.github.zerog228.usefless;

import io.github.zerog228.usefless.craft.CRecipeManager;
import lombok.Getter;
import io.github.zerog228.usefless.event.EventListeners;
import org.bukkit.plugin.java.JavaPlugin;

public final class UseflessLibrary {

    // For other classes in our library
    @Getter
    private static JavaPlugin plugin;

    // This method must not be used anywhere in the library and should be called on plugin enable!
    public static void setPlugin(final JavaPlugin plugin) {
        UseflessLibrary.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(new EventListeners(), plugin);
    }

    /**
     * This method should be called on plugin disable
     * */
    public static void onDisable(){
        CRecipeManager.removeAll();
    }
}
