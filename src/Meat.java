public enum Meat {
    /**
     * Created by Dr Andreas Shepley for COSC120 on 03/07/2023
     */
    BEEF, CHICKEN, VEGAN, NA;

    public String toString(){
        return switch (this) {
            case BEEF -> "Beef";
            case CHICKEN -> "Chicken";
            case VEGAN -> "Vegan";
            case NA -> "Any meat will do...";
        };
    }


}
