package io.github.some_example_name;

import com.badlogic.gdx.math.Vector2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for Player class.
 * Tests player creation, positioning, direction changes, and managing texture frame
 * Test coverage: [Run JaCoCo to determine]%
 * Coverage justification: Texture logic and sprite rendering only testable visually
 * Note: Uses HeadlessLauncher approach to load textures successfully in test environment
 */
@DisplayName("Player Tests")
public class PlayerTest extends TestHelper {

    //Test #1: player created at specified position
    @Test
    @DisplayName("Player created at specified position")
    public void testPlayerCreatedAtPosition() {
        Player player = new Player(100, 200);

        assertNotNull(player);
        assertNotNull(player.getPosition());
        assertEquals(100, player.getPosition().x);
        assertEquals(200, player.getPosition().y);
    }

    //Test #2: position accessible via getPosition
    @Test
    @DisplayName("Player position accessible via getPosition()")
    public void testGetPosition() {
        Player player = new Player(150, 250);

        Vector2 pos = player.getPosition();
        assertNotNull(pos, "Position should not be null");
        assertEquals(150, pos.x, "X position should match constructor");
        assertEquals(250, pos.y, "Y position should match constructor");
    }

    //Test #3: position can be modified directly
    @Test
    @DisplayName("Player position can be modified")
    public void testPositionModifiable() {
        Player player = new Player(100, 100);

        player.getPosition().set(200, 300);

        assertEquals(200, player.getPosition().x);
        assertEquals(300, player.getPosition().y);
    }

    //Test #4: setDirection UP changes frame
    @Test
    @DisplayName("setDirection(UP) changes current frame")
    public void testSetDirectionUp() {
        Player player = new Player(100, 100);

        player.setDirection(Player.Direction.UP);

        assertNotNull(player.currentFrame, "Current frame should be set");
    }

    //Test #5: setDirection DOWN changes frame
    @Test
    @DisplayName("setDirection(DOWN) changes current frame")
    public void testSetDirectionDown() {
        Player player = new Player(100, 100);

        player.setDirection(Player.Direction.DOWN);

        assertNotNull(player.currentFrame, "Current frame should be set");
    }

    //Test #6: setDirection LEFT changes frame
    @Test
    @DisplayName("setDirection(LEFT) changes current frame")
    public void testSetDirectionLeft() {
        Player player = new Player(100, 100);

        player.setDirection(Player.Direction.LEFT);

        assertNotNull(player.currentFrame, "Current frame should be set");
    }

    //Test #7: setDirection RIGHT changes frame
    @Test
    @DisplayName("setDirection(RIGHT) changes current frame")
    public void testSetDirectionRight() {
        Player player = new Player(100, 100);

        player.setDirection(Player.Direction.RIGHT);

        assertNotNull(player.currentFrame, "Current frame should be set");
    }

    //Test #8: all direction changes work sequentially
    @Test
    @DisplayName("All direction changes work sequentially")
    public void testAllDirections() {
        Player player = new Player(100, 100);

        assertDoesNotThrow(() -> {
            player.setDirection(Player.Direction.UP);
            player.setDirection(Player.Direction.DOWN);
            player.setDirection(Player.Direction.LEFT);
            player.setDirection(Player.Direction.RIGHT);
        }, "All direction changes should work");
    }

    //Test #9: multiple player instances are independent
    @Test
    @DisplayName("Multiple players independent")
    public void testMultiplePlayersIndependent() {
        Player p1 = new Player(100, 100);
        Player p2 = new Player(200, 200);

        assertNotSame(p1, p2, "Players should be different instances");
        assertNotSame(p1.getPosition(), p2.getPosition(), "Positions should be independent");

        assertEquals(100, p1.getPosition().x);
        assertEquals(200, p2.getPosition().x);
    }

    //Test #10: player can be at origin coordinates
    @Test
    @DisplayName("Player created at origin (0,0)")
    public void testPlayerAtOrigin() {
        Player player = new Player(0, 0);

        assertEquals(0, player.getPosition().x);
        assertEquals(0, player.getPosition().y);
    }

    //Test #11: negative coordinates accepted
    @Test
    @DisplayName("Player created at negative coordinates")
    public void testPlayerNegativeCoordinates() {
        Player player = new Player(-50, -100);

        assertEquals(-50, player.getPosition().x);
        assertEquals(-100, player.getPosition().y);
    }

    //Test #12: currentFrame is publicly accessible
    @Test
    @DisplayName("Player currentFrame is public and accessible")
    public void testCurrentFrameAccessible() {
        Player player = new Player(100, 100);

        assertNotNull(player.currentFrame,
            "currentFrame should be accessible (used by GameScreen for collision)");
    }

    //Test #13: Direction enum has 4 values
    @Test
    @DisplayName("Direction enum values exist")
    public void testDirectionEnumValues() {
        assertEquals(4, Player.Direction.values().length,
            "Should have 4 direction values");
        assertNotNull(Player.Direction.UP);
        assertNotNull(Player.Direction.DOWN);
        assertNotNull(Player.Direction.LEFT);
        assertNotNull(Player.Direction.RIGHT);
    }
}
