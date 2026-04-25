package gui;

import recipeManager.Recipe;
import recipeManager.RecipeManager;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the main user interface, including sidebar navigation,
 * recipe search functionality, and the dynamic display of recipe cards.
 * @author Saulo Gomes and Benjamin Paul
 */
public class MainPage {
    private static final Color SIDEBAR_BACKGROUND = new Color(34, 45, 62);
    private static final Color SEARCH_PANEL_BACKGROUND = new Color(44, 58, 80);
    private static final Color BUTTON_PRIMARY = new Color(255, 196, 61);
    private static final Color BUTTON_PRIMARY_HOVER = new Color(242, 175, 33);
    private static final Color BUTTON_TEXT = new Color(34, 45, 62);
    private static final Color SEARCH_FIELD_BACKGROUND = new Color(245, 247, 250);
    private static final Color SEARCH_FIELD_TEXT = new Color(34, 45, 62);
    private static final Color RECIPE_CARD_BACKGROUND = Color.WHITE;
    private static final Color RECIPE_CARD_BORDER = new Color(222, 228, 237);
    private static final Color RECIPE_CARD_TITLE = new Color(34, 45, 62);
    private static final Color RECIPE_CARD_IMAGE_FALLBACK = new Color(232, 237, 244);

    private JPanel sidebar;
    private JPanel recipeDisplay;
    private JPanel main;
    private JPanel searchPanel;
    private JPanel actionPanel;
    private JPanel rightSidebarSpacer;
    private JTextField searchField;
    private JButton searchButton;
    private JButton addRecipeButton;
    private JButton settingsButton;
    private Frame mainFrame;
    private boolean sidebarActionsBound;
    private boolean sidebarLayoutBound;

    private RecipeManager recipeManager;

    /**
     * Initializes a new instance of the main page view.
     * @param mainFrame the main window used to host the page and center dialogs.
     * @param recipeManager the manager handling recipes.
     */
    public MainPage(Frame mainFrame, RecipeManager recipeManager) {
        this.mainFrame = mainFrame;
        this.recipeManager = recipeManager;
    }

    /**
     * Refreshes the UI to display all currently stored recipes from the {@code RecipeManager}.
     */
    public void displayRecipes() {
        displayRecipes(getFilteredRecipes());
    }

