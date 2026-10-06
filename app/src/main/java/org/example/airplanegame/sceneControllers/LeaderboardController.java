package org.example.airplanegame.sceneControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import org.example.airplanegame.Main;
import org.example.airplanegame.playerControllers.UserManager;
import org.example.airplanegame.player.UserProfile;

/**
 * Controls all the buttons and processing of the leaderboard screen
 * @author Walid
 */
public class LeaderboardController {
    @FXML private Text first;
    @FXML private Text second;
    @FXML private Text third;
    @FXML private Text fourth;
    @FXML private Text fifth;
    @FXML private Text sixth;
    @FXML private Text seventh;
    @FXML private Text eighth;
    @FXML private Text ninth;
    @FXML private Text tenth;
    @FXML private Text firstScore;
    @FXML private Text secondScore;
    @FXML private Text thirdScore;
    @FXML private Text fourthScore;
    @FXML private Text fifthScore;
    @FXML private Text sixthScore;
    @FXML private Text seventhScore;
    @FXML private Text eighthScore;
    @FXML private Text ninthScore;
    @FXML private Text tenthScore;

    private Main application;
    private UserManager userManager;

    public void setMainApp(Main mainApp) {
        this.application = mainApp;
        this.userManager = new UserManager();

        fillLeaderboard();
    }//end of setMainApp method

    private void fillLeaderboard(){
        UserProfile[] topPlayers = userManager.getTopPlayers();

        first.setText("1. " + topPlayers[0].getUsername());
        firstScore.setText(topPlayers[0].getHighScore() + "");

        if(topPlayers.length >= 2) {
            second.setText("2. " + topPlayers[1].getUsername());
            secondScore.setText(topPlayers[1].getHighScore() + "");
        }else {
            second.setText("2. " + "---");
            secondScore.setText("---");
        }//end of if statement

        if(topPlayers.length >= 3) {
            third.setText("3. " + topPlayers[2].getUsername());
            thirdScore.setText(topPlayers[2].getHighScore() + "");
        }else {
            third.setText("3. " + "---");
            thirdScore.setText("---");
        }//end of if statement

        if(topPlayers.length >= 4) {
            fourth.setText("4. " + topPlayers[3].getUsername());
            fourthScore.setText(topPlayers[3].getHighScore() + "");
        }else {
            fourth.setText("4. " + "---");
            fourthScore.setText("---");
        }//end of if statement

        if(topPlayers.length >= 5) {
            fifth.setText("5. " + topPlayers[4].getUsername());
            fifthScore.setText(topPlayers[4].getHighScore() + "");
        }else {
            fifth.setText("5. " + "---");
            fifthScore.setText("---");
        }//end of if statement

        if(topPlayers.length >= 6) {
            sixth.setText("6. " + topPlayers[5].getUsername());
            sixthScore.setText(topPlayers[5].getHighScore() + "");
        } else {
            sixth.setText("6. " + "---");
            sixthScore.setText("---");
        }//end of if statement

        if(topPlayers.length >= 7) {
            seventh.setText("7. " + topPlayers[6].getUsername());
            seventhScore.setText(topPlayers[6].getHighScore() + "");
        }else {
            seventh.setText("7. " + "---");
            seventhScore.setText("---");
        }//end of if statement

        if(topPlayers.length >= 8) {
            eighth.setText("8. " + topPlayers[7].getUsername());
            eighthScore.setText(topPlayers[7].getHighScore() + "");
        }else {
            eighth.setText("8. " + "---");
            eighthScore.setText("---");
        }//end of if statement

        if(topPlayers.length >= 9) {
            ninth.setText("9. " + topPlayers[8].getUsername());
            ninthScore.setText(topPlayers[8].getHighScore() + "");
        }else {
            ninth.setText("9. " + "---");
            ninthScore.setText("---");
        }//end of if statement

        if(topPlayers.length >= 10) {
            tenth.setText("10. " + topPlayers[9].getUsername());
            tenthScore.setText(topPlayers[9].getHighScore() + "");
        }else {
            tenth.setText("10. " + "---");
            tenthScore.setText("---");
        }//end of if statement
    }//end of fillLeaderboard method

    /**
     * returns to main menu when the back button is clicked
     * @param event the event of the user clicking the back button
     */
    public void backButtonHandler(ActionEvent event) {
        application.mainMenuScene();
    }//end of backButtonMode method
}//end of class
