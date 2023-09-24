import java.text.DecimalFormat;

/**
 * Created by Dr Andreas Shepley for COSC120 on 03/07/2023
 */

public class MenuItem {

    //fields
    private final long menuItemIdentifier;
    private final String menuItemName;
    private final String description;
    private final double price;
    private final DreamMenuItem dreamMenuItem;

    //constructor/s
    public MenuItem(long menuItemIdentifier, String menuItemName, double price, String description, DreamMenuItem dreamMenuItem) {
        this.menuItemIdentifier = menuItemIdentifier;
        this.menuItemName = menuItemName;
        this.price = price;
        this.description = description;
        this.dreamMenuItem=dreamMenuItem;
    }

    public MenuItem(DreamMenuItem dreamMenuItem) {
        this.menuItemIdentifier = 0;
        this.menuItemName = "CUSTOM ORDER";
        this.price = -1;
        this.description = "custom - see preferences";
        this.dreamMenuItem=dreamMenuItem;
    }

    //getters
    public long getMenuItemIdentifier() {
        return menuItemIdentifier;
    }
    public String getMenuItemName() {
        return menuItemName;
    }
    public String getDescription() {
        return description;
    }
    public double getPrice() {
        return price;
    }
    public DreamMenuItem getDreamMenuItem(){ return dreamMenuItem;}

    /**
     * Get the information about a menu item in HTML format for display in a JLabel.
     *
     * @return A string containing the menu item information formatted as HTML.
     */
    public String getMenuItemInformation() {
        // Create a DecimalFormat object to format the price later
        DecimalFormat df = new DecimalFormat("0.00");

        // Initialize a StringBuilder and add HTML opening tag
        StringBuilder output = new StringBuilder("<html>");

        // Check if the menu item identifier is not 0 (presumably meaning it's valid)
        if (getMenuItemIdentifier() != 0) {
            // Append the menu item's name and identifier
            output.append(this.getMenuItemName())
                    .append(" (")
                    .append(getMenuItemIdentifier())
                    .append(")<br>")
                    // Append the menu item's description
                    .append(this.getDescription())
                    .append("<br>");
        }

        // Append the "dream" menu item's information and replace newlines with HTML line breaks
        output.append(getDreamMenuItem().getInfo().replace("\n", "<br>"));

        // Check if the price is not -1 (presumably meaning it's set)
        if (price != -1) {
            // Append the menu item's price, formatted using DecimalFormat
            output.append("<br>")
                    .append("Price: $")
                    .append(df.format(this.getPrice()))
                    .append("<br>");
        }

        // Add HTML closing tag
        output.append("</html>");

        // Debug print to the console; remove in production code
        System.out.println(output);

        // Return the final formatted string
        return output.toString();
    }
}
