package org.example.airplanegame;

/**
 * Launches the game
 * @author Walid
 */
public class Launcher {
    public static void main(String[] args) {
        System.setProperty("prism.allowhidpi", "false");//ignore scaling

        Main.launch(Main.class, args);
    }//end of main
}//end of class
