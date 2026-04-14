package gui;

import recipeManager.Recipe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class MainPage {
    private JPanel sidebar;
    private JPanel recipeDisplay;
    private JPanel main;
    private Frame mainFrame;

    public MainPage(Frame mainFrame) {
        this.mainFrame = mainFrame;
        test();
    }

    // TODO DEBUG REMOVE
    public void test() {
        // recipes
        for (int i = 0; i < 10; i++) {
            JPanel recipeCard = new JPanel();
            recipeCard.setPreferredSize(new Dimension(300, 200));
            recipeCard.setBorder(BorderFactory.createLineBorder(Color.RED));
            recipeCard.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    ViewRecipe viewRecipeDialog = new ViewRecipe(mainFrame);
                    viewRecipeDialog.setVisible(true);
                }
            });
            recipeDisplay.add(recipeCard);
        }

        // sidebar
            // constraints
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = GridBagConstraints.NORTH;

            // create recipe button
        gridBagConstraints.gridx = 0; gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(15, 0, 0, 0);
        JButton addRecipeButton = new JButton("Add Recipe");
        addRecipeButton.addActionListener(e -> {
            AddRecipeDialog dialog = new AddRecipeDialog(mainFrame);
            dialog.setVisible(true);
        });
        sidebar.add(addRecipeButton, gridBagConstraints);

            // space
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weighty = 0.5;
        sidebar.add(Box.createGlue(), gridBagConstraints);

            // search bar and search button
        JPanel pairPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        pairPanel.add(new TextField());
        pairPanel.add(new JButton("Button 3"));
        gridBagConstraints.gridy = 2;
        gridBagConstraints.weighty = 0;
        sidebar.add(pairPanel, gridBagConstraints);

            // space
        gridBagConstraints.gridy = 3;
        gridBagConstraints.weighty = 1;
        sidebar.add(Box.createGlue(), gridBagConstraints);

            // settings button
        gridBagConstraints.gridy = 4;
        gridBagConstraints.weighty = 0;
        gridBagConstraints.insets = new Insets(0, 0, 15, 0);
        sidebar.add(new JButton("Button 4"), gridBagConstraints);
    }

    public void display() {
        // todo display search bar
        // todo display search button
        // todo display settings button
        // todo display recipe cards
    }

    public void displayRecipes(List<Recipe> recipes) {
        // todo sort alphabetically

        for (Recipe recipe : recipes) {
            String recipeName = recipe.getName();
            String imagePath = recipe.getImagePath();

            // todo create card for recipe
        }
    }

    /**
     * Returns a list of recipes that have a name that matches the passed string.
     * @param recipes the list of recipes to search from (haystack).
     * @param searchStr the string to match recipe names to (needle).
     */
    public List<Recipe> searchRecipe(List<Recipe> recipes, String searchStr) {
        List<Recipe> recipeList = new ArrayList<>();

        for (Recipe recipe : recipes) {
            if (recipe.getName().toLowerCase().contains(searchStr.toLowerCase())) {
                recipeList.add(recipe);
            }
        }

        return recipeList;
    }

    public void openSettings() {

    }

    public JPanel getContentPanel() {
        return this.main;
    }
}