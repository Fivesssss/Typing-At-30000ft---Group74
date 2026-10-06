package org.example.airplanegame.disasters;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.example.airplanegame.Main;

import java.util.Random;

/**
 * Snow disaster implementation.
 * <p>
 * This disaster freezes the user control until they press the screen x times,
 * there is a visual and gameplay impact
 * </p>
 *
 * @see DisasterInterface
 * @author Aseem Lakhanpal
 */
public class SnowDisaster implements DisasterInterface {
    /** Indicates whether this disaster should trigger an effect. */
    private final boolean hasEffect;

    /** Duration of the snow disaster in seconds. */
    private float duration;

    /** Reference to the main application instance. */
    private Main mainApp;

    /** Tracks whether the snow disaster is currently active. */
    private boolean active = false;

    /** Prevents the effect from being initialized more than once per activation. */
    private boolean initialized = false;

    /** Number of clicks the player has made during the current snow event. */
    private int clickCounter;

    /** Number of clicks required to break the ice. */
    private int clicksNeeded;

    /** Container for the snow UI elements. */
    private StackPane overlayPane;

    /** Instruction text shown to the player. */
    private Text instructionText;

    /** Progress text showing the current click count. */
    private Text counterText;

    /** Ice overlay displayed over the plane. */
    private Rectangle iceCube;

    /** Scene reference used for cleanup and event handling removal. */
    private Scene activeScene;

    /** Mouse click handler used to count player clicks. */
    private EventHandler<MouseEvent> clickHandler;

    /** First snow animation image. */
    private ImageView snow1;

    /** Second snow animation image. */
    private ImageView snow2;

    /** Speed at which the snow animation scrolls downward. */
    private final double SNOW_SPEED = 10.0;

    /**
     * Creates a new snow disaster instance.
     *
     * @param duration the duration of the disaster in seconds
     * @param mainApp the main application instance
     */
    SnowDisaster(float duration, Main mainApp) {
        this.hasEffect = true;
        this.duration = duration;
        this.mainApp = mainApp;
        this.clickCounter = 0;
        this.clicksNeeded = 0;
    }

