import org.example.airplanegame.player.UserProfile;
import org.example.airplanegame.playerControllers.UserManager;
import org.junit.jupiter.api.*;
import java.io.File;
import static org.junit.jupiter.api.Assertions.*;

public class UserManagerTest {

    private UserManager userManager;
    private static final File ACTUAL_FILE = new File("users.json");

    //create a new json file before each test
    @BeforeEach
    public void setUp() {
        if (ACTUAL_FILE.exists()) {
            ACTUAL_FILE.delete();
        }
        userManager = new UserManager();
    }


    //test 1: test to make sure that users are actualy being registered and authenticated properly
    @Test
    public void testRegisterAndAuthenticateUser() {

        //set username and password for user
        String username = "Test";
        String password = "123";

        //register user
        assertTrue(userManager.registerUser(username, password, false), "User registered successfully");
        assertTrue(userManager.usernameExists(username), "Username should exist");
        assertFalse(userManager.registerUser(username, "1234", false), "username allready exists, should not be able to register");

        //authenticate user
        assertTrue(userManager.authenticateUser(username, password), "should authenitcate");
        assertFalse(userManager.authenticateUser(username, "456"), "wrong password, should not authenticate");
    }

    //test 2: test to make sure that the high score is being updated properly
    @Test
    public void testHighScoreLogic() {
        String username = "scoretest";
        //register new user
        userManager.registerUser(username, "123", false);

        //inital score should be zero since its a fresh file
        assertEquals(0, userManager.getHighScore(username), "High score should be 0");

        //update the users high score
        userManager.updateHighScore(username, 500);
        assertEquals(500, userManager.getHighScore(username), "High score should now be 500");

        //try and update the highscore with a score that is lower then teh current high score
        userManager.updateHighScore(username, 250);
        assertEquals(500, userManager.getHighScore(username), "highscore shouldnt be updated");
    }

    //test 3: test to make sure that the money is being updated properly
    @Test
    public void testAddMoney() {
        String username = "moneytest";
        //register new user
        userManager.registerUser(username, "123", false);

        //add money to the user
        userManager.addMoney(username, 150);
        userManager.addMoney(username, 50);

        // check to see if money equals what it should be (150+50 = 200)
        UserProfile profile = userManager.getUserProfile(username);
        assertEquals(200, profile.getMoney(), "Money should add properly");
    }

    //test 4: test to make sure that the top players are being returned properly
    @Test
    public void testGetTopPlayersSorting() {
        //create 12 users with different high scores
        for (int i = 1; i <= 12; i++) {
            String user = "Player" + i;
            userManager.registerUser(user, "123", false);
            userManager.updateHighScore(user, i * 100); //player 1 should have a high score of 100, player 2 should have a high score of 200, etc.
        }

        //get the top 10 players
        UserProfile[] topPlayers = userManager.getTopPlayers();

        //check if the the correct amount of players are grabbed and if the expected users are in their specific spots
        assertEquals(10, topPlayers.length, "Should only return top 10 players");
        assertEquals(1200, topPlayers[0].getHighScore(), "First player in top players should have the highest score");
        assertEquals(1100, topPlayers[1].getHighScore(), "Second player in top players should have the second highest score");
        assertEquals(300, topPlayers[9].getHighScore(), "Tenth player in top players should have the lowest score of the top 10");
    }

    //test 5: test to make sure that leaderboard, passwword and stats are able to be reset
    @Test
    public void testResets() {
        //create an account and update their stasts
        String username = "resettest";
        userManager.registerUser(username, "123", false);
        userManager.updateHighScore(username, 1000);
        userManager.updateTypingStats(username, 15.0, 95.4, 5, 60.0, 100);

        //try and reset the leaderboard
        userManager.resetLeaderboard();
        assertEquals(0, userManager.getHighScore(username), "Leaderboard reset should set score back to 0");

        //try and reset the users password
        userManager.resetPassword(username, "456");
        assertTrue(userManager.authenticateUser(username, "456"), "User should be able to authenticate with new password");

        //try and reset the users stats
        userManager.resetTypingStats(username);
        UserProfile profile = userManager.getUserProfile(username);
        assertEquals(0.0, profile.getAvgWpm(), "WPM should be reset to 0");
        assertEquals(0, profile.getErrorCount(), "Errors should be reset to 0");
    }
}