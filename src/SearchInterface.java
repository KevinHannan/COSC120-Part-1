import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class represents a SearchGUI for the Gobbledy-Geek app.
 * The GUI allows the user to select and order ingredients for a meal.
 * This class creates and manages the GUI components such as text fields, buttons, checkboxes and radio buttons.
 */
public class SearchInterface {
    private static final String LOGO_PATH = "gobbledy_geek_graphic.png";
    private static final String[] PREVIEW_PATHS = {
            "images/10895.png",
            "images/10922.png",
            "images/11706.png",
            "images/11786.png"
    };
    private static final String[] options = {
            "yes",
            "no",
            "I don't mind"
    };
    private static final int LOGO_WIDTH = 200;
    private static final int LOGO_HEIGHT = 400;
    private static final int PREVIEW_WIDTH = 120;
    private static final int PREVIEW_HEIGHT = 120;
    private final JFrame frame;
    private final GridBagConstraints gbc;
    private String bunType;
    private Set<String> sauceTypes;
    private Set<String> leafyGreensTypes;
    private String orderType;
    private String dressingType;
    private String meatType;
    private String tomatoType;
    private String cucumberType;
    private String pickleType;
    private String cheeseType;
    private String minPrice;
    private String maxPrice;

    /**
     * Constructor for the SearchGUI class.
     * Initializes the frame, panel, layout, and components of the GUI.
     *
     * @param menu The menu object containing the list of available ingredients.
     */
    public SearchInterface(Menu menu) {
        // Initialize the main frame of the GUI
        frame = InterfaceUtilities.createFrame();

        // Prepare the main control panel and add it to the main frame
        JPanel controlPanel = InterfaceUtilities.prepareControlPanel(frame);

        // Setup layout constraints
        gbc = InterfaceUtilities.createConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(4, 4, 4, 4);

        // Add company logo to the control panel
        prepareCompanyLogo(controlPanel);

        // Create and setup default panel
        JPanel defaultPanel = prepareTypePanel(true, controlPanel);
        prepareDefaultTypeSection(defaultPanel);

        // Create and setup burger-specific panel and its components
        JPanel burgerPanel = prepareTypePanel(false, controlPanel);
        prepareBunComboBox(burgerPanel, menu.getAllIngredientTypes(Filter.BUN));
        prepareSauceList(burgerPanel);

        // Create and setup salad-specific panel and its components
        JPanel saladPanel = prepareTypePanel(false, controlPanel);
        prepareDressingComboBox(saladPanel);
        prepareCucumberRadioButtons(saladPanel);
        prepareLeafyGreensList(saladPanel, menu.getAllIngredientTypes(Filter.LEAFY_GREENS));

        // Add a combo box for the user to select the type of order
        prepareTypeComboBox(controlPanel, defaultPanel, burgerPanel, saladPanel);

        // Additional components added to the main control panel
        prepareTomatoesRadioButtons(controlPanel);
        preparePicklesRadioButtons(controlPanel);
        prepareMeatComboBox(controlPanel);
        prepareCheeseCheckbox(controlPanel);
        prepareMinPriceTextField(controlPanel);
        prepareMaxPriceTextField(controlPanel);

        // Add a button to initiate the search
        prepareSearchButton(controlPanel);
    }

    /**
     * Makes the frame visible and non-resizable.
     */
    public void show() {
        // Make the main frame of the GUI visible
        frame.setVisible(true);

        // Ensure that the main frame cannot be resized
        frame.setResizable(false);
    }

    /**
     * Makes the frame invisible and non-resizable.
     */
    public void hide() {
        // Make the main frame of the GUI visible
        frame.setVisible(false);

        // Ensure that the main frame cannot be resized
        frame.setResizable(false);
    }

