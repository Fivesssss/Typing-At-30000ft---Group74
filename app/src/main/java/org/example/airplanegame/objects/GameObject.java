package org.example.airplanegame.objects;

import javafx.scene.Node;

/**
 * Creation of a new game object and ui intitialization for the objects
 * @author Mitchell
 */
public class GameObject {

    //visual representation of object
    private Node view;

    //physics properties
    private double velocityX;
    private double velocityY;

    //state management
    private boolean isAlive = true;

    /**
     * Creates a new game object with a visual node and starting position.
     * @param view the JavaFX node used to represent this object
     * @param startX the starting x position
     * @param startY the starting y position
     */
    public GameObject(Node view, double startX, double startY) {
        this.view = view;
        this.view.setTranslateX(startX);
        this.view.setTranslateY(startY);
    }

    /**
     * Updates the object's position based on its current velocity.
     */
    public void update() {
        view.setTranslateX(view.getTranslateX() + velocityX);
        view.setTranslateY(view.getTranslateY() + velocityY);
    }



    /**
     * Returns the visual node for this game object.
     * @return the node used to display the object
     */
    public Node getView() { return view; }

    /**
     * Sets the object's x and y velocity.
     * @param velocityX the velocity on the x-axis
     * @param velocityY the velocity on the y-axis
     */
    public void setVelocity(double velocityX, double velocityY) {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    /**
     * Returns the current x velocity.
     * @return the x velocity
     */
    public double getVelocityX(){
        return this.velocityX;
    }

    /**
     * Returns the current y velocity.
     * @return the y velocity
     */
    public double getVelocityY(){
        return this.velocityY;
    }

    /**
     * Checks whether the object is still alive.
     * @return true if the object is alive, false otherwise
     */
    public boolean isAlive() {
        return isAlive;
    }

    /**
     * Set the object as dead so it can be removed from the game.
     */
    public void setDead() {
        this.isAlive = false;
    }
}