    /**
     * Creates clickable recipe cards based on a provided list of recipes.
     * @param recipes the list of {@code Recipe} objects to render in the display area.
     */
    public void displayRecipes(List<Recipe> recipes) {
        recipeDisplay.removeAll();
        recipeDisplay.setLayout(new FlowLayout(FlowLayout.LEFT, 24, 12));

        if (recipes.isEmpty()) {
            JLabel emptyStateLabel = new JLabel("No recipes match your search.");
            emptyStateLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
            emptyStateLabel.setForeground(RECIPE_CARD_TITLE);
            recipeDisplay.add(emptyStateLabel);
        }

        for (Recipe recipe : recipes) {
            JPanel recipeCard = new JPanel(new BorderLayout(0, 12));
            recipeCard.setPreferredSize(new Dimension(300, 200));
            recipeCard.setBackground(RECIPE_CARD_BACKGROUND);
            recipeCard.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(RECIPE_CARD_BORDER, 1),
                    BorderFactory.createEmptyBorder(12, 12, 12, 12)
            ));
            ViewRecipe viewRecipeDialog = new ViewRecipe(mainFrame, this, recipe, recipeManager);
            recipeCard.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    viewRecipeDialog.setVisible(true);
                }
            });

            JLabel imageLabel = createRecipeCardImage(recipe);
            imageLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    viewRecipeDialog.setVisible(true);
                }
            });

            JLabel titleLabel = new JLabel(recipe.getName());
            titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
            titleLabel.setForeground(RECIPE_CARD_TITLE);
            titleLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    viewRecipeDialog.setVisible(true);
                }
            });

            recipeCard.add(imageLabel, BorderLayout.CENTER);
            recipeCard.add(titleLabel, BorderLayout.SOUTH);

            recipeDisplay.add(recipeCard);
        }

        recipeDisplay.revalidate();
        recipeDisplay.repaint();
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private JLabel createRecipeCardImage(Recipe recipe) {
        JLabel imageLabel = new JLabel("", SwingConstants.CENTER);
        imageLabel.setOpaque(true);
        imageLabel.setBackground(RECIPE_CARD_IMAGE_FALLBACK);
        imageLabel.setPreferredSize(new Dimension(276, 136));
        imageLabel.setBorder(BorderFactory.createLineBorder(RECIPE_CARD_BORDER, 1));

        String imagePath = recipe.getImagePath();
        if (imagePath == null || imagePath.isBlank()) {
            imageLabel.setText("No Image");
            return imageLabel;
        }

        File imageFile = new File(imagePath);
        if (!imageFile.exists()) {
            imageLabel.setText("Image Missing");
            return imageLabel;
        }

        ImageIcon imageIcon = new ImageIcon(imagePath);
        Image scaledImage = imageIcon.getImage().getScaledInstance(276, 136, Image.SCALE_SMOOTH);
        imageLabel.setIcon(new ImageIcon(scaledImage));
        return imageLabel;
    }

    /**
     * Initializes and organizes the sidebar layout.
     */
    private void displaySideBar() {
        configureSidebarLayout();
        bindSidebarActions();
        applySidebarTheme();
        sidebar.revalidate();
        sidebar.repaint();
    }

    /**
     * Attaches action listeners to sidebar components.
     */
    private void bindSidebarActions() {
        if (sidebarActionsBound) {
            return;
        }

        searchButton.addActionListener(e -> onSearch());
        searchField.addActionListener(e -> onSearch());
        addRecipeButton.addActionListener(e -> {
            AddRecipeDialog dialog = new AddRecipeDialog(mainFrame, this, recipeManager);
            dialog.setVisible(true);
        });
        settingsButton.addActionListener(e -> openSettings());
        sidebarActionsBound = true;
    }

    /**
     * Styles the sidebar components by applying colors, fonts, and borders.
     */
    private void applySidebarTheme() {
        Font buttonFont = new Font("SansSerif", Font.BOLD, 16);
        Dimension buttonSize = new Dimension(150, 42);
        Border sidebarPadding = BorderFactory.createEmptyBorder(10, 16, 10, 16);

        sidebar.setBackground(SIDEBAR_BACKGROUND);
        sidebar.setBorder(sidebarPadding);
        searchPanel.setBackground(SIDEBAR_BACKGROUND);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        actionPanel.setBackground(SIDEBAR_BACKGROUND);
        actionPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rightSidebarSpacer.setBackground(SIDEBAR_BACKGROUND);
        rightSidebarSpacer.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        searchField.setPreferredSize(new Dimension(180, 38));
        searchField.setBackground(SEARCH_FIELD_BACKGROUND);
        searchField.setForeground(SEARCH_FIELD_TEXT);
        searchField.setCaretColor(SEARCH_FIELD_TEXT);
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(SEARCH_PANEL_BACKGROUND.darker(), 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));

        styleButton(searchButton, buttonFont, buttonSize, BUTTON_PRIMARY_HOVER);
        styleButton(addRecipeButton, buttonFont, buttonSize, BUTTON_PRIMARY);
        styleButton(settingsButton, buttonFont, buttonSize, BUTTON_PRIMARY);
    }

    /**
     * Styles buttons with borders, fonts, and colors.
     * @param button the button to style.
     * @param font the font to apply.
     * @param size the preferred dimensions.
     * @param background the background color.
     */
    private void styleButton(JButton button, Font font, Dimension size, Color background) {
        button.setFont(font);
        button.setPreferredSize(size);
        button.setBackground(background);
        button.setForeground(BUTTON_TEXT);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        button.setOpaque(true);
    }

    /**
     * Sets sidebar layout and order.
     */
    private void configureSidebarLayout() {
        sidebar.setLayout(new BorderLayout());
        installSidebarResizeHandler();
        updateSidebarPreferredSizes();

        sidebar.remove(searchPanel);
        sidebar.remove(actionPanel);
        sidebar.remove(rightSidebarSpacer);
        sidebar.add(searchPanel, BorderLayout.WEST);
        sidebar.add(actionPanel, BorderLayout.CENTER);
        sidebar.add(rightSidebarSpacer, BorderLayout.EAST);
    }

    /**
     * Adds listener to resize component during window resize events.
     */
    private void installSidebarResizeHandler() {
        if (sidebarLayoutBound) {
            return;
        }

        sidebar.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateSidebarPreferredSizes();
                sidebar.revalidate();
            }
        });
        sidebarLayoutBound = true;
    }

    /**
     * Updates the preferred sizes of sidebar sections based on the width.
     */
    private void updateSidebarPreferredSizes() {
        int sidebarWidth = Math.max(sidebar.getWidth(), main.getWidth());
        int searchWidth = Math.max(220, (int) Math.round(sidebarWidth * 0.30));
        int rightSpacerWidth = Math.max(180, (int) Math.round(sidebarWidth * 0.30));
        int actionWidth = Math.max(260, sidebarWidth - searchWidth - rightSpacerWidth);
        int sidebarHeight = 62;

        searchPanel.setPreferredSize(new Dimension(searchWidth, sidebarHeight));
        searchPanel.setMinimumSize(new Dimension(searchWidth, sidebarHeight));
        actionPanel.setPreferredSize(new Dimension(actionWidth, sidebarHeight));
        actionPanel.setMinimumSize(new Dimension(actionWidth, sidebarHeight));
        rightSidebarSpacer.setPreferredSize(new Dimension(rightSpacerWidth, sidebarHeight));
        rightSidebarSpacer.setMinimumSize(new Dimension(rightSpacerWidth, sidebarHeight));
    }

    /**
     * Displays sidebar and the recipe grid.
     */
    public void display() {
        displaySideBar();
        displayRecipes();
    }

    /**
     * Returns a list of recipes that have a name that matches the passed string.
     * @param recipes the list of recipes to search from (haystack).
     * @param searchStr the string to match recipe names to (needle).
     */
    private List<Recipe> searchRecipe(List<Recipe> recipes, String searchStr) {
        String normalizedQuery = searchStr == null ? "" : searchStr.trim().toLowerCase();
        if (normalizedQuery.isEmpty()) {
            return new ArrayList<>(recipes);
        }

        List<Recipe> recipeList = new ArrayList<>();

        for (Recipe recipe : recipes) {
            boolean matchesName = recipe.getName() != null && recipe.getName().toLowerCase().contains(normalizedQuery);
            boolean matchesInstructions = recipe.getInstructions() != null
                    && recipe.getInstructions().toLowerCase().contains(normalizedQuery);
            boolean matchesIngredient = false;

            for (String ingredient : recipe.getIngredients()) {
                if (ingredient != null && ingredient.toLowerCase().contains(normalizedQuery)) {
                    matchesIngredient = true;
                    break;
                }
            }

            if (matchesName || matchesInstructions || matchesIngredient) {
                recipeList.add(recipe);
            }
        }

        return recipeList;
    }

    /**
     * Returns the filtered recipes from the search text field input.
     * @return the filtered recipes.
     */
    private List<Recipe> getFilteredRecipes() {
        return searchRecipe(recipeManager.getRecipes(), searchField.getText());
    }

    /**
     * Handles search input and updates the display with filtered results.
     */
    private void onSearch() {
        displayRecipes(getFilteredRecipes());
    }

    /**
     * Opens a dialog window for settings.
     */
    public void openSettings() {
        JDialog settingsDialog = new Settings(this.mainFrame, this, recipeManager);
        settingsDialog.setVisible(true);
    }

    /**
     * Provides access to the main content container panel.
     * @return the {@code JPanel} containing the GUI elements.
     */
    public JPanel getContentPanel() {
        return this.main;
    }
}
