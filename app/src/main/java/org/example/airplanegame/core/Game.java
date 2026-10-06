package org.example.airplanegame.core;

import javafx.animation.AnimationTimer;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.example.airplanegame.Main;
import org.example.airplanegame.objects.Cloud;
import org.example.airplanegame.player.Player;
import org.example.airplanegame.player.PlayerFuel;
import org.example.airplanegame.disasters.DisasterController;
import org.example.airplanegame.disasters.DisasterInterface;
import org.example.airplanegame.player.UserProfile;
import org.example.airplanegame.playerControllers.PlayerMovement;
import org.example.airplanegame.playerControllers.TypingController;

import java.util.ArrayList;
import java.util.List;

/**
 * Core game logic contained within the game loop
 * responsible for keeping track of the different game states, spawing of clouds, spawning of disasters
 * Houses core game functions responsible for the startup of playing a game
 * @author Everyone
 */
public class Game {
    private Main mainApp;
    private Scene scene;
    private Pane root;
    private Player player;

    //core game objects
    private Level level;

    //controllers
    private PlayerMovement playerMovement;
    private TypingController typingController;
    private AnimationTimer gameLoop;
    private DisasterController disasterController;

    //game state variables
    private ArrayList<Cloud> activeClouds = new ArrayList<>();
    private int frameCount = 0;
    private int disasterFrameCount = 0;
    private long lastFrameTime = 0;
    private PlayerFuel playerFuel;
    private DisasterInterface newDisaster;
    private Node disasterView;
    private int prevDisasterScore = 0;

    //UI references
    private Text timerUI;
    private Text targetWordUI;
    private TextFlow typedWordUI;
    private Text powerUpText;


    //powerups
    private boolean shieldActive = false;
    private boolean slowmo = false;
    private boolean doubleMoney = false;
    private long slowmoTime;
    private long doubleMoneyTime;


    /**
     * Creates a new game instance and initializes the main gameplay loop,
     * UI references, controllers, and powerups
     */
    public Game(Main mainApp, Scene scene, Pane root, Player player, Text timerUI, Text targetWordUI, TextFlow typedWordUI, List<String> wordBank) {
        this.mainApp = mainApp;
        this.scene = scene;
        this.root = root;
        this.player = player;
        this.timerUI = timerUI;
        this.targetWordUI = targetWordUI;
        this.typedWordUI = typedWordUI;


        //initialize level/game data
        this.level = new Level(mainApp.getIsEndless(), mainApp.getCurrentLevelNumber(), player, "CLOUD_01", root, activeClouds, wordBank);

        //fuel system and UI
        this.playerFuel = new PlayerFuel();
        this.root.getChildren().addAll(playerFuel.getFuelBarBackground(), playerFuel.getFuelBar());

        //input controllers
        this.playerMovement = new PlayerMovement(player, scene);
        this.typingController = new TypingController(mainApp, scene, typedWordUI, targetWordUI, timerUI, playerFuel, this.level);
        this.typingController.setGame(this);

        //ppwerup binds
        scene.addEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, event -> {
            switch (event.getCode()) {
                case DIGIT1:
                    activateShield();
                    break;
                case DIGIT2:
                    activateSlowmo();
                    break;
                case DIGIT3:
                    activateFuelUp();
                    break;
                case DIGIT4:
                    activateDoubleMoney();
                    break;
                default:
                    break;
            }
        });

        scene.setUserData(this);

        powerUpText = new Text();
        powerUpText.setFont(Font.font(20));
        powerUpText.setFill(Color.YELLOW);
        powerUpText.setLineSpacing(5);

        root.getChildren().add(powerUpText);
        shieldActive = false;
        slowmo = false;
        doubleMoney = false;
        slowmoTime = 0;
        doubleMoneyTime = 0;

        //disaster controller
        this.disasterController = new DisasterController(10, 5, mainApp, this);

        //FIXXXXXXX
        //to fix change so every 10 clouds a disaster spawns -> every 10 clouds new level

        this.newDisaster = null;
        this.disasterView = null;

