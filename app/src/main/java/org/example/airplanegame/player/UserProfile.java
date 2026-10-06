package org.example.airplanegame.player;
import org.example.airplanegame.shops.Inventory;

/**
 * Responsible for creating a new user profile and storing user stats
 * @author Mitchell, Mark, Walid
 */
public class UserProfile {
    private String username;
    private String password;
    private int highScore;
    private int money;
    private Inventory inventory;
    private boolean isTeacherAccount;

    private double peakWpm;
    private double avgWpm;
    private int gamesPlayed;
    private int unlockedLevel;
    private double accuracy;
    private int errorCount;
    private double totalTimePlayed;
    private int wordsTyped;

    /**
     * Creates a new user profile with default values for stats, money, and inventory.
     * @param username the user's username
     * @param password the user's password
     * @param isTeacherAccount whether this profile belongs to a teacher account
     */
    public UserProfile(String username, String password, boolean isTeacherAccount) {
        this.username = username;
        this.password = password;
        this.highScore = 0; // Starts at 0 for new accounts
        this.money = 0;
        this.inventory = new Inventory();
        this.isTeacherAccount = isTeacherAccount;
        this.peakWpm = 0.0;
        this.avgWpm = 0.0;
        this.gamesPlayed = 0;
        this.unlockedLevel = 1;
    }

    // --- Getters and Setters ---
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public void setPassword(String password){ this.password = password;}
    public int getHighScore() { return highScore; }
    public int getMoney() { return money; }
    public void setMoney(int money) { this.money = money; }
    public Inventory getInventory() { return inventory; }
    public void setInventory(Inventory inventory) { this.inventory = inventory; }
    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }
    public int getUnlockedLevel() {return unlockedLevel; }
    public void setUnlockedLevel(int unlockedLevel) {this.unlockedLevel = unlockedLevel; }

    //new typing get/set, for peak wpn, avg wpm, errors ad num games played
    public int getGamesPlayed() { return gamesPlayed; }
    public double getPeakWpm() { return peakWpm; }
    public double getAvgWpm() { return avgWpm; }
    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }
    public void setPeakWpm(double peakWpm) {
        this.peakWpm = peakWpm;
    }
    public void setAvgWpm(double avgWpm) {
        this.avgWpm = avgWpm;
    }
    public boolean getIsTeacherAccount(){return isTeacherAccount;}
    public double getAccuracy() { return accuracy; }
    public void setAccuracy(double accuracy) { this.accuracy = accuracy; }
    public int getErrorCount() { return errorCount; }
    public void setErrorCount(int errorCount) { this.errorCount = errorCount; }
    public double getTotalTimePlayed() { return totalTimePlayed; }
    public void setTotalTimePlayed(double totalTimePlayed) { this.totalTimePlayed = totalTimePlayed; }
    public int getWordsTyped() { return wordsTyped; }
    public void setWordsTyped(int wordsTyped) { this.wordsTyped = wordsTyped; }
}