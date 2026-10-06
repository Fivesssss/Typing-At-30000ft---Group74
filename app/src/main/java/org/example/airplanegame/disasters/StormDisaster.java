package org.example.airplanegame.disasters;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.example.airplanegame.Main;

/**
 * Storm disaster implementation.
 * <p>
 * This disaster currently provides only a UI and no gameplay effect.
 * </p>
 *
 * @see DisasterInterface
 * @author Aseem Lakhanpal
 */
public class StormDisaster implements DisasterInterface {
    /** Indicates whether this disaster should trigger a visual effect. */
    private final boolean hasEffect;

    /** Duration of the storm disaster in seconds. */
    private float duration;

    /** Reference to the main application instance. */
    private Main mainApp;

    /** Tracks whether the rain effect has already been initialized. */
    private boolean initialized = false;

    /** First rain animation layer. */
    private ImageView rain1;

    /** Second rain animation layer. */
    private ImageView rain2;

    /** Speed at which the rain animation scrolls downward. */
    private final double RAIN_SPEED = 20.0;

    /**
     * Creates a new storm disaster instance.
     *
     * @param duration the duration of the disaster in seconds
     * @param mainApp the main application instance
     */
    StormDisaster(float duration, Main mainApp) {
        this.duration = duration;
        this.mainApp = mainApp;
        this.hasEffect = true;
    }//end of Storm Disaster method

    /**
     * Creates and returns the text node used to display the disaster name.
     *
     * @return the storm disaster label node
     */
    @Override
    public Node spawnDisaster() {
        Font font = Font.loadFont(getClass().getResourceAsStream("/PressStart2P-Regular.ttf"), 28);
        Text text = new Text();
        text.setText("STORM DISASTER");
        text.setX(40);
        text.setY(1000);
        text.setFont(font);
        text.setFill(Color.RED);

        return text;
    }//end of spawnDisaster method

    /**
     * Sets the disaster duration.
     *
     * @param newDuration the new duration in seconds
     */
    @Override
    public void setDuration(float newDuration) {
        this.duration = newDuration;
    }//end of setDuration method

    /**
     * Returns the disaster duration.
     *
     * @return the disaster duration in seconds
     */
    @Override
    public float getDuration() {
        return this.duration;
    }//end of get duration method

    /**
     * Returns whether this disaster has a visual effect.
     *
     * @return {@code true} if the disaster has a visual effect; otherwise {@code false}
     */
    @Override
    public boolean getHasEffect() {
        return this.hasEffect;
    }//end of getHasEffect method

    /**
     * Returns the main application instance.
     *
     * @return the main application instance
     */
    @Override
    public Main getMainApp() {
        return this.mainApp;
    }//end of getMainApp method

    /**
     * Updates the main application reference.
     *
     * @param newMainApp the new main application instance
     */
    @Override
    public void setMainApp(Main newMainApp) {
        this.mainApp = newMainApp;
    }//end of setMainApp method

    /**
     * Spawns the rain effect and animates it so that it falls from the top of the screen.
     *
     * @param scene the scene of the game currently being played
     */
    @Override
    public void disasterEffect(Scene scene) {
        AnchorPane root = (AnchorPane) scene.getRoot();

        //only runs once when the disaster first starts
        if (!initialized) {
            initialized = true;

            //load the rain image
            Image rainImage = new Image(getClass().getResourceAsStream("/rain.png"));

            //set up the first image
            rain1 = new ImageView(rainImage);
            rain1.setMouseTransparent(true);//so that the image doesn't block mouse clicks
            rain1.setLayoutX(0);
            rain1.setLayoutY(0);

            //set up the second image
            rain2 = new ImageView(rainImage);
            rain2.setMouseTransparent(true);//so that the image doesn't block mouse clicks
            rain2.setLayoutX(0);
            rain2.setLayoutY(-1080);

            //adds them to the screen
            root.getChildren().addAll(rain1, rain2);

            //push the rain behind every thing so that it doesn't cover the UI
            rain1.toBack();
            rain2.toBack();
        }//end of if statement

        //moves the rain down
        rain1.setLayoutY(rain1.getLayoutY() + RAIN_SPEED);
        rain2.setLayoutY(rain2.getLayoutY() + RAIN_SPEED);

        //if an image falls below the screen reset it back to the top
        if (rain1.getLayoutY() >= 1080) {
            rain1.setLayoutY(rain2.getLayoutY() - 1080);
        }//end of if statement
        if (rain2.getLayoutY() >= 1080) {
            rain2.setLayoutY(rain1.getLayoutY() - 1080);
        }//end of if statement

    }//end of disasterEffect method

    /**
     * Removes the rain from the screen.
     */
    @Override
    public void disableDisasterEffect() {
        initialized = false;

        //removes both rain images from the screen when the disaster ends
        rain1.setOpacity(0);
        rain2.setOpacity(0);
        rain1 = null;
        rain2 = null;
    }//end of disableDisasterEffect method
}
