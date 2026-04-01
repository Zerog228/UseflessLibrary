package io.github.zerog228.usefless.config;

import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

public class CConfigSingleton {

    private static final ConcurrentHashMap<File, CConfig> configs = new ConcurrentHashMap<>();

    /**
     * Gets or creates an instance of a config singleton.
     * @param path Path should point to a .yml configuration file from plugin data folder.
     *             (i.e. 'test/config.yml' if it's 'plugins/our_plugin_data_folder/test/config.yml')
     * */
    public static CConfig get(Plugin plugin, String path){
        return get(new File(plugin.getDataFolder() + File.separator + path));
    }

    /**
     * Gets or creates an instance of a config singleton.
     * @param file File that points to a .yml configuration file from plugin data folder.
     *             (i.e. 'plugins/our_plugin_data_folder/test/config.yml')
     * */
    public static CConfig get(File file){
        if(!configs.containsKey(file)){
            configs.put(file, new CConfig(file));
        }
        return configs.get(file);
    }

    /**
     * Creates new instance of a config
     * @return Previously associated value
     */
    public static CConfig put(Plugin plugin, String path, CConfig config){
        return configs.put(new File(plugin.getDataFolder() + File.separator + path), config);
    }

    /**
     * Creates new instance of a config
     * @return Previously associated value
     * */
    public static CConfig put(File file, CConfig config){
        return configs.put(file, config);
    }
}
