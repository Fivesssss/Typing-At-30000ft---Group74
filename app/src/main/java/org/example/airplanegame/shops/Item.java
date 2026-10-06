package org.example.airplanegame.shops;

/**
 * Creates an item object that is used in the shop, stores the price, ID, and type of the item
 * @author Samar
 */
public class Item {

    /**
     * is the individual price
     */
    private int price;
    /**
     * a unique ID for each item
     */
    private String itemID;
    /**
     * a string variable to see what type of item it is COSMETIC||POWERUP
     */
    private String itemType;

    /**
     * Initializes the item object with its ID and if it has been purchased
     * @param itemID is the ID of the items
     * @param itemType is the type of item it is
     * @param price is the price of the item
     */
    public Item(String itemID, int price, String itemType) {
        this.itemID = itemID;
        this.itemType = itemType;
        this.price = price;
    } // end of Constructor

    /**
     * gets the itemType
     * @return the itemType variable
     */
    public String getItemType() {
        return this.itemType;
    } // end of getIsPurchased()

    /**
     * gets the itemID
     * @return the itemID
     */
    public String getItemID() {
        return itemID;
    } // end of getItemID()

    /**
     * gets the price of the item
     * @return the price of the item
     */
    public int getPrice() {
        return price;
    } // end of getPrice()
} // end of class
