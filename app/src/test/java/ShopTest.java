import org.example.airplanegame.player.UserProfile;
import org.example.airplanegame.shops.Item;
import org.example.airplanegame.shops.Shop;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ShopTest {

    @Test
    void testShopBackendPurchaseBehavior() {
        ArrayList<Item> items = new ArrayList<>();
        items.add(new Item("red_plane", 100, "COSMETIC"));
        items.add(new Item("shield", 25, "POWERUP"));

        Shop shop = new Shop(items);

        UserProfile profile = new UserProfile("BillyBobjr", "password", false);
        profile.setMoney(200);

        boolean buy1 = shop.purchaseItem("red_plane", profile);
        boolean buy2 = shop.purchaseItem("red_plane", profile);
        boolean buy3 = shop.purchaseItem("shield", profile);
        boolean buy4 = shop.purchaseItem("shield", profile);

        assertTrue(buy1, "First cosmetic purchase should succeed");
        assertFalse(buy2, "Buying the same cosmetic twice should fail");
        assertTrue(buy3, "First power-up purchase should succeed");
        assertTrue(buy4, "Power-ups should be purchasable multiple times");

        assertEquals(50, profile.getMoney(), "Money should be reduced correctly");
        assertTrue(profile.getInventory().hasCosmeticItem("red_plane"), "Player should own red_plane");
        assertEquals(2, profile.getInventory().getPowerUps("shield"), "Player should have 2 shields");
    }

    @Test
    void testPurchaseFailsWhenNotEnoughMoney() {
        ArrayList<Item> items = new ArrayList<>();
        items.add(new Item("red_plane", 100, "COSMETIC"));

        Shop shop = new Shop(items);

        UserProfile profile = new UserProfile("BillyBobjr", "password", false);
        profile.setMoney(50);

        boolean result = shop.purchaseItem("red_plane", profile);

        assertFalse(result, "Purchase should fail if the player cannot afford the item");
        assertEquals(50, profile.getMoney(), "Money should not change after failed purchase");
        assertFalse(profile.getInventory().hasCosmeticItem("red_plane"), "Item should not be added on failure");
    }

    @Test
    void testUnknownItemReturnsFalse() {
        ArrayList<Item> items = new ArrayList<>();
        items.add(new Item("shield", 25, "POWERUP"));

        Shop shop = new Shop(items);

        UserProfile profile = new UserProfile("BillyBobjr", "password", false);
        profile.setMoney(200);

        boolean result = shop.purchaseItem("missing_item", profile);

        assertFalse(result, "Unknown item should not be purchasable");
        assertEquals(200, profile.getMoney(), "Money should remain unchanged");
    }
}
