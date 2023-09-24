import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.List;

public class MenuSearcher {
    /**
     * Created by Dr Andreas Shepley for COSC120 on 03/07/2023
     */
    private static final String filePath = "./menu.txt";
    private static Menu menu;
    private static SearchInterface searchInterface;
    private static ResultInterface resultInterface;
    private static OrderInterface orderInterface;

    /**
     * Entry point for the application. This method initializes the system by loading
     * menu items and then launching the search interface.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        // Load the menu items from a file located at 'filePath'
        menu = loadMenu(filePath);

        // Initialize a new search interface with the loaded menu
        searchInterface = new SearchInterface(menu);

        // Start the search GUI for users to find menu items
        handleSearchGUI();
    }

    /**
     * Handles the search GUI for finding menu items based on certain criteria.
     * This method initializes and shows the SearchInterface window where the user can enter their search criteria.
     * It also adds an event listener to handle actions when the SearchInterface window is closed.
     */
    public static void handleSearchGUI() {
        // Display the search interface
        searchInterface.show();

        // Add a ComponentListener to the SearchInterface's frame to handle window events
        searchInterface.getFrame().addComponentListener(new ComponentAdapter() {
            /**
             * Invoked when the component has been made invisible.
             *
             * @param e The ComponentEvent object.
             */
            @Override
            public void componentHidden(ComponentEvent e) {
                // Create a DreamMenuItem object using the search criteria and price range obtained from the SearchInterface
                DreamMenuItem dreamMenuItem = new DreamMenuItem(
                        searchInterface.getFilterMap(),
                        Double.parseDouble(searchInterface.getMinPrice()),
                        Double.parseDouble(searchInterface.getMaxPrice())
                );

                // Find menu items that match the given DreamMenuItem's attributes
                List<MenuItem> matching = menu.findMatch(dreamMenuItem);

                // Check if any matching menu items are found
                if (!matching.isEmpty()) {
                    // If matches are found, display them in the Results GUI
                    handleResultsGUI(matching);
                } else {
                    // If no matches are found, show an error dialog and re-open the Search Interface
                    JOptionPane.showMessageDialog(null, "No Results Found", "Error", JOptionPane.ERROR_MESSAGE);
                    searchInterface.show();
                }
            }
        });
    }

    /**
     * Handles the Results GUI.
     * This method initializes and shows the ResultInterface window,
     * which displays a list of menu items that match the user's search.
     * It also sets up an event listener to perform actions when the ResultInterface window is closed.
     *
     * @param dreamMenuItem A list of MenuItem objects that match the user's search criteria.
     */
    public static void handleResultsGUI(List<MenuItem> dreamMenuItem) {
        // Create and show the ResultInterface window with the matching menu items.
        resultInterface = new ResultInterface(dreamMenuItem);
        resultInterface.show();

        // Add a ComponentListener to the ResultInterface's frame.
        resultInterface.getFrame().addComponentListener(new ComponentAdapter() {
            /**
             * Called when the ResultInterface window is hidden.
             *
             * @param e The ComponentEvent.
             */
            public void componentHidden(ComponentEvent e) {
                // Check the state of the ResultInterface to determine the next action.
                if (Objects.equals(resultInterface.getState(), "Continue")) {
                    // If the state is "Continue", proceed to handle the order.
                    handleOrderGUI(resultInterface.getSelectedMenuItem());
                } else {
                    // Otherwise, re-show the SearchInterface for a new search.
                    searchInterface.show();
                }
            }
        });
    }

