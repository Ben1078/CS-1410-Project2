package gui;

import recipeManager.RecipeManager;

import javax.swing.*;

public class GUITest {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Recipe Manager");
        MainPage gui = new MainPage(frame, new RecipeManager());
        frame.setContentPane(gui.getContentPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
}
