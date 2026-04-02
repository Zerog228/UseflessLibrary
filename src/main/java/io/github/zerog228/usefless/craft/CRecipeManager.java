package io.github.zerog228.usefless.craft;

import io.github.zerog228.usefless.UseflessLibrary;
import org.bukkit.Keyed;
import org.bukkit.inventory.Recipe;

import java.util.concurrent.ConcurrentLinkedQueue;

public class CRecipeManager {
    private static final ConcurrentLinkedQueue<Recipe> recipes = new ConcurrentLinkedQueue<>();

    /**
     * Add recipe to a recipe list. Skips duplicate recipes
     * @return 'true' if registered new recipe
     * */
    public static boolean add(Recipe recipe){
        return add(recipe, true);
    }

    /**
     * Add recipe to a recipe list
     * @param skipIfExists Should it skip recipe if it already exists or not
     * @return 'true' if registered new recipe, 'else' otherwise
     * */
    public static boolean add(Recipe recipe, boolean skipIfExists){
        if(!recipes.contains(recipe)){
            innerAdd(recipe);
            return true;
        }else if(skipIfExists){
            return false;
        }

        remove(recipe);
        innerAdd(recipe);
        return true;
    }

    /**
     * Removes recipe from inner list and from server
     * @return 'true' if recipe was keyed and was removed, 'false' otherwise
     * */
    public static boolean remove(Recipe recipe){
        if(recipe instanceof Keyed keyed){
            UseflessLibrary.getPlugin().getServer().removeRecipe(keyed.getKey());
            return true;
        }
        return false;
    }

    private static void innerAdd(Recipe recipe){
        recipes.add(recipe);
        UseflessLibrary.getPlugin().getServer().addRecipe(recipe);
    }

    /**
     * Removes all recipes from inner list and deletes them from server
     * */
    public static void removeAll(){
        for(Recipe recipe : recipes){
            remove(recipe);
        }
    }
}
