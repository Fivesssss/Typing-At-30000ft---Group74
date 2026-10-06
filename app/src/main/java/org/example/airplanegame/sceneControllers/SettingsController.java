package org.example.airplanegame.sceneControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import org.example.airplanegame.Main;
import org.example.airplanegame.player.UserProfile;
import org.example.airplanegame.playerControllers.UserManager;

import java.io.IOException;

/**
 * Controls all the buttons in the top bar of the settings
 * Controls the swapping between different setting screens
 * FXML file: settingsScene.fxml used scenebuilder
 *
 * @author Mark Ziza
 */
public class SettingsController {

    @FXML
    private AnchorPane contentPane;/**part of settings screen that will change depending on what settings tab is chosen*/

    @FXML
    private ToggleGroup settingsGroup;/**group of toggle buttons that correspond to different setting tabs*/

    @FXML
    private ToggleButton settingsButton;/**control setting button*/

    @FXML
    private ToggleButton teacherControlsButton;/**teacher controls setting button*/

    @FXML
    private ToggleButton typingStatsButton;/**typing statistics setting button*/

    //used to call back main menu
    private Main application;
    private UserManager userManager;
    private UserProfile currentUser;

    /**
     * setting the components to reload the main menu for back button
     *
     * @param mainApp main application to be called back later
     */
    public void setMainApp(Main mainApp, String username) {
        this.application = mainApp;
        this.userManager = new UserManager();
        this.currentUser = userManager.getUserProfile(username);

        // only show teacher controls and stats tabs for teacher accounts
        boolean isTeacher = currentUser != null && currentUser.getIsTeacherAccount();

        teacherControlsButton.setVisible(isTeacher);
        teacherControlsButton.setManaged(isTeacher);

        typingStatsButton.setVisible(isTeacher);
        typingStatsButton.setManaged(isTeacher);
    }

    /**
     * Handles what settings screen to be shown depending on which setting is chosen
     *Artificial Intelligence Tool: chatGPT; Execution: got help from chatGPT to add
     *listener to see which toggled button to switch to
     */
    @FXML
    public void initialize() {
        //Artificial Intelligence Tool: chatGPT; Execution: got help from chatGPT to add
        //listener to see which toggled button to switch to
        settingsGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == null) {
                settingsGroup.selectToggle(oldToggle);
                return;
            }

            //depending on which toggle button in pressed
            if (newToggle == settingsButton) {
                loadFXML("settingsTab.fxml");
            } else if (newToggle == teacherControlsButton) {
                loadFXML("teacherControlsTab.fxml");
            } else if (newToggle == typingStatsButton) {
                loadFXML("typingStatsTab.fxml");
            }
        });
        //default screen when settings is entered
        loadFXML("settingsTab.fxml");
    }


    /**
     * Loads given fxml onto screen depending on which setting button is pushed
     * @param fxmlFile path to fxml file to be loaded onto screen
     */
    private void loadFXML(String fxmlFile) {
        try {
            //setting bounds of the pane
            AnchorPane pane = FXMLLoader.load(getClass().getResource("/" + fxmlFile));
            AnchorPane.setTopAnchor(pane, 0.0);
            AnchorPane.setBottomAnchor(pane, 0.0);
            AnchorPane.setLeftAnchor(pane, 0.0);
            AnchorPane.setRightAnchor(pane, 0.0);
            contentPane.getChildren().setAll(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Back button that goes back to main menu
     *
     * @param e represents back button press
     */
    public void backButtonSettings(ActionEvent e) {
        application.mainMenuScene();
    }

}//end of class
