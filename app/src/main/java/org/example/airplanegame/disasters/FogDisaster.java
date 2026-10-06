package org.example.airplanegame.disasters;

import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.example.airplanegame.Main;

/**
 * Fog disaster implementation.
 * <p>
 * This disaster currently provides only a UI and no gameplay effect.
 * </p>
 *
 * @see DisasterInterface
 * @author Aseem Lakhanpal
 */
public class FogDisaster implements DisasterInterface {

    /** Indicates whether this disaster should trigger a visual effect. */
    private final boolean hasEffect;

    /** Duration of the fog disaster in seconds. */
    private float duration;

    /** Reference to the main application instance. */
    private Main mainApp;

    /** Tracks whether the fog overlay has already been created. */
    private boolean initialized = false;

    /** The overlay rectangle used to display the fog effect. */
    private Rectangle fogOverlay;

    /**
     * Creates a new fog disaster instance.
     *
     * @param duration the duration of the disaster in seconds
     * @param mainApp the main application instance
     */
    public FogDisaster(float duration, Main mainApp) {
        this.duration = duration;
        this.mainApp = mainApp;
        //set to true so Game.java calls disasterEffect() in its loop
        this.hasEffect = true;
    }

    /**
     * Creates and returns the text node used to display the disaster name.
     *
     * @return the fog disaster label node
     */
    @Override
    public Node spawnDisaster() {
        //this returns the text that Game.java will automatically add/remove
        Font font = Font.loadFont(getClass().getResourceAsStream("/PressStart2P-Regular.ttf"), 28);
        Text text = new Text("FOG DISASTER");
        text.setX(40);
        text.setY(1000);
        text.setFont(font);
        text.setFill(Color.RED);

        return text;
    }

    /**
     * Sets the disaster duration.
     *
     * @param newDuration the new duration in seconds
     */
    @Override
    public void setDuration(float newDuration) {
        this.duration = newDuration;
    }

    /**
     * Returns the disaster duration.
     *
     * @return the disaster duration in seconds
     */
    @Override
    public float getDuration() {
        return duration;
    }

    /**
     * Returns whether this disaster has a visual effect.
     *
     * @return {@code true} if the disaster has a visual effect; otherwise {@code false}
     */
    @Override
    public boolean getHasEffect() {
        return this.hasEffect;
    }

    /**
     * Returns the main application instance.
     *
     * @return the main application instance
     */
    @Override
    public Main getMainApp() {
        return this.mainApp;
    }

    /**
     * Updates the main application reference.
     *
     * @param newMainApp the new main application instance
     */
    @Override
    public void setMainApp(Main newMainApp) {
        this.mainApp = newMainApp;
    }

    /**
     * puts the fog effect on the screen
     *
     * @param scene the scene of the game being played
     */
    @Override
    public void disasterEffect(Scene scene) {
        //trigger the fade-in once when the disaster starts
        if (!initialized && scene != null) {
            initialized = true;

            AnchorPane root = (AnchorPane) scene.getRoot();
            if (root != null) {
                //create the fog overlay
                fogOverlay = new Rectangle(1920, 1080);

                fogOverlay.setFill(Color.rgb(200, 200, 200));//make the fog gray
                fogOverlay.setOpacity(0.0); //make the rectangle transparent

                fogOverlay.setMouseTransparent(true); //so that the user can still press the pause button

                //add to the screen
                root.getChildren().add(fogOverlay);

                //the fog fades in
                FadeTransition fadeIn = new FadeTransition(Duration.seconds(duration/2), fogOverlay);
                fadeIn.setToValue(0.80);
                fadeIn.play();
            }//end of if statement
        }//end of if statement
    }//end of disasterEffect method

    /**
     * removes the fog effect from the screen
     */
    @Override
    public void disableDisasterEffect() {
        initialized = false;

        if (fogOverlay == null) {
            return;
        }

        FadeTransition fadeOut = new FadeTransition(Duration.seconds(duration / 2), fogOverlay);

        //removes the fog overlay after the disaster is finished,
        //fixed bug where fog overlay would apear on the game over screen
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(event -> {
            if (fogOverlay != null && fogOverlay.getParent() instanceof AnchorPane root) {
                root.getChildren().remove(fogOverlay);
            }
            fogOverlay = null;
        });

        fadeOut.play();

    }//end of disableDisasterEffect method
}//end of class