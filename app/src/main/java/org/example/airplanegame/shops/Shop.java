package org.example.airplanegame.shops;
import org.example.airplanegame.player.UserProfile;

import java.util.ArrayList;

/**
 * Shop class that holds all the items in the shop and tracks if a user purchased an item
 * @author Samar
 */
public class Shop {
    /**
     * an arraylist holding all the items
     */
    private ArrayList<Item> items;

    /**
     * Initializes the shop class and adds items to the list
     * @param items is the list of items in the shop
     */
    public Shop(ArrayList<Item> items) {
        this.items = new ArrayList<>(items);
    } // end of constructor

    /**
     * This method purchases an item correctly after checking if its been bought already or have enough funds
     * @param itemID is the id of the item
     * @return true or false if item has been purchased
     */
    public boolean purchaseItem(String itemID, UserProfile user) {
        for(int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            if (item.getItemID().equals(itemID)) {
                if(user.getMoney() < item.getPrice()) {
                    return false;
                }
                if (item.getItemType().equals("COSMETIC")) {
                    if(user.getInventory().hasCosmeticItem(itemID)) {
                        return false;
                    }
                    user.setMoney(user.getMoney() - item.getPrice());
                    user.getInventory().addCosmeticItem(itemID);
                    return true;
                }
                if (item.getItemType().equals("POWERUP")) {
                    user.setMoney(user.getMoney() - item.getPrice());
                    user.getInventory().addPowerUpItem(itemID);
                    return true;
                }
            }
        }
        return false; // item not found
    } // end of purchaseItem(String itemID)

    /**
     * gets the items
     * @return the items
     */
    public ArrayList<Item> getItems() {
        return items;
    } // end of getItems()

} // end of class
