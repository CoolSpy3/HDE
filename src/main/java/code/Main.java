package code;

import javax.swing.SwingUtilities;

/**
 * Holds the entry point for the program
 */
public class Main {

    /**
     * The entry point from the program. Starts the main GUI and displays it to the user
     * @param args The command-line arguments
     */
    public static void main(String[] args) {
        // Create a new GUI
        SwingUtilities.invokeLater(GUI::new);
    }

}