    /**
     * Creates and returns the text node used to display the disaster name.
     *
     * @return the snow disaster label node
     */
    @Override
    public Node spawnDisaster() {
        // Simple title text used by the game to show the disaster has started
        Font font = Font.loadFont(getClass().getResourceAsStream("/PressStart2P-Regular.ttf"), 28);
        Text text = new Text("SNOW DISASTER");
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
     * Applies the snow effect to the current scene and updates its gameplay logic.
     *
     * @param scene the game scene currently being played
     */
    @Override
    public void disasterEffect(Scene scene) {
        if (scene == null || mainApp == null) return;

        //sets disaster to active if not already
        if (!active) {
            active = true;
            initialized = false;
            clickCounter = 0;
            clicksNeeded = new Random().nextInt(1, 11); // 1-10 inclusive
        }

        //initializes the snow disaster
        if (!initialized) {
            initialized = true;
            activeScene = scene;

            //snow freezes gameplay, but clouds still keep moving
            mainApp.freezeGame();

            //ui text showing target click count and current click count
            Pane root = (AnchorPane) scene.getRoot();

            Node plane = mainApp.getPlayer().getView();
            iceCube = new Rectangle();
            // Make it slightly larger than the plane
            iceCube.setX(35);
            iceCube.setY(plane.getBoundsInParent().getMinY() - 15);
            iceCube.setWidth(150);
            iceCube.setHeight(120);

            // make it look like a block of ice
            iceCube.setFill(Color.rgb(173, 216, 230, 0.7)); //transparent blue
            iceCube.setStroke(Color.WHITE);
            iceCube.setStrokeWidth(3);
            iceCube.setArcWidth(20);//round corners
            iceCube.setArcHeight(20);//round corners
            iceCube.setMouseTransparent(true);

            root.getChildren().add(iceCube);//adds the ice cube to the screen

            instructionText = new Text("SNOW DISASTER: click " + clicksNeeded + " times to break the ice");
            instructionText.setFont(Font.font("Arial", FontWeight.BOLD, 24));
            instructionText.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
            instructionText.setWrappingWidth(800);
            instructionText.setFill(Color.RED);

            counterText = new Text("Clicks: 0 / " + clicksNeeded);
            counterText.setFont(Font.font("Arial", FontWeight.BOLD, 24));
            counterText.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
            counterText.setWrappingWidth(800);
            counterText.setFill(Color.RED);

            //puts both texts into a vertical stack and center them
            overlayPane = new StackPane();
            overlayPane.setPrefSize(scene.getWidth(), scene.getHeight());
            overlayPane.getChildren().addAll(instructionText, counterText);
            StackPane.setAlignment(instructionText, Pos.CENTER);
            StackPane.setAlignment(counterText, Pos.CENTER);
            counterText.setTranslateY(40);

            root.getChildren().add(overlayPane);

            //count clicks anywhere on the scene
            clickHandler = e -> {
                if (!active) return;

                clickCounter++;
                counterText.setText("Clicks: " + clickCounter + " / " + clicksNeeded);

                //removes the ice from the plane as the user clicks
                double remainingOpacity = 0.7 * (1.0 - ((double) clickCounter / clicksNeeded));
                iceCube.setOpacity(Math.max(0, remainingOpacity));

                //end snow disaster once the required clicks are reached
                if (clickCounter >= clicksNeeded) {
                    disableDisasterEffect();
                }//end of if statement
            };//end of action event

            //load the snow image
            Image snowImage = new Image(getClass().getResourceAsStream("/snow.png"));

            //set up the first image
            snow1 = new ImageView(snowImage);
            snow1.setMouseTransparent(true);//so that the image doesn't block mouse clicks
            snow1.setLayoutX(0);
            snow1.setLayoutY(0);

            //set up the second image
            snow2 = new ImageView(snowImage);
            snow2.setMouseTransparent(true);//so that the image doesn't block mouse clicks
            snow2.setLayoutX(0);
            snow2.setLayoutY(-1080);

            //adds them to the screen
            root.getChildren().addAll(snow1, snow2);

            //push the snow behind every thing so that it doesn't cover the UI
            snow1.toBack();
            snow2.toBack();

            scene.addEventFilter(MouseEvent.MOUSE_PRESSED, clickHandler);
        }//end of if statement

        //moves the snow down
        snow1.setLayoutY(snow1.getLayoutY() + SNOW_SPEED);
        snow2.setLayoutY(snow2.getLayoutY() + SNOW_SPEED);

        //if an image falls below the screen reset it back to the top
        if (snow1.getLayoutY() >= 1080) {
            snow1.setLayoutY(snow2.getLayoutY() - 1080);
        }//end of if statement
        if (snow2.getLayoutY() >= 1080) {
            snow2.setLayoutY(snow1.getLayoutY() - 1080);
        }//end of if statement
    }//end of disasterEffect method

    /**
     * Cleans up the snow disaster and restores normal gameplay.
     */
    @Override
    public void disableDisasterEffect() {
        if (!active) return;

        active = false;
        initialized = false;

        //remove mouse handler so future gameplay works normally
        activeScene.removeEventFilter(MouseEvent.MOUSE_PRESSED, clickHandler);

        //restore normal gameplay state
        mainApp.unfreezeGame();
        mainApp.endDisaster();

        //remove the snow overlay from the scene
        AnchorPane root = (AnchorPane) activeScene.getRoot();
        root.getChildren().remove(overlayPane);

        //removes both snow images from the screen when the disaster ends
        snow1.setOpacity(0);
        snow2.setOpacity(0);
        snow1 = null;
        snow2 = null;

        //removes the ice cube from the screen when the disaster ends
        iceCube.setOpacity(0);
        iceCube = null;

        //clear all references so the next snow disaster works correctly
        overlayPane = null;
        instructionText = null;
        counterText = null;
        activeScene = null;
        clickHandler = null;
        clickCounter = 0;
        clicksNeeded = 0;
    }//end of disableDisasterEffect
}