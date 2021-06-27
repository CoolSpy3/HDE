package code;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.function.Function;
import javax.swing.Icon;
import javax.swing.JToggleButton;

/**
 * An instance of {@link JToggleButton} which supports creating a component
 */
public abstract class ComponentButton extends JToggleButton {
    
    private static final long serialVersionUID = 5839885054615152118L;
    
    /**
     * Creates an initially unselected toggle button
     * without setting the text or image.
     */
    public ComponentButton() {
        super();
    }

    /**
     * Creates an initially unselected toggle button
     * with the specified image but no text.
     *
     * @param icon  The image that the button should display
     */
    public ComponentButton(Icon icon) {
        super(icon);
    }

    /**
     * Creates an unselected toggle button with the specified text.
     *
     * @param text The string displayed on the toggle button
     */
    public ComponentButton(String text) {
        super(text);
    }

    /**
     * Creates a toggle button that has the specified text and image,
     * and that is initially unselected.
     *
     * @param text The string displayed on the button
     * @param icon The image that the button should display
     */
    public ComponentButton(String text, Icon icon) {
        super(text, icon);
    }

    /**
     * Creates a new DComponent at the specified position
     * @param location The center of the new component
     * @return The newly created component
     */
    public abstract DComponent getComponent(Point2D.Double location);

    /**
     * Represents a {@link ComponentButton} which preforms the math to center a given component
     */
    public static abstract class CenteredComponentButton extends ComponentButton {

        private static final long serialVersionUID = -205625619713823577L;
        
        /**
         * The size of the component which will be created by the {@link #getComponent(java.awt.geom.Point2D.Double) } method of this button
         */
        public final Dimension size;

        /**
         * Creates an initially unselected toggle button
         * without setting the text or image.
         * 
         * @param size The size of the component which will be created by the {@link #getComponent(java.awt.geom.Point2D.Double) } method of this button
         */
        public CenteredComponentButton(Dimension size) {
            super();
            this.size = size;
        }

        /**
         * Creates an initially unselected toggle button
         * with the specified image but no text.
         *
         * @param icon The image that the button should display
         * @param size The size of the component which will be created by the {@link #getComponent(java.awt.geom.Point2D.Double) } method of this button
         */
        public CenteredComponentButton(Icon icon, Dimension size) {
            super(icon);
            this.size = size;
        }

        
        /**
         * Creates an unselected toggle button with the specified text.
         *
         * @param text The string displayed on the toggle button
         * @param size The size of the component which will be created by the {@link #getComponent(java.awt.geom.Point2D.Double) } method of this button
         */
        public CenteredComponentButton(String text, Dimension size) {
            super(text);
            this.size = size;
        }

        /**
         * Creates a toggle button that has the specified text and image,
         * and that is initially unselected.
         *
         * @param text The string displayed on the button
         * @param icon The image that the button should display
         * @param size The size of the component which will be created by the {@link #getComponent(java.awt.geom.Point2D.Double) } method of this button
         */
        public CenteredComponentButton(String text, Icon icon, Dimension size) {
            super(text, icon);
            this.size = size;
        }

        @Override
        public DComponent getComponent(Point2D.Double location) {
            // The top left corner of the component will be at the center minus half the component's width and height
            return getUncenteredDComponent(new Point2D.Double(location.x-size.width/2,location.y-size.height/2));
        }
        
        /**
         * Creates a new DComponent at the specified position
         * @param location The top left corner of the new component
         * @return The newly created component
         */
        public abstract DComponent getUncenteredDComponent(Point2D.Double location);

    }

    /**
     * Represents a {@link ComponentButton} which can have its creation function specified by a lambda expression.
     * This enables the quick creation of buttons through statements such as <code>new LambdaComponentButton(size, ComponentName::new)</code>
     */
    public static class LambdaComponentButton extends CenteredComponentButton {

        private static final long serialVersionUID = 3922329501741839302L;
        
        /**
         * The function which provides the implementation for {@link #getUncenteredDComponent(java.awt.geom.Point2D.Double) }
         */
        public final Function<Point2D.Double, DComponent> componentFunction;

        /**
         * Creates an initially unselected toggle button
         * without setting the text or image.
         * 
         * @param size The size of the component which will be created by the {@link #getComponent(java.awt.geom.Point2D.Double) } method of this button
         * @param componentFunction The function which will be used to generate new components
         */
        public LambdaComponentButton(Dimension size, Function<Point2D.Double, DComponent> componentFunction) {
            super(size);
            this.componentFunction = componentFunction;
        }

        /**
         * Creates an initially unselected toggle button
         * with the specified image but no text.
         *
         * @param icon The image that the button should display
         * @param size The size of the component which will be created by the {@link #getComponent(java.awt.geom.Point2D.Double) } method of this button
         * @param componentFunction The function which will be used to generate new components
         */
        public LambdaComponentButton(Icon icon, Dimension size, Function<Point2D.Double, DComponent> componentFunction) {
            super(icon, size);
            this.componentFunction = componentFunction;
        }

        /**
         * Creates an unselected toggle button with the specified text.
         *
         * @param text The string displayed on the toggle button
         * @param size The size of the component which will be created by the {@link #getComponent(java.awt.geom.Point2D.Double) } method of this button
         * @param componentFunction The function which will be used to generate new components
         */
        public LambdaComponentButton(String text, Dimension size, Function<Point2D.Double, DComponent> componentFunction) {
            super(text, size);
            this.componentFunction = componentFunction;
        }

        /**
         * Creates a toggle button that has the specified text and image,
         * and that is initially unselected.
         *
         * @param text The string displayed on the button
         * @param icon The image that the button should display
         * @param size The size of the component which will be created by the {@link #getComponent(java.awt.geom.Point2D.Double) } method of this button
         * @param componentFunction The function which will be used to generate new components
         */
        public LambdaComponentButton(String text, Icon icon, Dimension size, Function<Point2D.Double, DComponent> componentFunction) {
            super(text, icon, size);
            this.componentFunction = componentFunction;
        }

        @Override
        public DComponent getUncenteredDComponent(Point2D.Double location) {
            return componentFunction.apply(location);
        }

    }

}
