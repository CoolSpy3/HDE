package com.coolspy3.hde;

import com.coolspy3.hde.component.ComponentButton;
import com.coolspy3.hde.component.EditorComponent;
import com.coolspy3.hde.component.components.EANDGate;
import com.coolspy3.hde.component.components.EBuffer;
import com.coolspy3.hde.component.components.EJunction;
import com.coolspy3.hde.component.components.ENANDGate;
import com.coolspy3.hde.component.components.ENORGate;
import com.coolspy3.hde.component.components.ENOTGate;
import com.coolspy3.hde.component.components.EORGate;
import com.coolspy3.hde.component.components.EXNORGate;
import com.coolspy3.hde.component.components.EXORGate;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.Point2D;
import java.util.Enumeration;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JToggleButton;

/**
 * Represents the window which will be used to select new components in editing mode
 */
public class ComponentSelector extends JFrame {

    private static final long serialVersionUID = 7652914966349826679L;

    private final ButtonGroup bg;
    private final JToggleButton noneButton;

    /**
     * Creates a new ComponentSelector and sets the selected component to "None"
     * @param master The master window to on which to listen for close events
     * @param listener The WindowListener which should be notified if the user attempts to close this window
     */
    public ComponentSelector(Window master, WindowListener listener) {
        // Setup window
        super("Insert Components");

        setSize(320, 600);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLayout(new GridLayout(10, 1));

        // Add buttons for all components
        bg = new ButtonGroup();
        addButton(noneButton = new JToggleButton("None"));
        addButton(new ComponentButton.LambdaComponentButton("Junction", new ImageIcon(ResourceManager.getImage("Junction")), new Dimension(20, 20), EJunction::new));
        addButton(new ComponentButton.LambdaComponentButton("Buffer", new ImageIcon(ResourceManager.getImage("Buffer")), new Dimension(100, 50), EBuffer::new));
        addButton(new ComponentButton.LambdaComponentButton("NOT Gate", new ImageIcon(ResourceManager.getImage("DNOTGate")), new Dimension(100, 50), ENOTGate::new));
        addButton(new ComponentButton.LambdaComponentButton("AND Gate", new ImageIcon(ResourceManager.getImage("DANDGate")), new Dimension(100, 50), EANDGate::new));
        addButton(new ComponentButton.LambdaComponentButton("OR Gate", new ImageIcon(ResourceManager.getImage("DORGate")), new Dimension(100, 50), EORGate::new));
        addButton(new ComponentButton.LambdaComponentButton("NAND Gate", new ImageIcon(ResourceManager.getImage("DNANDGate")), new Dimension(100, 50), ENANDGate::new));
        addButton(new ComponentButton.LambdaComponentButton("NOR Gate", new ImageIcon(ResourceManager.getImage("DNORGate")), new Dimension(100, 50), ENORGate::new));
        addButton(new ComponentButton.LambdaComponentButton("XOR Gate", new ImageIcon(ResourceManager.getImage("DXORGate")), new Dimension(100, 50), EXORGate::new));
        addButton(new ComponentButton.LambdaComponentButton("XNOR Gate", new ImageIcon(ResourceManager.getImage("DXNORGate")), new Dimension(100, 50), EXNORGate::new));

        // No component is selected
        noneButton.setSelected(true);

        // Make frame visible
        setVisible(true);

        // If the master window closes, close this window
        master.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                setVisible(false);
                dispose();
            }
        });

        //If the "X" button on this window is clicked, fire an event to the listener
        ComponentSelector me = this;
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                listener.windowClosing(new WindowEvent(me, WindowEvent.WINDOW_CLOSING));
            }
        });
    }

    private void addButton(AbstractButton b) {
        // Add a button to the button group and the window
        bg.add(b);
        add(b);
    }

    /**
     * Retrieves the component currently selected by the user and resets the selected component to "None"
     * @param location The point on which to center this component
     * @return A new instance of the component currently selected by the user or <code>null</code> if none exists
     */
    public EditorComponent getAndReset(Point2D.Double location) {
        EditorComponent out = null;
        Enumeration<AbstractButton> buttons = bg.getElements();
        // For each button
        while(buttons.hasMoreElements()) {
            AbstractButton b = buttons.nextElement();

            // If it's selected
            if(b.isSelected()) {
                if(b instanceof ComponentButton) {
                    // Return the component it's associated with (if it exists)
                    out = ((ComponentButton)b).getComponent(location);
                }

                // And stop looping
                break;
            }
        }

        // Deselect all components
        noneButton.setSelected(true);

        // Return the selected component
        return out;
    }

}
