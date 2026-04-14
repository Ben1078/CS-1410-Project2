package gui;

import recipeManager.Recipe;

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

    private boolean isEditing = false;
    private List<JTextField> ingredientsList = new ArrayList<>();

    public ViewRecipe(Frame parent) {
        super(parent, true);
        setContentPane(contentPane);

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
        // todo pull ingredients from recipe and display them (editable)
    }

    private void onCancel() {
        toggleEditing();
    }

    public void onConfirm() {
        toggleEditing();
        // todo update recipe
        // todo save recipe
    }
}
