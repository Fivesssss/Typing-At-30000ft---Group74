package org.example.airplanegame.objects;

import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Contains the creation of the clouds ui and initializes the cloud object
 * @author Mitchell
 */
public class Cloud extends GameObject {

    private String targetWord;

    /**
     * Creates a new cloud object with the cloud image, a target word, a starting position,
     * and a horizontal speed.
     */
    public Cloud(ImageView cloudImage, String word, double startX, double startY, double speed) {
        super(new StackPane(), startX, startY);
        this.targetWord = word;

        //create the text
        Text wordText = new Text(word);
        wordText.setFont(new Font("Arial", 20)); //make it readable

        //grab the StackPane we passed to the parent and add our visuals
        StackPane view = (StackPane) this.getView();
        view.getChildren().addAll(cloudImage, wordText);

        //set the cloud flying to the left
        this.setVelocity(speed, 0);
    }

    /**
     * Returns the word attached to this cloud.
     * @return the cloud's target word
     */
    public String getTargetWord() {
        return targetWord;
    }
}