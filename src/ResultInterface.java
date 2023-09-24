import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.List;
import java.util.Objects;

/**
 * A graphical user interface (GUI) for displaying the results of a menu item search.
 * This class encapsulates the creation and management of UI components for presenting
 * menu items to the user and allowing them to make a selection.
 */
public class ResultInterface {
    private static final int PREVIEW_WIDTH = 250;
    private static final int PREVIEW_HEIGHT = 300;
    private static final int INFO_WIDTH = 400;
    private static final int INFO_HEIGHT = 300;
    private static final int SCROLL_WIDTH = 700;
    private static final int SCROLL_HEIGHT = 400;
    private final JFrame frame;
    private final GridBagConstraints gbc;
    private String state;
    private MenuItem selectedMenuItem;

    /**
     * Constructs a new ResultGUI that displays a list of menu items provided as an argument.
     *
     * <p>This constructor sets up the main frame, control panel, and various UI components
     * to display and interact with the provided menu items.</p>
     *
     * @param menuItems The list of menu items to be displayed in the GUI.
     */
    public ResultInterface(List<MenuItem> menuItems) {
        // Prepare the main frame for the GUI.
        frame = InterfaceUtilities.createFrame();

        // Set up the main control panel inside the frame.
        JPanel controlPanel = InterfaceUtilities.prepareControlPanel(frame);

        // Prepare the GridBagConstraints for positioning elements inside the panel.
        gbc = InterfaceUtilities.createConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(4, 4, 4, 4);

        // Prepare combo box to display results.
        prepareResultsComboBox(controlPanel, menuItems);

        // Prepare the button for starting a new search.
        prepareSearchAgainButton(controlPanel);

        // Prepare the button for confirming the selected item.
        prepareConfirmButton(controlPanel);

        // Display a preview of the results.
        prepareResultsPreview(controlPanel, menuItems);
    }

    /**
     * Prepares a combo box for displaying a list of menu items.
     *
     * @param parent     The parent JPanel to which the combo box will be added.
     * @param menuItems  The list of MenuItem objects to display in the combo box.
     */
    private void prepareResultsComboBox(JPanel parent, List<MenuItem> menuItems) {
        // Create a combo box to display menu items.
        JComboBox comboBox = new JComboBox<>();

        // Set positioning for the combo box within the grid.
        InterfaceUtilities.setGridBagConstraints(0, 1, 1, 1, gbc);

        // Convert the list of menu items to an array of display names.
        String[] names = menuItems.stream()
                .map(item -> item.getMenuItemName() + " (" + item.getMenuItemIdentifier() + ")")
                .toArray(String[]::new);

        // Initially select the first item, if available.
        if (names.length > 0) {
            selectedMenuItem = menuItems.get(0);
        }

        // Set the array of names as the model for the combo box.
        comboBox.setModel(new DefaultComboBoxModel<>(names));
        parent.add(comboBox, gbc);

        // Listen for item selections in the combo box and update the selected burger.
        comboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                for (MenuItem menuItem : menuItems) {
                    if (Objects.equals(menuItem.getMenuItemName(), e.getItem())) {
                        selectedMenuItem = menuItem;
                    }
                }
            }
        });
    }

    /**
     * Prepares a "Confirm" button and adds it to the specified parent JPanel.
     * When clicked, the button will change the internal state to "Continue" and hide the current frame.
     *
     * @param parent  The parent JPanel to which the button will be added.
     */
    private void prepareConfirmButton(JPanel parent) {
        // Create a "Confirm" button.
        JButton button = InterfaceUtilities.createButton("Confirm");

        // Set the position for the button within the grid.
        InterfaceUtilities.setGridBagConstraints(1, 1, 1, 1, gbc);

        // Add the button to the provided parent panel.
        parent.add(button, gbc);

        // Add an action listener to handle button click events.
        button.addActionListener(e -> {
            // Set the state to indicate continuation.
            state = "Continue";

            // Hide the current GUI frame.
            hide();
        });

    }

    /**
     * Prepares and adds a "Search Again" button to the provided parent panel.
     *
     * @param parent The parent panel where the button should be added.
     */
    private void prepareSearchAgainButton(JPanel parent) {
        // Create a "Search Again" button.
        JButton button = InterfaceUtilities.createButton("Search Again");

        // Define the position and size of the button within the grid.
        InterfaceUtilities.setGridBagConstraints(2, 1, 1, 1, gbc);

        // Add the button to the parent panel.
        parent.add(button, gbc);

        // Add an action listener to the button.
        // When pressed, it updates the state to "Back" and hides the GUI frame.
        button.addActionListener(e -> {
            state = "Back";
            hide();
        });
    }

    /**
     * Prepares and adds a results preview list to the provided parent panel.
     *
     * @param parent        The parent panel where the list should be added.
     * @param menuItems The list of menu items to display.
     */
    private void prepareResultsPreview(JPanel parent, List<MenuItem> menuItems) {
        // Create a container panel to hold all the item panels
        JPanel containerPanel = InterfaceUtilities.createPanel();

        for (int i = 0; i < menuItems.size(); i++) {
            // Initialize and style each item panel
            JPanel panel = InterfaceUtilities.createPanel();
            panel.setBackground(new Color(230, 230, 230));
            panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            // Add image to the panel
            Image image = InterfaceUtilities.createImage("images/" + menuItems.get(i).getMenuItemIdentifier() + ".png", PREVIEW_WIDTH, PREVIEW_HEIGHT);
            JLabel imgLabel = InterfaceUtilities.createLabel();
            imgLabel.setIcon(new ImageIcon(image));
            InterfaceUtilities.setGridBagConstraints(1, 0, 1, 10, gbc);
            panel.add(imgLabel, gbc);

            // Add item information to the panel
            JLabel infoLabel = new JLabel(menuItems.get(i).getMenuItemInformation());
            InterfaceUtilities.setPreferredSize(infoLabel, INFO_WIDTH, INFO_HEIGHT);
            InterfaceUtilities.setGridBagConstraints(0, 0, 1, 1, gbc);
            panel.add(infoLabel, gbc);

            // Add the panel to the container panel
            InterfaceUtilities.setGridBagConstraints(0, i, 1, 1, gbc);
            containerPanel.add(panel, gbc);
        }

        // Create a scrollable pane containing the container panel
        JScrollPane scrollPane = new JScrollPane(containerPanel);
        InterfaceUtilities.setPreferredSize(scrollPane, SCROLL_WIDTH, SCROLL_HEIGHT);
        InterfaceUtilities.setGridBagConstraints(0, 0, 10, 1, gbc);
        parent.add(scrollPane, gbc);
    }

    /**
     * Makes the frame visible to the user and ensures it's not resizable.
     */
    public void show() {
        frame.setVisible(true);
        frame.setResizable(false);
    }

    /**
     * Hides the frame from the user.
     */
    public void hide() {
        frame.setVisible(false);
    }

    /**
     * Retrieves the main JFrame of this GUI.
     *
     * @return The main JFrame.
     */
    public JFrame getFrame() {
        return frame;
    }

    /**
     * Retrieves the current state of the GUI.
     * (e.g., whether user wants to "Continue", "Back", etc.)
     *
     * @return The current state.
     */
    public String getState() {
        return state;
    }

    /**
     * Retrieves the currently selected menu items's name from the results.
     *
     * @return Menu Item.
     */
    public MenuItem getSelectedMenuItem() {
        return selectedMenuItem;
    }
}