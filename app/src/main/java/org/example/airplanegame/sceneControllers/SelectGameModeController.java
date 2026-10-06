package org.example.airplanegame.sceneControllers;

import javafx.event.ActionEvent;
import org.example.airplanegame.Main;

/**
 * controls game mode selections buttons
 * FXML file: modeSelectScene.fxml used scenebuilder
 *
 * @author Mark Ziza
 */
public class SelectGameModeController {
    private Main application;

    public void setMainApp(Main mainApp) {
        this.application = mainApp;
    }//end of setMainApp method

    /**
     * returns to main menu when the back button is clicked
     * @param event the event of the user clicking the back button
     */
    public void backButtonHandler(ActionEvent event) {
        application.mainMenuScene();
    }//end of backButtonHandler method

    /**
     * goes to select level scene when level button is clicked
     * @param event the event of the user clicking the level button
     */
    public void levelsButtonHandler(ActionEvent event){
        application.selectLevelScene();
    }//end of levelButtonHandler method

    /**
     * goes to endless mode when the endless button is clicked
     * @param event the event of the user clicking the endless button
     */
    public void endlessButtonHandler(ActionEvent event){
        application.endlessScene();
    }//end of levelButtonHandler method
}//end of class

