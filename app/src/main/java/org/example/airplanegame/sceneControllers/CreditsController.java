package org.example.airplanegame.sceneControllers;

import javafx.event.ActionEvent;
import org.example.airplanegame.Main;
import org.example.airplanegame.playerControllers.UserManager;

/**
 * Class controls all buttons on the credits screen
 * FXML file: creditsScene.fxml used scenebuilder
 *
 * @author Mark Ziza
 */
public class CreditsController {

    //getting the components to reload the main menu for back button
    private Main application;
    private UserManager userManager;

    /**
     * setting the components to reload the main menu for back button
     *
     * @param mainApp main application to be called back later
     */
    public void setMainApp(Main mainApp) {
        this.application = mainApp;
        this.userManager = new UserManager();
    }

    /**
     * Back button that goes back to main menu
     *
     * @param e represents back button press
     */
    public void backButtonCredit(ActionEvent e) {
        application.mainMenuScene();
    }

}//end of class
