# **Library with many random things for Minecraft Plugin development!**

## **You will need to initialize this library in your "onEnable()" method in "Plugin.java"**
```
@Override
public void onEnable() {
    UseflessLibrary.setPlugin(this);
}
```

## **Most useful things:**
- ### Persistent data/meta container wrapper

Instead of writing *this* for setting data
```
ItemMeta meta = container.getItemMeta();
PersistentDataContainer container = meta.getPersistentDataContainer();
container.set(new NamespacedKey(YourPlugin.getPlugin(), KEY), PersistentDataType.TYPE, value);
itemstack.setItemMeta(meta);
```
or *this* for checking data <br/>

`blueprintStack.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(YourPlugin.getPlugin(), KEY));`

you can use built-in class '*PersistentData*':
```
itemstack.setItemMeta((ItemMeta) PersistentData.setData(itemstack.getItemMeta(), KEY, value)); //Setting data
PersistentData.getDataS(stack.getItemMeta(), KEY); //Getting data
PersistentData.hasData(stack.getItemMeta(), KEY); //Check if it has data
```
It also works with every PersistentDataHolder (Entities, Chunks, Items etc.)


- ### Custom item registy
You can register custom items just like in mods with 3 easy steps:

1. Create class with your custom item registries where you will register your item:
```
public class CustomItemRegistries {
    public static CItem customItem;

    public static void register(){
        undoButtonItem = new CustomItem("custom_item"); //For now it will glow red because we haven't added custom item yet
    }
}
```
2. Create CustomItem class that extends CItem. Because 'CItem' implements EventListener, we can also add "onClick" event:
```
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
```
@Override
public void onEnable() {
    UseflessLibrary.setPlugin(this);
    CustomItemRegistries.register();
}
```
Voilà!
