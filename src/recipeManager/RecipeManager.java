package recipeManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Manages and maintains a list of recipes.
 * @author Benjamin Paul
 */
public class RecipeManager {
    private ArrayList<Recipe> recipeList;

    /**
     * Instantiates the manager and loads recipes.
     */
    public RecipeManager() {
        loadRecipes();
    }

    /**
     * Adds a new recipe to the list and saves the list of recipes.
     * @param recipe the {@code Recipe} object to be added.
     */
    public void addRecipe(Recipe recipe) {
        recipeList.add(recipe);
        saveRecipes();
    }

    /**
     * Deletes a recipe from the list, removes its image file,
     * and saves the list of recipes.
     * @param recipe the {@code Recipe} object to be removed.
     */
    public void removeRecipe(Recipe recipe) {
        try {
            Files.deleteIfExists(Path.of(recipe.getImagePath()));
        } catch (IOException e) {
            System.err.println("Error deleting recipe image: " + e.getMessage());
        }
        recipeList.remove(recipe);
        saveRecipes();
    }

    /**
     * Updates a recipe by matching the name and replacing it with a new recipe.
     * @param recipe the new {@code Recipe}.
     */
    public void editRecipe(Recipe recipe) {
        for (int i = 0; i <recipeList.size(); i++) {
            Recipe current = recipeList.get(i);

            if (current.getName().equals(recipe.getName())) {

                recipeList.set(i, recipe);
                saveRecipes();
                return;
            }
        }
    }

    /**
     * Loads a recipe from the file.
     */
    public void loadRecipes() {
        recipeList = FileManager.loadRecipes();
    }

    /**
     * Saves the current list of recipes.
     */
    public void saveRecipes() {
        FileManager.saveRecipes(recipeList);
    }

    public ArrayList<Recipe> getRecipes() {
        return recipeList;
    }
}