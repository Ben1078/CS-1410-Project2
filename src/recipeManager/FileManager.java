package recipeManager;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Handles the saving and loading of recipes.
 * @author Benjamin Paul
 */
public class FileManager {
    private static String FILE_NAME = "./resources/recipes.dat";

    /**
     * Saves the provided list of recipes to the current file path.
     * @param recipes the list of {@code Recipe} objects to save.
     */
    public static void saveRecipes(ArrayList<Recipe> recipes) {
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get(FILE_NAME)))) {

            oos.writeObject(recipes);

        } catch (IOException e) {
            System.err.println("Error saving recipes to file: " + e.getMessage());
        }
    }

    /**
     * Reads and loads the recipe list from the current file path.
     * If the file does not exist, returns an empty list.
     * @return the list of {@code Recipe} objects loaded.
     */
    @SuppressWarnings("unchecked") // Suppresses a warning about casting to ArrayList
    public static ArrayList<Recipe> loadRecipes() {
        ArrayList<Recipe> recipes = new ArrayList<>();
        File file = new File(FILE_NAME);

        // Only try to read if the file actually exists (prevents errors on first run)
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(file.toPath()))) {

                // Read the object and cast it back to an ArrayList<Recipe>
                recipes = (ArrayList<Recipe>) ois.readObject();

            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error loading recipes from file: " + e.getMessage());
            }
        }

        return recipes;
    }

    public static void setFilePath(String filePath) {
        FILE_NAME = filePath;
    }

    public static String getFilePath() {
        return FILE_NAME;
    }
}