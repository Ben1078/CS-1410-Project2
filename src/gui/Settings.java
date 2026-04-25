package gui;

import recipeManager.FileManager;
import recipeManager.RecipeManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

/**
 * Dialog window for managing settings.
 * @author Benjamin Paul and Saulo Gomes
 */
public class Settings extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField recipeFilePathTextField;
    private JLabel recipeFilePathLabel;
    private JButton chooseFileButton;

    private MainPage mainPage;
    private RecipeManager recipeManager;

    /**
     * Initializes a new settings dialog and manages components.
     * @param parent the parent frame used to center the dialog and set size.
     * @param mainPage the main page to trigger UI updates.
     * @param recipeManager the manager used to reload recipes after changing settings.
     */
    public Settings(Frame parent, MainPage mainPage, RecipeManager recipeManager) {
        super(parent, true);

        this.mainPage = mainPage;
        this.recipeManager = recipeManager;

        setContentPane(contentPane);
        getRootPane().setDefaultButton(buttonOK);

        int width = (int) (parent.getWidth() * 0.2);
        int height = (int) (parent.getHeight() * 0.2);
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

        recipeFilePathTextField.setText(FileManager.getFilePath());

        chooseFileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.setCurrentDirectory(new File(FileManager.getFilePath()));

                int returnVal = fileChooser.showOpenDialog(new JFrame("Choose a directory"));

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    String path = selectedFile.getAbsolutePath();

                    recipeFilePathTextField.setText(path);
                }
            }
        });

        pack();
        setLocationRelativeTo(parent);
    }

    /**
     * Saves the modified settings, reloads the recipes via the {@code RecipeManager},
     * refreshes the main display, and closes the dialog.
     */
    private void onOK() {
        FileManager.setFilePath(recipeFilePathTextField.getText());
        recipeManager.loadRecipes();
        mainPage.display();
        dispose();
    }

    /**
     * Closes the dialog without applying any changes to the settings.
     */
    private void onCancel() {
        dispose();
    }
}