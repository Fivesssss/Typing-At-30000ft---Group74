package org.example.airplanegame.core;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.example.airplanegame.objects.Cloud;
import org.example.airplanegame.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Level {
    private boolean endless;
    private int difficulty;
    private Player player;
    private String disasterID;
    private int currentLevelNumber = 1;
    private double baseCloudSpeed = -3.0;
    private double baseTypingTime = 5.0;
    private Level currentLevel;

    //references passed in from the Game class to allow spawning on the screen
    private Pane root;
    private ArrayList<Cloud> activeClouds;
    private List<String> wordBank;
    private Random random = new Random();


    /**
     * Creates a new level and stores the game data needed for cloud spawning
     * and level progression.
     * @author Everyone
     */
    public Level(boolean endless, int difficulty, Player player, String disasterID, Pane root, ArrayList<Cloud> activeClouds, List<String> wordBank) {
        this.endless = endless;
        this.difficulty = difficulty;
        this.currentLevelNumber = difficulty;
        this.player = player;
        this.disasterID = disasterID;
        this.root = root;
        this.activeClouds = activeClouds;
        this.wordBank = wordBank;
        this.currentLevel = this;
    }

    /**
     * Increases the current level number and prints a message to the console.
     */
    public void levelUp() {
        currentLevelNumber++;
        System.out.println("Level Up! Welcome to Level " + currentLevelNumber);
    }

    /**
     * Spawns a new cloud with a random word from the word bank and adds it
     * to the game scene and cloud list.
     */
    public void spawnCloud() {
        if (wordBank == null || wordBank.isEmpty()) return;

        String word = wordBank.get(random.nextInt(wordBank.size()));

        ImageView cloudView = new ImageView(new Image(getClass().getResourceAsStream("/cloud.png")));
        cloudView.setFitWidth(100);
        cloudView.setPreserveRatio(true);

        double randomY = 50 + random.nextInt(850);
        Cloud newCloud = new Cloud(cloudView, word, 1950, randomY, getCloudSpeed());

        activeClouds.add(newCloud);
        root.getChildren().add(newCloud.getView());
        newCloud.getView().toBack();
    }

    //----GETTERS AND SETTERS-----
    /**
     * Sets the current level.
     */
    public void setLevel(Level level) {
        this.currentLevel = level;
    }

    /**
     * Gets the current level.
     * @return the current level.
     */
    public Level getLevel() {
        return this.currentLevel;
    }

    /**
     * Calculates the current cloud speed based on level number, and slows the speed
     * down if slowmo powerup is active.
     * @return the speed for the newly spawned cloud
     */
    public double getCloudSpeed() {
        double speed = baseCloudSpeed - currentLevelNumber; //this has to be - not + b/c clouds are coming from right to left

        if(((Game) root.getScene().getUserData()).isSlowmoActive()) {
            speed *= 0.5;
        }
        return speed;
    }

    /**
     * Calculates how much time the player gets for typing a cloud's word,
     * typing timer decreaes as the level increases.
     * Decreases time by 0.5s per level, bottoming out at 2.0s
     * @return the typing time in seconds for the current level
     */

    public double getTypingTime() {
        return Math.max(2.0, baseTypingTime - (currentLevelNumber - 1) * 0.5);
    }

    /**
     * setter for currentLevelNumber
     * @param currentLevelNumber the number you want to set it to
     */
    public void setCurrentLevelNumber(int currentLevelNumber){
        this.currentLevelNumber = currentLevelNumber;
    }//end of setCurrentLevelNumber method
}//end of class