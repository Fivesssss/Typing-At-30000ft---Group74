package org.example.airplanegame.shops;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import org.example.airplanegame.Main;
import org.example.airplanegame.player.UserProfile;
import org.example.airplanegame.playerControllers.UserManager;

import java.util.ArrayList;

/**
 * Controls all buttons and processing of the power up shop screen
 * @author Samar
 */
public class PowerUpShopController {
    private Main application;
    private UserProfile currentUser;
    private UserManager userManager;
    private Shop shop;

    @FXML
    Label shieldCostLabel;
    @FXML
    Label moneyCostLabel;
    @FXML
    Label slowCostLabel;
    @FXML
    Label fuelCostLabel;
    @FXML
    Label shieldPowerAmount;
    @FXML
    Label moneyPowerAmount;
    @FXML
    Label slowPowerAmount;
    @FXML
    Label fuelPowerAmount;
    @FXML
    private Label moneyLabel;//shows how much money user has

    public void setMainApp(Main mainApp) {
        this.application = mainApp;
        this.userManager = new UserManager();
        this.currentUser = userManager.getUserProfile(mainApp.getCurrentUser());

        ArrayList<Item> shopItems = new ArrayList<>();
        shopItems.add(new Item("shield", 25, "POWERUP"));// shield power up
        shopItems.add(new Item("slowmo", 50, "POWERUP"));// slow motion power up
        shopItems.add(new Item("fuel_up", 30, "POWERUP"));//fuel up power up
        shopItems.add(new Item("double_money", 120, "POWERUP"));//double money power up

        shieldPowerAmount.setText(String.valueOf(currentUser.getInventory().getPowerUps("shield")));
        slowPowerAmount.setText(String.valueOf(currentUser.getInventory().getPowerUps("slowmo")));
        fuelPowerAmount.setText(String.valueOf(currentUser.getInventory().getPowerUps("fuel_up")));
        moneyPowerAmount.setText(String.valueOf(currentUser.getInventory().getPowerUps("double_money")));

        //if they have enough money the cost label is green and red if they dont have enough
        if(currentUser.getMoney() >= 25)shieldCostLabel.setTextFill(Color.GREEN);
        if(currentUser.getMoney() >= 50)slowCostLabel.setTextFill(Color.GREEN);
        if(currentUser.getMoney() >= 30)fuelCostLabel.setTextFill(Color.GREEN);
        if(currentUser.getMoney() >= 120)moneyCostLabel.setTextFill(Color.GREEN);

        moneyLabel.setText("MONEY: " + currentUser.getMoney());//shows how much money they have

        this.shop = new Shop(shopItems);
    }//end of setMain app method

    /**
     *purchase a shield power up if they press the button and have enough money
     */
    public void shieldButtonController(ActionEvent e){
        if(shop.purchaseItem("shield", currentUser)) {
            userManager.saveUserProfile(currentUser);
        }//end of if statement

        //if they don't have enough money the label becomes red
        if(currentUser.getMoney() < 25)shieldCostLabel.setTextFill(Color.RED);
        if(currentUser.getMoney() < 50)slowCostLabel.setTextFill(Color.RED);
        if(currentUser.getMoney() < 30)fuelCostLabel.setTextFill(Color.RED);
        if(currentUser.getMoney() < 120)moneyCostLabel.setTextFill(Color.RED);

        shieldPowerAmount.setText(String.valueOf(currentUser.getInventory().getPowerUps("shield")));//updates the inventory label
        moneyLabel.setText("MONEY: " + currentUser.getMoney());//updates the users money shown on the screen
    }//end of shieldButtonController

    /**
     *purchase a double money power up if they press the button and have enough money
     */
    public void moneyButtonController(ActionEvent e){
        if(shop.purchaseItem("double_money", currentUser)) {
            userManager.saveUserProfile(currentUser);
        }//end of if statement

        //if they don't have enough money the label becomes red
        if(currentUser.getMoney() < 25)shieldCostLabel.setTextFill(Color.RED);
        if(currentUser.getMoney() < 50)slowCostLabel.setTextFill(Color.RED);
        if(currentUser.getMoney() < 30)fuelCostLabel.setTextFill(Color.RED);
        if(currentUser.getMoney() < 120)moneyCostLabel.setTextFill(Color.RED);

        moneyPowerAmount.setText(String.valueOf(currentUser.getInventory().getPowerUps("double_money")));//updates the inventory label
        moneyLabel.setText("MONEY: " + currentUser.getMoney());//updates the users money shown on the screen
    }//end of moneyButtonController method

    /**
     *purchase a fuel up power up if they press the button and have enough money
     */
    public void fuelButtonController(ActionEvent e){
        if(shop.purchaseItem("fuel_up", currentUser)) {
            userManager.saveUserProfile(currentUser);
        }//end of if statement

        //if they don't have enough money the label becomes red
        if(currentUser.getMoney() < 25)shieldCostLabel.setTextFill(Color.RED);
        if(currentUser.getMoney() < 50)slowCostLabel.setTextFill(Color.RED);
        if(currentUser.getMoney() < 30)fuelCostLabel.setTextFill(Color.RED);
        if(currentUser.getMoney() < 120)moneyCostLabel.setTextFill(Color.RED);

        fuelPowerAmount.setText(String.valueOf(currentUser.getInventory().getPowerUps("fuel_up")));//updates the inventory label
        moneyLabel.setText("MONEY: " + currentUser.getMoney());//updates the users money shown on the screen
    }//end of fuelButtonController method

    /**
     *purchase a slow motion power up if they press the button and have enough money
     */
    public void slowButtonController(ActionEvent e){
        if(shop.purchaseItem("slowmo", currentUser)) {
            userManager.saveUserProfile(currentUser);
        }//end of if statement

        //if they don't have enough money the label becomes red
        if(currentUser.getMoney() < 25)shieldCostLabel.setTextFill(Color.RED);
        if(currentUser.getMoney() < 50)slowCostLabel.setTextFill(Color.RED);
        if(currentUser.getMoney() < 30)fuelCostLabel.setTextFill(Color.RED);
        if(currentUser.getMoney() < 120)moneyCostLabel.setTextFill(Color.RED);

        slowPowerAmount.setText(String.valueOf(currentUser.getInventory().getPowerUps("slowmo")));//updates the inventory label
        moneyLabel.setText("MONEY: " + currentUser.getMoney());//updates the users money shown on the screen
    }//end of slowButtonController method

    /**
     * Back button that goes back to main menu
     * @param e represents back button press
     */
    public void backButtonHandler(ActionEvent e) {
        application.mainMenuScene();
    }//end of backButtonHandler method

    /**
     * switches to the cosmetic shop
     * @param e the event of the user pressing the cosmetic shop button
     */
    public void cosmeticShopButtonHandler(ActionEvent e){
        application.shopScene();
    }//end of cosmeticShopButtonHandler method
}//end of class
