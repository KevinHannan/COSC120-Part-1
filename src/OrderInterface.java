import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class OrderInterface {
    private static final int PREVIEW_WIDTH = 275;
    private static final int PREVIEW_HEIGHT = 275;
    private final JFrame frame;
    private final GridBagConstraints gbc;
    private Geek geek;
    private String specialRequests;
    private String name;
    private String contact;

    /**
     * Constructor to initialize the GUI components.
     *
     * @param menuItem The menu item being ordered.
     */
    public OrderInterface(MenuItem menuItem) {
        // Prepare the main frame for the GUI.
        frame = InterfaceUtilities.createFrame();

        // Set up the main control panel inside the frame.
        JPanel controlPanel = InterfaceUtilities.prepareControlPanel(frame);

        // Prepare the GridBagConstraints for positioning elements inside the panel.
        gbc = InterfaceUtilities.createConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(4, 4, 4, 4);

        prepareNameTextField(controlPanel);
        prepareContactTextField(controlPanel);
        prepareSpecialRequestTextField(controlPanel);
        prepareResultPreview(controlPanel, menuItem);
        prepareSearchButton(controlPanel);
    }

    /**
     * Initializes the Result Preview section.
     *
     * @param parent The parent JPanel.
     * @param menuItem The menu item being previewed.
     */
    private void prepareResultPreview(JPanel parent, MenuItem menuItem) {
        // Initialize and style each item panel
        JPanel panel = InterfaceUtilities.createPanel();
        panel.setBackground(new Color(230, 230, 230));
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Add image to the panel
        Image image = InterfaceUtilities.createImage("images/" + menuItem.getMenuItemIdentifier() + ".png", PREVIEW_WIDTH, PREVIEW_HEIGHT);
        JLabel imgLabel = InterfaceUtilities.createLabel();
        imgLabel.setIcon(new ImageIcon(image));
        Dimension imgSize = new Dimension(PREVIEW_WIDTH, PREVIEW_HEIGHT);
        imgLabel.setPreferredSize(imgSize);
        InterfaceUtilities.setGridBagConstraints(0, 0, 1, 1, gbc);
        panel.add(imgLabel, gbc);

        JLabel infoLabel = new JLabel(menuItem.getMenuItemInformation());
        InterfaceUtilities.setPreferredSize(infoLabel, PREVIEW_WIDTH,400);
        infoLabel.setVerticalAlignment(SwingConstants.TOP);

        InterfaceUtilities.setGridBagConstraints(0, 1, 1, 1, gbc);
        panel.add(infoLabel, gbc);

        // Create a scrollable pane containing the container panel
        JScrollPane scrollPane = new JScrollPane(panel);
        InterfaceUtilities.setPreferredSize(scrollPane, 325,400);

        InterfaceUtilities.setGridBagConstraints(1, 0, 1, 9, gbc);
        parent.add(scrollPane, gbc);
    }

    /**
     * Initializes the Name text field.
     *
     * @param parent The parent JPanel.
     */
    private void prepareNameTextField(JPanel parent) {
        JLabel label = new JLabel("Name: *");
        InterfaceUtilities.setGridBagConstraints(0, 0, 1, 1, gbc);
        parent.add(label, gbc);

        JTextField textField = new JTextField();
        InterfaceUtilities.setPreferredSize(textField, 350, 25);
        InterfaceUtilities.setGridBagConstraints(0, 1, 1, 1, gbc);
        parent.add(textField, gbc);
        name = "";

        // Listen for changes in the text field to update maxPrice.
        textField.getDocument().addDocumentListener(new DocumentListener() {
            private void updateText() {
                name = textField.getText();
            }

            public void changedUpdate(DocumentEvent e) { updateText(); }
            public void removeUpdate(DocumentEvent e) { updateText(); }
            public void insertUpdate(DocumentEvent e) { updateText(); }
        });
    }

    /**
     * Initializes the Contact text field.
     *
     * @param parent The parent JPanel.
     */
    private void prepareContactTextField(JPanel parent) {
        JLabel label = new JLabel("Contact Number: *");
        InterfaceUtilities.setGridBagConstraints(0, 4, 1, 1, gbc);
        parent.add(label, gbc);

        JTextField textField = new JTextField();
        InterfaceUtilities.setPreferredSize(textField, 350, 25);
        InterfaceUtilities.setGridBagConstraints(0, 5, 1, 1, gbc);
        parent.add(textField, gbc);
        contact = "";

        // Listen for changes in the text field to update maxPrice.
        textField.getDocument().addDocumentListener(new DocumentListener() {
            private void updateText() {
                contact = textField.getText();
            }

            public void changedUpdate(DocumentEvent e) { updateText(); }
            public void removeUpdate(DocumentEvent e) { updateText(); }
            public void insertUpdate(DocumentEvent e) { updateText(); }
        });
    }

    /**
     * Initializes the Special Request text field.
     *
     * @param parent The parent JPanel.
     */
    private void prepareSpecialRequestTextField(JPanel parent) {
        JLabel label = new JLabel("Special Requests:");
        InterfaceUtilities.setGridBagConstraints(0, 7, 1, 1, gbc);
        parent.add(label, gbc);

        JTextArea textField = new JTextArea();
        InterfaceUtilities.setPreferredSize(textField, 350, 265);
        InterfaceUtilities.setGridBagConstraints(0, 8, 1, 1, gbc);
        parent.add(textField, gbc);
        specialRequests = "";

        // Listen for changes in the text field to update maxPrice.
        textField.getDocument().addDocumentListener(new DocumentListener() {
            private void updateText() {
                specialRequests = textField.getText();
            }

            public void changedUpdate(DocumentEvent e) { updateText(); }
            public void removeUpdate(DocumentEvent e) { updateText(); }
            public void insertUpdate(DocumentEvent e) { updateText(); }
        });
    }

    /**
     * Prepares the search button, adds it to the parent panel, and assigns its action listener.
     *
     * @param parent The parent JPanel to which the search button will be added.
     */
    private void prepareSearchButton(JPanel parent) {
        // Create and position the "Submit Order" button.
        JButton button = InterfaceUtilities.createButton("Submit Order");
        InterfaceUtilities.setGridBagConstraints(0, 10, 2, 1, gbc);
        parent.add(button, gbc);

        // Assign action to handle user submission.
        button.addActionListener(e -> handleButtonAction());
    }

    /**
     * Handles the logic for when the search button is clicked.
     */
    private void handleButtonAction() {
        // Validate name
        if (name == null || name.isEmpty()) {
            displayError("Please Enter A Valid Name");
            return;
        }

        // Validate and parse contact number
        long newContact = validateContact();
        if (newContact == 0) return;

        // Create new Geek object and hide this UI.
        geek = new Geek(name, newContact);
        hide();
    }

    /**
     * Validates the contact number and converts it to long.
     *
     * @return The contact number as a long value, or 0 if invalid.
     */
    private long validateContact() {
        try {
            if (contact == null || contact.isEmpty()) {
                displayError("Please Enter A Valid Number");
                return 0;
            }
            long newContact = Long.parseLong(contact);

            if (String.valueOf(newContact).length() != 9) {
                displayError("Please Enter A Valid Number");
                return 0;
            }
            return newContact;
        } catch (NumberFormatException e) {
            displayError("Please Enter A Valid Number");
            return 0;
        }
    }

    /**
     * Displays an error message dialog.
     *
     * @param msg The error message to display.
     */
    private void displayError(String msg) {
        JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);
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
     * Gets the frame.
     *
     * @return the frame
     */
    public JFrame getFrame() {
        return frame;
    }


    /**
     * Gets the geek object.
     *
     * @return the geek object
     */
    public Geek getGeek() {
        return geek;
    }

    /**
     * Gets the special Requests String.
     *
     * @return the specialRequests string
     */
    public String getSpecialRequests() {
        return specialRequests;
    }
}
