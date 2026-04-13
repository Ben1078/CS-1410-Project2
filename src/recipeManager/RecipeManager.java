package recipeManager;

import java.util.ArrayList;

public class RecipeManager {
    private ArrayList<Recipe> recipeList;

    public RecipeManager() {
        this.recipeList = FileManager.loadRecipes();
    }

    public void addRecipe(Recipe recipe) {
        recipeList.add(recipe);
    }

    public void removeRecipe(Recipe recipe) {
        recipeList.remove(recipe);
    }

    public void editRecipe(Recipe recipe) {
        for (int i = 0; i <recipeList.size(); i++) {
            Recipe current = recipeList.get(i);

            if (current.getName().equals(recipe.getName())) {

                recipeList.set(i, recipe);
                return;
            }
        }
    }

    public ArrayList<Recipe> getRecipes() {
        return recipeList;
    }
}
