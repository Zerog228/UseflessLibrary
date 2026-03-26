package me.zink.useflessLibrary.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class CConfig {

    private final File file;
    private FileConfiguration config;

    /**
     * Create an instance of a config.
     * @param path Path should point to a .yml configuration file from plugin data folder.
     *             (i.e. 'test/config.yml' if it's 'plugins/our_plugin_data_folder/test/config.yml')
     * */
    public CConfig(Plugin plugin, String path){
        this(new File(plugin.getDataFolder() + File.separator + path));
    }

    /**
     * Create an instance of a config.
     * @param file File that points to a .yml configuration file from plugin data folder.
     *             (i.e. 'plugins/our_plugin_data_folder/test/config.yml')
     * */
    public CConfig(File file){
        this.file = file;
        if(!file.exists()){
            try{
                file.createNewFile();
                config = YamlConfiguration.loadConfiguration(file);
            }catch (Exception ignored){
                System.err.println("Exception on creating new file with path "+file.getPath()+"!");
            }
        }
    }

    public FileConfiguration getConfig(){
        if(config == null){
            try{
                if(!file.exists()){
                    file.createNewFile();
                }
                config = YamlConfiguration.loadConfiguration(file);
            }catch (Exception ignored){
                System.err.println("Error on getting config!");
            }
        }
        return config;
    }

    public boolean delete(){
        config = null;
        return file.delete();
    }

    public void save(){
        try{
            config.save(file);
        }catch (Exception ignored){
            System.err.println("Failed to save config!");
        }
    }

}
