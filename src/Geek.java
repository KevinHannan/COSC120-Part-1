public class Geek {
    /**
     * Created by Dr Andreas Shepley for COSC120 on 03/07/2023
     */
    private final String name;
    private final long orderNumber;

    public Geek(String name, long orderNumber) {
        this.name = name;
        this.orderNumber = orderNumber;
    }

    public String getName() {
        return name;
    }

    public long getOrderNumber() {
        return orderNumber;
    }

}
