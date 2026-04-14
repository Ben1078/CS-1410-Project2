package gui;

import javax.swing.*;
import java.awt.*;

public class ViewRecipe extends JDialog {
    private JPanel contentPane;
    private JButton backButton;
    private JButton editButton;
    private JLabel recipeTitleLabel;
    private JPanel recipeImagePanel;
    private JLabel ingredientsLabel;
    private JLabel instructionsLabel;
    private JTextArea instructionsTextArea;

    private boolean isEditing = false;

    public ViewRecipe(Frame parent) {
        super(parent, true);
        setContentPane(contentPane);

        int width = (int) (parent.getWidth() * 0.65);
        int height = (int) (parent.getHeight() * 0.8);
        setPreferredSize(new Dimension(width, height));

        pack();
        setLocationRelativeTo(parent);
    }

    private void toggleEditing() {
        if (isEditing) {
            backButton.setText("CANCEL");
            editButton.setText("CONFIRM");

            // todo make texts editable
        } else {
            backButton.setText("BACK");
            editButton.setText("EDIT");

            // todo make texts not editable
            // todo save recipe
        }

        isEditing = !isEditing;
    }
}
