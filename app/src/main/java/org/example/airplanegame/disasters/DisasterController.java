package org.example.airplanegame.disasters;

import org.example.airplanegame.Main;
import org.example.airplanegame.core.Game;
import org.example.airplanegame.core.Level;

import java.util.ArrayList;
import java.util.Random;

/**
 * Controls disaster selection and probability progression across levels.
 * <p>
 * The controller maintains a fixed set of disasters and updates their selection
 * probabilities as the game progresses through levels.
 * </p>
 *
 * @author Aseem Lakhanpal
 */
public class DisasterController {

    /**This is a list of disaster interfaces that will be selected from */
    private ArrayList<DisasterInterface> disasters;

    /**This is the list of the base probabilites of each disaster, this list does not get modified*/
    private ArrayList<Double> baseProbabilites;

    /**This is the list of the probabilites of each disaster, this list gets modified*/
    private ArrayList<Double> probabilites;

    /**This is the list of the prefix sums of the probabilites, this list does get modified*/
    private ArrayList<Double> prefixSum;

    /**This is the max level of the game*/
    private final int maxLevel;

    /**This is the current level of the game*/
    private int currentLevel;

    /**
     * Finds the first index in the list whose value is greater than the target.
     *
     * @param arr the prefix-sum array to search
     * @param target the random value to locate
     * @return the matching index, or {@code -1} if no index is found
     */
    private int linearSearch(ArrayList<Double> arr, double target) {
        for(int i = 0; i <= arr.size() - 1; i++)
        {
            if(target < arr.get(i))
            {
                return i;
            }
        }
        return -1; //this should never happen
    }

    /**
     * Recomputes the prefix sums for the current disaster probabilities.
     */
    private void updatePrefixSums() {
        double temp = 0d;
        for(int i = 0; i <= this.disasters.size() - 1; i++)
        {
            temp += this.probabilites.get(i);
            this.prefixSum.set(i, temp);

        }
    }

    /**
     * Creates a disaster controller with the default disaster set and starting probabilities.
     *
     * @param maxLevel the maximum level used for probability scaling
     * @param duration the duration assigned to disasters
     * @param mainApp the main application instance
     * @param game the active game instance
     */
    public DisasterController(int maxLevel,int duration, Main mainApp, Game game) {
        this.disasters = new ArrayList<DisasterInterface>();
        //the list looks like [Fog, storm, wind, snow]
        this.disasters.add((new FogDisaster(duration, mainApp)));
        this.disasters.add((new StormDisaster(duration, mainApp)));
        this.disasters.add((new WindDisaster(duration, mainApp, game)));
        this.disasters.add((new SnowDisaster(999999f, mainApp)));

        //init probabilites are hardcoded, will look like [30, 30, 25, 10, 5]
        //these probabilities have one to one indexes to the disasters
        this.probabilites = new ArrayList<Double>();
        this.probabilites.add(30d); //30
        this.probabilites.add(30d); //30
        this.probabilites.add(25d);//25
        this.probabilites.add(15d); //15
        this.baseProbabilites = new ArrayList<Double>();
        for(int i = 0; i <= this.probabilites.size() - 1; i++){
            this.baseProbabilites.add(this.probabilites.get(i));
        }

        double temp = 0d;
        this.prefixSum = new ArrayList<Double>(); // an index representation of the probabilites
        for(int i = 0; i <= this.disasters.size() - 1; i++)
        {
            temp += this.probabilites.get(i);
            this.prefixSum.add(temp);
        }
        //this list should look like [30, 60, 85, 100]

        this.maxLevel = maxLevel; //10 Levels Max
        this.currentLevel = 1;
    }

    /**
     * Randomly selects a disaster according to the current weighted probabilities.
     *
     * @return the selected disaster instance
     */
    public DisasterInterface getDisaster() {
        Random random = new Random();
        //this picks a double between 0 and the max number found in prefix sum(exclusive)
        double pickDisaster = random.nextDouble(0,this.prefixSum.get(this.disasters.size() - 1));
    
        int index = linearSearch(this.prefixSum, pickDisaster); //picks the disaster
        return this.disasters.get(index);
    }

    /**
     * Updates disaster probabilities based on the current level.
     * <p>
     * As the level increases, probabilities are gradually shifted towards all being around a 20% chance
     * </p>
     */
    public void updateProbabilites() {
        for(int i = 0; i <= this.disasters.size() - 1; i++) {
            double initProb = this.baseProbabilites.get(i);
            double finalProb = 20d; // this is to get ALL disasters to equal probability by the max level
            double rate;
            double newProb;

            //calculate rate
            rate = Math.pow(finalProb / initProb, 1.0 / this.maxLevel);
            //newProb = baseProb + (20d - baseProb) * ((double) currentLevel / maxLevel);
            newProb = initProb * Math.pow(rate, this.currentLevel);

            this.probabilites.set(i, newProb);
        }

        updatePrefixSums();

        if(this.currentLevel < 10){
            this.currentLevel++;
        }
    }

}
