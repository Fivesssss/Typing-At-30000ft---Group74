package org.example.airplanegame.sceneControllers;

import javafx.event.ActionEvent;
import org.example.airplanegame.Main;

/**
 * Controls all buttons on the main menu
 * @author Walid
 */
public class MainMenuController {
    private Main application;

    public void setMainApp(Main mainApp) {
        this.application = mainApp;
    }//end of setMain app method

    /**
     * switches to leaderboard scene when the leaderboard button is pressed
     * @param event The event of the user clicking the leaderboard button
     */
    public void leaderboardButtonHandler(ActionEvent event) {
        application.leaderboardScene();
    }//end of leaderboardButtonHandler method

    /**
     * controls what happens when the start button is pressed
     * @param event the event of the user pressing the start button
     */
    public void startButtonHandler(ActionEvent event) {
        application.selectGameModeScene();
    }//end of startButtonHandler method

    /**
     * controls what happens when the logout button is pressed
     * @param event the event of the user clicking the logout button
     */
    public void logoutButtonHandler(ActionEvent event){
        application.setCurrentUser("");
        application.loginScene();
    }//end of logoutButtonHandler method

    /**
     * controls what happens when the credits button is pressed
     * @param event event of clicking credits button
     */
    public void CreditsButtonHandler(ActionEvent event) {
        application.CreditsScene();
    }

    /**
     * controls what happens when the shop button is pressed
     * @param event event of clicking shop button
     */
    public void ShopButtonHandler(ActionEvent event) {
        application.shopScene();
    }

    /**
     * controls what happens when the settings button is pressed
     * @param event event of clicking settings button
     */
    public void SettingsButtonHandler(ActionEvent event) {
        application.SettingsScene();
    }

    /**
     * goes to the tutorial scene when the tutorial button is pressed
     * @param event the event of the user clicking on the tutorial button
     */
    public void tutorialButtonHandler(ActionEvent event){
        application.tutorialScene();
    }

    /**
     * controls what happens when the exit button is pressed
     * @param event event of clicking exit button
     */
    public void ExitButtonHandler(ActionEvent event){
        application.ExitGame();
    }
}//end of class
