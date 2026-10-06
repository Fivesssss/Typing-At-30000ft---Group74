import org.example.airplanegame.shops.Inventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class InventoryTest {
    private Inventory inventory;

    //runs before every test, give a new empty inventory to use
    @BeforeEach
    public void setUp() {
        inventory = new Inventory();
    }



    //test 1: test to make sure that the cosmetics are being added and removed properly
    @Test
    public void testCosmetics() {
        //chekc that inventory starts empty
        assertFalse(inventory.hasCosmeticItem("FIGHTER_JET"), "Should not have item");
        assertEquals(0, inventory.getOwnedCosmetics().size(), "Cosmetics in inventory should be empty");

        //add cosmetic to inventory
        inventory.addCosmeticItem("FIGHTER_JET");

        //check if it was added properly
        assertTrue(inventory.hasCosmeticItem("FIGHTER_JET"), "Should have item");
        assertEquals(1, inventory.getOwnedCosmetics().size(), "Cosmetics size be 1");

        //try and add the same one again
        inventory.addCosmeticItem("FIGHTER_JET");

        //check that duplicates cant be added
        assertEquals(1, inventory.getOwnedCosmetics().size(), "size should still be 1 since cant add duplicates");
    }

    //test 2: test to make sure that the power-ups are being added and removed properly
    @Test
    public void testPowerUp() {
        String powerUp = "SLOW_MOTION";

        //should have no power-ups
        assertEquals(0, inventory.getPowerUps(powerUp), "should start with 0 powerups");
        assertFalse(inventory.usePowerUp(powerUp), "should return false if trying to use a powerup you don't have");

        //add 2 powerups
        inventory.addPowerUpItem(powerUp);
        inventory.addPowerUpItem(powerUp);

        //check that the two powerups were added
        assertEquals(2, inventory.getPowerUps(powerUp), "should have 2 powerups");
        assertTrue(inventory.getOwnedPowerUps().containsKey(powerUp), "should contain the powerup");

        //use the 1 of the powerups
        boolean used = inventory.usePowerUp(powerUp);

        //chekc if powerup was used and that it was taken away
        assertTrue(used, "should return true when used a powerup");
        assertEquals(1, inventory.getPowerUps(powerUp), "should only have 1 powerup left");
    }

    //test 3: test to make sure that the equipped items are being set and retrieved properly
    @Test
    public void testEquippedItems() {
        //equiped plane and background should be null
        assertNull(inventory.getEquippedPlane(), "plane should be null");
        assertNull(inventory.getEquippedBackground(), "background should be null");

        //equipt a plane and background
        inventory.setEquippedPlane("FIGHTER_JET");
        inventory.setEquippedBackground("NIGHT_SKY");

        //check if they were actually equiped
        assertEquals("FIGHTER_JET", inventory.getEquippedPlane(), "equiped plane should be fighter jet");
        assertEquals("NIGHT_SKY", inventory.getEquippedBackground(), "equiped background should be night sky");
    }
}