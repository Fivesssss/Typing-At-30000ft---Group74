package org.example.airplanegame.sceneControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import org.example.airplanegame.Main;

/**
 * Controls all the buttons and processing of the tutorial screen
 * @author Walid
 */
public class TutorialController {
    private Main application;
    private int tutorialNumber;//the tutorial number that the user is on

    @FXML
    Text title;
    @FXML
    Text tutorialText;
    @FXML
    Button leftButton;
    @FXML
    Button rightButton;

    /**
     *gives this controller access to the Main application class so it can switch scenes.
     */
    public void setMainApp(Main mainApp) {
        this.application = mainApp;
        this.tutorialNumber = 1;

        tutorialOne();
    }//end of setMainApp method

    /**
     * goes back to the main menu when the back button is pressed
     * @param e the event of the user clicking on the back button
     */
    public void backButtonHandler(ActionEvent e) {
        application.mainMenuScene();
    }//end of backButtonHandler

    /**
     * goes to the previous tutorial
     * @param e the event of the user clicking on the left button
     */
    public void leftButtonHandler(ActionEvent e) {
        tutorialNumber--;
        if(tutorialNumber==1)tutorialOne();
        if(tutorialNumber==2)tutorialTwo();
        if(tutorialNumber==3)tutorialThree();
        if(tutorialNumber==4)tutorialFour();
        if(tutorialNumber==5)tutorialFive();
        if(tutorialNumber==6)tutorialSix();
        if(tutorialNumber==7)tutorialSeven();
        if(tutorialNumber==8)tutorialEight();
    }//end of backButtonHandler

    /**
     * goes to the next tutorial
     * @param e the event of the user clicking on the right button
     */
    public void rightButtonHandler(ActionEvent e) {
        tutorialNumber++;
        if(tutorialNumber==1)tutorialOne();
        if(tutorialNumber==2)tutorialTwo();
        if(tutorialNumber==3)tutorialThree();
        if(tutorialNumber==4)tutorialFour();
        if(tutorialNumber==5)tutorialFive();
        if(tutorialNumber==6)tutorialSix();
        if(tutorialNumber==7)tutorialSeven();
        if(tutorialNumber==8)tutorialEight();
    }//end of backButtonHandler

    /**
     * changes the title and text to the tutorial 1 title and text
     */
    private void tutorialOne(){
        title.setText("OBJECTIVE");
        tutorialText.setText("When you load into a level or the endless mode, you’ll see the plane hovering, you can move the plane up and down using the arrow keys.\n" +
                "You want to move your plane to hit the clouds and type the word in that cloud in the time limit that appears at the top in red.");
        leftButton.setVisible(false);
    }//end of tutorialOne method

    /**
     * changes the title and text to the tutorial 2 title and text
     */
    private void tutorialTwo(){
        title.setText("DAMAGE");
        tutorialText.setText("Your fuel bar goes down gradually. It will also go down by 10 percentage points if a cloud goes off the screen and will go down by 20 percentage points if you don’t type a word in before the time runs out. Your fuel bar will go up by 20 percentage points if you type a word correctly before the time runs out.");
        leftButton.setVisible(true);
    }//end of tutorialTwo method

    /**
     * changes the title and text to the tutorial 3 title and text
     */
    private void tutorialThree(){
        title.setText("GAME MODES");
        tutorialText.setText("There are 5 levels in level mode, which get increasingly more difficult as you progress, and each level takes 1 minute to complete. In endless mode you go on forever, and gain score as you type a word correctly, and as your score goes up the harder the endless mode becomes.");
    }//end of tutorialThree method

    /**
     * changes the title and text to the tutorial 4 title and text
     */
    private void tutorialFour(){
        title.setText("ACCOUNTS");
        tutorialText.setText("You can make two different types of accounts, a student account and a teacher account. A student account can play the game, see their stats, buy things from shop, and change settings. A teacher account and do all what a student account can but you are able to reset leaderboard, reset an accounts stat and password, and can see all students stats. To make a teacher account you need a special code.");
    }//end of tutorialFour method

    /**
     * changes the title and text to the tutorial 5 title and text
     */
    private void tutorialFive(){
        title.setText("SHOP");
        tutorialText.setText("You can earn coins when you get words correct. Each correct word rewards the user with 10 coins. You can use these coins in the shop. There is a shop for cosmetics and powerups. You can use your hard-earned money on cosmetics which once bought stay forever or powerups you can buy over and over and once used they are gone.");
    }//end of tutorialFive method

    /**
     * changes the title and text to the tutorial 6 title and text
     */
    private void tutorialSix(){
        title.setText("SETTINGS");
        tutorialText.setText("In settings you can change volume and bind buttons for shortcuts. You can see your typing stats in that tab and both teacher and student accounts can see that. A teacher account however can see a tab which is exclusive to them, and it lets them reset stats and passwords and the leaderboard.");
    }//end of tutorialSix method

    /**
     * changes the title and text to the tutorial 7 title and text
     */
    private void tutorialSeven(){
        title.setText("DISASTERS");
        tutorialText.setText("During the game there’s a chance for a natural disaster to occur and their probabilities become higher as the levels and scores go up in endless mode. There are 4 disasters, first one is fog. The fog essentially adds a transparent tint on the screen making it difficult to see the words. The second one is snow and when that happens your plane freezes, and you have to spam click the screen to break out of it. The third one is wind, and it makes the clouds harder to collide with so you could take damage. The last one is rain, and it adds rain effects on the screen which covers the whole screen.");
        rightButton.setVisible(true);
    }//end of tutorialSeven method

    /**
     * changes the title and text to the tutorial 8 title and text
     */
    private void tutorialEight(){
        title.setText("POWER UPS");
        tutorialText.setText("There are 4 power ups in the game and each do unique things and are shown when playing in the bottom right of the screen and timed power ups will show a timer when active. The first power up is a shield. When you activate the shield it blocks one hit either from a cloud leaving the screen or miss spelling the word. The second power up is slowmo. If you’re ever feeling over whelmed by the speed of the clouds you can just use this and slow them to half their speed for 10 seconds. The third one is fuel up. This basically gives you hp/fuel so you can live longer. And lastly there is double money and for 20 seconds any word typed will award the user double coins.");
        rightButton.setVisible(false);
    }//end of tutorialEight method
}//end of class
