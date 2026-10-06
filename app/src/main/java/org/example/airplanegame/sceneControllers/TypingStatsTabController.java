package org.example.airplanegame.sceneControllers;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import org.example.airplanegame.player.UserProfile;
import org.example.airplanegame.playerControllers.UserManager;

/**
 * Controls how to display all typing stats to screen
 * and what happens in the typing stats settings tab
 * FXML file: typingStatsTab.fxml used scenebuilder
 *
 * @author Mark Ziza
 */
public class TypingStatsTabController {

    //all labels for typing stats and dropdown to choose username
    @FXML private ChoiceBox<String> usernameChoiceBox;

    @FXML private Label avgWpmLabel;
    @FXML private Label peakWpmLabel;
    @FXML private Label accuracyLabel;
    @FXML private Label errorCountLabel;
    @FXML private Label totalTimePlayedLabel;
    @FXML private Label highScoreLabel;
    @FXML private Label levelProgressionLabel;
    @FXML private Label wordsTypedLabel;

    //user data
    private final UserManager userManager = new UserManager();

    /**
     * initializes the typing stats page, sets up the username choice box dropdown
     * Artificial Intelligence Tool: chatGPT; Execution: Got help on how to
     * initialize a choice box with values
     */
    @FXML
    public void initialize() {
        usernameChoiceBox.getItems().addAll(userManager.getAllUsernames());

        usernameChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                loadStats(newValue);
            }
        });

        //display first item if choice box empty
        if (!usernameChoiceBox.getItems().isEmpty()) {
            usernameChoiceBox.setValue(usernameChoiceBox.getItems().get(0));
            loadStats(usernameChoiceBox.getValue());
        }
    }

    /**
     * loads in to all labels the users typing stats depending on which one
     * is chosen from choice box
     *
     * @param username A player's username
     */
    private void loadStats(String username) {
        //get username
        UserProfile profile = userManager.getUserProfile(username);
        if (profile == null) return;

        //set the stats text
        avgWpmLabel.setText(String.valueOf(Math.round(profile.getAvgWpm() * 10.0) / 10.0));
        peakWpmLabel.setText(String.valueOf(Math.round(profile.getPeakWpm() * 10.0) / 10.0));
        accuracyLabel.setText(String.valueOf(Math.round(profile.getAccuracy() * 10.0) / 10.0) + "%");
        errorCountLabel.setText(String.valueOf(profile.getErrorCount()));
        totalTimePlayedLabel.setText(String.valueOf(Math.round(profile.getTotalTimePlayed() * 10.0) / 10.0));
        highScoreLabel.setText(String.valueOf(profile.getHighScore()));
        levelProgressionLabel.setText(String.valueOf(profile.getUnlockedLevel()));
        wordsTypedLabel.setText(String.valueOf(profile.getWordsTyped()));
    }
}//end of class
