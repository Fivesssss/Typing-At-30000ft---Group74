package org.example.airplanegame.sceneControllers;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;
import org.example.airplanegame.Main;
import org.example.airplanegame.playerControllers.UserManager;

/**
 * Controls all buttons and processing of the login screen
 * @author Walid
 */
public class LoginController {
    @FXML private TextField usernameInput;//the text box where the user inputs their username
    @FXML private PasswordField passwordInput;//the text box where the user inputs their password
    @FXML private Text errorText;//the error text that pops up when the login is incorrect
    private Main application;
    private UserManager userManager;

    public void setMainApp(Main mainApp) {
        this.application = mainApp;
        this.userManager = new UserManager();
    }//end of setMain app method

    /**
     * controls what happens when the login button is pressed
     * @param event The event of the user clicking the login button
     */
    @FXML
    public void loginButtonHandler(ActionEvent event) {
        String username = usernameInput.getText();//the username entered
        String password = passwordInput.getText();//the password entered

        //checks if the login entered is valid
        if (userManager.authenticateUser(username, password)) {
            application.setCurrentUser(username);
            application.mainMenuScene();
        } else {
            passwordInput.clear();
            errorText.setVisible(true);
        }//end of if statement
    }//end of loginButtonHandler method

    /**
     * controls what happens when the create account button is pressed
     * @param event the event of the user clicking the create account button
     */
    public void createAccountButtonHandler(ActionEvent event){
        application.createAccountScene();
    }//end of createAccountButtonHandler

}//end of class
