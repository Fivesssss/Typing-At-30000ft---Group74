package org.example.airplanegame.disasters;

/*use import org.example.airplanegame.disasters.* */

import javafx.scene.Node;
import javafx.scene.Scene;
import org.example.airplanegame.Main;

/**
 * Defines the common behavior for all disasters in the game.
 */
public interface DisasterInterface {

    /**
     * Creates the visual node used to represent the disaster in the UI.
     *
     * @return a JavaFX node for the disaster
     */
    public Node spawnDisaster();

    /**
     * Sets the duration of the disaster.
     *
     * @param newDuration the new disaster duration
     */
    public void setDuration(float newDuration);

    /**
     * Gets the duration of the disaster.
     *
     * @return the current duration
     */
    public float getDuration();

    /**
     * Indicates whether this disaster has an in-game effect.
     *
     * @return {@code true} if the disaster affects gameplay, otherwise {@code false}
     */
    public boolean getHasEffect();

    /**
     * Returns the main application instance associated with the disaster.
     *
     * @return the main application instance
     *
     * @author Aseem Lakhanpal
     */
    public Main getMainApp();

    /**
     * Updates the main application instance associated with the disaster.
     *
     * @param newMainApp the new main application instance
     */
    public void setMainApp(Main newMainApp);

    /**
     * Applies the disaster's active effect to the provided scene.
     * <p>
     * This method should be called once per frame while the disaster is active.
     * </p>
     *
     * @param scene the current game scene
     */
    public void disasterEffect(Scene scene);

    /**
     * Disables the disaster's active effect and restores normal gameplay state.
     */
    public void disableDisasterEffect();
}
