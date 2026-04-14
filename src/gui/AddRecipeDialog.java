package gui;

import recipeManager.Recipe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class AddRecipeDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JPanel addRecipeFormPanel;
    private JTextField recipeNameTextField;
    private JLabel recipeNameLabel;
    private JButton addIngredientButton;
    private JLabel directionsLabel;
    private JTextArea directionsTextField;
    private JPanel ingredientsPanel;
    private JTextField labelTextField;

    private List<JTextField> ingredientsList = new ArrayList<>();

    public AddRecipeDialog(Frame parent) {
        super(parent, "Add New Recipe", true);
        setContentPane(contentPane);
        getRootPane().setDefaultButton(buttonOK);

        int width = (int) (parent.getWidth() * 0.3);
        int height = (int) (parent.getHeight() * 0.45);
        setPreferredSize(new Dimension(width, height));

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        setup();
        pack();
        setLocationRelativeTo(parent);
    }

    private void setup() {
        addRecipeFormPanel.setLayout(new GridBagLayout());

        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;

            // recipe name
        gridBagConstraints.gridx = 0; gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 0.25;
        addRecipeFormPanel.add(recipeNameLabel, gridBagConstraints);

        gridBagConstraints.gridy = 1;
        addRecipeFormPanel.add(recipeNameTextField, gridBagConstraints);

            // recipe ingredients
        gridBagConstraints.gridy = 2;
        addIngredientButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addIngredient();
            }
        });
        addRecipeFormPanel.add(addIngredientButton, gridBagConstraints);

        gridBagConstraints.gridy = 3;
        addRecipeFormPanel.add(ingredientsPanel, gridBagConstraints);

            // recipe directions
        gridBagConstraints.gridy = 4;
        addRecipeFormPanel.add(directionsLabel, gridBagConstraints);

        gridBagConstraints.gridy = 5;
        addRecipeFormPanel.add(directionsTextField, gridBagConstraints);
    }

    private void addIngredient() {
        JTextField ingredientTextField = new JTextField();
        int index = ingredientsList.size();
        ingredientTextField.setName("ingredientTextField" + index);
        int width = (int) (this.getWidth() * 0.8);
        int height = 25;
        ingredientTextField.setPreferredSize(new Dimension(width, height));

        ingredientsList.add(ingredientTextField);

        JButton removeIngredientButton = new JButton("X");
        removeIngredientButton.setName("removeIngredientButton" + index);
        removeIngredientButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeIngredient(ingredientTextField, removeIngredientButton);
            }
        });

        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0; gridBagConstraints.gridy = index;
        ingredientsPanel.add(ingredientTextField, gridBagConstraints);

        gridBagConstraints.gridx = 1;
        ingredientsPanel.add(removeIngredientButton, gridBagConstraints);

        pack();
    }

    private void removeIngredient(JTextField ingredientTextField, JButton button) {
        ingredientsPanel.remove(ingredientTextField);
        ingredientsPanel.remove(button);
        ingredientsList.remove(ingredientTextField);

        updateIngredients();
    }

    private void updateIngredients() {
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
                    removeIngredient(ingredientTextField, removeIngredientButton);
                }
            });

            gridBagConstraints.gridx = 1;
            ingredientsPanel.add(removeIngredientButton, gridBagConstraints);

            index++;
        }

        pack();
    }

    private List<String> getIngredients() {
        List<String> ingredients = new ArrayList<>();

        for (JTextField ingredientTextField : ingredientsList) {
            ingredients.add(ingredientTextField.getText());
        }

        return ingredients;
    }

    private void onOK() {
        String recipeName = recipeNameTextField.getText();
        List<String> ingredients = getIngredients();
        String directions = directionsTextField.getText();
        String imagePath = "";

        Recipe recipe = new Recipe(recipeName, (ArrayList<String>) ingredients, directions, imagePath);
        // todo register recipe with RecipeManager

        System.out.println(recipe);

        dispose();
    }

    private void onCancel() {
        dispose();
    }
}