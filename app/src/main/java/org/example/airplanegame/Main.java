package org.example.airplanegame;

//-----IMPORTS-----
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

import org.example.airplanegame.playerControllers.UserManager;
import org.example.airplanegame.core.Game;
import org.example.airplanegame.objects.Cloud;
import org.example.airplanegame.player.Player;
import org.example.airplanegame.player.UserProfile;
import org.example.airplanegame.sceneControllers.*;
import org.example.airplanegame.shops.PowerUpShopController;

/**
 * Houses all logic related to scene switching and change states of disasters
 * @author Everyone
 */
public class Main extends Application {
    private static final double SCROLL_SPEED = 4.0;//The speed that the background moves
    private static final double IMAGE_WIDTH = 770.0; //The width of the background image
    private static final double IMAGE_HEIGHT = 1080.0;
    private static final double START_X = 672.0;

    private double timeLeft;
    private long lastTime;
    private double runTimeSeconds;

    private int currentScore = 0;//the score of the current game
    private Text endlessScoreUI;
    private Text wpmUI;
    private UserManager userManager = new UserManager();
    private String currentUser = "";//the username of the user that is currently logged in
    private Stage mainStage;

    private int correctWords;
    private int totalTypedChars;
    private int correctTypedChars;
    private int errorCount;

    private Text timerUI;
    private Text targetWordUI;
    private TextFlow typedWordUI;

    private AnchorPane root;
    private Player player;
    private List<String> wordBank = new ArrayList<>();

    private Game currentGame;
    private GameState currentState = GameState.PLAYING;
    private Cloud targetCloud = null;

    private AnimationTimer levelTimerLoop;
    private Runnable currentLevel;//the level that the user is currently playing
    private int currentLevelNumber;//the number of the level that the user is playing

    boolean isEndless;//true if the user is playing the endless game mode and false if they are not
    private boolean inDisaster = false;

    public enum GameState {
        PLAYING,
        TYPING,
        GAME_OVER,
        FREEZE,
        PAUSED
    }

    @Override
    public void start(Stage primaryStage) {
        Font font = Font.loadFont(getClass().getResourceAsStream("/PressStart2P-Regular.ttf"), 20);
        System.out.println(font);
        System.out.println(font.getFamily());
        System.out.println(font.getName());
        this.mainStage = primaryStage;

        mainStage.setTitle("Typing at 30,000 FT");
        mainStage.setResizable(false); // Prevents the user from resizing the window and breaking the layout

        // Load in the words from the words.txt file
        loadWordBank();

        // Boot up the game by showing the Login Scene first
        loginScene();

        //makes it fullscreen if the users screen is too small
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        if(1920 > screenBounds.getWidth() || 1080 > screenBounds.getHeight())
            mainStage.setFullScreen(true);

        mainStage.show();
    }

    /**
     * creates the login scene for the game
     */
    public void loginScene() {
        final int IMAGE_WIDTH = 770;//the width of the background image
        final int SCROLL_SPEED = 1;//the speed that the background moves

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/loginScene.fxml"));
        try {
            root = fxmlLoader.load();
            LoginController controller = fxmlLoader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        Image backgroundImage = new Image(getClass().getResourceAsStream("/background.png"));
        Image planeImage = new Image(getClass().getResourceAsStream("/plane.png"));
        ImageView background1 = new ImageView(backgroundImage);
        ImageView background2 = new ImageView(backgroundImage);
        ImageView background3 = new ImageView(backgroundImage);
        ImageView background4 = new ImageView(backgroundImage);
        ImageView plane = new ImageView(planeImage);


        background1.setLayoutX(0);
        background2.setLayoutX(IMAGE_WIDTH);
        background3.setLayoutX(IMAGE_WIDTH*2);
        background4.setLayoutX(IMAGE_WIDTH*3);
        plane.setLayoutX(750);
        plane.setLayoutY(150);

        //adds cloud backgrounds to the pane and push them to the back
        root.getChildren().addAll(plane, background1, background2, background3, background4);
        background1.toBack();
        background2.toBack();
        background3.toBack();
        background4.toBack();

        //animates the background (got help from Gemini)
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                background1.setLayoutX(background1.getLayoutX() - SCROLL_SPEED);
                background2.setLayoutX(background2.getLayoutX() - SCROLL_SPEED);
                background3.setLayoutX(background3.getLayoutX() - SCROLL_SPEED);
                background4.setLayoutX(background4.getLayoutX() - SCROLL_SPEED);

                if (background1.getLayoutX() <= -IMAGE_WIDTH) {
                    background1.setLayoutX(background4.getLayoutX() + IMAGE_WIDTH);
                }
                if (background2.getLayoutX() <= -IMAGE_WIDTH) {
                    background2.setLayoutX(background1.getLayoutX() + IMAGE_WIDTH);
                }
                if (background3.getLayoutX() <= -IMAGE_WIDTH) {
                    background3.setLayoutX(background2.getLayoutX() + IMAGE_WIDTH);
                }
                if (background4.getLayoutX() <= -IMAGE_WIDTH) {
                    background4.setLayoutX(background3.getLayoutX() + IMAGE_WIDTH);
                }
            }
        };
        timer.start();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(this.getClass().getResource("/stylesheet.css").toExternalForm());

