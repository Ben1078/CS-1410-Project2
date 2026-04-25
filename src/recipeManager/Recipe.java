/**
 * @Author Benjamin Paul
 */

package recipeManager;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents a recipe, containing a name, a list of ingredients,
 * preparation instructions, and an image.
 * @author Benjamin Paul
 */
public class Recipe implements Serializable {
    private String name;
    private ArrayList<String> ingredients;
    private String instructions;
    private String imagePath;

    /**
     *
     * @param name name of the recipe
     * @param ingredients list of ingredients for recipe
     * @param instructions String explaining how to make the recipe
     * @param imagePath path to recipe's image in ./images
     */
    public Recipe(String name, ArrayList<String> ingredients, String instructions, String imagePath) {
        this.name = name;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.imagePath = imagePath;
    }

    /**
     * Sets toString() to the specific format for the GUI
     * @return
     */
    @Override
    public String toString() {
        // You can format this however you want! Here is a clean example:
        return name + " (Ingredients: " + ingredients.size() + ") - Path: " + imagePath;
    }

    // ====================== Getters and setters ======================

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
