package org.example.airplanegame.sceneControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import org.example.airplanegame.Main;
import org.example.airplanegame.playerControllers.UserManager;

/**
 * Controls all buttons and processing of the create account screen
 * @author Walid
 */
public class CreateAccountController {
    @FXML
    private TextField usernameInput;//the text box where the user inputs their username
    @FXML private PasswordField passwordInput;//the text box where the user inputs their password
    @FXML private Text errorText;//the error text that pops up
    @FXML private Button teacherButton;//the teacher button
    @FXML private Button studentButton;//the student button

    //the window that pops up when a teacher account is being created
    @FXML private Rectangle teacherCodeWindow;
    @FXML private Button xButton;
    @FXML private Button confirmButton;
    @FXML private Text teacherCodeNeeded;
    @FXML private Text enterTeacherCode;
    @FXML private Text teacherCodeErrorMessage;
    @FXML private TextField teacherCodeInput;

    private boolean isTeacherAccount;//if the user presses the teacher button it is true and if they press the student button it is false
    private boolean accountTypeButtonPressed = false;//if the user presses the teacher or student button this becomes true

    private Main application;
    private UserManager userManager;

    public void setMainApp(Main mainApp) {
        this.application = mainApp;
        this.userManager = new UserManager();
    }//end of setMainApp method

    /**
     * changes to the login scene when the login button is clicked
     * @param event the event of the user clicking on the login button
     */
    public void loginButtonHandler(ActionEvent event){
        application.loginScene();
    }//end of loginButtonHandler method

    /**
     * controls what happens when the create account button is pressed
     * @param event the event of the user clicking on the create button
     */
    public void createAccountButtonHandler(ActionEvent event){
        if(usernameInput.getText().length() < 3){
            passwordInput.clear();
            errorText.setText("Username needs to be at least 3 characters long.");
        }else if(passwordInput.getText().length() < 3) {
            passwordInput.clear();
            errorText.setText("Password needs to be at least 3 characters long.");
        }else if(usernameInput.getText().length() >= 15){
            passwordInput.clear();
            errorText.setText("Username needs to be less than 15 characters long.");
        }else if(!accountTypeButtonPressed) {
            errorText.setText("Select an account type.");
        }else if(!userManager.usernameExists(usernameInput.getText())){
            if(!isTeacherAccount){
                userManager.registerUser(usernameInput.getText(),passwordInput.getText(),isTeacherAccount);
                application.loginScene();
            }else{
                xButton.setVisible(true);
                confirmButton.setVisible(true);
                teacherCodeNeeded.setVisible(true);
                enterTeacherCode.setVisible(true);
                teacherCodeInput.setVisible(true);
                teacherCodeWindow.setVisible(true);
            }//end of if statement
        }else{
            errorText.setText("The username already exists.");
        }//end of if statement
    }//end of createAccountButtonHandler

    /**
     * controls what happens if the teacher button is pressed
     * @param event the event of the user clicking on the teacher button
     */
    public void teacherButtonHandler(ActionEvent event){
        isTeacherAccount = true;
        accountTypeButtonPressed = true;
        teacherButton.setStyle("-fx-background-color: green;");
        studentButton.setStyle("-fx-background-color: #444444;");
    }//end of loginButtonHandler method

    /**
     * controls what happens when the student button is pressed
     * @param event the event of the user clicking on the student button
     */
    public void studentButtonHandler(ActionEvent event){
        isTeacherAccount = false;
        accountTypeButtonPressed = true;
        studentButton.setStyle("-fx-background-color: green;");
        teacherButton.setStyle("-fx-background-color: #444444;");
    }//end of loginButtonHandler method

    /**
     * controls what happens when the x button is pressed
     * @param event the event of the user clicking on the x button
     */
    public void xButtonHandler(ActionEvent event){
        xButton.setVisible(false);
        confirmButton.setVisible(false);
        teacherCodeNeeded.setVisible(false);
        enterTeacherCode.setVisible(false);
        teacherCodeInput.setVisible(false);
        teacherCodeWindow.setVisible(false);
        teacherCodeErrorMessage.setVisible(false);
    }//end of xButtonHandler method

    /**
     * controls what happens when the confirm button is pressed
     * @param event the event of the user clicking on the confirm button
     */
    public void confirmButtonHandler(ActionEvent event){
        if(teacherCodeInput.getText().equalsIgnoreCase("plane123")){
            userManager.registerUser(usernameInput.getText(),passwordInput.getText(),isTeacherAccount);
            application.loginScene();
        }else{
            teacherCodeErrorMessage.setVisible(true);
        }//end of if statement
    }//end of confirmButtonHandler method

}//end of class
