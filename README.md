# **Library with many random things for Minecraft Plugin development!**

**Maven dependency:**
```xml
<dependency>
    <groupId>io.github.zerog228</groupId>
    <artifactId>UseflessLibrary</artifactId>
    <version>1.0.3</version>
</dependency>
```

## **You will need to initialize this library in your "onEnable()" method in "Plugin.java"**
```Java
@Override
public void onEnable() {
    UseflessLibrary.setPlugin(this);
}
```

# **Table of contents**
- [PersistentData](#persistent-data-meta-container-wrapper)
- [Custom Items](#custom-item-registy)
- [Custom Structures](#custom-structures)

# **Showcase-Plugins**
- [Ave Maria](https://github.com/Zerog228/AveMaria)


# Persistent data-meta container wrapper
Instead of writing *this* for setting data
```Java
ItemMeta meta = container.getItemMeta();
PersistentDataContainer container = meta.getPersistentDataContainer();
container.set(new NamespacedKey(YourPlugin.getPlugin(), KEY), PersistentDataType.TYPE, value);
itemstack.setItemMeta(meta);
```
or *this* for checking data <br/>

`blueprintStack.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(YourPlugin.getPlugin(), KEY));`

you can use built-in class '*PersistentData*':
```Java
itemstack.setItemMeta((ItemMeta) PersistentData.setData(itemstack.getItemMeta(), KEY, value)); //Setting data
PersistentData.getDataS(stack.getItemMeta(), KEY); //Getting data
PersistentData.hasData(stack.getItemMeta(), KEY); //Check if it has data
```
It also works with every PersistentDataHolder (Entities, Chunks, Items etc.)


# Custom item registy
You can register custom items just like in mods with 3 easy steps:

1. Create class with your custom item registries where you will register your item:
```Java
public class CustomItemRegistries {
    public static CItem customItem;

    public static void register(){
        undoButtonItem = new CustomItem("custom_item"); //For now it will glow red because we haven't added custom item yet
    }
}
```
2. Create CustomItem class that extends CItem. Because 'CItem' implements EventListener, we can also add "onClick" event:
```Java
public class CustomItem extends CItem {

    public CustomItem(String namespaceKey) {
        super(namespaceKey); //'namespace' is the tag that will be used in pair with your item. It should be lowercase, english latters and numbers, without spaces
    }

    //Here you should create your item instance.
    @Override
    public ItemStack initItem(String itemKey) {
        // CStackCreator will come in handy for it. I will discuss its functionality later
        return CStackCreator.builder(Material.ARROW)
                .maxStackSize(1)
                .name("<red>Undo")
                .build();
    }

    @Override
    public void initRecipe() {}

    //Event handler for our test item
    @EventHandler
    public void onClick(PlayerInteractEvent e){
        /*
        *This event fires every time player interacts with something
        *To narrow it down to only interactions with our item we can use 'equals()'.
        *This is a built-in method for comparing provided item with our custom item instance by checking itemKey
        */
        if(equals(e.getItem()) && e.getAction() == Action.RIGHT_CLICK_AIR){
            e.getPlayer().sendMessage("Clicked!");
        }
    }
}
```
3. Initialize item registries class in your "onEnable()" method right after initializing library
```Java
@Override
public void onEnable() {
    UseflessLibrary.setPlugin(this);
    CustomItemRegistries.register();
}
```
<img width="1060" height="203" alt="image" src="https://github.com/user-attachments/assets/c731dc24-c6b1-4f1c-bc52-57a389a2d68a" />

Voilà!


# Custom structures
To register custom structure you will need to follow this steps:
1. Build structure in world and save it using structure blocks
<img width="1295" height="970" alt="image" src="https://github.com/user-attachments/assets/68b6d661-d699-4ce8-8a71-d53807aee8fe" />

2. Create folder in the "resources" path and place your structure here
<img width="1010" height="243" alt="image" src="https://github.com/user-attachments/assets/5e9ff450-8487-45f2-8681-1865bbd82bff" />

3. Place your structure in your plugin subfolder in "server/your_plugin" and register it in "onEnable()" method using CStructure.init() </br>
You can do all of this automatically using following code:
```Java
    @Override
    public void onEnable() {
        plugin = this;
        UseflessLibrary.setPlugin(plugin);

        //Registering items
        CustomItemRegistries.init();

        //This line of code automatically copies all files from "resources/structures/" to "server/plugins/your_plugin/structures/"
        CFileUtils.copyResources(plugin, "structures", "structures", 1, false);
        //This line of code automatically initializes all structures from "server/plugins/your_plugin/structures/"
        CStructure.initAllFromPath(Path.of(plugin.getDataPath().toString(), "structures"), true, true);
    }
```

!!! **NOTE THAT YOU WILL NEED TO DISABLE FILTERING IF YOU PLACE ANYTHING IN "RESOURCE" FOLDER** !!! </br>
!!! **FILES WILL BE MALFORMED IF YOU WON'T DO THAT** </br>
<img width="456" height="144" alt="image" src="https://github.com/user-attachments/assets/703cca10-719e-4727-ab5f-a5122f58c86b" />

4. Best practice in this step would be creating separate "MegaCrafterLogic" class where all related to structure logic will be located and registering it in the "onClick" event listener </br>
You can also register event listener inside this class, but don't forget to initialize it in "onEnable()" method
```Java
public class MegaCrafterLogic implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e){
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() == Material.BEACON){
            //Checking if it's our structure
            if(checkStructure(e.getClickedBlock())){
                e.setCancelled(true); //Cancel block clock event
                e.getPlayer().sendMessage("Clicked on structure!");
            }
        }
    }

    private static boolean checkStructure(Block block){
        //Creating zone where structure could be located
        Zone zone = new Zone(block.getRelative(-2, -1, -2), block.getRelative(2, 2, 2));

        //Comparing our zone with reference
        return CStructureHelper.compare(block, zone, "mega_crafter.nbt", true, true);
    }
}
```

Registering event listener in the "onEnable()" method
```Java
    @Override
    public void onEnable() {
        plugin = this;
        UseflessLibrary.setPlugin(plugin);

        //Registering items
        CustomItemRegistries.init();

        //This line of code automatically copies all files from "resources/structures/" to "server/plugins/your_plugin/structures/"
        CFileUtils.copyResources(plugin, "structures", "structures", 1, false);
        //This line of code automatically initializes all structures from "server/plugins/your_plugin/structures/"
        CStructure.initAllFromPath(Path.of(plugin.getDataPath().toString(), "structures"), true, true);

        //Registering our custom event listener from MegaCrafter
        plugin.getServer().getPluginManager().registerEvents(new MegaCrafterLogic(), plugin);
    }
```
<img width="1416" height="911" alt="image" src="https://github.com/user-attachments/assets/7625aebe-4d06-4a0a-a8f2-cb947fdf12dc" />
Voilà!


# Custom GUI (Guide W.I.P.)
