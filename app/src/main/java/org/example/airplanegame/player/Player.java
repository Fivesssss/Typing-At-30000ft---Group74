package org.example.airplanegame.player;


import javafx.scene.image.ImageView;
import org.example.airplanegame.objects.GameObject;

/**
 * Create a new player object
 * @author Mitchell
 */
public class Player extends GameObject {

    /**
     * Creates a new player object using the given image and starting position.
     * @param view the player image used
     * @param startX the starting x-coordinate
     * @param startY the starting y-coordinate
     */
    public Player(ImageView view, double startX, double startY) {
        super(view, startX, startY);
    }
}