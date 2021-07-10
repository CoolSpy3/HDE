package com.coolspy3.hde;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static java.awt.event.InputEvent.*;
import static java.awt.event.KeyEvent.*;
import java.awt.event.WindowEvent;
import static java.awt.event.WindowEvent.*;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Manages the main frame which will be interacted with by the user.
 * This class will load a number of panel classes which will display context-specific information to the user
 */
public class GUI extends JFrame implements ActionListener, WindowListener {

    private static final long serialVersionUID = -131881419240091231L;

    /**
     * The refresh rate of the frame in frames per second
     */
    public static final int FPS = 60;
    /**
     * The delay between paint updates of the frame. This is equal to a value of <code>1000/FPS</code>
     */
    public static final int FDELAY = 1000/FPS;

    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem newMenuItem;
    private JMenuItem openMenuItem;
    private JMenuItem closeMenuItem;
    private JMenuItem saveMenuItem;
    private JMenuItem saveAsMenuItem;
    private JMenuItem renderMenuItem;
    private JMenuItem quitMenuItem;
    private JMenu optionsMenu;
    private JCheckBoxMenuItem showGridMenuItem;
    private String filename;

    private ContentPanel contentPanel;

    /**
     * Creates a new GUI and initializes it with the default layout and a thread which repaints the frame every <code>FDELAY</code> milliseconds.
     */
    @SuppressWarnings({"LeakingThisInConstructor", "CallToThreadStartDuringObjectConstruction"})
    public GUI() {
        // Setup window
        super("Software Hardware Designer and Emulator");
        setSize(1000, 1000);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);

        // Add listeners and components
        addWindowListener(this);
        setupMenus();

        // Set default vars
        filename = null;
        contentPanel = null;

        // Make window visible
        setVisible(true);

