package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

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
        // todo create text field for ingredient
        // todo create button to delete ingredient
        // todo add to and update frame
    }

    private void onOK() {
        // todo add your code here
        dispose();
    }

    private void onCancel() {
        // todo add your code here if necessary
        dispose();
    }
}
