package org.example.airplanegame.sceneControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.example.airplanegame.Main;
import org.example.airplanegame.playerControllers.UserManager;
import org.example.airplanegame.player.UserProfile;

/**
 * controls all the buttons and processing of the cosmetic shop
 * FXML file: shopScene.fxml used scenebuilder
 *
 * @author Mark Ziza
 */
public class ShopController {

    @FXML
    private Label equippedJetLabel;//label that shows if jet is equipped

    @FXML
    private Label equippedPropellerLabel;//label that shows if propeller plane is equipped

    @FXML
    private Label equippedHelicopterLabel;//label that shows if helicopter is equipped

    @FXML
    private Label equippedUfoLabel;//label that shows if ufo is equipped

    @FXML
    private Button jetButton;//button used to equip jet

    @FXML
    private Button propellerButton;//button used to equip/buy propeller plane

    @FXML
    private Button helicopterButton;//button used to equip/buy helicopter

    @FXML
    private Button ufoButton;//button used to equip/buy ufo

    @FXML
    private Label propellerCostLabel;//shows cost of propeller plane

    @FXML
    private Label helicopterCostLabel;//shows cost of helicopter

    @FXML
    private Label ufoCostLabel;//shows cost of ufo

    @FXML
    private Label equippedSkyLabel;//label that shows if sky background is equipped

    @FXML
    private Label equippedNightLabel;//label that shows if night background is equipped

    @FXML
    private Label equippedCityLabel;//label that shows if city background is equipped

    @FXML
    private Label equippedMountainLabel;//label that shows if mountain background is equipped

    @FXML
    private Label equippedSpaceLabel;//label that shows if space background is equipped

    @FXML
    private Label nightCostLabel;//shows cost of night

    @FXML
    private Label cityCostLabel;//shows cost of city

    @FXML
    private Label mountainCostLabel;//shows cost of mountain

    @FXML
    private Label spaceCostLabel;//shows cost of space

    @FXML
    private Button skyButton;//button used to equip sky background

    @FXML
    private Button nightButton;//button used to equip/buy night backgroun

    @FXML
    private Button cityButton;//button used to equip/buy city backgroun

    @FXML
    private Button mountainButton;//button used to equip/buy mountain backgroun

    @FXML
    private Button spaceButton;//button used to equip/buy space backgroun

    @FXML
    private Label moneyLabel;//shows how much money user has

    //prices of all cosmetics
    private static final int PROPELLER_PRICE = 200;
    private static final int HELICOPTER_PRICE = 300;
    private static final int UFO_PRICE = 500;
    private static final int NIGHT_PRICE = 200;
    private static final int CITY_PRICE = 300;
    private static final int MOUNTAIN_PRICE = 400;
    private static final int SPACE_PRICE = 600;
    //used to load main application again
    private Main application;
    private UserManager userManager;

    /**
     * initializing all data corresponding to shop buttons
     */
    @FXML
    public void initialize() {
        jetButton.setUserData("JET");
        propellerButton.setUserData("PROPELLER");
        helicopterButton.setUserData("HELICOPTER");
        ufoButton.setUserData("UFO");
        nightButton.setUserData("NIGHT");
        cityButton.setUserData("CITY");
        mountainButton.setUserData("MOUNTAIN");
        spaceButton.setUserData("SPACE");
        skyButton.setUserData("SKY");
    }

    /**
     * setting the components to reload the main menu for back button
     * @param mainApp main application to be called back later
     */
    public void setMainApp(Main mainApp) {
        this.application = mainApp;
        this.userManager = new UserManager();
    }

    /**
     * loads how much money user has
     */
    public void loadMoney() {
        UserProfile profile = userManager.getUserProfile(application.getCurrentUser());

        if (profile == null) return;

        moneyLabel.setText("MONEY: " + profile.getMoney());
    }

    /**
     * Helper method that clears all equipped labels for planes
     */
    private void clearEquippedLabels() {
        equippedJetLabel.setText("");
        equippedPropellerLabel.setText("");
        equippedHelicopterLabel.setText("");
        equippedUfoLabel.setText("");
    }