        // Start render thread
        @SuppressWarnings("SleepWhileInLoop")
        Thread t = new Thread(() -> {
            while(true) {
                if(isVisible()) {
                    repaint();
                    try {
                        Thread.sleep(FDELAY);
                    } catch(InterruptedException e) {}
                }
            }
        });
        t.setDaemon(true);
        t.start();
    }

    private void setupMenus() {
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        newMenuItem = new JMenuItem("New Project");
        newMenuItem.setAccelerator(KeyStroke.getKeyStroke(VK_N, CTRL_DOWN_MASK));
        newMenuItem.addActionListener(this);
        fileMenu.add(newMenuItem);
        openMenuItem = new JMenuItem("Open Project");
        openMenuItem.setAccelerator(KeyStroke.getKeyStroke(VK_O, CTRL_DOWN_MASK));
        openMenuItem.addActionListener(this);
        fileMenu.add(openMenuItem);
        closeMenuItem = new JMenuItem("Close Project");
        closeMenuItem.setAccelerator(KeyStroke.getKeyStroke(VK_W, CTRL_DOWN_MASK));
        closeMenuItem.addActionListener(this);
        fileMenu.add(closeMenuItem);
        fileMenu.addSeparator();
        saveMenuItem = new JMenuItem("Save Project");
        saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(VK_S, CTRL_DOWN_MASK));
        saveMenuItem.addActionListener(this);
        saveMenuItem.setEnabled(false);
        fileMenu.add(saveMenuItem);
        saveAsMenuItem = new JMenuItem("Save Project As");
        saveAsMenuItem.setAccelerator(KeyStroke.getKeyStroke(VK_S, CTRL_DOWN_MASK | SHIFT_DOWN_MASK));
        saveAsMenuItem.addActionListener(this);
        saveAsMenuItem.setEnabled(false);
        fileMenu.add(saveAsMenuItem);
        renderMenuItem = new JMenuItem("Render Project");
        renderMenuItem.setAccelerator(KeyStroke.getKeyStroke(VK_R, CTRL_DOWN_MASK));
        renderMenuItem.addActionListener(this);
        renderMenuItem.setEnabled(false);
        fileMenu.add(renderMenuItem);
        fileMenu.addSeparator();
        quitMenuItem = new JMenuItem("Exit");
        quitMenuItem.addActionListener(this);
        fileMenu.add(quitMenuItem);
        menuBar.add(fileMenu);
        optionsMenu = new JMenu("Options");
        showGridMenuItem = new JCheckBoxMenuItem("Show Grid", true);
        optionsMenu.add(showGridMenuItem);
        menuBar.add(optionsMenu);
        setJMenuBar(menuBar);
    }

    /**
     * Checks the user's selection to see whether a grid should be shown behind displayed components
     * @return Whether a grid should be shown behind components
     */
    public boolean shouldShowGrid() {
        return showGridMenuItem.isSelected();
    }

    /**
     * Attempts to close the current content panel.
     * The user will be prompted to save any unsaved data. The panel will then be closed and removed from the frame unless the operation was canceled by the user.
     * @return Whether the operation was successful
     * @throws IOException if an error occurs while saving
     */
    public boolean closeContentPanel() throws IOException {
        if(contentPanel != null) {
            // If contentPanel is open, attempt to save data
            if(saveUnsaved()) {
                // Remove content panel
                remove(contentPanel);
                contentPanel = null;

                // Disable saving
                saveMenuItem.setEnabled(false);
                saveAsMenuItem.setEnabled(false);
                renderMenuItem.setEnabled(false);

                // Revalidate frame
                revalidate();
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * Prompts the user to save any unsaved data
     * @return Whether the data was successfully saved
     * @throws IOException if an error occurs while saving
     */
    public boolean saveUnsaved() throws IOException {
        int op = JOptionPane.showOptionDialog(this, "Would You Like to Save Your Changes?", "Save Changes",
            JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, new String[] {"Save", "Discard", "Cancel"}, "Save");
        switch(op) {
            // "Save"
            case 0:
                return save();
            // "Discard"
            case 1:
                return true;
            // "Cancel" / Popup Closed
            case 2:
            case JOptionPane.DEFAULT_OPTION:
            default:
                return false;
        }
    }

    /**
     * Saves any unsaved data to the file it was read from or prompts the user for a file location if none was specified
     * @return Whether the data was successfully saved
     * @throws IOException if an error occurs while saving
     */
    public boolean save() throws IOException {
        if(!saveAsMenuItem.isEnabled()) {
            // There is no data to save
            return true;
        }
        // Save with same filename if possible
        if(filename != null) {
            return saveAs(filename);
        } else {
            return saveAs();
        }
    }

    /**
     * Prompts the user for a location and then saves any unsaved data to that location
     * @return Whether the data was successfully saved
     * @throws IOException if an error occurs while saving
     */
    public boolean saveAs() throws IOException {
        if(!saveAsMenuItem.isEnabled()) {
            // There is no data to save
            return true;
        }

        // Prompt user for file
        JFileChooser fc = new JFileChooser();
        fc.setAcceptAllFileFilterUsed(false);
        fc.setFileFilter(new FileNameExtensionFilter("Logic Map Files", "lm"));
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setMultiSelectionEnabled(false);
        fc.showSaveDialog(this);
        File file = fc.getSelectedFile();

        if(file == null) {
            // User did not select a file
            return false;
        }
        return saveAs(file);
    }

    /**
     * Saves any unsaved data to the specified file
     * @param filename The name of the file to which the data should be saved
     * @return Whether the data was saved successfully
     * @throws IOException if an error occurs while saving
     */
    public boolean saveAs(String filename) throws IOException {
        return saveAs(new File(filename));
    }

    /**
     * Saves any unsaved data to the specified file
     * @param file The file to which the data should be saved
     * @return Whether the data was saved successfully
     * @throws IOException if an error occurs while saving
     */
    public boolean saveAs(File file) throws IOException {
        if(!saveAsMenuItem.isEnabled()) {
            // There is no data to save
            return true;
        }
        if(file.exists()) {
            // Ask to overwrite file
            int op = JOptionPane.showConfirmDialog(this, "File Already Exists! Overwrite?", "Save Project", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if(op == JOptionPane.NO_OPTION || op == JOptionPane.CLOSED_OPTION) {
                return false;
            }
        } else {
            // The file does not exist. Create it
            file.createNewFile();
        }

        // Save the file name
        filename = file.getCanonicalPath();

        // Write data to file
        contentPanel.save(file);

        return true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if(e.getSource() == newMenuItem) {
                // Try to close open project
                if(closeContentPanel()) {
                    // No file is associates with the new project
                    filename = null;

                    // Create a new editor
                    contentPanel = new ContentPanel(this);
                    add(contentPanel);

                    // Enable saving
                    saveMenuItem.setEnabled(true);
                    saveAsMenuItem.setEnabled(true);
                    renderMenuItem.setEnabled(true);

                    // Revalidate frame
                    revalidate();
                }
            } else if(e.getSource() == openMenuItem) {
                // PRompt user to select file
                JFileChooser fc = new JFileChooser();
                fc.setAcceptAllFileFilterUsed(false);
                fc.setFileFilter(new FileNameExtensionFilter("Logic Map Files", "lm"));
                fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fc.setMultiSelectionEnabled(false);
                fc.showOpenDialog(this);
                File file = fc.getSelectedFile();

                // If the file is not null, check if it exists. If it does, attempt to close the open project
                if(file != null && file.exists() && closeContentPanel()) {
                    // Store the file name
                    filename = file.getCanonicalPath();

                    // Create a new editor
                    contentPanel = new ContentPanel(this, file);
                    add(contentPanel);

                    // Enable saving
                    saveMenuItem.setEnabled(true);
                    saveAsMenuItem.setEnabled(true);
                    renderMenuItem.setEnabled(true);

                    // Revalidate frame
                    revalidate();
                }
            } else if(e.getSource() == closeMenuItem) {
                closeContentPanel();
            } else if(e.getSource() == saveMenuItem) {
                save();
            } else if(e.getSource() == saveAsMenuItem) {
                saveAs();
            } else if(e.getSource() == renderMenuItem) {
                JOptionPane.showMessageDialog(this, "Rendering is not yet supported!", "Render", JOptionPane.ERROR_MESSAGE);
            } else if(e.getSource() == quitMenuItem) {
                dispatchEvent(new WindowEvent(this, WINDOW_CLOSING));
            }
        } catch(IOException exc) {
            exc.printStackTrace(System.err);
            System.exit(1);
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        try {
            // Attempt to close the open project
            if(closeContentPanel()) {
                // Hide and dispose frame
                setVisible(false);
                dispose();
            }
        } catch(IOException exc) {
            exc.printStackTrace(System.err);
        }
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

}
