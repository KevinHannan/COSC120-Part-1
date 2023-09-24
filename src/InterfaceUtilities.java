import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Utility class for creating and manipulating various Swing components used in
 * the interface. This class provides a centralized way to manage the UI components.
 */
public class InterfaceUtilities {
    private static final String FRAME_TITLE = "Eets 4 Gobbledy-Geeks";
    private static final int FRAME_WIDTH = 750;
    private static final int FRAME_HEIGHT = 500;

    /**
     * Private constructor to prevent instantiation.
     */
    private InterfaceUtilities() { throw new AssertionError(); }

    /**
     * Creates a JFrame with default title, width, and height.
     *
     * @return a JFrame object.
     */
    public static JFrame createFrame() {
        JFrame frame = new JFrame(FRAME_TITLE);
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        return frame;
    }

    /**
     * Sets the preferred size for a Swing component.
     *
     * @param parent the component to set the size for.
     * @param width  the preferred width.
     * @param height the preferred height.
     */
    public static void setPreferredSize(JComponent parent, int width, int height) {
        parent.setPreferredSize(new Dimension(width, height));
    }

    /**
     * Creates a new JCheckBox with the given text label.
     *
     * @param text The label text for the JCheckBox.
     * @return a new JCheckBox object.
     */
    public static JCheckBox createCheckBox(String text){
        return new JCheckBox(text);
    }

    /**
     * Creates a new JButton with the given text label.
     *
     * @param text The label text for the JButton.
     * @return a new JButton object.
     */
    public static JButton createButton(String text) {
        return new JButton(text);
    }

    /**
     * Creates a new JPanel with GridBagLayout.
     *
     * @return a new JPanel object.
     */
    public static JPanel createPanel() {
        return new JPanel(new GridBagLayout());
    }

    /**
     * Creates a new JLabel with optional text label.
     *
     * @param text Optional label text for the JLabel.
     * @return a new JLabel object.
     */
    public static JLabel createLabel(String... text) {
        return new JLabel(text.length > 0 ? text[0] : "");
    }

    /**
     * Creates a new JRadioButton with the given text label.
     *
     * @param text The label text for the JRadioButton.
     * @return a new JRadioButton object.
     */
    public static JRadioButton createRadioButton(String text) {
        return new JRadioButton(text);
    }

    /**
     * Creates a new GridBagConstraints object.
     *
     * @return a new GridBagConstraints object.
     */
    public static GridBagConstraints createConstraints() {
        return new GridBagConstraints();
    }

    /**
     * Creates a new JTextField.
     *
     * @return a new JTextField object.
     */
    public static JTextField createTextField() {
        return new JTextField();
    }

    /**
     * Creates a new Image object from the given file path.
     *
     * @param path        The path to the image file.
     * @param imageWidth  The desired width of the image.
     * @param imageHeight The desired height of the image.
     * @return a new Image object or null if the image could not be loaded.
     */
    public static Image createImage(String path, int imageWidth, int imageHeight) {
        Image image = null;

        try {
            BufferedImage bufferedImage = ImageIO.read(new File(path));
            image = bufferedImage.getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
        }

        return image;
    }

    /**
     * Creates a new JComboBox with String type.
     *
     * @return a new JComboBox<String> object.
     */
    public static JComboBox<String> createComboBox() {
        return new JComboBox<>();
    }

    /**
     * Prepares and returns a control panel added to the parent JFrame.
     *
     * @param parent The parent JFrame where the panel will be added.
     * @return a new JPanel object added to the parent JFrame.
     */
    public static JPanel prepareControlPanel(JFrame parent) {
        JPanel panel = createPanel();
        parent.add(panel);
        return panel;
    }

    /**
     * Sets the grid bag constraints for layout management.
     *
     * @param x          The grid x-coordinate.
     * @param y          The grid y-coordinate.
     * @param width      The number of cells in a row.
     * @param height     The number of cells in a column.
     * @param constraint The GridBagConstraints object to set.
     */
    public static void setGridBagConstraints(int x, int y, int width, int height, GridBagConstraints constraint) {
        constraint.gridx = x;
        constraint.gridy = y;
        constraint.gridwidth = width;
        constraint.gridheight = height;
    }
}
