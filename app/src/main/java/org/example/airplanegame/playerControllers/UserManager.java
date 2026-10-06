package org.example.airplanegame.playerControllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.JsonSyntaxException;
import org.example.airplanegame.player.UserProfile;
import org.example.airplanegame.shops.Inventory;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Managers all reading and writing of user data to and from JSON file
 * @author Mark, Mitchell, Walid, Samaer
 */
public class UserManager {

    private static final String FILE_PATH = "users.json";
    private Gson gson;

    /**
     * Creates a new user manager.
     */
    public UserManager() {
        gson = new Gson();
    }

    /**
     * Loads all saved user profiles from the JSON file.
     * @return a hash map of usernames to user profiles
     */
    private Map<String, UserProfile> loadUsers() {
        //read in json file if it exists, create new one of it doesent
        try (Reader reader = new FileReader(FILE_PATH)) {
            Type type = new TypeToken<HashMap<String, UserProfile>>(){}.getType();
            Map<String, UserProfile> users = gson.fromJson(reader, type);
            return users != null ? users : new HashMap<>();

        } catch (IOException | JsonSyntaxException e) {
            System.out.println("Save file missing or corrupted. Starting fresh!");
            return new HashMap<>();
        }
    }

    /**
     * Saves all users back to the JSON file.
     * @param users the user map to save
     */
    private void saveUsers(Map<String, UserProfile> users) {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.toJson(users, writer);
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }

    // --- ACCOUNT LOGIC ---

    /**
     * Registers a new user account if the username is not already taken.
     * @param username the new username
     * @param password the new password
     * @param isTeacherAccount whether the account should be marked as a teacher account
     * @return true if registration succeeded, otherwise false
     */
    public boolean registerUser(String username, String password, boolean isTeacherAccount) {
        Map<String, UserProfile> users = loadUsers();

        if (users.containsKey(username)) {
            return false; //username already exists
        }

        users.put(username, new UserProfile(username, password, isTeacherAccount));
        saveUsers(users);
        return true;
    }

    /**
     * Checks whether a username and password match a saved user account.
     * @param username the username to verify
     * @param password the password to verify
     * @return true if the credentials are valid, false otherwise
     */
    public boolean authenticateUser(String username, String password) {
        Map<String, UserProfile> users = loadUsers();

        if (users.containsKey(username)) {
            UserProfile profile = users.get(username);
            //check if the provided password matches the saved one
            return profile.getPassword().equals(password);
        }
        return false;
    }

    // --- SCORE LOGIC ---
    /**
     * Updates a user's saved high score if the new score is higher.
     * @param username the user to update
     * @param newScore the new score to compare against the saved high score
     */
    public void updateHighScore(String username, int newScore) {
        Map<String, UserProfile> users = loadUsers();
        if (users.containsKey(username)) {
            UserProfile profile = users.get(username);
            if (newScore > profile.getHighScore()) {
                profile.setHighScore(newScore);
                saveUsers(users);
                System.out.println("New high score saved for " + username + "!");
            }
        }
    }

    /**
     * Returns the saved high score for a user.
     * @param username the username to look up
     * @return the user's high score, or 0 if the user is not found
     */
    public int getHighScore(String username) {
        Map<String, UserProfile> users = loadUsers();
        if (users.containsKey(username)) {
            return users.get(username).getHighScore();
        }
        return 0;
    }

    /**
     * Updates a user's typing statistics after a game.
     * @param username the user to update
     * @param sessionWpm the WPM for the current session
     * @param sessionAccuracy the accuracy for the current session
     * @param sessionErrors the number of errors made in the session
     * @param sessionTimePlayed the time spent playing this session
     * @param sessionWordsTyped the number of words typed in this session
     */
    public void updateTypingStats(String username, double sessionWpm, double sessionAccuracy,
                                  int sessionErrors, double sessionTimePlayed, int sessionWordsTyped) {
        Map<String, UserProfile> users = loadUsers();
        if (users.containsKey(username)) {
            UserProfile profile = users.get(username);

            //update num ganes played
            int totalGames = profile.getGamesPlayed() + 1;
            profile.setGamesPlayed(totalGames);

            //updates the avg wpm
            double currentAvg = profile.getAvgWpm();
            double newAvg = currentAvg + ((sessionWpm - currentAvg) / totalGames);
            profile.setAvgWpm(newAvg);

            //updates users peak wpm
            if (sessionWpm > profile.getPeakWpm()) {
                profile.setPeakWpm(sessionWpm);
            }

            profile.setErrorCount(profile.getErrorCount() + sessionErrors);
            profile.setTotalTimePlayed(profile.getTotalTimePlayed() + sessionTimePlayed);
            profile.setWordsTyped(profile.getWordsTyped() + sessionWordsTyped);

            //running average accuracy
            double currentAccuracy = profile.getAccuracy();
            double newAccuracy = currentAccuracy + ((sessionAccuracy - currentAccuracy) / totalGames);
            profile.setAccuracy(newAccuracy);

            saveUsers(users);
            System.out.println("Typing stats updated successfully for " + username + "!"); //test to make sure it works
        }
    }

