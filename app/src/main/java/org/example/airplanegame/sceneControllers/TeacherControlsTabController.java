package org.example.airplanegame.sceneControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import org.example.airplanegame.playerControllers.UserManager;

import java.util.Optional;

/**
 * controls all elements in the teacher controls tab
 * and all button functions
 * FXML file: teacherControlsTab.fxml used scenebuilder
 *
 * @author Mark Ziza
 */
public class TeacherControlsTabController {

    //username choice and status label
    @FXML private ChoiceBox<String> usernameChoiceBox;
    @FXML private Label statusLabel;

    //password reset elements

    @FXML private Rectangle passwordResetWindow;
    @FXML private Button xButton;
    @FXML private Button confirmButton;
    @FXML private Text passwordNeeded;
    @FXML private Text passwordCode;
    @FXML private Text passwordErrorMessage;
    @FXML private TextField passwordInput;

    //user data
    private UserManager userManager = new UserManager();

    /**
     * initializing the choice box with all users
     */
    @FXML
    public void initialize() {
        //fill choice box with all users
        usernameChoiceBox.getItems().addAll(userManager.getAllUsernames());


        if (!usernameChoiceBox.getItems().isEmpty()) {
            usernameChoiceBox.setValue(usernameChoiceBox.getItems().get(0));
        }

        //hide password reset UI
        passwordResetWindow.setVisible(false);
        xButton.setVisible(false);
        confirmButton.setVisible(false);
        passwordNeeded.setVisible(false);
        passwordCode.setVisible(false);
        passwordInput.setVisible(false);
        passwordErrorMessage.setVisible(false);
    }

    /**
     * confirm window that pops up whenever you choose any reset action
     * Artificial Intelligence Tool: chatGPT; Execution: got help from chatGPT to add
     * the confirm option window and how to set it up
     *
     * @param message message displayed on the pop-up
     * @return true if the confirmation went through and false if not
     */
    private boolean confirmAction(String message) {
        //setting up what pop-up looks like
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Action");
        alert.setHeaderText(null);
        alert.setContentText(message);

        //getting if user confirmed or not
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    /**
     * handles what happens when user clicks the reset typing stats button
     */
    @FXML
    public void resetTypingStatsButtonHandler() {
        //get user to reset
        String username = usernameChoiceBox.getValue();

        if (username == null) {
            statusLabel.setText("Select a username.");
            return;
        }

        //look if user confirmed action
        boolean confirmed = confirmAction("Are you sure you want to reset typing statistics for " + username + "?");
        if (!confirmed) {
            return;
        }

        //reset stats and display confirmation
        userManager.resetTypingStats(username);
        statusLabel.setText("Typing stats reset for " + username);
    }

    /**
     * handles what happens when user clicks the reset password button
     */
    @FXML
    public void resetPasswordButtonHandler() {
        //get user
        String username = usernameChoiceBox.getValue();

        if (username == null) {
            statusLabel.setText("Select a username.");
            return;
        }

        //make all password reset UI visible and clear their input
        passwordResetWindow.setVisible(true);
        xButton.setVisible(true);
        confirmButton.setVisible(true);
        passwordNeeded.setVisible(true);
        passwordCode.setVisible(true);
        passwordInput.setVisible(true);
        passwordErrorMessage.setVisible(false);

        passwordInput.clear();
    }

    /**
     * handles what happens when user clicks the reset leaderboard button
     */
    @FXML
    public void resetLeaderboardButtonHandler() {
        //check for user confirmation
        boolean confirmed = confirmAction("Are you sure you want to reset the leaderboard?");
        if (!confirmed) {
            return;
        }

        //reset leaderboard and display the success
        userManager.resetLeaderboard();
        statusLabel.setText("Leaderboard reset.");
    }

    /**
     * controls what happens when the x button is pressed in password reset
     * @param event the event of the user clicking on the x button
     */
    public void xButtonHandler(ActionEvent event){
        xButton.setVisible(false);
        confirmButton.setVisible(false);
        passwordNeeded.setVisible(false);
        passwordCode.setVisible(false);
        passwordInput.setVisible(false);
        passwordResetWindow.setVisible(false);
        passwordErrorMessage.setVisible(false);
        passwordInput.clear();
    }

    /**
     * resetting user password when confirm is clicked
     *
     * @param event user clicks confirm on reset password
     */
    @FXML
    public void confirmResetPasswordButtonHandler(ActionEvent event) {
        //getting username and new password
        String username = usernameChoiceBox.getValue();
        String newPassword = passwordInput.getText();

        //error if no user was selected for reset
        if (username == null) {
            statusLabel.setText("Select a username.");
            return;
        }

        //printing errors if passwords are empty or less then 3 chars
        if (newPassword == null || newPassword.isBlank()) {
            passwordErrorMessage.setText("Enter a new password.");
            passwordErrorMessage.setVisible(true);
            return;
        }else if (newPassword.length() < 3) {
            passwordErrorMessage.setText("Password must be at least 3 characters long.");
            passwordErrorMessage.setVisible(true);
            return;
        }

        //checking user confirmation
        boolean confirmed = confirmAction("Are you sure you want to reset the password for " + username + "?");
        if (!confirmed) {
            return;
        }

        //reset password, display confirmation, remove password reset UI
        userManager.resetPassword(username, newPassword);
        statusLabel.setText("Password reset for " + username);

        passwordInput.clear();
        passwordErrorMessage.setVisible(false);

        passwordResetWindow.setVisible(false);
        xButton.setVisible(false);
        confirmButton.setVisible(false);
        passwordNeeded.setVisible(false);
        passwordCode.setVisible(false);
        passwordInput.setVisible(false);
    }
}//end of class
