package org.example.airplanegame.playerControllers;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.example.airplanegame.player.Player;

/**
 * Responsible for controlling the player's movement based on keyboard input
 * @author Mitchell, Aseem, Samer
 */
public class PlayerMovement {
    private Player player;
    private boolean isUpPressed = false;
    private boolean isDownPressed = false;
    private final int SPEED = 5;

    /**
     * Creates a new movement controller for the player and sets up keyboard controls.
     * @param player the player to move
     * @param scene the scene used to listen for key input
     */
    public PlayerMovement(Player player, Scene scene) {
        this.player = player;
        setupControls(scene);
    }

    //responsible for the players movement (arrow keys)
    /**
     * Sets up key listeners that track when the up and down arrow keys are pressed
     * or released.
     * @param scene the scene to attach the keyboard controls to
     */
    private void setupControls(Scene scene) {

        //toggle movement flags when keys are pressed
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.UP) {
                    isUpPressed = true;
                } else if (event.getCode() == KeyCode.DOWN) {
                    isDownPressed = true;
                }
            }
        });

        //toggle movement flags to stop movement when keys are released
        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.UP) {
                    isUpPressed = false;
                } else if (event.getCode() == KeyCode.DOWN) {
                    isDownPressed = false;
                }
            }
        });
    }

    /**
     * Updates the player's position based on the currently pressed movement keys.
     * Also keeps the player within the vertical bounds of the screen.
     */
    public void update() {
        //variables to prevent out of bounds
        double currentY = player.getView().getTranslateY();
        double minY = 0;
        double maxY = 1080 - player.getView().getBoundsInParent().getHeight();

        //allows movement if Y value is within bounds,
        //if not sets velocity to zero so they cant move off the screen
        if (isUpPressed && currentY > minY) {
            player.setVelocity(0, -SPEED);
        } else if (isDownPressed && currentY < maxY) {
            player.setVelocity(0, SPEED);
        } else {
            player.setVelocity(0, 0);
        }

        player.update();
    }
}
