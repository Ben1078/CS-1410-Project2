package gui;

import recipeManager.FileManager;
import recipeManager.Recipe;
import recipeManager.RecipeManager;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class MainPage {
    private static final Color SIDEBAR_BACKGROUND = new Color(34, 45, 62);
    private static final Color SEARCH_PANEL_BACKGROUND = new Color(44, 58, 80);
    private static final Color BUTTON_PRIMARY = new Color(255, 196, 61);
    private static final Color BUTTON_PRIMARY_HOVER = new Color(242, 175, 33);
    private static final Color BUTTON_TEXT = new Color(34, 45, 62);
    private static final Color SEARCH_FIELD_BACKGROUND = new Color(245, 247, 250);
    private static final Color SEARCH_FIELD_TEXT = new Color(34, 45, 62);

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

    public MainPage(Frame mainFrame, RecipeManager recipeManager) {
        this.mainFrame = mainFrame;
        this.recipeManager = recipeManager;
    }

    public void displayRecipes() {
        displayRecipes(recipeManager.getRecipes());
    }

    public void displayRecipes(List<Recipe> recipes) {
        recipeDisplay.removeAll();
        recipeDisplay.setLayout(new FlowLayout(FlowLayout.LEFT, 24, 12));

        for (Recipe recipe : recipes) {
            JPanel recipeCard = new JPanel();
            recipeCard.setPreferredSize(new Dimension(300, 200));
            recipeCard.setBorder(BorderFactory.createLineBorder(Color.RED));
            ViewRecipe viewRecipeDialog = new ViewRecipe(mainFrame, this, recipe, recipeManager);
            recipeCard.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    viewRecipeDialog.setVisible(true);
                }
            });

            // todo add recipe title, image, etc. to card
            recipeCard.add(new JLabel(recipe.getName()));

            recipeDisplay.add(recipeCard);
        }

        mainFrame.revalidate();
    }

    private void displaySideBar() {
        configureSidebarLayout();
        bindSidebarActions();
        applySidebarTheme();
        sidebar.revalidate();
        sidebar.repaint();
    }

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

    private void styleButton(JButton button, Font font, Dimension size, Color background) {
        button.setFont(font);
        button.setPreferredSize(size);
        button.setBackground(background);
        button.setForeground(BUTTON_TEXT);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        button.setOpaque(true);
    }

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
        List<Recipe> recipeList = new ArrayList<>();

        for (Recipe recipe : recipes) {
            if (recipe.getName().toLowerCase().contains(searchStr.toLowerCase())) {
                recipeList.add(recipe);
            }
        }

        return recipeList;
    }

    private void onSearch() {
        String searchQuery = searchField.getText();
        List<Recipe> recipeList = searchRecipe(recipeManager.getRecipes(), searchQuery);

        displayRecipes(recipeList);
    }

    public void openSettings() {
        JDialog settingsDialog = new Settings(this.mainFrame, this, recipeManager);
        settingsDialog.setVisible(true);
    }

    public JPanel getContentPanel() {
        return this.main;
    }
}
