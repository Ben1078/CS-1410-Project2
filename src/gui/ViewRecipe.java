package gui;

import recipeManager.Recipe;
import recipeManager.RecipeManager;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ViewRecipe extends JDialog {
    private static final Color DIALOG_BACKGROUND = new Color(245, 247, 250);
    private static final Color SURFACE_BACKGROUND = Color.WHITE;
    private static final Color IMAGE_PLACEHOLDER = new Color(222, 228, 237);
    private static final Color HEADING_TEXT = new Color(34, 45, 62);
    private static final Color BODY_TEXT = new Color(74, 85, 104);
    private static final Color ACCENT = new Color(255, 196, 61);
    private static final Color DANGER = new Color(201, 59, 48);

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
    private JLabel imagePlaceholderLabel;

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

        applyStyling();
        displayRecipe(recipe);
        pack();
        applyDialogSize(parent);
        setLocationRelativeTo(parent != null ? parent : null);
    }

    private void applyDialogSize(Frame parent) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int referenceWidth = parent != null && parent.getWidth() > 0 ? parent.getWidth() : screenSize.width;
        int referenceHeight = parent != null && parent.getHeight() > 0 ? parent.getHeight() : screenSize.height;

        int width = Math.max(820, (int) (referenceWidth * 0.68));
        int height = Math.max(680, (int) (referenceHeight * 0.82));

        width = Math.min(width, screenSize.width - 120);
        height = Math.min(height, screenSize.height - 120);

        Dimension dialogSize = new Dimension(width, height);
        setMinimumSize(new Dimension(760, 620));
        setPreferredSize(dialogSize);
        setSize(dialogSize);
    }

    private void addIngredientTextField(String ingredient) {
        JTextField ingredientTextField = new JTextField(ingredient);
        ingredientTextField.setMinimumSize(new Dimension(320, 38));
        ingredientTextField.setPreferredSize(new Dimension(420, 38));
        ingredientTextField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        ingredientTextField.setFont(new Font("SansSerif", Font.PLAIN, 15));
        ingredientTextField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(208, 214, 224), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        ingredientsList.add(ingredientTextField);
        updateIngredients();
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
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(0, 0, 10, 10);
        gridBagConstraints.weightx = 1.0;

        for (JTextField ingredientTextField : ingredientsList) {
            gridBagConstraints.gridx = 0; gridBagConstraints.gridy = index;
            ingredientsPanel.add(ingredientTextField, gridBagConstraints);

            JButton removeIngredientButton = new JButton("X");
            removeIngredientButton.setName("removeIngredientButton" + index);
            styleActionButton(removeIngredientButton, DANGER, Color.WHITE);
            removeIngredientButton.setPreferredSize(new Dimension(48, 36));
            removeIngredientButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    onRemoveIngredient(ingredientTextField, removeIngredientButton);
                }
            });

            gridBagConstraints.gridx = 1;
            gridBagConstraints.weightx = 0.0;
            ingredientsPanel.add(removeIngredientButton, gridBagConstraints);

            index++;
        }

        refreshDialogLayout();
    }

    private void displayRecipe(Recipe recipe) {
        recipeTitleLabel.setText(recipe.getName());
        recipeTitleTextField.setText(recipe.getName());
        updateRecipeImage(recipe.getImagePath());

        StringBuilder ingredientsSB = new StringBuilder();

        for (int i = 0; i < recipe.getIngredients().size(); i++) {
            String ingredient = recipe.getIngredients().get(i);
            ingredientsSB.append((i > 0 ? "\n- " : "- ")).append(ingredient);
        }

        ingredientsTextArea.setText(ingredientsSB.toString());
        instructionsTextArea.setText(recipe.getInstructions());
    }

    private void updateRecipeImage(String imagePath) {
        if (imagePath == null || imagePath.isBlank()) {
            imagePlaceholderLabel.setText("No Recipe Image");
            imagePlaceholderLabel.setIcon(null);
            return;
        }

        File imageFile = new File(imagePath);
        if (!imageFile.exists()) {
            imagePlaceholderLabel.setText("Image Not Found");
            imagePlaceholderLabel.setIcon(null);
            return;
        }

        ImageIcon imageIcon = new ImageIcon(imagePath);
        int targetWidth = Math.max(420, recipeImagePanel.getPreferredSize().width - 24);
        int targetHeight = Math.max(180, recipeImagePanel.getPreferredSize().height - 24);
        Image scaledImage = imageIcon.getImage().getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);

        imagePlaceholderLabel.setText("");
        imagePlaceholderLabel.setIcon(new ImageIcon(scaledImage));
    }

    private void applyStyling() {
        Border sectionPadding = BorderFactory.createEmptyBorder(12, 14, 12, 14);

        contentPane.setBackground(DIALOG_BACKGROUND);

        recipeImagePanel.setLayout(new BorderLayout());
        recipeImagePanel.setBackground(IMAGE_PLACEHOLDER);
        recipeImagePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(202, 210, 220), 1),
                BorderFactory.createEmptyBorder(24, 24, 24, 24)
        ));

        imagePlaceholderLabel = new JLabel("Recipe Image", SwingConstants.CENTER);
        imagePlaceholderLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        imagePlaceholderLabel.setForeground(new Color(111, 122, 138));
        recipeImagePanel.add(imagePlaceholderLabel, BorderLayout.CENTER);

        recipeTitleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        recipeTitleLabel.setForeground(HEADING_TEXT);
        recipeTitleTextField.setFont(new Font("SansSerif", Font.BOLD, 22));
        recipeTitleTextField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(208, 214, 224), 1),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));

        styleSectionLabel(ingredientsLabel);
        styleSectionLabel(instructionsLabel);
        styleSectionLabel(insertTitleLabel);

        ingredientsTextArea.setLineWrap(true);
        ingredientsTextArea.setWrapStyleWord(true);
        ingredientsTextArea.setFont(new Font("SansSerif", Font.PLAIN, 16));
        ingredientsTextArea.setForeground(BODY_TEXT);
        ingredientsTextArea.setBackground(SURFACE_BACKGROUND);
        ingredientsTextArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(222, 228, 237), 1),
                sectionPadding
        ));

        instructionsTextArea.setLineWrap(true);
        instructionsTextArea.setWrapStyleWord(true);
        instructionsTextArea.setFont(new Font("SansSerif", Font.PLAIN, 16));
        instructionsTextArea.setForeground(BODY_TEXT);
        instructionsTextArea.setBackground(SURFACE_BACKGROUND);
        instructionsTextArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(222, 228, 237), 1),
                BorderFactory.createEmptyBorder(14, 14, 14, 14)
        ));

        ingredientsPanel.setBackground(DIALOG_BACKGROUND);

        styleActionButton(backButton, new Color(228, 233, 240), HEADING_TEXT);
        styleActionButton(editButton, ACCENT, HEADING_TEXT);
        styleActionButton(addIngredientButton, new Color(225, 234, 246), HEADING_TEXT);
        styleActionButton(deleteRecipeButton, DANGER, Color.WHITE);
    }

    private void styleSectionLabel(JLabel label) {
        label.setFont(new Font("SansSerif", Font.BOLD, 16));
        label.setForeground(HEADING_TEXT);
    }

    private void styleActionButton(JButton button, Color background, Color foreground) {
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
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

        refreshDialogLayout();
    }

    private void refreshDialogLayout() {
        ingredientsPanel.revalidate();
        ingredientsPanel.repaint();
        contentPane.revalidate();
        contentPane.repaint();
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
