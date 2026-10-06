package org.example.airplanegame.playerControllers;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.example.airplanegame.core.Game;
import javafx.scene.text.TextFlow;
import org.example.airplanegame.core.Level;
import org.example.airplanegame.objects.Cloud;
import org.example.airplanegame.Main;
import org.example.airplanegame.player.PlayerFuel;
import org.example.airplanegame.playerControllers.UserManager;

import javafx.scene.paint.Color;

/**
 * Controls the typing phase of the game, allows the user to type the target word and monitor keyboard inputs.
 * handles logic related to correct word typed, fuel, and score
 * @author Everyone
 */
public class TypingController {
    private String currentTypedWord = "";
    private Main gameApp;
    private TextFlow typedWordUI;
    private Text targetWordUI;
    private Text timerUI;
    private PlayerFuel playerFuel;
    private Level level;
    private Font gameFont;
    private UserManager userManager;
    private Game game;

    /**
     * Creates a typing controller and initializes typing-related UI, game state,
     * and input handling.
     * @param gameApp the main game instance
     * @param scene the scene used to listen for keyboard input
     * @param typedWordUI the UI that displays the player's typed text
     * @param targetWordUI the UI that displays the target word
     * @param timerUI the UI that displays the typing timer
     * @param playerFuel the players fuel
     * @param level the current level
     */
    public TypingController(Main gameApp, Scene scene, TextFlow typedWordUI, Text targetWordUI, Text timerUI, PlayerFuel playerFuel, Level level) {
        this.gameFont = Font.loadFont(
                getClass().getResourceAsStream("/PressStart2P-Regular.ttf"),20);
        this.gameApp = gameApp;
        this.typedWordUI = typedWordUI;
        this.targetWordUI = targetWordUI;
        this.timerUI = timerUI;
        this.playerFuel = playerFuel;
        this.level = level;
        this.userManager = new UserManager();
        setupTypingInput(scene);
    }

    /**
     * Sets the game
     * @param game is being set
     */
    public void setGame(Game game) {
        this.game = game;
    } // end of setGame(Game game)

    /**
     * Sets up keyboard input handling for the typing phase.
     * This listens for the inputed keys, then updates the typed word
     * and checks if a word was correctly typed.
     * @param scene the scene to attach keyboard listeners to
     */
    private void setupTypingInput(Scene scene) {
        scene.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {

                if (gameApp.getCurrentState() != Main.GameState.TYPING || gameApp.getTargetCloud() == null) {
                    return;
                }

                Cloud targetCloud = gameApp.getTargetCloud();
                String targetWord = targetCloud.getTargetWord().toLowerCase();

                //delete last typed letter
                if (event.getCode() == KeyCode.BACK_SPACE) {
                    if (!currentTypedWord.isEmpty()) {
                        currentTypedWord = currentTypedWord.substring(0, currentTypedWord.length() - 1);
                        updateTypedWordUI(targetWord);
                    }
                    return;
                }

                //only add normal letter keys
                String text = event.getText().toLowerCase();
                if (!text.isEmpty() && text.matches("[a-z]")) {
                    currentTypedWord += text;
                    int i = currentTypedWord.length() - 1;
                    boolean correct = i < targetWord.length() && currentTypedWord.charAt(i) == targetWord.charAt(i);
                    gameApp.addTypedChar(correct);
                    updateTypedWordUI(targetWord);

                        //if the entire word has been successfully typed
                        if (currentTypedWord.equals(targetWord)) {
                            targetCloud.setDead(); //mark cloud for removal
                            resetTyping();
                            gameApp.setTargetCloud(null);

                            gameApp.addCorrectWord();
                            playerFuel.addFuel(20); //add fuel on correct word typed
                            gameApp.setCurrentState(Main.GameState.PLAYING); //unpause the game
                            gameApp.addScore(100); //increase score on correct word

                            //award money for sucsessful word
                            int money = 10;

                            //for double money powerup
                            if (game != null && game.isDoubleMoneyActive()) {
                                money *= 2;
                            }

                            userManager.addMoney(gameApp.getCurrentUser(), money);

                            //check if score is multiple of 500, if so increase the level
                            if (gameApp.getIsEndless() && gameApp.getCurrentScore() > 0 && gameApp.getCurrentScore() % 500 == 0) {
                                level.levelUp();
                            }


                            targetWordUI.setVisible(false);
                        typedWordUI.setVisible(false);
                        timerUI.setVisible(false);
                    }
                }
            }
        });
    }

    /**
     * Resets the current typed word and resets the typed text UI.
     */
    public void resetTyping() {
        currentTypedWord = "";
        typedWordUI.getChildren().clear();

        Text typedLabel = new Text("Typed: ");
        typedLabel.setFill(Color.FORESTGREEN);
        typedLabel.setFont(gameFont);

        typedWordUI.getChildren().add(typedLabel);
    }

    /**
     * Updates the typed word UI to show the player's current input
     * and highlight correct or incorrect letters.
     * @param targetWord the word the player is supposed to type
     */
    private void updateTypedWordUI(String targetWord) {
        typedWordUI.getChildren().clear();

        //prefix text
        Text typedLabel = new Text("Typed: ");
        typedLabel.setFill(Color.FORESTGREEN);
        typedLabel.setFont(gameFont);
        typedWordUI.getChildren().add(typedLabel);

        for (int i = 0; i < currentTypedWord.length(); i++) {
            char typedChar = currentTypedWord.charAt(i);

            Text letter = new Text(String.valueOf(typedChar));
            letter.setFont(gameFont);

            if (i < targetWord.length() && typedChar == targetWord.charAt(i)) {
                letter.setFill(Color.FORESTGREEN); //correct
            } else {
                letter.setFill(Color.RED); //incorrect
            }

            typedWordUI.getChildren().add(letter);
        }
    }
}