    /**
     * loads the current equipped plane by the user
     */
    public void loadEquippedState() {
        //getting user data and clearing all plane labels
        UserProfile profile = userManager.getUserProfile(application.getCurrentUser());
        clearEquippedLabels();

        if (profile == null || profile.getInventory() == null) {
            return;
        }

        //getting equipped plane
        String equipped = profile.getInventory().getEquippedPlane();

        //setting equipped to the plane the user has equipped
        if ("JET".equals(equipped)) {
            equippedJetLabel.setText("Equipped");
        }else if ("PROPELLER".equals(equipped)) {
            equippedPropellerLabel.setText("Equipped");
        }else if ("HELICOPTER".equals(equipped)) {
            equippedHelicopterLabel.setText("Equipped");
        }else if ("UFO".equals(equipped)) {
            equippedUfoLabel.setText("Equipped");
        }
    }

    /**
     * equipping/buying a plane when user clicks any plane button
     * @param e user clicking any plane cosmetic button
     */
    public void equipPlane(ActionEvent e) {
        //getting ID of plane button pressed
        Button clickedButton = (Button) e.getSource();
        String planeID = (String) clickedButton.getUserData();

        UserProfile profile = userManager.getUserProfile(application.getCurrentUser());

        if (profile == null || profile.getInventory() == null) {
            return;
        }

        // If plane already owned just equip it
        if (profile.getInventory().hasCosmeticItem(planeID)) {
            profile.getInventory().setEquippedPlane(planeID);
            userManager.saveUserProfile(profile);
            showEquippedMessage(planeID);
            loadCostState();
            return;
        }

        // if plane not owned try to buy and equip it
        int price = getPlanePrice(planeID);

        if (profile.getMoney() >= price) {
            profile.setMoney(profile.getMoney() - price);
            profile.getInventory().addCosmeticItem(planeID);
            profile.getInventory().setEquippedPlane(planeID);
            userManager.saveUserProfile(profile);

            //load all costs,equipped message, and user money after they buy/equip something
            showEquippedMessage(planeID);
            loadCostState();
            loadMoney();
        }
    }

    /**
     * helper method that shows equipped depending on what plane is chosen
     * @param planeID ID of cosmetic plane button clicked
     */
    private void showEquippedMessage(String planeID) {
        clearEquippedLabels();

        if (planeID.equals("JET")){
            equippedJetLabel.setText("Equipped");
        }else if(planeID.equals("PROPELLER")){
            equippedPropellerLabel.setText("Equipped");
        }else if(planeID.equals("HELICOPTER")){
            equippedHelicopterLabel.setText("Equipped");
        }else if(planeID.equals("UFO")){
            equippedUfoLabel.setText("Equipped");
        }
    }

    /**
     * helper method that gets price of specific plane
     * @param planeID ID of plane we want price of
     * @return price of plane
     */
    private int getPlanePrice(String planeID) {
        if ("PROPELLER".equals(planeID)) {
            return PROPELLER_PRICE;
        } else if ("HELICOPTER".equals(planeID)) {
            return HELICOPTER_PRICE;
        } else if ("UFO".equals(planeID)) {
            return UFO_PRICE;
        }
        return 0;
    }

    /**
     * puts cost of planes on screen that are not bought
     */
    public void loadCostState() {
        UserProfile profile = userManager.getUserProfile(application.getCurrentUser());

        if (profile == null || profile.getInventory() == null) {
            return;
        }

        if (profile.getInventory().hasCosmeticItem("PROPELLER")) {
            propellerCostLabel.setText("");
        } else {
            propellerCostLabel.setText("COST: " + PROPELLER_PRICE);
        }

        if (profile.getInventory().hasCosmeticItem("HELICOPTER")) {
            helicopterCostLabel.setText("");
        } else {
            helicopterCostLabel.setText("COST: " + HELICOPTER_PRICE);
        }

        if (profile.getInventory().hasCosmeticItem("UFO")) {
            ufoCostLabel.setText("");
        } else {
            ufoCostLabel.setText("COST: " + UFO_PRICE);
        }
    }

    /**
     * loads the current equipped background by the user
     */
    public void loadEquippedBackgroundState() {
        //getting user data and clearing all background labels
        UserProfile profile = userManager.getUserProfile(application.getCurrentUser());
        clearBackgroundLabels();

        if (profile == null || profile.getInventory() == null) {
            return;
        }

        //getting equipped background
        String equippedBackground = profile.getInventory().getEquippedBackground();

        //setting equipped to the background the user has equipped
        if ("SKY".equals(equippedBackground)) {
            equippedSkyLabel.setText("Equipped");
        } else if ("NIGHT".equals(equippedBackground)) {
            equippedNightLabel.setText("Equipped");
        } else if ("CITY".equals(equippedBackground)) {
            equippedCityLabel.setText("Equipped");
        } else if ("MOUNTAIN".equals(equippedBackground)) {
            equippedMountainLabel.setText("Equipped");
        } else if ("SPACE".equals(equippedBackground)) {
            equippedSpaceLabel.setText("Equipped");
        }
    }

