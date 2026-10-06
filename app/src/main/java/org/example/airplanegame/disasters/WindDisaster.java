package org.example.airplanegame.disasters;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.example.airplanegame.Main;
import org.example.airplanegame.core.Game;
import org.example.airplanegame.objects.Cloud;

/**
 * Represents a wind disaster in the game.
 * <p>
 * This disaster applies a wind-based effect to the game, there is a visual effect to the game
 * where the clouds move in a sinusoidal pattern. The impact on the gameplay is caused by the
 * clouds moving in a sinusoidal pattern and not in the predictable horizontal direction.
 * </p>
 *
 * @see DisasterInterface
 * @author Aseem Lakhanpal
 */
public class WindDisaster implements DisasterInterface {
    /** Indicates whether this disaster should trigger an effect. */
    private final boolean hasEffect;

    /** Duration of the wind disaster in seconds. */
    private float duration;

    /** Reference to the main application instance. */
    private Main mainApp;

    /** Reference to the active game instance used to access clouds. */
    private Game game;

    /** Internal timer used to animate wind variation over time. */
    private double timer;

    /**
     * Creates a new wind disaster instance.
     *
     * @param duration the duration of the disaster in seconds
     * @param mainApp the main application instance
     * @param game the active game instance
     */
    WindDisaster(float duration, Main mainApp, Game game)
    {
        this.hasEffect = true;
        this.duration = duration;
        this.mainApp = mainApp;
        this.game = game;
        this.timer = 0d;
    }

    /**
     * Creates and returns the text node used to display the disaster name.
     *
     * @return the wind disaster label node
     */
    @Override
    public Node spawnDisaster()
    {
        Font font = Font.loadFont(getClass().getResourceAsStream("/PressStart2P-Regular.ttf"), 28);
        Text text = new Text();
        text.setText("WIND DISASTER");
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
    public void setDuration(float newDuration) {
        this.duration = newDuration;
    }

    /**
     * Returns the disaster duration.
     *
     * @return the disaster duration in seconds
     */
    public float getDuration() {
        return this.duration;
    }

    /**
     * Returns whether this disaster has an effect.
     *
     * @return {@code true} if the disaster has an effect; otherwise {@code false}
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
     * Applies the wind effect by adjusting the Y velocity of active clouds.
     *
     * @param scene the game scene currently being played
     */
    @Override
    public void disasterEffect(Scene scene){

        for(Cloud cloud : this.game.getActiveClouds()){
            double vX = cloud.getVelocityX();
            double vY = Math.sin(this.timer) * vX;
            cloud.setVelocity(vX, vY);
        }
        this.timer += 0.1d;
    }

    /**
     * Disables the wind effect and restores cloud vertical velocity to zero.
     */
    @Override
    public void disableDisasterEffect(){
        for(Cloud cloud : this.game.getActiveClouds()){
            double vX = cloud.getVelocityX();
            double vY = 0;
            cloud.setVelocity(vX, vY);
        }
        this.timer = 0d;
    }
}
