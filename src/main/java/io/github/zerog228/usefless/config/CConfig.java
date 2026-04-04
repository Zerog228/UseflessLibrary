package io.github.zerog228.usefless.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class CConfig {

    protected final File file;
    protected YamlConfiguration config;

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
            }catch (Exception exception){
                System.err.println("Exception on creating new file with path "+file.getPath()+"! "+exception.getMessage());
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
            }catch (Exception exception){
                System.err.println("Error on getting config! "+exception.getMessage());
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
        }catch (Exception exception){
            System.err.println("Failed to save config! "+exception.getMessage());
        }
    }

}
