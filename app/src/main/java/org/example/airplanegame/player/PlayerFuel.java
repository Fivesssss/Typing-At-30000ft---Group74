package org.example.airplanegame.player;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * Responsible for managing the player's fuel and fuel bar UI
 * @author Everyone
 */
public class PlayerFuel {
    //fuel values
    private final double MAX_FUEL = 100.0;
    private double currentFuel = MAX_FUEL;
    private final double FUEL_DECREASE_RATE = 3.0; //fuel lost per second while flying

    //visual UI Elements
    private Rectangle fuelBarBackground;
    private Rectangle fuelBar;
    private final double MAX_BAR_WIDTH = 800.0;

    //timer state
    private double currentTypingTimeLeft = 0;


    /**
     * Creates the fuel bar UI elements and initializes the player's fuel values.
     */
    public PlayerFuel() {
        //dark gray background bar
        fuelBarBackground = new Rectangle(560, 1020, MAX_BAR_WIDTH, 30);
        fuelBarBackground.setFill(Color.DARKGRAY);
        fuelBarBackground.setStroke(Color.BLACK);
        fuelBarBackground.setStrokeWidth(2);

        //active green fuel bar
        fuelBar = new Rectangle(560, 1020, MAX_BAR_WIDTH, 30);
        fuelBar.setFill(Color.GREEN);
    }

//FUEL STUFF
    //constantly drains fuel by the FUEL_DECREASE_RATE variable

    /**
     * Constantly reduces the fuel over time.
     * Drains fuel by the FUEL_DECREASE_RATE variable
     * @param deltaTimeSeconds the time passed since the last frame
     */
    public void drainPassiveFuel(double deltaTimeSeconds) {
        currentFuel -= (FUEL_DECREASE_RATE * deltaTimeSeconds);
        updateUI();
    }

    /**
     * Adds fuel to the player and caps it at the maximum fuel value.
     * @param amount the amount of fuel to add
     */
    public void addFuel(double amount) {
        currentFuel += amount;
        if (currentFuel > MAX_FUEL) currentFuel = MAX_FUEL;
        updateUI();
    }

    /**
     * Removes fuel from the player.
     * @param amount the amount of fuel to remove
     */
    public void removeFuel(double amount) {
        currentFuel -= amount;
        updateUI();
    }

//TIMER STUFF
    /**
     * Starts the timer used during the typing phase.
     * @param timeLimit the amount of time the player has to type
     */
    public void startTypingTimer(double timeLimit) {
        currentTypingTimeLeft = timeLimit;
    }

    /**
     * Updates the typing timer and refreshes the timer text on screen.
     * @param deltaTimeSeconds the time passed since the last frame
     * @param timerUI the text element used to display the timer
     * @return true if the timer reached zero, false otherwise
     */
    public boolean updateTypingTimer(double deltaTimeSeconds, Text timerUI) {
        currentTypingTimeLeft -= deltaTimeSeconds;

        //time runs out
        if (currentTypingTimeLeft <= 0) {
            currentTypingTimeLeft = 0;
            timerUI.setText(String.format("Time: %.1f", currentTypingTimeLeft));
            return true; //signal to the game loop that the timer expired
        }

        //show timer on screen
        timerUI.setText(String.format("Time: %.1f", currentTypingTimeLeft));
        return false;
    }

//-----HELPER METHODS-----

    /**
     * Updates the fuel bar width and color to match the current fuel amount.
     */
    private void updateUI() {
        //prevent negative visual widths
        double displayFuel = Math.max(currentFuel, 0);
        //calculate the percentage of fuel left and scale the bar width
        double width = (displayFuel / MAX_FUEL) * MAX_BAR_WIDTH;
        fuelBar.setWidth(width);

        //changes the color of the fuel bar gradually as the fuel goes down
        double fuelPercentage = displayFuel/MAX_FUEL;//the percentage of the fuel between 0 and 1
        double hue = fuelPercentage * 120;//calculates the hue for the fuel bar color
        fuelBar.setFill(Color.hsb(hue,.85,.90));
    }

    /**
     * Checks whether the player's fuel has run out.
     * If so the game has ended
     * @return true if fuel is empty or below zero, false otherwise
     */
    public boolean isGameOver() {
        return currentFuel <= 0;
    }

    /**
     * Returns the background bar used for the fuel UI.
     * @return the fuel bar background rectangle
     */
    public Rectangle getFuelBarBackground() { return fuelBarBackground; }

    /**
     * Returns the current fuel bar used for the fuel UI.
     * @return the active fuel bar rectangle
     */
    public Rectangle getFuelBar() { return fuelBar; }
}