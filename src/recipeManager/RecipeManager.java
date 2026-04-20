package recipeManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class RecipeManager {
    private ArrayList<Recipe> recipeList;

    public RecipeManager() {
        loadRecipes();
    }

    public void addRecipe(Recipe recipe) {
        recipeList.add(recipe);
        saveRecipes();
    }

    public void removeRecipe(Recipe recipe) {
        try {
            Files.deleteIfExists(Path.of(recipe.getImagePath()));
        } catch (IOException e) {
            System.err.println("Error deleting recipe image: " + e.getMessage());
        }
        recipeList.remove(recipe);
        saveRecipes();
    }

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

    public void loadRecipes() {
        recipeList = FileManager.loadRecipes();
    }

    public void saveRecipes() {
        FileManager.saveRecipes(recipeList);
    }

    public ArrayList<Recipe> getRecipes() {
        return recipeList;
    }


}
