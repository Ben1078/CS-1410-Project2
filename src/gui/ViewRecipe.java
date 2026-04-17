package gui;

import recipeManager.Recipe;
import recipeManager.RecipeManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ViewRecipe extends JDialog {
    private JPanel contentPane;
    private JButton backButton;
    private JButton editButton;
    private JLabel recipeTitleLabel;
    private JPanel recipeImagePanel;
    private JLabel ingredientsLabel;
    private JLabel instructionsLabel;
    private JTextArea instructionsTextArea;
    private JPanel ingredientsPanel;
    private JTextField recipeTitleTextField;
    private JTextArea ingredientsTextArea;
    private JLabel insertTitleLabel;
    private JButton addIngredientButton;
    private JButton deleteRecipeButton;

    private boolean isEditing = false;
    private List<JTextField> ingredientsList = new ArrayList<>();
    private Recipe recipe;
    private MainPage mainPage;
    private Frame parent;
    private RecipeManager recipeManager;

    public ViewRecipe(Frame parent, MainPage mainPage, Recipe recipe, RecipeManager recipeManager) {
        super(parent, true);
        setContentPane(contentPane);

        this.parent = parent;
        this.recipeManager = recipeManager;
        this.mainPage = mainPage;
        this.recipe = recipe;

        int width = (int) (parent.getWidth() * 0.65);
        int height = (int) (parent.getHeight() * 0.8);
        setPreferredSize(new Dimension(width, height));

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (isEditing) {
                    onCancel();
                } else {
                    onBack();
                }
            }
        });
        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (isEditing) {
                    onConfirm();
                } else {
                    onEdit();
                }
            }
        });
        addIngredientButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onAddIngredient();
            }
        });
        deleteRecipeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onDeleteRecipe();
            }
        });

        displayRecipe(recipe);
        pack();
        setLocationRelativeTo(parent);
    }

    private void addIngredientTextField(String ingredient) {
        JTextField ingredientTextField = new JTextField(ingredient);
        int index = ingredientsList.size();
        int width = (int) (this.getWidth() * 0.8);
        int height = 25;
        ingredientTextField.setPreferredSize(new Dimension(width, height));

        ingredientsList.add(ingredientTextField);

        JButton removeIngredientButton = new JButton("X");
        removeIngredientButton.setName("removeIngredientButton" + index);
        removeIngredientButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onRemoveIngredient(ingredientTextField, removeIngredientButton);
            }
        });

        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0; gridBagConstraints.gridy = index;
        ingredientsPanel.add(ingredientTextField, gridBagConstraints);

        gridBagConstraints.gridx = 1;
        ingredientsPanel.add(removeIngredientButton, gridBagConstraints);

        pack();
    }

    public void onAddIngredient() {
        addIngredientTextField("");
    }

    public void onRemoveIngredient(JTextField ingredientTextField, JButton removeIngredientButton) {
        ingredientsPanel.remove(ingredientTextField);
        ingredientsPanel.remove(removeIngredientButton);
        ingredientsList.remove(ingredientTextField);

        updateIngredients();
    }

    public void updateIngredients() {
        ingredientsPanel.removeAll();

        int index = 0;

        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0; gridBagConstraints.gridy = 0;

        for (JTextField ingredientTextField : ingredientsList) {
            gridBagConstraints.gridx = 0; gridBagConstraints.gridy = index;
            ingredientsPanel.add(ingredientTextField, gridBagConstraints);

            JButton removeIngredientButton = new JButton("X");
            removeIngredientButton.setName("removeIngredientButton" + index);
            removeIngredientButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    onRemoveIngredient(ingredientTextField, removeIngredientButton);
                }
            });

            gridBagConstraints.gridx = 1;
            ingredientsPanel.add(removeIngredientButton, gridBagConstraints);

            index++;
        }

        pack();
    }

    private void displayRecipe(Recipe recipe) {
        recipeTitleLabel.setText(recipe.getName());

        StringBuilder ingredientsSB = new StringBuilder();

        for (int i = 0; i < recipe.getIngredients().size(); i++) {
            String ingredient = recipe.getIngredients().get(i);
            ingredientsSB.append((i > 0 ? "\n- " : "- ")).append(ingredient);
        }

        ingredientsTextArea.setText(ingredientsSB.toString());
        instructionsTextArea.setText(recipe.getInstructions());
    }

    private void toggleEditing() {
        if (isEditing) {
            backButton.setText("BACK");
            editButton.setText("EDIT");

            recipeTitleLabel.setVisible(true);
            recipeTitleTextField.setEditable(false);
            recipeTitleTextField.setVisible(false);
            instructionsTextArea.setEditable(false);
            instructionsTextArea.setFocusable(false);
            ingredientsTextArea.setVisible(true);
            insertTitleLabel.setVisible(false);
            addIngredientButton.setVisible(false);
        } else {
            backButton.setText("CANCEL");
            editButton.setText("CONFIRM");

            recipeTitleLabel.setVisible(false);
            recipeTitleTextField.setText(recipe.getName());
            recipeTitleTextField.setEditable(true);
            recipeTitleTextField.setVisible(true);
            instructionsTextArea.setEditable(true);
            instructionsTextArea.setFocusable(true);
            ingredientsTextArea.setVisible(false);
            insertTitleLabel.setVisible(true);
            addIngredientButton.setVisible(true);
        }

        isEditing = !isEditing;

        pack();
    }

    private void onBack() {
        dispose();
    }

    private void onEdit() {
        toggleEditing();
        for (String ingredient : recipe.getIngredients()) {
            addIngredientTextField(ingredient);
        }
    }

    private void onCancel() {
        ingredientsPanel.removeAll();
        ingredientsList.clear();
        toggleEditing();
        displayRecipe(recipe);
    }

    public void onConfirm() {
        toggleEditing();

        recipe.setName(recipeTitleTextField.getText());

        ArrayList<String> ingredients = new ArrayList<>();

        for (JTextField ingredientTextField : ingredientsList) {
            ingredients.add(ingredientTextField.getText());
        }

        ingredientsPanel.removeAll();
        ingredientsList.clear();

        recipe.setIngredients(ingredients);
        recipe.setInstructions(instructionsTextArea.getText());

        recipeManager.editRecipe(recipe);
        displayRecipe(recipe);
        mainPage.displayRecipes();
    }

    public void onDeleteRecipe() {
        int result = JOptionPane.showConfirmDialog(
                parent,
                "Are you sure you want to delete this recipe?\n(This cannot be undone!)",
                "Delete Recipe",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (result == JOptionPane.NO_OPTION) {
            return;
        }

        recipeManager.removeRecipe(recipe);
        mainPage.displayRecipes();
        dispose();
    }
}