    /**
     * Creates and returns a type panel, which is a panel that contains components specific to a particular order type.
     *
     * @param isVisible A boolean value indicating whether the panel should be initially visible.
     * @param parent The parent panel to which the type panel will be added.
     * @return The created type panel.
     */
    private JPanel prepareTypePanel(boolean isVisible, JPanel parent) {
        // Create a new panel
        JPanel panel = InterfaceUtilities.createPanel();

        // Set the layout constraints for this panel's position and size within the parent container
        InterfaceUtilities.setGridBagConstraints(1, 1, 4, 1, gbc);

        // Add the panel to the parent container using the defined constraints
        parent.add(panel, gbc);

        // Set the visibility of the panel based on the provided flag
        panel.setVisible(isVisible);

        return panel;
    }

    /**
     * Creates and sets up the bun combo box in the specified parent panel.
     *
     * @param parent The parent panel to which the bun combo box will be added.
     * @param bunTypes The set of available bun types.
     */
    private void prepareBunComboBox(JPanel parent, Set<Object> bunTypes) {
        // Create and set up a label for bun preference
        JLabel label = InterfaceUtilities.createLabel("Preferred bun?");
        InterfaceUtilities.setGridBagConstraints(1, 1, 1, 1, gbc);
        parent.add(label, gbc);

        // Create and set up the bun type combo box
        JComboBox<String> comboBox = InterfaceUtilities.createComboBox();
        InterfaceUtilities.setGridBagConstraints(2, 1, 9, 1, gbc);
        parent.add(comboBox, gbc);

        // Convert set to reversed array
        String[] newBunTypes = bunTypes.toArray(new String[0]);
        String[] reversedBunTypes = new String[newBunTypes.length];
        for (int i = 0; i < newBunTypes.length; i++) {
            reversedBunTypes[i] = newBunTypes[newBunTypes.length - 1 - i];
        }
        bunType = reversedBunTypes[0];

        // Set combo box items and default selection
        comboBox.setModel(new DefaultComboBoxModel<>(reversedBunTypes));

        // Update the selected bun type based on user choice
        comboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                bunType = (String) e.getItem();
            }
        });
    }

    /**
     * Prepares and sets up the list of leafy greens for the given parent JPanel.
     * The user can select their preferred type of leafy greens from the list.
     *
     * @param parent           The JPanel to which the leafy greens list will be added.
     * @param leafyGreensTypes The set of available leafy greens types.
     */
    private void prepareLeafyGreensList(JPanel parent, Set<Object> leafyGreensTypes) {
        // Create and set label for preferred leafy greens type
        JLabel label = InterfaceUtilities.createLabel("Preferred Leafy Greens?");
        InterfaceUtilities.setGridBagConstraints(1, 3, 9, 1, gbc);
        parent.add(label, gbc);

        // Create and configure list for selecting leafy greens
        JList<String> list = new JList<>();
        list.setVisibleRowCount(5);

        // Convert and reverse the set of leafy greens types into an array
        String[] leafyGreensArray = leafyGreensTypes.toArray(new String[0]);
        String[] reversedLeafyGreens = new String[leafyGreensArray.length];
        for (int i = 0; i < leafyGreensArray.length; i++) {
            reversedLeafyGreens[i] = leafyGreensArray[leafyGreensArray.length - 1 - i];
        }

        // Set list's model and wrap in a scrollable pane
        list.setModel(new DefaultListModel<String>() {{
            for(String greens : reversedLeafyGreens) addElement(greens);
        }});
        JScrollPane scrollPane = new JScrollPane(list);
        InterfaceUtilities.setPreferredSize(scrollPane, 503,75);

        // Add scroll pane with list to parent panel
        InterfaceUtilities.setGridBagConstraints(1, 4, 9, 1, gbc);
        parent.add(scrollPane, gbc);
        this.leafyGreensTypes = new HashSet<>();

        // Update the set of selected leafy greens when user makes a selection
        list.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                this.leafyGreensTypes = new HashSet<>(list.getSelectedValuesList());
            }
        });
    }

    /**
     * Prepares and sets up the radio buttons for the cucumber choice for the given parent JPanel.
     * The user can choose between "Yes", "No", or "I Ddon't Mind" options regarding the inclusion
     * of cucumbers in their meal.
     *
     * @param parent The JPanel to which the cucumber choice radio buttons will be added.
     */
    private void prepareCucumberRadioButtons(JPanel parent) {
        // Create and add label for cucumber choice
        JLabel label = InterfaceUtilities.createLabel("Cucumbers: ");
        InterfaceUtilities.setGridBagConstraints(1, 2, 1, 1, gbc);
        parent.add(label, gbc);

        // Create a group for the radio buttons and a panel to hold them
        ButtonGroup buttonGroup = new ButtonGroup();
        JPanel panel = InterfaceUtilities.createPanel();

        // Add radio buttons to the group and panel
        for (int x = 0; x < options.length; x++) {
            JRadioButton radioButton = InterfaceUtilities.createRadioButton(options[x]);
            InterfaceUtilities.setGridBagConstraints(x+1, 2, 1, 1, gbc);

            // Set default choice as "I don't Mind"
            if (x == options.length-1) {
                radioButton.setSelected(true);
                cucumberType = options[2];
            }

            // Add action listener to update cucumberType based on selection
            radioButton.addActionListener(e -> {
                cucumberType = ((JRadioButton) e.getSource()).getText();
            });
            panel.add(radioButton);
            buttonGroup.add(radioButton);
        }
        parent.add(panel, gbc);
    }

    /**
     * Prepares and sets up a combo box for dressing choices for the given parent JPanel.
     * The user can select their preferred type of dressing from the combo box.
     *
     * @param parent The JPanel to which the dressing choice combo box will be added.
     */
    private void prepareDressingComboBox(JPanel parent) {
        // Add label for dressing choice
        JLabel label = InterfaceUtilities.createLabel("Preferred Dressing?");
        InterfaceUtilities.setGridBagConstraints(1, 1, 1, 1, gbc);
        parent.add(label, gbc);

        // Create and set up combo box for dressing choices
        JComboBox<String> comboBox = InterfaceUtilities.createComboBox();
        InterfaceUtilities.setGridBagConstraints(2, 1, 9, 1, gbc);
        Dressing[] dressings = Dressing.values();
        String[] reversedDressings = new String[dressings.length];
        for (int x = 0; x < dressings.length; x++) {
            reversedDressings[x] = dressings[dressings.length - 1 - x].toString();
        }
        comboBox.setModel(new DefaultComboBoxModel<>(reversedDressings));
        dressingType = reversedDressings[0];
        parent.add(comboBox, gbc);

        // Update dressingType when a choice is selected
        comboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                dressingType = (String) e.getItem();
            }
        });
    }

    /**
     * Creates and sets up the sauce list in the specified parent panel.
     *
     * @param parent The parent panel to which the sauce list will be added.
     */
    private void prepareSauceList(JPanel parent) {
        // Create a label for the sauce choice and add to parent
        JLabel label = InterfaceUtilities.createLabel("Preferred Sauces?");
        InterfaceUtilities.setGridBagConstraints(1, 2, 9, 1, gbc);
        parent.add(label, gbc);

        // Set up sauce list with scroll pane and add to parent
        JList<String> list = new JList<>();
        list.setVisibleRowCount(5);
        JScrollPane scrollPane = new JScrollPane(list);
        InterfaceUtilities.setPreferredSize(scrollPane,399,75);
        InterfaceUtilities.setGridBagConstraints(1, 3, 9, 1, gbc);
        parent.add(scrollPane, gbc);

        // Populate list and initialize sauceTypes
        sauceTypes = new HashSet<>();
        list.setModel(new DefaultComboBoxModel<>(Arrays.stream(Sauce.values()).map(Sauce::toString).toArray(String[]::new)));

        // Update sauceTypes set when items are selected
        list.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) sauceTypes = new HashSet<>(list.getSelectedValuesList());
        });
    }

    /**
     * Creates and sets up the order type combo box in the specified parent panel.
     *
     * @param parent The parent panel to which the order type combo box will be added.
     * @param defaultPanel The default panel, which contains components that are always visible.
     * @param burgerPanel The burger panel, which contains components specific to burger orders.
     * @param saladPanel The salad panel, which contains components specific to salad orders.
     */
    private void prepareTypeComboBox(JPanel parent, JPanel defaultPanel, JPanel burgerPanel, JPanel saladPanel) {
        // Create and set up the combo box for selecting types
        JComboBox<String> comboBox = InterfaceUtilities.createComboBox();
        InterfaceUtilities.setGridBagConstraints(1, 0, 9, 1, gbc);
        parent.add(comboBox, gbc);

        // Populate the combo box with options, starting with a default "Select Type" option
        String[] typeOptions = Arrays.stream(Type.values()).map(Type::toString).toArray(String[]::new);
        comboBox.setModel(new DefaultComboBoxModel<>(Stream.concat(Stream.of("Select Type"), Arrays.stream(typeOptions)).toArray(String[]::new)));
        orderType = "Select Type";

        // Add an item listener to change panel visibility based on the selected item
        comboBox.addItemListener(arg0 -> {
            if (arg0.getStateChange() == ItemEvent.SELECTED) {
                String selectedType = comboBox.getSelectedItem().toString();
                switch (selectedType) {
                    case "Burger":
                        defaultPanel.setVisible(false);
                        saladPanel.setVisible(false);
                        burgerPanel.setVisible(true);
                        break;
                    case "Salad":
                        burgerPanel.setVisible(false);
                        defaultPanel.setVisible(false);
                        saladPanel.setVisible(true);
                        break;
                    default:
                        saladPanel.setVisible(false);
                        burgerPanel.setVisible(false);
                        defaultPanel.setVisible(true);
                        break;
                }
                orderType = selectedType;
            }
        });
    }

    /**
     * Creates and sets up the company logo in the specified parent panel.
     *
     * @param parent The parent panel to which the company logo will be added.
     */
    private void prepareCompanyLogo(JPanel parent) {
        // Create and scale the logo image.
        Image image = InterfaceUtilities.createImage(LOGO_PATH, LOGO_WIDTH, LOGO_HEIGHT);
        JLabel label = InterfaceUtilities.createLabel();
        label.setIcon(new ImageIcon(image));

        // Position the logo in the top left corner of the panel.
        InterfaceUtilities.setGridBagConstraints(0, 0, 1, 10, gbc);
        parent.add(label, gbc);
    }


    /**
     * Prepares the meat combo box component and adds it to the parent panel.
     *
     * @param parent the parent panel to which the components will be added
     */
    private void prepareMeatComboBox(JPanel parent) {
        // Set up label for meat preference.
        JLabel label = InterfaceUtilities.createLabel("Preferred meat?");
        InterfaceUtilities.setGridBagConstraints(1, 5, 1, 1, gbc);
        parent.add(label, gbc);

        // Create and position combo box for meat selections.
        JComboBox<String> comboBox = InterfaceUtilities.createComboBox();
        InterfaceUtilities.setGridBagConstraints(2, 5, 6, 1, gbc);
        parent.add(comboBox, gbc);

        // Populate combo box with reversed meat types.
        Meat[] meatValues = Meat.values();
        String[] newMeatTypes = new String[meatValues.length];
        for (int i = 0; i < meatValues.length; i++) {
            newMeatTypes[i] = meatValues[meatValues.length - 1 - i].toString();
        }
        comboBox.setModel(new DefaultComboBoxModel<>(newMeatTypes));
        meatType = newMeatTypes[0];

        // Update meatType variable when a new meat type is selected.
        comboBox.addItemListener(arg0 -> {
            if(arg0.getStateChange() == ItemEvent.SELECTED) {
                meatType = comboBox.getSelectedItem().toString();
            }
        });
    }

    /**
     * Prepares the tomatoes radio buttons component and adds it to the parent panel.
     *
     * @param parent the parent panel to which the components will be added
     */
    private void prepareTomatoesRadioButtons(JPanel parent) {
        // Set up a label for the tomato preference.
        JLabel label = InterfaceUtilities.createLabel("Tomatoes: ");
        InterfaceUtilities.setGridBagConstraints(1, 3, 1, 1, gbc);
        parent.add(label, gbc);

        // Grouping the radio buttons to ensure only one can be selected at a time.
        ButtonGroup buttonGroup = new ButtonGroup();
        JPanel panel = InterfaceUtilities.createPanel();

        // Iterate over options to create radio buttons.
        for (int x = 0; x < options.length; x++) {
            JRadioButton radioButton = InterfaceUtilities.createRadioButton(options[x]);
            panel.add(radioButton);
            buttonGroup.add(radioButton);

            // Set default selected option.
            if (x == options.length-1) {
                radioButton.setSelected(true);
                tomatoType = options[x];
            }

            // Update tomatoType variable when an option is selected.
            radioButton.addActionListener(e -> {
                tomatoType = radioButton.getText();
            });
        }
        InterfaceUtilities.setGridBagConstraints(2, 3, options.length, 1, gbc);
        parent.add(panel, gbc);
    }

    /**
     * Prepares the pickles radio buttons component and adds it to the parent panel.
     *
     * @param parent the parent panel to which the components will be added
     */
    private void preparePicklesRadioButtons(JPanel parent) {
        // Set up a label for the pickle preference.
        JLabel label = InterfaceUtilities.createLabel("Pickles: ");
        InterfaceUtilities.setGridBagConstraints(1, 4, 1, 1, gbc);
        parent.add(label, gbc);

        // Grouping the radio buttons to ensure only one can be selected at a time.
        ButtonGroup buttonGroup = new ButtonGroup();
        JPanel panel = InterfaceUtilities.createPanel();

        // Iterate over options to create radio buttons.
        for (int x = 0; x < options.length; x++) {
            JRadioButton radioButton = InterfaceUtilities.createRadioButton(options[x]);
            panel.add(radioButton);
            buttonGroup.add(radioButton);

            // Set default selected option.
            if (x == options.length-1) {
                radioButton.setSelected(true);
                pickleType = options[x];
            }

            // Update pickleType variable when an option is selected.
            radioButton.addActionListener(e -> {
                pickleType = radioButton.getText();
            });
        }
        InterfaceUtilities.setGridBagConstraints(2, 4, options.length, 1, gbc);
        parent.add(panel, gbc);
    }

    /**
     * Prepares the cheese checkbox component and adds it to the parent panel.
     *
     * @param parent the parent panel to which the components will be added
     */
    private void prepareCheeseCheckbox(JPanel parent) {
        // Set up and position the checkbox for cheese preference.
        JCheckBox checkBox = InterfaceUtilities.createCheckBox("Cheese?");
        InterfaceUtilities.setGridBagConstraints(1, 6, 6, 1, gbc);
        parent.add(checkBox, gbc);

        // Default preference for cheese.
        cheeseType = checkBox.isSelected() ? "true" : "false";

        // Update cheeseType based on checkbox state.
        checkBox.addActionListener(e -> cheeseType = String.valueOf(checkBox.isSelected()));
    }

    /**
     * Prepares the minimum price text field component and adds it to the parent panel.
     *
     * @param parent the parent panel to which the components will be added
     */
    private void prepareMinPriceTextField(JPanel parent) {
        // Set up and position the label and text field for minimum price input.
        JLabel label = InterfaceUtilities.createLabel("Min Price?");
        InterfaceUtilities.setGridBagConstraints(1, 7, 9, 1, gbc);
        parent.add(label, gbc);

        JTextField textField = InterfaceUtilities.createTextField();
        InterfaceUtilities.setGridBagConstraints(2, 7, 9, 1, gbc);
        parent.add(textField, gbc);

        // Default value for minPrice.
        minPrice = "";

        // Listen for changes in the text field to update minPrice.
        textField.getDocument().addDocumentListener(new DocumentListener() {
            private void updateText() {
                minPrice = textField.getText();
            }

            public void changedUpdate(DocumentEvent e) { updateText(); }
            public void removeUpdate(DocumentEvent e) { updateText(); }
            public void insertUpdate(DocumentEvent e) { updateText(); }
        });
    }

    /**
     * Prepares the max price text field and adds it to the parent panel.
     *
     * @param parent the JPanel to which the max price text field will be added.
     */
    private void prepareMaxPriceTextField(JPanel parent) {
        // Set up and position the label and text field for maximum price input.
        JLabel label = InterfaceUtilities.createLabel("Max Price?");
        InterfaceUtilities.setGridBagConstraints(1, 8, 8, 1, gbc);
        parent.add(label, gbc);

        JTextField textField = InterfaceUtilities.createTextField();
        InterfaceUtilities.setGridBagConstraints(2, 8, 9, 1, gbc);
        parent.add(textField, gbc);

        // Default value for maxPrice.
        maxPrice = "";

        // Listen for changes in the text field to update maxPrice.
        textField.getDocument().addDocumentListener(new DocumentListener() {
            private void updateText() {
                maxPrice = textField.getText();
            }

            public void changedUpdate(DocumentEvent e) { updateText(); }
            public void removeUpdate(DocumentEvent e) { updateText(); }
            public void insertUpdate(DocumentEvent e) { updateText(); }
        });

    }

    /**
     * Prepares the search button and adds it to the parent panel.
     *
     * @param parent the JPanel to which the search button will be added.
     */
    private void prepareSearchButton(JPanel parent) {
        // Set up and position the search button.
        JButton button = InterfaceUtilities.createButton("Search");
        InterfaceUtilities.setGridBagConstraints(0, 10, 10, 1, gbc);
        parent.add(button, gbc);

        // Handle search logic.
        button.addActionListener(e -> {
            if (Objects.equals(orderType, "Select Type")) {
                JOptionPane.showMessageDialog(null, "Order Type Is Set To Select Type", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (sauceTypes.isEmpty() && "Burger".equals(orderType)) {
                JOptionPane.showMessageDialog(null, "Sauce List Is Empty", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (minPrice.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Min Price Is Empty", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double newMinPrice = parseDoubleOrWarn(minPrice, "Min Price Cannot Be Converted To Double");
            if (newMinPrice < 0) {
                JOptionPane.showMessageDialog(null, "Min Price Cannot Be Less Than 0", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double newMaxPrice = parseDoubleOrWarn(maxPrice, "Max Price Cannot Be Converted To Double");
            if (newMaxPrice < newMinPrice) {
                JOptionPane.showMessageDialog(null, "Max Price Cannot Be Less Than Min Price", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (leafyGreensTypes.isEmpty() && "Salad".equals(orderType)) {
                JOptionPane.showMessageDialog(null, "Leafy Greens List Is Empty", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            hide();
        });
    }

    /**
     * Parses the provided string value into a double.
     * If parsing fails, it prints the provided warning message
     * and re-throws the exception to halt the current flow.
     *
     * @param value   The string value to be parsed into a double.
     * @param warning The warning message to be printed if parsing fails.
     * @return The parsed double value.
     * @throws NumberFormatException If the provided string cannot be parsed into a double.
     */
    private double parseDoubleOrWarn(String value, String warning) {
        try {
            return Double.parseDouble(value);
        } catch (Exception x) {
            JOptionPane.showMessageDialog(null, warning, "Error", JOptionPane.ERROR_MESSAGE);
            throw x; // Re-throwing the exception to stop the current flow.
        }
    }

    /**
     * Prepares the default type section and adds it to the parent panel.
     *
     * @param parent the JPanel to which the default type section will be added.
     */
    private void prepareDefaultTypeSection(JPanel parent) {
        // Loop through the preview paths to create image icons and add them to the panel
        for (int x = 0; x < 4; x++) {
            Image image = InterfaceUtilities.createImage(PREVIEW_PATHS[x], PREVIEW_WIDTH, PREVIEW_HEIGHT);
            JLabel label = InterfaceUtilities.createLabel();
            label.setIcon(new ImageIcon(image));
            InterfaceUtilities.setGridBagConstraints(x+1, 1, 1, 1, gbc);
            parent.add(label, gbc);
        }
    }

    /**
     * Retrieves a map of filter criteria based on the order type.
     *
     * @return A map where the keys are filter criteria and the values are their corresponding values.
     */
    public Map<Filter, Object> getFilterMap() {
        // Create a map to store filters for the order.
        Map<Filter, Object> filterMap = new LinkedHashMap<>();

        // Add the type of order to the map.
        filterMap.put(Filter.TYPE, Type.valueOf(orderType.toUpperCase()));

        // If the order is of type "Burger", add specific filters.
        if ("Burger".equals(orderType)) {
            if (!"I don't mind".equals(bunType)) {
                filterMap.put(Filter.BUN, bunType);
            }

            boolean sauceSave = true;
            String[] sauceArray = sauceTypes.toArray(new String[0]);
            for (int x = 0; x < sauceArray.length; x++) {
                if (Objects.equals(sauceArray[x], "Any sauce will do...")) {
                    sauceSave = false;
                } else if (Objects.equals(sauceArray[x], "Aioli (vegan friendly)")) {
                    sauceArray[x] = "AIOLI";
                } else if (Objects.equals(sauceArray[x], "Special sauce")) {
                    sauceArray[x] = "SPECIAL";
                } else {
                    sauceArray[x] = sauceArray[x].toUpperCase();
                }
            }
            if (sauceSave) {
                filterMap.put(Filter.SAUCE_S,Arrays.stream(sauceArray)
                        .map(String::toUpperCase)
                        .map(Sauce::valueOf)
                        .collect(Collectors.toSet()));
            }
        }

        // Add meat type filter if it's specified.
        if (!"Any meat will do...".equals(meatType)) {
            filterMap.put(Filter.MEAT, Meat.valueOf(meatType.toUpperCase()));
        }

        // Add pickle preference if it's not the default.
        if (!options[2].equals(pickleType)) {
            filterMap.put(Filter.PICKLES, options[0].equals(pickleType));
        }

        // Add cheese preference to the map.
        filterMap.put(Filter.CHEESE, "true".equals(cheeseType));

        // Add tomato preference if it's not the default.
        if (!options[2].equals(tomatoType)) {
            filterMap.put(Filter.TOMATO, options[0].equals(tomatoType));
        }

        // If the order is of type "Salad", add specific filters.
        if ("Salad".equals(orderType)) {
            if (!Objects.equals(dressingType, "I don't mind...")) {
                filterMap.put(Filter.DRESSING, dressingType);
            }

            boolean leafySave = true;
            String[] leafyArray = leafyGreensTypes.toArray(new String[0]);
            for (String s : leafyArray) {
                if (Objects.equals(s, "I don't mind")) {
                    leafySave = false;
                    break;
                }
            }
            if (leafySave) {
                filterMap.put(Filter.LEAFY_GREENS,Arrays.stream(leafyArray)
                        .map(String::toUpperCase)
                        .map(Sauce::valueOf)
                        .collect(Collectors.toSet()));
            }

            if (!options[2].equals(cucumberType)) {
                filterMap.put(Filter.CUCUMBER, options[0].equals(cucumberType));
            }
        }

        // Return the populated filter map.
        return filterMap;
    }

    /**
     * Gets the minimum price.
     *
     * @return the minimum price
     */
    public String getMinPrice() {
        return minPrice;
    }

    /**
     * Gets the maximum price.
     *
     * @return the maximum price
     */
    public String getMaxPrice() {
        return maxPrice;
    }

    /**
     * Gets the frame.
     *
     * @return the frame
     */
    public JFrame getFrame() {
        return frame;
    }
}