    /**
     * Handles the GUI flow for processing an order.
     *
     * @param menuItem The menu item to be ordered.
     */
    public static void handleOrderGUI(MenuItem menuItem) {
        // Create the OrderInterface and display it.
        orderInterface = new OrderInterface(menuItem);
        orderInterface.show();

        // Add a listener to handle when the frame is hidden.
        orderInterface.getFrame().addComponentListener(new ComponentAdapter() {
            /**
             * Called when the frame is hidden. Submits the order and terminates the program.
             *
             * @param e The ComponentEvent triggering this method.
             */
            public void componentHidden(ComponentEvent e) {
                submitOrder(orderInterface.getGeek(), menuItem, orderInterface.getSpecialRequests());
                JOptionPane.showMessageDialog(null, "Order Has Been Placed", "Confirmation", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            }
        });
    }

    public static void submitOrder(Geek geek, MenuItem menuItem, String specialRequests) {
        String filePath = geek.getName().replace(" ","_")+"_"+menuItem.getMenuItemIdentifier()+".txt";
        Path path = Path.of(filePath);
        String lineToWrite = "Order details:\n\t" +
                "Name: "+geek.getName()+
                " (0"+geek.getOrderNumber()+")";
        if(menuItem.getMenuItemIdentifier()==0) lineToWrite+="\n\nCUSTOM ORDER...\n"+menuItem.getMenuItemInformation();
        else lineToWrite+="\n\tItem: "+menuItem.getMenuItemName()+ " ("+menuItem.getMenuItemIdentifier()+")";
        lineToWrite+="\n\nCustomisation Requests:\n"+specialRequests;

        try {
            Files.writeString(path, lineToWrite);
        }catch (IOException io){
            System.out.println("Order could not be placed. \nError message: "+io.getMessage());
            System.exit(0);
        }
    }

    public static Menu loadMenu(String filePath) {
        Menu menu = new Menu();
        Path path = Path.of(filePath);
        List<String> fileContents = null;
        try {
            fileContents = Files.readAllLines(path);
        }catch (IOException io){
            System.out.println("File could not be found");
            System.exit(0);
        }

        for(int i=1;i<fileContents.size();i++){

            String[] info = fileContents.get(i).split("\\[");
            String[] singularInfo = info[0].split(",");

            String leafyGreensRaw = info[1].replace("]","");
            String saucesRaw = info[2].replace("]","");
            String description = info[3].replace("]","");

            long menuItemIdentifier = 0;
            try{
                menuItemIdentifier = Long.parseLong(singularInfo[0]);
            }catch (NumberFormatException n) {
                System.out.println("Error in file. Menu item identifier could not be parsed for item on line "+(i+1)+". Terminating. \nError message: "+n.getMessage());
                System.exit(0);
            }

            Type type = null;
            try{
                type = Type.valueOf(singularInfo[1].toUpperCase().strip());
            }catch (IllegalArgumentException e){
                System.out.println("Error in file. Type data could not be parsed for item on line "+(i+1)+". Terminating. \nError message: "+e.getMessage());
                System.exit(0);
            }

            String menuItemName = singularInfo[2];

            double price = 0;
            try{
                price = Double.parseDouble(singularInfo[3]);
            }catch (NumberFormatException n){
                System.out.println("Error in file. Price could not be parsed for item on line "+(i+1)+". Terminating. \nError message: "+n.getMessage());
                System.exit(0);
            }

            String bun = singularInfo[4].toLowerCase().strip();

            Meat meat = null;
            try {
                meat = Meat.valueOf(singularInfo[5].toUpperCase());
            }catch (IllegalArgumentException e){
                System.out.println("Error in file. Meat data could not be parsed for item on line "+(i+1)+". Terminating. \nError message: "+e.getMessage());
                System.exit(0);
            }

            boolean cheese = false;
            String cheeseRaw = singularInfo[6].strip().toUpperCase();
            if(cheeseRaw.equals("YES")) cheese = true;

            boolean pickles = false;
            String pickleRaw = singularInfo[7].strip().toUpperCase();
            if(pickleRaw.equals("YES")) pickles = true;

            boolean cucumber = false;
            String cucumberRaw = singularInfo[8].strip().toUpperCase();
            if(cucumberRaw.equals("YES")) cucumber = true;

            boolean tomato = false;
            String tomatoRaw = singularInfo[9].strip().toUpperCase();
            if(tomatoRaw.equals("YES")) tomato = true;

            Dressing dressing = null;
            try {
                dressing = Dressing.valueOf(singularInfo[10].toUpperCase().replace(" ","_"));
            }catch (IllegalArgumentException e){
                System.out.println("Error in file. Dressing data could not be parsed for item on line "+(i+1)+". Terminating. \nError message: "+e.getMessage());
                System.exit(0);
            }

            Set<String> leafyGreens = new HashSet<>();
            for(String l: leafyGreensRaw.split(",")){
                leafyGreens.add(l.toLowerCase().strip());
            }

            Set<Sauce> sauces = new HashSet<>();
            for(String s: saucesRaw.split(",")){
                Sauce sauce = null;
                try {
                    sauce = Sauce.valueOf(s.toUpperCase().strip());
                }catch (IllegalArgumentException e){
                    System.out.println("Error in file. Sauce/s data could not be parsed for item on line "+(i+1)+". Terminating. \nError message: "+e.getMessage());
                    System.exit(0);
                }
                sauces.add(sauce);
            }

            Map<Filter,Object> filterMap = new LinkedHashMap<>();
            filterMap.put(Filter.TYPE,type);
            if(type.equals(Type.BURGER)){
                filterMap.put(Filter.BUN, bun);
                if(!sauces.isEmpty()) filterMap.put(Filter.SAUCE_S,sauces);
            }
            if(!meat.equals(Meat.NA)) filterMap.put(Filter.MEAT,meat);
            filterMap.put(Filter.PICKLES, pickles);
            filterMap.put(Filter.CHEESE, cheese);
            filterMap.put(Filter.TOMATO, tomato);
            if(type.equals(Type.SALAD)){
                filterMap.put(Filter.DRESSING,dressing);
                filterMap.put(Filter.LEAFY_GREENS,leafyGreens);
                filterMap.put(Filter.CUCUMBER, cucumber);
            }

            DreamMenuItem dreamMenuItem = new DreamMenuItem(filterMap);
            MenuItem menuItem = new MenuItem(menuItemIdentifier, menuItemName,price,description, dreamMenuItem);
            menu.addItem(menuItem);
        }
        return menu;
    }

}






























