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

/**
 * Class for a dialog window that allows the user to add a recipe to the system.
 * Allows users to add an image, a title for the recipe, ingredients, and directions.
 * @author Saulo Gomes and Benjamin Paul
 */
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

    /**
     * Constructor for AddRecipeDialog. Dynamically creates elements such as buttons, text fields, etc.
     * @param parent the parent frame from which the dialog was opened from. Used primarily to center the dialog according to the parent frame.
     * @param mainPage the main page of the app. Needed to update recipes once a new recipe is added.
     * @param recipeManager a recipe manager to add new recipes to.
     */
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

    /**
     * Sets up dynamic elements, such as buttons, text fields, etc. once the dialog is opened.
     */
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

    /**
     * Creates a text field to input an ingredient and a delete button to allow users to remove the ingredient text field.
     */
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

    /**
     * Removes both the text field and delete buttons and adjusts the layout of existing ingredient inputs.
     * @param ingredientTextField the text field to remove.
     * @param button the 'delete' button to remove.
     */
    private void removeIngredient(JTextField ingredientTextField, JButton button) {
        int index = ingredientsList.indexOf(ingredientTextField);
        if (index >= 0) {
            ingredientsList.remove(index);
            JPanel ingredientRow = ingredientRows.remove(index);
            ingredientsPanel.remove(ingredientRow);
        }

        updateIngredients();
    }

    /**
     * Adjusts the position of existing ingredient inputs.
     */
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

    /**
     * Reads all ingredient inputs from text fields and compiles them into a list of string ingredients.
     * @return the list of ingredients.
     */
    private List<String> getIngredients() {
        List<String> ingredients = new ArrayList<>();

        for (JTextField ingredientTextField : ingredientsList) {
            ingredients.add(ingredientTextField.getText());
        }

        return ingredients;
    }

    /**
     * Creates a {@code Recipe} from the dialog inputs, registers it with the {@code RecipeManager}, and closes the dialog.
     */
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

    /**
     * Closes the dialog without creating a {@code Recipe}
     */
    private void onCancel() {
        dispose();
    }

    /**
     * Creates a dialog for users to input an image file that is used to display the recipe's image.
     */
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

    /**
     * Cleans up the filename to ensure it's valid, and adds a timestamp. In case there's an error, a GUI dialog is displayed to the user
     * @param recipeName the name of the recipe that will be used to name the file.
     * @return the path where the image was saved, or an empty string, if there was an error.
     */
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
