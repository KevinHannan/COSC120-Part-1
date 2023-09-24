public enum Dressing {
    /**
     * Created by Dr Andreas Shepley for COSC120 on 03/07/2023
     */
    RANCH,FRENCH,ITALIAN,GREEN_GODDESS,NA;

    public String toString(){
        return switch (this) {
            case RANCH -> "Ranch";
            case FRENCH -> "French";
            case ITALIAN -> "Italian";
            case GREEN_GODDESS -> "Green goddess";
            case NA -> "I don't mind...";
        };
    }

}