    /**
     * Helper method that clears all equipped labels for backgrounds
     */
    private void clearBackgroundLabels() {
        equippedSkyLabel.setText("");
        equippedNightLabel.setText("");
        equippedCityLabel.setText("");
        equippedMountainLabel.setText("");
        equippedSpaceLabel.setText("");
    }

    /**
     * helper method that gets price of specific background
     * @param backgroundID ID of background we want price of
     * @return price of background
     */
    private int getBackgroundPrice(String backgroundID) {
        if ("NIGHT".equals(backgroundID)) return NIGHT_PRICE;
        if ("CITY".equals(backgroundID)) return CITY_PRICE;
        if ("MOUNTAIN".equals(backgroundID)) return MOUNTAIN_PRICE;
        if ("SPACE".equals(backgroundID)) return SPACE_PRICE;
        return 0;
    }

    /**
     * equipping/buying a background when user clicks any background button
     * @param e user clicking any background cosmetic button
     */
    public void equipBackground(ActionEvent e) {
        //getting ID of background button pressed
        Button clickedButton = (Button) e.getSource();
        String backgroundID = (String) clickedButton.getUserData();

        UserProfile profile = userManager.getUserProfile(application.getCurrentUser());

        if (profile == null) return;

        // If background already owned just equip it
        if (profile.getInventory().hasCosmeticItem(backgroundID)) {
            profile.getInventory().setEquippedBackground(backgroundID);
            userManager.saveUserProfile(profile);
            showEquippedBackground(backgroundID);
            loadBackgroundCostState();
            return;
        }

        // if background not owned try to buy and equip it
        int price = getBackgroundPrice(backgroundID);

        if (profile.getMoney() >= price) {
            profile.setMoney(profile.getMoney() - price);
            profile.getInventory().addCosmeticItem(backgroundID);
            profile.getInventory().setEquippedBackground(backgroundID);
            userManager.saveUserProfile(profile);

            //load all costs,equipped message, and user money after they buy/equip something
            showEquippedBackground(backgroundID);
            loadBackgroundCostState();
            loadMoney();
        }
    }

    /**
     * puts cost of backgrounds on screen that are not bought
     */
    public void loadBackgroundCostState() {
        UserProfile profile = userManager.getUserProfile(application.getCurrentUser());

        if (profile == null) return;

        if (profile.getInventory().hasCosmeticItem("NIGHT"))
            nightCostLabel.setText("");
        else
            nightCostLabel.setText("COST: " + NIGHT_PRICE);

        if (profile.getInventory().hasCosmeticItem("CITY"))
            cityCostLabel.setText("");
        else
            cityCostLabel.setText("COST: " + CITY_PRICE);

        if (profile.getInventory().hasCosmeticItem("MOUNTAIN"))
            mountainCostLabel.setText("");
        else
            mountainCostLabel.setText("COST: " + MOUNTAIN_PRICE);

        if (profile.getInventory().hasCosmeticItem("SPACE"))
            spaceCostLabel.setText("");
        else
            spaceCostLabel.setText("COST: " + SPACE_PRICE);
    }

    /**
     * helper method that shows equipped depending on what background is chosen
     * @param backgroundID ID of cosmetic background button clicked
     */
    private void showEquippedBackground(String backgroundID) {
        clearBackgroundLabels();

        if ("SKY".equals(backgroundID)) equippedSkyLabel.setText("Equipped");
        else if ("NIGHT".equals(backgroundID)) equippedNightLabel.setText("Equipped");
        else if ("CITY".equals(backgroundID)) equippedCityLabel.setText("Equipped");
        else if ("MOUNTAIN".equals(backgroundID)) equippedMountainLabel.setText("Equipped");
        else if ("SPACE".equals(backgroundID)) equippedSpaceLabel.setText("Equipped");
    }

    /**
     * Back button that goes back to main menu
     * @param e represents back button press
     */
    public void backButtonShop(ActionEvent e) {
        application.mainMenuScene();
    }

    /**
     * changes to the power up shop
     * @param e event of user pressing the power up shop button
     */
    public void powerUpShopButtonHandler(ActionEvent e){
        application.powerUpShopScene();
    }//end of powerUpShopButtonHandler method
}//end of class
