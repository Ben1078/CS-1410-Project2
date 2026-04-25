package gui;

import recipeManager.Recipe;
import recipeManager.RecipeManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class AddRecipeDialog extends JDialog {
    private static final Path IMAGE_DIRECTORY = Path.of("resources", "images");

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
    private JButton setImageButton;
    private JLabel selectedImageLabel;

    private List<JTextField> ingredientsList = new ArrayList<>();
    private List<JPanel> ingredientRows = new ArrayList<>();
    private RecipeManager recipeManager;
    private MainPage mainPage;
    private Path selectedImageSource;

    public AddRecipeDialog(Frame parent, MainPage mainPage, RecipeManager recipeManager) {
        super(parent, "Add New Recipe", true);
        this.recipeManager = recipeManager;
        this.mainPage = mainPage;
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
        setImageButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onSetImage();
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
        ingredientsPanel.setLayout(new BoxLayout(ingredientsPanel, BoxLayout.Y_AXIS));
        directionsTextField.setLineWrap(true);
        directionsTextField.setWrapStyleWord(true);

        addIngredientButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addIngredient();
            }
        });
    }

    private void addIngredient() {
        JTextField ingredientTextField = new JTextField();
        int index = ingredientsList.size();
        ingredientTextField.setName("ingredientTextField" + index);
        ingredientTextField.setColumns(28);
        ingredientTextField.setMinimumSize(new Dimension(320, 36));
        ingredientTextField.setPreferredSize(new Dimension(420, 36));
        ingredientTextField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));

        ingredientsList.add(ingredientTextField);

        JButton removeIngredientButton = new JButton("X");
        removeIngredientButton.setName("removeIngredientButton" + index);
        removeIngredientButton.setPreferredSize(new Dimension(48, 36));
        removeIngredientButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeIngredient(ingredientTextField, removeIngredientButton);
            }
        });

        JPanel ingredientRow = new JPanel(new BorderLayout(8, 0));
        ingredientRow.setOpaque(false);
        ingredientRow.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        ingredientRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        ingredientRow.setMinimumSize(new Dimension(380, 36));
        ingredientRow.setPreferredSize(new Dimension(468, 36));
        ingredientRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        ingredientRow.add(ingredientTextField, BorderLayout.CENTER);
        ingredientRow.add(removeIngredientButton, BorderLayout.EAST);

        ingredientRows.add(ingredientRow);
        ingredientsPanel.add(ingredientRow);

        refreshIngredientLayout();
    }

    private void removeIngredient(JTextField ingredientTextField, JButton button) {
        int index = ingredientsList.indexOf(ingredientTextField);
        if (index >= 0) {
            ingredientsList.remove(index);
            JPanel ingredientRow = ingredientRows.remove(index);
            ingredientsPanel.remove(ingredientRow);
        }

        updateIngredients();
    }

    private void updateIngredients() {
        ingredientsPanel.removeAll();
        ingredientRows.clear();

        for (JTextField ingredientTextField : ingredientsList) {
            JButton removeIngredientButton = new JButton("X");
            removeIngredientButton.setPreferredSize(new Dimension(48, 36));
            removeIngredientButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    removeIngredient(ingredientTextField, removeIngredientButton);
                }
            });

            JPanel ingredientRow = new JPanel(new BorderLayout(8, 0));
            ingredientRow.setOpaque(false);
            ingredientRow.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
            ingredientRow.setAlignmentX(Component.LEFT_ALIGNMENT);
            ingredientRow.setMinimumSize(new Dimension(380, 36));
            ingredientRow.setPreferredSize(new Dimension(468, 36));
            ingredientRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
            ingredientRow.add(ingredientTextField, BorderLayout.CENTER);
            ingredientRow.add(removeIngredientButton, BorderLayout.EAST);

            ingredientRows.add(ingredientRow);
            ingredientsPanel.add(ingredientRow);
        }

        refreshIngredientLayout();
    }

    private void refreshIngredientLayout() {
        ingredientsPanel.revalidate();
        ingredientsPanel.repaint();
        contentPane.revalidate();
        contentPane.repaint();
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
        String imagePath = copySelectedImage(recipeName);

        Recipe recipe = new Recipe(recipeName, (ArrayList<String>) ingredients, directions, imagePath);
        recipeManager.addRecipe(recipe);
        mainPage.displayRecipes();

        dispose();
    }

    private void onCancel() {
        dispose();
    }

    private void onSetImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Recipe Image");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Image Files", "jpg", "jpeg", "png", "gif", "webp"
        ));

        int result = fileChooser.showOpenDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }

        selectedImageSource = fileChooser.getSelectedFile().toPath();
        selectedImageLabel.setText(selectedImageSource.getFileName().toString());
    }

    private String copySelectedImage(String recipeName) {
        if (selectedImageSource == null) {
            return "";
        }

        try {
            Files.createDirectories(IMAGE_DIRECTORY);

            String originalName = selectedImageSource.getFileName().toString();
            String extension = "";
            int dotIndex = originalName.lastIndexOf('.');
            if (dotIndex >= 0) {
                extension = originalName.substring(dotIndex);
            }

            String safeRecipeName = recipeName == null || recipeName.isBlank()
                    ? "recipe"
                    : recipeName.trim().toLowerCase().replaceAll("[^a-z0-9]+", "-").replaceAll("^-|-$", "");

            String targetName = safeRecipeName + "-" + System.currentTimeMillis() + extension;
            Path targetPath = IMAGE_DIRECTORY.resolve(targetName);
            Files.copy(selectedImageSource, targetPath, StandardCopyOption.REPLACE_EXISTING);
            return targetPath.toString();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Could not save the selected image.",
                    "Image Save Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return "";
        }
    }
}