        mainStage.setScene(scene);
    }//end of loginScene method

    /**
     * the scene where the user can create an account
     */
    public void createAccountScene(){
        final int IMAGE_WIDTH = 770;//the width of the background image
        final int SCROLL_SPEED = 1;//the speed that the background moves

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/createAccountScene.fxml"));
        try {
            root = fxmlLoader.load();
            CreateAccountController controller = fxmlLoader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }//end of try catch

        Image backgroundImage = new Image(getClass().getResourceAsStream("/background.png"));
        Image planeImage = new Image(getClass().getResourceAsStream("/plane.png"));
        ImageView background1 = new ImageView(backgroundImage);
        ImageView background2 = new ImageView(backgroundImage);
        ImageView background3 = new ImageView(backgroundImage);
        ImageView background4 = new ImageView(backgroundImage);
        ImageView plane = new ImageView(planeImage);


        background1.setLayoutX(0);
        background2.setLayoutX(IMAGE_WIDTH);
        background3.setLayoutX(IMAGE_WIDTH*2);
        background4.setLayoutX(IMAGE_WIDTH*3);
        plane.setLayoutX(750);
        plane.setLayoutY(150);

        //adds cloud backgrounds to the pane and push them to the back
        root.getChildren().addAll(plane, background1, background2, background3, background4);
        background1.toBack();
        background2.toBack();
        background3.toBack();
        background4.toBack();

        //animates the background (got help from Gemini)
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                background1.setLayoutX(background1.getLayoutX() - SCROLL_SPEED);
                background2.setLayoutX(background2.getLayoutX() - SCROLL_SPEED);
                background3.setLayoutX(background3.getLayoutX() - SCROLL_SPEED);
                background4.setLayoutX(background4.getLayoutX() - SCROLL_SPEED);

                if (background1.getLayoutX() <= -IMAGE_WIDTH) {
                    background1.setLayoutX(background4.getLayoutX() + IMAGE_WIDTH);
                }
                if (background2.getLayoutX() <= -IMAGE_WIDTH) {
                    background2.setLayoutX(background1.getLayoutX() + IMAGE_WIDTH);
                }
                if (background3.getLayoutX() <= -IMAGE_WIDTH) {
                    background3.setLayoutX(background2.getLayoutX() + IMAGE_WIDTH);
                }
                if (background4.getLayoutX() <= -IMAGE_WIDTH) {
                    background4.setLayoutX(background3.getLayoutX() + IMAGE_WIDTH);
                }
            }
        };
        timer.start();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(this.getClass().getResource("/stylesheet.css").toExternalForm());

        mainStage.setScene(scene);
    }//end of createAccountScene method

    /**
     * creates the scene for the main menu for the game
     */
    public void mainMenuScene(){

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/menuScene.fxml"));
        try {
            root = fxmlLoader.load();
            MainMenuController controller = fxmlLoader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }//end of try catch
        addBackground(root);

        Scene scene = new Scene(root);
        addMenuKeybinds(scene);
        scene.getStylesheets().add(this.getClass().getResource("/stylesheet.css").toExternalForm());

        mainStage.setScene(scene);//sets the scene to the login scene
    }//end of createLoginScreen method

    /**
     * creates and shows the level one scene
     */
    public void levelScene(int levelNumber){
        if (levelTimerLoop != null) {
            levelTimerLoop.stop();
        }//end of if statement
        if (currentGame != null) {
            currentGame.stop();
        }//end of if statement

        currentState = GameState.PLAYING;
        currentLevelNumber = levelNumber;
        currentLevel = () -> levelScene(levelNumber);
        isEndless = false;
        runTimeSeconds = 0;
        timeLeft = 60.0;
        correctWords = 0;
        lastTime = 0;
        totalTypedChars = 0;
        correctTypedChars = 0;
        errorCount = 0;


        root = new AnchorPane();
        Scene scene = new Scene(root,1920,1080);
        scene.getStylesheets().add(getClass().getResource("/stylesheet.css").toExternalForm());

        //default plane
        String equippedPlane = "JET";

        //finds the plane that the user has equipped
        UserProfile profile = userManager.getUserProfile(currentUser);
        if (profile != null && profile.getInventory() != null) {
            if (profile.getInventory().getEquippedPlane() != null) {
                equippedPlane = profile.getInventory().getEquippedPlane();
            }//end of if statement
        }//end of if statement

        //set up background image depending on level
        String backgroundPath = "/skyBackground.jpg";

        if (levelNumber == 1) {
            backgroundPath = "/skyBackground.jpg";
        } else if (levelNumber == 2) {
            backgroundPath = "/nightSkyBackground.jpg";
        } else if (levelNumber == 3) {
            backgroundPath = "/cityBackground.jpg";
        } else if (levelNumber == 4) {
            backgroundPath = "/mountainBackground.jpg";
        } else if (levelNumber == 5) {
            backgroundPath = "/spaceBackground.jpg";
        }//end of if statement

        //load in the background image
        Image bgImage = new Image(getClass().getResourceAsStream(backgroundPath));
        BackgroundImage bImg = new BackgroundImage(
                bgImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                new BackgroundSize(100, 100, true, true, true, true));
        root.setBackground(new Background(bImg));

        //set up the airplane
        String planePath = "/plane.png";
        if ("PROPELLER".equals(equippedPlane)) {
            planePath = "/propeller plane.png";
        } else if ("HELICOPTER".equals(equippedPlane)) {
            planePath = "/helicopter.png";
        } else if ("UFO".equals(equippedPlane)) {
            planePath = "/ufo.png";
        }//end of if statement

        //load in the airplane
        Image airplaneImage = new Image(getClass().getResourceAsStream(planePath));
        ImageView airplaneView = new ImageView(airplaneImage);
        airplaneView.setFitWidth(120);
        airplaneView.setPreserveRatio(true);
        player = new Player(airplaneView, 50, 300);
        root.getChildren().add(player.getView());

        Font gameFont = Font.loadFont(getClass().getResourceAsStream("/PressStart2P-Regular.ttf"), 20);
        Font timerFont = Font.loadFont(getClass().getResourceAsStream("/PressStart2P-Regular.ttf"), 28);

        targetWordUI = new Text("Target: ");
        targetWordUI.setFont(gameFont);
        targetWordUI.setFill(Color.FORESTGREEN);
        targetWordUI.setX(700);
        targetWordUI.setY(920);
        targetWordUI.setVisible(false);

        typedWordUI = new TextFlow();
        typedWordUI.setLayoutX(700);
        typedWordUI.setLayoutY(980);
        typedWordUI.setVisible(false);

        Text typedLabel = new Text("Typed: ");
        typedLabel.setFill(Color.FORESTGREEN);
        typedLabel.setFont(gameFont);
        typedWordUI.getChildren().add(typedLabel);

        timerUI = new Text("Time: 5.0");
        timerUI.setFont(timerFont);
        timerUI.setFill(Color.RED);
        timerUI.setX(900);
        timerUI.setY(150);
        timerUI.setVisible(false);

        Text levelTimer = new Text("TIME: " );
        levelTimer.setFont(timerFont);
        levelTimer.setFill(Color.RED);
        levelTimer.setX(650);
        levelTimer.setY(75);

        Text levelText = new Text("LEVEL: " + currentLevelNumber);
        levelText.setFont(timerFont);
        levelText.setFill(Color.RED);
        levelText.setX(40);
        levelText.setY(75);

        wpmUI = new Text("WPM: 0.0");
        wpmUI.setFont(timerFont);
        wpmUI.setFill(Color.RED);
        wpmUI.setX(350);
        wpmUI.setY(75);

        Text fuelText = new Text("FUEL");
        fuelText.setFont(gameFont);
        fuelText.setFill(Color.RED);
        fuelText.setX(450);
        fuelText.setY(1045);

        Image pauseImage = new Image(getClass().getResourceAsStream("/pause.png"));
        ImageView pauseIcon = new ImageView(pauseImage);
        pauseIcon.setFitWidth(75);
        pauseIcon.setPreserveRatio(true);
        Button pauseButton = new Button();
        pauseButton.setGraphic(pauseIcon);
        pauseButton.setLayoutX(1800);
        pauseButton.setLayoutY(20);
        pauseButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
        pauseButton.setFocusTraversable(false);

        //controls how the timer for the level decreases (got help from Gemini)
        levelTimerLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {

                //initialize the start time on the very first frame
                if (lastTime == 0) {
                    lastTime = now;
                    return;
                }//end of if statement

                //pauses the timer if the game is paused or the user is typing a word
                if(currentState != GameState.PLAYING){
                    lastTime = now;
                    return;
                }//end of if statement

                //calculate exactly how many seconds passed since the last frame
                double elapsedSeconds = (now - lastTime) / 1_000_000_000.0;
                lastTime = now;

                //decrease the time
                timeLeft -= elapsedSeconds;

                //update the UI and if the time left is 0 then game over
                if (timeLeft <= 0) {
                    levelTimer.setText("Time: 0.0");

                    if (currentLevelNumber < 5) {
                        levelCompleteScene(currentLevel, () -> levelScene(currentLevelNumber + 1));
                    } else {
                        levelCompleteScene(currentLevel, null);
                    }
                    this.stop(); //stops the game loop
                } else {
                    levelTimer.setText(String.format("TIME: %.1f", timeLeft));
                }//end of if statement
            }//end of handle method
        };
        levelTimerLoop.start();

        pauseButton.toFront();
        root.getChildren().addAll(targetWordUI, typedWordUI, timerUI, levelText, fuelText, pauseButton, wpmUI, levelTimer);

        //---DELEGATE TO GAME CLASS---
        currentGame = new Game(this, scene, root, player, timerUI, targetWordUI, typedWordUI, wordBank);
        currentGame.start();

        //changes the difficulty depending on what level is being played
        if(levelNumber == 1) currentGame.getLevel().setCurrentLevelNumber(1);
        else if(levelNumber == 2) currentGame.getLevel().setCurrentLevelNumber(5);
        else if(levelNumber == 3) currentGame.getLevel().setCurrentLevelNumber(10);
        else if(levelNumber == 4) currentGame.getLevel().setCurrentLevelNumber(15);
        else if(levelNumber == 5) currentGame.getLevel().setCurrentLevelNumber(20);



        mainStage.setScene(scene);//sets the scene to the level one scene
        pauseButton.setOnAction(event -> pauseScene(root, currentLevel));
    }//end of levelOneScene method

    /**
     * Shows the pause screen when they press the pause button
     * @param root the anchor pane of the game level that is being played
     * @param level the method of the level that is currently being played
     */
    public void pauseScene(AnchorPane root, Runnable level){
        currentState = GameState.PAUSED;

        //create the transparent gray background
        Rectangle greyBackground = new Rectangle(1920,1080);
        greyBackground.setFill(Color.rgb(0,0,0,0.7));

        //create the resume button
        Button resumeButton = new Button("RESUME");
        resumeButton.getStyleClass().add("pauseButton");
        resumeButton.setTextFill(Color.rgb(0,153,0));
        resumeButton.setLayoutX(660);
        resumeButton.setLayoutY(400);
        resumeButton.setFocusTraversable(false);

        //create the restart button
        Button restartButton = new Button("RESTART");
        restartButton.setTextFill(Color.rgb(153,0,0));
        restartButton.getStyleClass().add("pauseButton");
        restartButton.setLayoutX(660);
        restartButton.setLayoutY(600);
        restartButton.setFocusTraversable(false);

        //create the main menu button
        Button menuButton = new Button("MAIN MENU");
        menuButton.setTextFill(Color.rgb(153,0,0));
        menuButton.getStyleClass().add("pauseButton");
        menuButton.setLayoutX(660);
        menuButton.setLayoutY(800);
        menuButton.setFocusTraversable(false);

        //controls what happens when the buttons are pressed
        resumeButton.setOnAction(action -> {
            root.getChildren().removeAll(greyBackground, resumeButton,restartButton, menuButton);
            currentState = GameState.PLAYING;
        });
        restartButton.setOnAction(action -> level.run());
        menuButton.setOnAction(action -> mainMenuScene());

        root.getChildren().addAll(greyBackground, resumeButton,restartButton, menuButton);
    }//end of pauseScene method

    /**
     * creates and shows the endless game mode scene
     */
    public void endlessScene() {
        if (levelTimerLoop != null) {
            levelTimerLoop.stop();
        }//end of if statement
        if (currentGame != null) {
            currentGame.stop();
        }//end of if statement

        currentState = GameState.PLAYING;
        currentLevel = () -> endlessScene();
        isEndless = true;
        runTimeSeconds = 0;
        correctWords = 0;
        lastTime = 0;
        currentScore = 0;
        totalTypedChars = 0;
        correctTypedChars = 0;
        errorCount = 0;

        root = new AnchorPane();
        Scene scene = new Scene(root, 1920, 1080);
        scene.getStylesheets().add(getClass().getResource("/stylesheet.css").toExternalForm());

        String equippedPlane = "JET";
        String equippedBackground = "SKY";

        UserProfile profile = userManager.getUserProfile(currentUser);
        if (profile != null && profile.getInventory() != null) {
            if (profile.getInventory().getEquippedPlane() != null) {
                equippedPlane = profile.getInventory().getEquippedPlane();
            }//end of if statement
            if (profile.getInventory().getEquippedBackground() != null) {
                equippedBackground = profile.getInventory().getEquippedBackground();
            }//end of if statement
        }//end of if statement

        String backgroundPath = "/skyBackground.jpg";

        if ("NIGHT".equals(equippedBackground)) {
            backgroundPath = "/nightSkyBackground.jpg";
        } else if ("CITY".equals(equippedBackground)) {
            backgroundPath = "/cityBackground.jpg";
        } else if ("MOUNTAIN".equals(equippedBackground)) {
            backgroundPath = "/mountainBackground.jpg";
        } else if ("SPACE".equals(equippedBackground)) {
            backgroundPath = "/spaceBackground.jpg";
        }//end of if statement

        Image bgImage = new Image(getClass().getResourceAsStream(backgroundPath));
        BackgroundImage bImg = new BackgroundImage(
                bgImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                new BackgroundSize(100, 100, true, true, true, true));
        root.setBackground(new Background(bImg));

        String planePath = "/plane.png";
        if ("PROPELLER".equals(equippedPlane)) {
            planePath = "/propeller plane.png";
        } else if ("HELICOPTER".equals(equippedPlane)) {
            planePath = "/helicopter.png";
        } else if ("UFO".equals(equippedPlane)) {
            planePath = "/ufo.png";
        }//end of if statement

        Image airplaneImage = new Image(getClass().getResourceAsStream(planePath));
        ImageView airplaneView = new ImageView(airplaneImage);
        airplaneView.setFitWidth(120);
        airplaneView.setPreserveRatio(true);
        player = new Player(airplaneView, 50, 300);
        root.getChildren().add(player.getView());

        Font gameFont = Font.loadFont(getClass().getResourceAsStream("/PressStart2P-Regular.ttf"), 20);
        Font timerFont = Font.loadFont(getClass().getResourceAsStream("/PressStart2P-Regular.ttf"), 28);

        targetWordUI = new Text("Target: ");
        targetWordUI.setFont(gameFont);
        targetWordUI.setFill(Color.GREEN);
        targetWordUI.setX(700);
        targetWordUI.setY(920);
        targetWordUI.setVisible(false);

        typedWordUI = new TextFlow();
        typedWordUI.setLayoutX(700);
        typedWordUI.setLayoutY(980);
        typedWordUI.setVisible(false);

        Text typedLabel = new Text("Typed: ");
        typedLabel.setFill(Color.FORESTGREEN);
        typedLabel.setFont(gameFont);
        typedWordUI.getChildren().add(typedLabel);

        timerUI = new Text("Time: 5.0");
        timerUI.setFont(timerFont);
        timerUI.setFill(Color.RED);
        timerUI.setX(900);
        timerUI.setY(150);
        timerUI.setVisible(false);

        endlessScoreUI = new Text("SCORE: 0");
        endlessScoreUI.setFont(timerFont);
        endlessScoreUI.setFill(Color.RED);
        endlessScoreUI.setX(40);
        endlessScoreUI.setY(75);

        wpmUI = new Text("WPM: 0.0");
        wpmUI.setFont(timerFont);
        wpmUI.setFill(Color.RED);
        wpmUI.setX(400);
        wpmUI.setY(75);

        Text fuelText = new Text("FUEL");
        fuelText.setFont(gameFont);
        fuelText.setFill(Color.RED);
        fuelText.setX(450);
        fuelText.setY(1045);

        Image pauseImage = new Image(getClass().getResourceAsStream("/pause.png"));
        ImageView pauseIcon = new ImageView(pauseImage);
        pauseIcon.setFitWidth(75);
        pauseIcon.setPreserveRatio(true);
        Button pauseButton = new Button();
        pauseButton.setGraphic(pauseIcon);
        pauseButton.setLayoutX(1800);
        pauseButton.setLayoutY(20);
        pauseButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
        pauseButton.setFocusTraversable(false);

        root.getChildren().addAll(targetWordUI, typedWordUI, timerUI, endlessScoreUI, fuelText, pauseButton, wpmUI);

        pauseButton.toFront();
        currentGame = new Game(this, scene, root, player, timerUI, targetWordUI, typedWordUI, wordBank);
        currentGame.start();

        mainStage.setScene(scene);
        pauseButton.setOnAction(event -> pauseScene(root, currentLevel));
    }//end of endless scene

    /**
     * creates and shows the leaderboard scene
     */
    public void leaderboardScene(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/leaderboardScene.fxml"));

        try {
            root = fxmlLoader.load();
            LeaderboardController controller = fxmlLoader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }//end of try catch

        //add plane background
        addBackground(root);

        Scene scene = new Scene(root);
        addMenuKeybinds(scene);
        scene.getStylesheets().add(this.getClass().getResource("/stylesheet.css").toExternalForm());

        mainStage.setScene(scene);//sets the scene to the login scene
    }//end of leaderboardScene method

    /**
     * Loads all elements of the credits page
     * initializes the controller and applies styling
     */
    public void CreditsScene() {
        //load fxml file and button controller for credits
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/creditsScene.fxml"));

        try {
            root = fxmlLoader.load();
            CreditsController controller = fxmlLoader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }//end of try catch
        //add plane background
        addBackground(root);

        //set the scene and add style sheet
        Scene scene = new Scene(root);
        addMenuKeybinds(scene);
        scene.getStylesheets().add(getClass().getResource("/stylesheet.css").toExternalForm());

        mainStage.setScene(scene);
    }//end of creditsScene method

    /**
     * Loads all elements of the shop page
     * initializes the controller and applies styling
     */
    public void shopScene(){
        //load fxml file and button controller for shop while also loading in what the user has
        //already bought and has equipped
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/shopScene.fxml"));

        try {
            root = fxmlLoader.load();
            ShopController controller = fxmlLoader.getController();
            controller.setMainApp(this);
            controller.loadEquippedState();
            controller.loadCostState();
            controller.loadEquippedBackgroundState();
            controller.loadBackgroundCostState();
            controller.loadMoney();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //load in all previews of cosmetics
        Image planeImage = new Image(getClass().getResourceAsStream("/plane.png"));
        Image propellerImage = new Image(getClass().getResourceAsStream("/propeller plane.png"));
        Image helicopterImage = new Image(getClass().getResourceAsStream("/helicopter.png"));
        Image ufoImage = new Image(getClass().getResourceAsStream("/ufo.png"));
        Image skyBackgroundImage = new Image(getClass().getResourceAsStream("/skyBackground.jpg"));
        Image nightSkyBackgroundImage = new Image(getClass().getResourceAsStream("/nightSkyBackground.jpg"));
        Image mountainBackgroundImage = new Image(getClass().getResourceAsStream("/mountainBackground.jpg"));
        Image cityBackgroundImage = new Image(getClass().getResourceAsStream("/cityBackground.jpg"));
        Image spaceBackgroundImage = new Image(getClass().getResourceAsStream("/spaceBackground.jpg"));

        ImageView plane = new ImageView(planeImage);
        ImageView propeller = new ImageView(propellerImage);
        ImageView helicopter = new ImageView(helicopterImage);
        ImageView ufo = new ImageView(ufoImage);
        ImageView skyBackground = new ImageView(skyBackgroundImage);
        ImageView nightSkyBackground = new ImageView(nightSkyBackgroundImage);
        ImageView mountainBackground = new ImageView(mountainBackgroundImage);
        ImageView cityBackground = new ImageView(cityBackgroundImage);
        ImageView spaceBackground = new ImageView(spaceBackgroundImage);

        plane.setLayoutX(125);
        plane.setLayoutY(425);
        plane.setPreserveRatio(true);
        plane.setFitWidth(200);

        propeller.setLayoutX(525);
        propeller.setLayoutY(425);
        propeller.setPreserveRatio(true);
        propeller.setFitWidth(200);

        helicopter.setLayoutX(125);
        helicopter.setLayoutY(795);
        helicopter.setPreserveRatio(true);
        helicopter.setFitWidth(200);

        ufo.setLayoutX(515);
        ufo.setLayoutY(780);
        ufo.setPreserveRatio(true);
        ufo.setFitWidth(200);

        skyBackground.setLayoutX(1105);
        skyBackground.setLayoutY(410);
        skyBackground.setPreserveRatio(true);
        skyBackground.setFitWidth(200);

        nightSkyBackground.setLayoutX(1520);
        nightSkyBackground.setLayoutY(410);
        nightSkyBackground.setPreserveRatio(true);
        nightSkyBackground.setFitWidth(200);

        cityBackground.setLayoutX(990);
        cityBackground.setLayoutY(825);
        cityBackground.setPreserveRatio(true);
        cityBackground.setFitWidth(200);

        mountainBackground.setLayoutX(1325);
        mountainBackground.setLayoutY(825);
        mountainBackground.setPreserveRatio(true);
        mountainBackground.setFitWidth(200);

        spaceBackground.setLayoutX(1645);
        spaceBackground.setLayoutY(805);
        spaceBackground.setPreserveRatio(true);
        spaceBackground.setFitHeight(115);
        spaceBackground.setFitWidth(200);

        root.getChildren().addAll(plane,propeller,helicopter,ufo,skyBackground,nightSkyBackground,cityBackground
                ,mountainBackground,spaceBackground);

        //set scene and add style sheet
        Scene scene = new Scene(root);
        addMenuKeybinds(scene);
        scene.getStylesheets().add(getClass().getResource("/stylesheet.css").toExternalForm());

        mainStage.setScene(scene);
    }

    /**
     * creates and shows the power ups shop screen
     */
    public void powerUpShopScene(){
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/powerUpShopScene.fxml"));

        try {
            root = fxmlLoader.load();
            PowerUpShopController controller = fxmlLoader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }//end of try catch

        //set scene and add style sheet
        Scene scene = new Scene(root);
        addMenuKeybinds(scene);
        scene.getStylesheets().add(getClass().getResource("/stylesheet.css").toExternalForm());

        mainStage.setScene(scene);
    }//end of powerUpShopScene

    /**
     * creates and displays the tutorial scene
     */
    public void tutorialScene(){
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/tutorialScene.fxml"));

        try {
            root = fxmlLoader.load();
            TutorialController controller = fxmlLoader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }//end of try catch

        //set scene and add style sheet
        Scene scene = new Scene(root);
        addMenuKeybinds(scene);
        scene.getStylesheets().add(getClass().getResource("/stylesheet.css").toExternalForm());

        mainStage.setScene(scene);
    }//end of tutorialScene method

    /**
     * Loads all elements of the shop page
     * initializes the controller and applies styling
     */
    public void SettingsScene() {
        //load fxml file and button controller for credits
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/settingsScene.fxml"));

        try {
            root = fxmlLoader.load();
            SettingsController controller = fxmlLoader.getController();
            controller.setMainApp(this,currentUser);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //set the scene and add style sheet
        Scene scene = new Scene(root);
        addMenuKeybinds(scene);
        scene.getStylesheets().add(getClass().getResource("/stylesheet.css").toExternalForm());

        mainStage.setScene(scene);
    }

    /**
     * creates and shows the screen where the user can select a game mode
     */
    public void selectGameModeScene(){
        //load fxml file and button controller for credits
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/modeSelectScene.fxml"));

        try {
            root = fxmlLoader.load();
            SelectGameModeController controller = fxmlLoader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //add plane background
        addBackground(root);

        //set the scene and add style sheet
        Scene scene = new Scene(root);
        addMenuKeybinds(scene);
        scene.getStylesheets().add(getClass().getResource("/stylesheet.css").toExternalForm());

        mainStage.setScene(scene);
    }//end of selectGameModeScene method

    /**
     * creates and shows the screen where the user can select a level
     */
    public void selectLevelScene(){
        //load fxml file and button controller for credits
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/levelSelectScene.fxml"));

        try {
            root = fxmlLoader.load();
            LevelSelectController controller = fxmlLoader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //add plane background
        addBackground(root);

        //set the scene and add style sheet
        Scene scene = new Scene(root);
        addMenuKeybinds(scene);
        scene.getStylesheets().add(getClass().getResource("/stylesheet.css").toExternalForm());

        mainStage.setScene(scene);
    }//end of selectLevelScene method








    //----------------DISASTER METHODS------------------

    /**
     * Freezes the game for the snow disaster.
     */
    public void freezeGame() {
        this.currentState = GameState.FREEZE;
    }

    /**
     * Changes the game state back to playing after snowdisaster ends.
     */
    public void unfreezeGame() {
        if (this.currentState == GameState.FREEZE) {
            this.currentState = GameState.PLAYING;
        }
    }

    /**
     * Get the boolean of inDisaster to check if a disaster is currently active
     * @return true if disaster is active, false otherwise
     */
    public boolean getInDisaster() {
        return this.inDisaster;
    }

    /**
     * Starts a disaster by setting inDisaster to true
     */
    public void startDisaster() {
        this.inDisaster = true;
    }

    /**
     * End the disaster, callefd when a disaster is over
     */
    public void endDisaster() {
        this.inDisaster = false;
        if (this.currentState == GameState.FREEZE) {
            this.currentState = GameState.PLAYING;
        }//end of if statement
    }









    //----------------HELPER METHODS---------------------
    /**
     * Exits the game (close application)
     */
    public void ExitGame(){
        Platform.exit();
    }

    /**
     * when the game is over this scene will show up
     * @param level the level or endless game that was being played
     * @param isEndless true if it is endless and false if it is levels
     */
    public void triggerGameOver(Runnable level, boolean isEndless) {
        currentState = GameState.GAME_OVER;//changes the current state to game over
        int personalBest = userManager.getHighScore(currentUser);//gets their personal best

        userManager.updateTypingStats(currentUser, getWPM(), getAccuracyPercent(), getErrorCount()
                ,getRunTimeSeconds(), getCorrectWords());

        //updates the users high score if they are playing the endless game mode because high scores should only be tracked for endless
        if(isEndless){
            userManager.updateHighScore(currentUser, currentScore);//updates the high score if they got better than their previous personal best
        }//end of if statement

        Rectangle background = new Rectangle(1920, 1080);//creates a black background

        Font gameOverFont = Font.loadFont(getClass().getResourceAsStream("/PressStart2P-Regular.ttf"), 100);
        Text gameOverText = new Text("GAME OVER");
        gameOverText.setFont(gameOverFont);
        gameOverText.setFill(Color.WHITE);
        gameOverText.setTextAlignment(TextAlignment.CENTER);
        gameOverText.setX(0);
        gameOverText.setY(200);
        gameOverText.setWrappingWidth(1920);

        Font font = Font.loadFont(getClass().getResourceAsStream("/PressStart2P-Regular.ttf"), 40);

        Text levelText = new Text("LEVEL: " + currentLevelNumber);
        levelText.setFont(font);
        levelText.setFill(Color.WHITE);
        levelText.setTextAlignment(TextAlignment.CENTER);
        levelText.setX(0);
        levelText.setY(350);
        levelText.setWrappingWidth(1920);

        Text currentScoreText = new Text("FINAL SCORE: " + currentScore);
        currentScoreText.setFont(font);
        currentScoreText.setFill(Color.WHITE);
        currentScoreText.setTextAlignment(TextAlignment.CENTER);
        currentScoreText.setX(0);
        currentScoreText.setY(350);
        currentScoreText.setWrappingWidth(1920);

        Text highScoreText = new Text("PERSONAL BEST: " + personalBest);
        highScoreText.setFont(font);
        highScoreText.setFill(Color.WHITE);
        highScoreText.setTextAlignment(TextAlignment.CENTER);
        highScoreText.setX(0);
        highScoreText.setY(450);
        highScoreText.setWrappingWidth(1920);

        double wpmValue = Math.round(getWPM() * 10.0) / 10.0;
        Text wpmText = new Text("WPM: " + wpmValue);
        wpmText.setFont(font);
        wpmText.setFill(Color.WHITE);
        wpmText.setTextAlignment(TextAlignment.CENTER);
        wpmText.setX(0);
        wpmText.setY(550);
        wpmText.setWrappingWidth(1920);

        Text remainingTime = new Text(String.format("REMAINING TIME: %.1f seconds", timeLeft));
        remainingTime.setFont(font);
        remainingTime.setFill(Color.WHITE);
        remainingTime.setTextAlignment(TextAlignment.CENTER);
        remainingTime.setX(0);
        remainingTime.setY(550);
        remainingTime.setWrappingWidth(1920);

        Button restartButton = new Button("RESTART");
        restartButton.getStyleClass().add("gameOverRestartButton");
        restartButton.setOnAction(e -> {
            if (levelTimerLoop != null) {
                levelTimerLoop.stop();
            }//end of if statement
            if (currentGame != null) {
                currentGame.stop();
            }//end of if statement
            currentGame.cleanupActiveDisaster(); //cleanup leftover disasters before restarting
            setCurrentScore(0); //reset the score
            level.run();
        });
        restartButton.setLayoutX(810);
        restartButton.setLayoutY(650);

        Button mainMenuButton = new Button("MENU");
        mainMenuButton.getStyleClass().add("gameOverMenuButton");
        mainMenuButton.setOnAction(e -> {
            if (levelTimerLoop != null) {
                levelTimerLoop.stop();
            }//end of if statement
            if (currentGame != null) {
                currentGame.stop();
            }//end of if statement
            currentGame.cleanupActiveDisaster(); //cleanup leftover disasters before going back to main menu
            setCurrentScore(0); //reset the score
            mainMenuScene();
        });
        mainMenuButton.setLayoutX(810);
        mainMenuButton.setLayoutY(850);

        if(!isEndless) wpmText.setY(450);

        //changes the game over screen if the user got a new personal best
        if(currentScore > personalBest){
            highScoreText.setText("NEW PERSONAL BEST!");
            highScoreText.setFill(Color.rgb(211,175,55));//makes the text gold color
            highScoreText.setY(350);
            currentScoreText.setY(450);
        }//end of if statement

        //changes what is displayed depending on the gamemode
        if(!isEndless) root.getChildren().addAll(background, gameOverText, restartButton,mainMenuButton, wpmText, levelText, remainingTime);
        else root.getChildren().addAll(background, gameOverText, restartButton,mainMenuButton, wpmText, currentScoreText, highScoreText);
    }//end of triggerGameOver method

    /**
     * the screen that shows up after the user completes a level
     * @param levelFinished the level that the user completed
     * @param nextLevel the level after the one that the user completed
     */
    public void levelCompleteScene(Runnable levelFinished, Runnable nextLevel){
        UserProfile profile = userManager.getUserProfile(currentUser);

        if (profile != null && currentLevelNumber < 5) {
            if (profile.getUnlockedLevel() < currentLevelNumber + 1) {
                profile.setUnlockedLevel(currentLevelNumber + 1);
                userManager.saveUserProfile(profile);
            }//end of if statement
        }//end of if statement

        currentState = GameState.GAME_OVER;//changes the current state to game over

        userManager.updateTypingStats(currentUser, getWPM(), getAccuracyPercent(), getErrorCount()
                ,getRunTimeSeconds(), getCorrectWords());

        Rectangle background = new Rectangle(1920, 1080);//creates a black background

        Font levelCompleteFont = Font.loadFont(getClass().getResourceAsStream("/PressStart2P-Regular.ttf"), 90);
        Text levelCompleteText = new Text("LEVEL " + currentLevelNumber + " COMPLETE");
        levelCompleteText.setFont(levelCompleteFont);
        levelCompleteText.setFill(Color.WHITE);
        levelCompleteText.setTextAlignment(TextAlignment.CENTER);
        levelCompleteText.setX(0);
        levelCompleteText.setY(200);
        levelCompleteText.setWrappingWidth(1920);

        Font font = Font.loadFont(getClass().getResourceAsStream("/PressStart2P-Regular.ttf"), 40);

        double wpmValue = Math.round(getWPM() * 10.0) / 10.0;
        Text wpmText = new Text("WPM: " + wpmValue);
        wpmText.setFont(font);
        wpmText.setFill(Color.WHITE);
        wpmText.setTextAlignment(TextAlignment.CENTER);
        wpmText.setX(0);
        wpmText.setY(350);
        wpmText.setWrappingWidth(1920);

        Button restartButton = new Button("RESTART");
        restartButton.getStyleClass().add("gameOverMenuButton");
        restartButton.setOnAction(e -> {
            if (levelTimerLoop != null) {
                levelTimerLoop.stop();
            }//end of if statement
            if (currentGame != null) {
                currentGame.stop();
            }//end of if statement
            setCurrentScore(0); //reset the score
            currentGame.cleanupActiveDisaster(); //cleanup any leftover disasters before restarting
            levelFinished.run();
        });
        restartButton.setLayoutX(610);
        restartButton.setLayoutY(850);

        Button mainMenuButton = new Button("MENU");
        mainMenuButton.getStyleClass().add("gameOverMenuButton");
        mainMenuButton.setOnAction(e -> {
            if (levelTimerLoop != null) {
                levelTimerLoop.stop();
            }//end of if statement
            if (currentGame != null) {
                currentGame.stop();
            }//end of if statement
            setCurrentScore(0); //reset the score
            currentGame.cleanupActiveDisaster(); //cleanup leftover disasters before going back to main menu
            mainMenuScene();
        });
        mainMenuButton.setLayoutX(1010);
        mainMenuButton.setLayoutY(850);

        Button nextLevelButton = new Button("NEXT LEVEL");
        nextLevelButton.getStyleClass().add("gameOverRestartButton");
        nextLevelButton.setOnAction(e -> {
            if (levelTimerLoop != null) {
                levelTimerLoop.stop();
            }//end of if statement
            if (currentGame != null) {
                currentGame.stop();
            }//end of if statement
            setCurrentScore(0); //reset the score
            currentGame.cleanupActiveDisaster(); //cleanup leftover disasters before going back to next level
            nextLevel.run();
        });
        nextLevelButton.setLayoutX(810);
        nextLevelButton.setLayoutY(650);

        if (nextLevel != null) {
            root.getChildren().addAll(background, restartButton, mainMenuButton, wpmText, levelCompleteText, nextLevelButton);
        } else {
            root.getChildren().addAll(background, restartButton, mainMenuButton, wpmText, levelCompleteText);
        }//end of if statement
    }//end of levelCompleteScene method

    /**
     * switches to endless or level select on certain binds for power users
     * can only happen in menus except for login/create account
     *
     * @param scene passed scene to switch from
     */
    private void addMenuKeybinds(Scene scene) {
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case DIGIT0:
                    endlessScene(); // start endless
                    break;

                case DIGIT9:
                    selectLevelScene(); // go to level select
                    break;
            }
        });

        // focus the scene
        scene.getRoot().setFocusTraversable(true);
        scene.getRoot().requestFocus();
    }

    /**
     * Adds specific background used for some menus with moving parts
     * @param root anchorpane used to put UI elements of that page on the screen
     */
    public static void addBackground(AnchorPane root){
        Image backgroundImage = new Image(Main.class.getResourceAsStream("/background.png"));
        Image planeImage = new Image(Main.class.getResourceAsStream("/plane.png"));
        ImageView background1 = new ImageView(backgroundImage);
        ImageView background2 = new ImageView(backgroundImage);
        ImageView background3 = new ImageView(backgroundImage);
        ImageView plane = new ImageView(planeImage);

        background1.setFitWidth(IMAGE_WIDTH);
        background1.setFitHeight(IMAGE_HEIGHT);
        background2.setFitWidth(IMAGE_WIDTH);
        background2.setFitHeight(IMAGE_HEIGHT);
        background3.setFitWidth(IMAGE_WIDTH);
        background3.setFitHeight(IMAGE_HEIGHT);

        background1.setLayoutX(START_X);
        background2.setLayoutX(IMAGE_WIDTH+START_X);
        background3.setLayoutX(IMAGE_WIDTH*2+START_X);
        plane.setLayoutX(1050);
        plane.setLayoutY(350);

        //Add cloud backgrounds to the pane and push them to the back
        root.getChildren().addAll(plane, background1, background2, background3);
        background1.toBack();
        background2.toBack();
        background3.toBack();

        //animates the background (got help from Gemini)
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                background1.setLayoutX(background1.getLayoutX() - SCROLL_SPEED);
                background2.setLayoutX(background2.getLayoutX() - SCROLL_SPEED);
                background3.setLayoutX(background3.getLayoutX() - SCROLL_SPEED);

                if (background1.getLayoutX() <= START_X-IMAGE_WIDTH) {
                    background1.setLayoutX(background3.getLayoutX() + IMAGE_WIDTH);
                }//end of if statement
                if (background2.getLayoutX() <= START_X-IMAGE_WIDTH) {
                    background2.setLayoutX(background1.getLayoutX() + IMAGE_WIDTH);
                }//end of if statement
                if (background3.getLayoutX() <= START_X-IMAGE_WIDTH) {
                    background3.setLayoutX(background2.getLayoutX() + IMAGE_WIDTH);
                }//end of if statement
            }//end of if statement
        };
        timer.start();
    }//end of addBackground method


    /**
     * Loads in teh words to be put into the clouds from the words.txt file
      */
    private void loadWordBank() {
        try {
            InputStream is = getClass().getResourceAsStream("/words.txt");
            if (is == null) {
                System.out.println("WARNING: words.txt not found! Loading default words.");
                wordBank.addAll(Arrays.asList("flight", "pilot", "cloud", "wings", "speed"));
                return;
            }//end of if statement
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    wordBank.add(line.trim().toLowerCase());
                }//end of if statement
            }//end of while loop
            System.out.println("Successfully loaded " + wordBank.size() + " words into the bank!");

        } catch (Exception e) {
            System.out.println("Error reading words.txt: " + e.getMessage());
        }//end of try catch
    }//end of loadWordBank method

    /**
     * saves current user profile
     * @param p is the profile being saved
     */
    public void saveCurrentProfile(UserProfile p) {
        userManager.saveUserProfile(p);
    } // end of saveCurrentProfile(UserProfile p)

    /**
     * increments the current score
     * @param points the amount of points to increment the score by
     */
    public void addScore(int points) {
        this.currentScore += points;
        if (isEndless && endlessScoreUI != null) {
            endlessScoreUI.setText("SCORE: " + currentScore);
        }//end of if statement
    }

    /**
     * increments the number fo correct words typed
     */
    public void addCorrectWord() {
        this.correctWords++;
    }

    /**
     * Gets the current user profile so it can be used to see users powerups
     * @return the current user profile
     */
    public UserProfile getCurrentUserProfile() {
        return userManager.getUserProfile(currentUser);
    } // end of getCurrentUserProfile()

    /**
     * increments the run time by the specified number of seconds.
     * @param seconds the number of seconds to add to the total run time
     */
    public void addRunTime(double seconds) {
        runTimeSeconds += seconds;
    }

    /**
     * Updates the WPM UI
     */
    public void updateWPMUI() {
        if (wpmUI != null) {
            double wpmValue = Math.round(getWPM() * 10.0) / 10.0;
            wpmUI.setText("WPM: " + wpmValue);
        }//end of if statement
    }

    /**
     * Updates the total count of typed characters
     * @param correct a boolean indicating whether the typed character was correct
     */
    public void addTypedChar(boolean correct) {
        totalTypedChars++;
        if (correct) {
            correctTypedChars++;
        } else {
            errorCount++;
        }//end of if statement
    }

    /**
     * Resets the current score
     */
    public void resetScore(){
        this.currentScore = 0;
    }


    //------------GETTERS AND SETTERS--------------------

    public GameState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(GameState state) {
        this.currentState = state;
    }

    public Cloud getTargetCloud() {
        return targetCloud;
    }

    public void setTargetCloud(Cloud targetCloud) {
        this.targetCloud = targetCloud;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String username) {
        this.currentUser = username;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public void setCurrentScore(int score) {currentScore = score;}

    public Runnable getCurrentLevel() {
        return currentLevel;
    }

    public boolean getIsEndless(){
        return isEndless;
    }

    public int getCurrentLevelNumber() {
        return currentLevelNumber;
    }

    public double getRunTimeSeconds() {
        return runTimeSeconds;
    }

    public double getWPM() {
        if (runTimeSeconds <= 0) return 0.0;
        return correctWords / (runTimeSeconds / 60.0);
    }

    public int getCorrectWords() {
        return correctWords;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public double getAccuracyPercent() {
        if (totalTypedChars == 0) return 100.0;
        return (correctTypedChars * 100.0) / totalTypedChars;
    }

    public Player getPlayer() {
        return player;
    }

}//end of class