        buildGameLoop();
    }

    /**
     * Returns the list of clouds currently active in the game.
     */
    public ArrayList<Cloud> getActiveClouds(){
        return this.activeClouds;
    }

    /**
     * Builds the main game loop that updates gameplay, UI, powerups,
     * disasters and player movement every frame.
     * It is also responsible for handling the collision detection with the clouds.
     */
    private void buildGameLoop() {
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {

                updatePowerUpUI();
                powerUpText.setX(scene.getWidth() - powerUpText.getLayoutBounds().getWidth() - 20);
                powerUpText.setY(scene.getHeight() - 120);// adds the powerups to the bottom right
                if(slowmo &&System.currentTimeMillis() > slowmoTime) {
                    slowmo = false;
                    for(int i = 0; i < activeClouds.size(); i++) {
                        Cloud cloud = activeClouds.get(i);
                        cloud.setVelocity(cloud.getVelocityX() / 0.5, cloud.getVelocityY());
                    }
                } // makes sure the slomo powerup is active for 10 seconds

                if (doubleMoney&&System.currentTimeMillis() > doubleMoneyTime) {
                    doubleMoney = false;
                } // makes sure double money is 20 seconds long

                // ---------------- DISASTER LOGIC ----------------
                int currScore = mainApp.getCurrentScore();

                //spawn a disaster once when the score hits a threshold
                if (newDisaster == null && currScore != 0 && (currScore % 500) == 0 && !mainApp.getInDisaster() && prevDisasterScore != currScore) {

                    //mark disaster as active
                    mainApp.startDisaster();

                    //update prev disaster score
                    prevDisasterScore = currScore;

                    //pick and spawn the disaster
                    newDisaster = disasterController.getDisaster();

                    disasterView = newDisaster.spawnDisaster();
                    root.getChildren().add(disasterView);


                    disasterFrameCount = 0;
                }

                //run the active disaster each frame
                if (mainApp.getInDisaster() && newDisaster != null) {
                    disasterFrameCount++;

                    //convert duration from seconds to frames (60fps)
                    int disasterDuration = (int) (newDisaster.getDuration() * 60);

                    //only disasters with effects need per-frame logic
                    if (newDisaster.getHasEffect()) {
                        newDisaster.disasterEffect(scene);
                    }

                    //end disaster when its duration is done
                    if (disasterFrameCount >= disasterDuration) {
                        disasterFrameCount = 0;

                        //clean up the disaster, swapped old code to a new helper method in game
                        cleanupActiveDisaster();
                        disasterController.updateProbabilites();
                    }
                }

                //disaster cleanup
                if (!mainApp.getInDisaster() && newDisaster != null) {
                    cleanupActiveDisaster();
                }

                // ------------------------------------------------


                //calculate time for fuel timers and typing timers
                if (lastFrameTime == 0) lastFrameTime = now;
                double deltaTime = (now - lastFrameTime) / 1_000_000_000.0;
                if (mainApp.getCurrentState() == Main.GameState.PLAYING || mainApp.getCurrentState() == Main.GameState.TYPING) {
                    mainApp.addRunTime(deltaTime);
                }
                mainApp.updateWPMUI();
                lastFrameTime = now;

                //game over check
                if (playerFuel.isGameOver()) {

                    mainApp.setCurrentState(Main.GameState.GAME_OVER);
                    this.stop();
                    mainApp.triggerGameOver(mainApp.getCurrentLevel(), mainApp.getIsEndless());

                    //reset disaster and score states
                    cleanupActiveDisaster();
                    mainApp.resetScore();

                    return;
                }

                //----------------NORMAL GAMEPLAY----------------
                //PLAYING = normal movement and cloud interaction
                //FREEZE = snow disaster; clouds still move/spawn, but player/cloud collisions are disabled
                //         along with the players movement
                if (mainApp.getCurrentState() == Main.GameState.PLAYING || mainApp.getCurrentState() == Main.GameState.FREEZE) {

                    //only allow the plane to move when not frozen
                    if (mainApp.getCurrentState() != Main.GameState.FREEZE) {
                        playerMovement.update();
                    }

                    //passive fuel drain continues during both states
                    playerFuel.drainPassiveFuel(deltaTime);

                    //clouds continue spawning during snow
                    frameCount++;
                    int spawnRate = 100;

                    if (slowmo) {
                        spawnRate = 160; // slower spawn while less windy is active
                    }
                    if (frameCount % spawnRate == 0) {
                        level.spawnCloud();
                    }

                    //update clouds and handle collision/removal
                    for (int i = activeClouds.size() - 1; i >= 0; i--) {
                        Cloud cloud = activeClouds.get(i);
                        cloud.update();

                        //only allow collision/typing when the game is not frozen
                        if (mainApp.getCurrentState() != Main.GameState.FREEZE && cloud.isAlive() && player.getView().getBoundsInParent().intersects(cloud.getView().getBoundsInParent())) {

                            mainApp.setCurrentState(Main.GameState.TYPING);
                            mainApp.setTargetCloud(cloud);

                            playerFuel.startTypingTimer(level.getTypingTime());
                            typingController.resetTyping();

                            //show typing UI
                            timerUI.setVisible(true);
                            targetWordUI.setText("Target: " + mainApp.getTargetCloud().getTargetWord());
                            targetWordUI.setVisible(true);
                            typedWordUI.setVisible(true);

                            //highlight the target cloud
                            mainApp.getTargetCloud().getView().setStyle("-fx-effect: dropshadow(three-pass-box, red, 10, 0, 0, 0);");
                            break;
                        }

                            //cloud off the screen
                            if (cloud.getView().getTranslateX() < -150) {
                                if(shieldActive) {
                                    shieldActive = false;
                                }else {
                                    playerFuel.removeFuel(10);
                                }
                                cloud.setDead();
                            }

                        //remove dead clouds from scene and list
                        if (!cloud.isAlive()) {
                            root.getChildren().remove(cloud.getView());
                            activeClouds.remove(i);
                        }
                    }

                } else if (mainApp.getCurrentState() == Main.GameState.TYPING) {

                    // Update the timer and check if time ran out
                    boolean timeOut = playerFuel.updateTypingTimer(deltaTime, timerUI);

                    //if ran out of time
                    if (timeOut) {
                        if(shieldActive) { // checks if player has the power up active or not
                            shieldActive = false;
                        } else {
                        playerFuel.removeFuel(20);
                        }

                        //remove the cloud and reset typing state
                        mainApp.getTargetCloud().setDead();
                        typingController.resetTyping();
                        mainApp.setTargetCloud(null);

                        targetWordUI.setVisible(false);
                        typedWordUI.setVisible(false);
                        timerUI.setVisible(false);

                        mainApp.setCurrentState(Main.GameState.PLAYING);
                    }
                }
            }
        };
    }

    /**
     * Returns the current level used by the game.
     */
    public Level getLevel(){return this.level;}//end of get level method

    /**
     * Activates the shield powerup if user has it,
     * consumes a sheild and updates the users inventory
     */
    private void activateShield() {
        if(shieldActive) {
            return;
        }
        UserProfile profile = mainApp.getCurrentUserProfile();

        if (profile.getInventory().usePowerUp("shield")) {
                shieldActive = true;
            mainApp.saveCurrentProfile(profile);
        }
    } // end of activateShield()

    /**
     * Activates the slowmo power up which slows cloud
     */
    private void activateSlowmo() {
        if (slowmo) {
            return;
        }

        UserProfile profile = mainApp.getCurrentUserProfile();

        if (profile.getInventory().usePowerUp("slowmo")) {
            slowmo = true;
            slowmoTime = System.currentTimeMillis() + 10000;

            for(int i = 0; i < activeClouds.size(); i++) {
                Cloud cloud = activeClouds.get(i);
                cloud.setVelocity(cloud.getVelocityX() * 0.5, cloud.getVelocityY());
            }

            mainApp.saveCurrentProfile(profile);
        }
    } // end of activateLessWindy()

    /**
     * activates the fuel up powerup
     */
    private void activateFuelUp() {
        UserProfile profile = mainApp.getCurrentUserProfile();

        if (profile.getInventory().usePowerUp("fuel_up")) {
            playerFuel.addFuel(25);
            mainApp.saveCurrentProfile(profile);
        }
    } // end of activateFuelUp()

    /**
     * activates double money received at the end of the round
     */
    private void activateDoubleMoney() {
        if(doubleMoney) {
            return;
        }

        UserProfile profile = mainApp.getCurrentUserProfile();

        if (profile.getInventory().usePowerUp("double_money")) {
            doubleMoney = true;
            doubleMoneyTime = System.currentTimeMillis() + 20000;

            mainApp.saveCurrentProfile(profile);
        }
    } // end of activateDoubleMoney()

    /**
     * returns if lesswindy is active or not
     * @return state of less windy
     */
    public boolean isSlowmoActive() {
        return slowmo;
    } // end of isLessWindyActive()

    /**
     * Checks if double money is active
     * @return state of double money
     */
    public boolean isDoubleMoneyActive() {
        return doubleMoney;
    } // end of isDoubleMoneyActive()

    /**
     * Adds powerups to game ui
     */
    private void updatePowerUpUI() {
        UserProfile profile = mainApp.getCurrentUserProfile();

        Font font = Font.loadFont(getClass().getResourceAsStream("/PressStart2P-Regular.ttf"), 20);

        int shield = profile.getInventory().getPowerUps("shield");
        int wind = profile.getInventory().getPowerUps("slowmo");
        int fuel = profile.getInventory().getPowerUps("fuel_up");
        int money = profile.getInventory().getPowerUps("double_money");

        String windText = "";
        String moneyText = "";

        if(slowmo) {
            long secondsLeft = Math.max(0, (slowmoTime - System.currentTimeMillis()) / 1000);
            windText = " [" + secondsLeft + "s]";
        }
        if(doubleMoney) {
            long secondsLeft = Math.max(0, (doubleMoneyTime - System.currentTimeMillis()) / 1000);
            moneyText = " [" + secondsLeft + "s]";
        }

        powerUpText.setText(
                        "1: Shield (" + shield + ")" + "     " + "\n" +
                        "2: SlowMo (" + wind + ")" + windText + "\n" +
                        "3: Fuel   (" + fuel + ")\n" +
                        "4: Money  (" + money + ")" + moneyText + "\n"
        );
        powerUpText.setFont(font);

    } // end of updatePowerUpUI()

    /**
     *cleans up any active disaster so it does not leak into the next scene
     */
    public void cleanupActiveDisaster() {
        if (newDisaster != null) {
            try {
                newDisaster.disableDisasterEffect();
            } catch (Exception ignored) {
                //ignored for disasters without effects
            }
            newDisaster = null;
        }

        if (disasterView != null) {
            root.getChildren().remove(disasterView);
            disasterView = null;
        }

        disasterFrameCount = 0;
        mainApp.endDisaster();
    }

    /**
     * Starts the main game loop.
     */
    public void start() {
        gameLoop.start();
    }

    /**
     * Stops the main game loop if its running.
     */
    public void stop() {
        if (gameLoop != null) gameLoop.stop();
    }
}