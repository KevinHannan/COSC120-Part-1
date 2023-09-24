public enum Filter {
    /**
     * Created by Dr Andreas Shepley for COSC120 on 03/07/2023
     */
    TYPE,BUN,MEAT,CHEESE,PICKLES,CUCUMBER,TOMATO,DRESSING,LEAFY_GREENS,SAUCE_S;

    public String toString(){
        return switch (this) {
            case TYPE -> "Menu item type";
            case BUN -> "Bun/bread";
            case MEAT -> "Meat";
            case CHEESE -> "Cheese";
            case PICKLES -> "Pickles (gherkins)";
            case CUCUMBER -> "Cucumber (continental)";
            case TOMATO -> "Tomato";
            case DRESSING -> "Salad dressing";
            case LEAFY_GREENS -> "Leafy greens";
            case SAUCE_S -> "Sauces";
        };
    }

}
