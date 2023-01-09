package com.andreinicolae.app;
import javax.swing.*;

/**
 * @author Andrei Nicolae Căluțiu.
 * @version 1.0
 * This is the main application, that launches the frame, together with the app.
 */
public class MainApp {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        }
        catch (Exception e) {}
        new AppFrame().setVisible(true);
    }
}
