package org.example.airplanegame.shops;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Inventory class that holds all the cosmetic items and power ups
 * @author Samar
 */
public class Inventory {

    /**
     * list holding all the cosmetic items
     */
    private ArrayList<String> cosmeticItems;

    private HashMap<String, Integer> powerUpItems;

    private String equippedPlane;

    private String equippedBackground;

    /**
     * initializes the inventory class
     */
    public Inventory() {
        this.cosmeticItems = new ArrayList<>();
        this.powerUpItems = new HashMap<>();
    } // end of constructor

    /**
     * adds a cosmetic item to the inventory
     * @param itemID is the id of the cosmetic item
     */
    public void addCosmeticItem(String itemID) {
        if(!cosmeticItems.contains(itemID)) {
            cosmeticItems.add(itemID);
        }
    } // end of addCosmeticItem(String itemID)

    /**
     * checks if the player owns a cosmetic
     * @param itemID is the id of the cosmetic item
     * @return true or false if player owns it
     */
    public boolean hasCosmeticItem(String itemID) {
        return cosmeticItems.contains(itemID);
    } // end of hasCosmeticItem(String itemID)

    /**
     * adds power up item to inventory
     * @param itemID is the id of the item
     */
    public void addPowerUpItem(String itemID) {
        powerUpItems.put(itemID, powerUpItems.getOrDefault(itemID, 0) + 1);
    } // end of addPowerUpItem(String itemID)

    /**
     * gets the count of an item
     * @param itemID is the id of that item
     * @return number of this item the user holds
     */
    public int getPowerUps(String itemID) {
        return powerUpItems.getOrDefault(itemID, 0);
    } // end of getPowerUps(String itemID)

    /**
     * uses the power up
     * @param itemID is the id of the power up
     * @return true or false if power up was used
     */
    public boolean usePowerUp(String itemID) {
        int count = powerUpItems.getOrDefault(itemID, 0);
        if (count <= 0) return false;
        if (count == 1) {
            powerUpItems.remove(itemID);
        } else {
            powerUpItems.put(itemID, count - 1);
        }
        return true;
    } // end of usePowerUp(String itemID)


    public void setEquippedPlane(String itemID) {
        this.equippedPlane = itemID;
    }

    public String getEquippedPlane() {
        return equippedPlane;
    }

    public void setEquippedBackground(String backgroundID) {
        this.equippedBackground = backgroundID;
    }

    public String getEquippedBackground() {
        return equippedBackground;
    }

    /**
     * gets the owned cosmetics
     * @return cosmetic list
     */
    public ArrayList<String> getOwnedCosmetics() {
        return cosmeticItems;
    } // end of getOwnedCosmetics()

    /**
     * gets the owned powerups
     * @return powerup map
     */
    public HashMap<String, Integer> getOwnedPowerUps() {
        return powerUpItems;
    } // end of getOwnedPowerUps()
} // end of class
