package org.example.airplanegame.sceneControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import org.example.airplanegame.Main;
import org.example.airplanegame.player.UserProfile;
import org.example.airplanegame.playerControllers.UserManager;

/**
 * Controls all buttons that lets the user choose what level they want to play
 * FXML file: levelSelectScene.fxml used scenebuilder
 *
 * @Author Mark Ziza
 */
public class LevelSelectController {

    //buttons and text to say that the level is locked

    @FXML
    private Button level1Button;

    @FXML
    private Button level2Button;

    @FXML
    private Button level3Button;

    @FXML
    private Button level4Button;

    @FXML
    private Button level5Button;

    @FXML
    private Text level2LockText;

    @FXML
    private Text level3LockText;

    @FXML
    private Text level4LockText;

    @FXML
    private Text level5LockText;

    private Main application;
    private UserManager userManager = new UserManager();

    /**
     * Sets connection to main application and unlocks levels depending which one the user is on
     *
     * @param mainApp the main application storing all data
     */
    public void setMainApp(Main mainApp) {
        this.application = mainApp;

        //getting users unlocked level data
        UserProfile profile = userManager.getUserProfile(application.getCurrentUser());
        int unlockedLevel = 1;

        if (profile != null) {
            unlockedLevel = profile.getUnlockedLevel();
        }

        //unlocking levels based on how many levels completed
        level1Button.setDisable(false);


        level2Button.setDisable(unlockedLevel < 2);
        level2LockText.setVisible(unlockedLevel < 2);


        level3Button.setDisable(unlockedLevel < 3);
        level3LockText.setVisible(unlockedLevel < 3);


        level4Button.setDisable(unlockedLevel < 4);
        level4LockText.setVisible(unlockedLevel < 4);


        level5Button.setDisable(unlockedLevel < 5);
        level5LockText.setVisible(unlockedLevel < 5);
    }

    /**
     * Back button to game mode select screen
     *
     * @param event event of button press
     */
    public void backButtonLevelSelect(ActionEvent event) {
        application.selectGameModeScene();
    }

    /**
     * Button that starts level 1
     *
     * @param event event of button press
     */
    public void level1Button(ActionEvent event) {
        application.levelScene(1);
    }

    /**
     * Button that starts level 2
     *
     * @param event event of button press
     */
    public void level2Button(ActionEvent event) {
        application.levelScene(2);
    }

    /**
     * Button that starts level 3
     *
     * @param event event of button press
     */
    public void level3Button(ActionEvent event) {
        application.levelScene(3);
    }

    /**
     * Button that starts level 4
     *
     * @param event event of button press
     */
    public void level4Button(ActionEvent event) {
        application.levelScene(4);
    }

    /**
     * Button that starts level 5
     *
     * @param event event of button press
     */
    public void level5Button(ActionEvent event) {
        application.levelScene(5);
    }
}//end of class
