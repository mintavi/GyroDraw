package ch.epfl.sweng.SDP.shop;

import static ch.epfl.sweng.SDP.utils.Preconditions.checkPrecondition;

/**
 * Enum representing the colors in the shop.
 */
public enum ColorsShop {
    BLUE(100), GREEN(150), YELLOW(200), RED(150);

    private int price;

    ColorsShop(int price) {
        checkPrecondition(price >= 0, "price is negative");
        this.price = price;
    }

    public int getPrice() {
        return this.price;
    }

    /**
     * Converts the given string to the related color.
     *
     * @param color the string describing the color
     * @return the color represented by the string
     * @throws IllegalArgumentException if the given string does not correspond to a color
     *                                  in the shop
     */
    public static ColorsShop getColorFromString(String color) {
        checkPrecondition(color != null, "color is null");
        switch (color) {
            case "BLUE":
                return BLUE;
            case "GREEN":
                return GREEN;
            case "YELLOW":
                return YELLOW;
            case "RED":
                return RED;
            default:
                throw new IllegalArgumentException(color + " not found");
        }
    }

}