    /**
     * Adds money to a user's money balance.
     * @param username the user to update
     * @param amount the amount of money to add
     */
    public void addMoney(String username, int amount) {
        Map<String, UserProfile> users = loadUsers();

        if (users.containsKey(username)) {
            UserProfile profile = users.get(username);
            profile.setMoney(profile.getMoney() + amount);
            saveUsers(users);
        }
    }

    /**
     * Gets an array of the users that have a top ten highscore
     * @return returns a sorted array of the users with the top ten high scores with the first element of the array being the highest score and the last element being the lowest of the top ten
     */
    public UserProfile[] getTopPlayers(){
        Map<String, UserProfile> users = loadUsers();
        UserProfile[] allUsers = new UserProfile[users.size()];//stores all users
        UserProfile[] topUsers;

        if(users.size()>=10) {
            topUsers = new UserProfile[10];//stores top ten users in order
        }else{
            topUsers = new UserProfile[users.size()];//stores top users in order
        }//end of for loop


        //stores the users in the array
        int count = 0;
        for(UserProfile user : users.values()){
            allUsers[count] = user;
            count++;
        }//end of for loop

        //puts the top users in the topUsers array and sorts it
        for(int i = 0; i < topUsers.length; i++){
            int highest = 0;
            for(int j = 0; j < users.size()-i; j++){
                if(allUsers[j].getHighScore() > allUsers[highest].getHighScore())
                    highest = j;
            }//end of for loop
            topUsers[i] = allUsers[highest];
            allUsers[highest]=allUsers[allUsers.length-1-i];//remove the highest for the array so they aren't counted for the next iteration
        }//end of for loop

        return topUsers;
    }//end of getTopPlayers method

    /**
     * Adds inventories to userprofiles
     * @param username
     * @return
     */
    public UserProfile getUserProfile(String username) {
        Map<String, UserProfile> users = loadUsers();
        UserProfile profile = users.get(username);

        if (profile == null) {
            return null;
        }

        //adding JET plane and SKY background as default free cosmetics
        if (profile.getInventory() == null) {
            Inventory inventory = new Inventory();
            inventory.addCosmeticItem("JET");
            inventory.setEquippedPlane("JET");
            inventory.addCosmeticItem("SKY");
            inventory.setEquippedBackground("SKY");
            profile.setInventory(inventory);
            saveUserProfile(profile);   
        } else {
            if (!profile.getInventory().hasCosmeticItem("JET")) {
                profile.getInventory().addCosmeticItem("JET");
            }

            if (profile.getInventory().getEquippedPlane() == null) {
                profile.getInventory().setEquippedPlane("JET");
            }

            if (!profile.getInventory().hasCosmeticItem("SKY")) {
                profile.getInventory().addCosmeticItem("SKY");
            }

            if (profile.getInventory().getEquippedBackground() == null) {
                profile.getInventory().setEquippedBackground("SKY");
            }

            //save profile
            saveUserProfile(profile);
        }

        if (profile.getUnlockedLevel() < 1) {
            profile.setUnlockedLevel(1);
            saveUserProfile(profile);
        }

        return profile;
    } // end of getUserProfile(String username)

    /**
     * Saves the user profile after purchases in the shop
     * @param profile
     */
    public void saveUserProfile(UserProfile profile) {
        Map<String, UserProfile> users = loadUsers();
        users.put(profile.getUsername(), profile);
        saveUsers(users);
    } // end of saveUserProfile(UserProfile profile)

    /**
     * Checks if there is already an account with the username
     * @param username the username to check
     * @return true if the username already exists and false if it doesn't
     */
    public boolean usernameExists(String username){
        Map<String, UserProfile> users = loadUsers();

        if (users.containsKey(username)) return true; //username already exists
        return false;
    }//end of usernameExists method

    public java.util.List<String> getAllUsernames() {
        java.util.Map<String, UserProfile> users = loadUsers();
        return new java.util.ArrayList<>(users.keySet());
    }

    /**
     * Resets a user's typing statistics back to default.
     * @param username the username of account to reset the stats for
     */
    public void resetTypingStats(String username) {
        java.util.Map<String, UserProfile> users = loadUsers();
        UserProfile profile = users.get(username);

        if (profile != null) {
            profile.setAvgWpm(0.0);
            profile.setPeakWpm(0.0);
            profile.setAccuracy(0.0);
            profile.setErrorCount(0);
            profile.setTotalTimePlayed(0.0);
            profile.setWordsTyped(0);
            profile.setGamesPlayed(0);
            saveUsers(users);
        }
    }

    /**
     * Reset a user's password and saves the change.
     * @param username the username for the password to reset
     * @param newPassword the new password to store
     */
    public void resetPassword(String username, String newPassword) {
        java.util.Map<String, UserProfile> users = loadUsers();
        UserProfile profile = users.get(username);

        if (profile != null) {
            profile.setPassword(newPassword);
            saveUsers(users);
        }
    }

    /**
     * Resets every user's high score to zero.
     */
    public void resetLeaderboard() {
        java.util.Map<String, UserProfile> users = loadUsers();

        for (UserProfile profile : users.values()) {
            profile.setHighScore(0);
        }

        saveUsers(users);
    }
}//end  of class
