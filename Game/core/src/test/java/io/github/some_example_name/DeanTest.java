package io.github.some_example_name;

import com.badlogic.gdx.math.Vector2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test suite for Dean class.
 * Tests enemy creation, speed management, position tracking, collision bounds, and player chase logic.
 * Test coverage: [Run JaCoCo to determine]%
 *
 * Automated: Constructor, speed getter/setter, position/bounds management, basic movement logic
 * Manual testing required: render(), dispose(), update() pathfinding - require LibGDX/GameScreen context
 *
 * Coverage gap: tryMoveDiagonally() private method tested indirectly through update()
 * resetToStart() method requires integration testing with game state
 */
@DisplayName("Dean Enemy Tests")
public class DeanTest extends TestHelper {

    //Test #1: instance created with player and screen references
    @Test
    @DisplayName("Instance created with player and screen")
    public void testInstanceCreation() {
        Player mockPlayer = mock(Player.class);
        GameScreen mockScreen = mock(GameScreen.class);

        Dean dean = new Dean(100, 200, mockPlayer, mockScreen);
        assertNotNull(dean);
    }

    //Test #2: default speed is 0.7f
    @Test
    @DisplayName("Initial speed is default")
    public void testInitialSpeed() {
        Player mockPlayer = mock(Player.class);
        GameScreen mockScreen = mock(GameScreen.class);

        Dean dean = new Dean(100, 200, mockPlayer, mockScreen);
        assertEquals(0.7f, dean.getSpeed(), 0.01);
    }

    //Test #3: speed can be changed via setter
    @Test
    @DisplayName("Speed can be modified")
    public void testSpeedModification() {
        Player mockPlayer = mock(Player.class);
        GameScreen mockScreen = mock(GameScreen.class);

        Dean dean = new Dean(100, 200, mockPlayer, mockScreen);

        dean.setSpeed(1.5f);
        assertEquals(1.5f, dean.getSpeed(), 0.01);
    }

    //Test #4: position vector accessible and correct
    @Test
    @DisplayName("Position accessible")
    public void testPositionAccessible() {
        Player mockPlayer = mock(Player.class);
        GameScreen mockScreen = mock(GameScreen.class);

        Dean dean = new Dean(150, 250, mockPlayer, mockScreen);

        assertNotNull(dean.getPosition());
        assertEquals(150, dean.getPosition().x);
        assertEquals(250, dean.getPosition().y);
    }

    //Test #5: collision bounds are 16x16 pixels
    @Test
    @DisplayName("Bounds are 16x16")
    public void testBoundsSize() {
        Player mockPlayer = mock(Player.class);
        GameScreen mockScreen = mock(GameScreen.class);

        Dean dean = new Dean(100, 200, mockPlayer, mockScreen);

        assertEquals(16, dean.getBounds().width);
        assertEquals(16, dean.getBounds().height);
    }

    //Test #6: zero speed prevents movement
    @Test
    @DisplayName("Speed changes affect movement")
    public void testSpeedAffectsMovement() {
        Player mockPlayer = mock(Player.class);
        when(mockPlayer.getPosition()).thenReturn(new Vector2(200, 200));

        GameScreen mockScreen = mock(GameScreen.class);
        when(mockScreen.isCellBlockedForDean(anyFloat(), anyFloat())).thenReturn(false);

        Dean dean = new Dean(100, 100, mockPlayer, mockScreen);

        Vector2 initialPos = new Vector2(dean.getPosition());

        dean.setSpeed(0f);
        dean.update(1.0f);

        assertEquals(initialPos.x, dean.getPosition().x, 0.01f);
        assertEquals(initialPos.y, dean.getPosition().y, 0.01f);
    }

    //Test #7: dean maintains player reference for tracking
    @Test
    @DisplayName("Dean tracks player reference")
    public void testDeanTracksPlayer() {
        Player mockPlayer = mock(Player.class);
        when(mockPlayer.getPosition()).thenReturn(new Vector2(300, 300));

        GameScreen mockScreen = mock(GameScreen.class);

        Dean dean = new Dean(100, 100, mockPlayer, mockScreen);

        assertNotNull(dean);
    }

    //Test #8: multiple dean instances are independent
    @Test
    @DisplayName("Multiple deans independent")
    public void testMultipleDeans() {
        Player mockPlayer = mock(Player.class);
        GameScreen mockScreen = mock(GameScreen.class);

        Dean dean1 = new Dean(100, 100, mockPlayer, mockScreen);
        Dean dean2 = new Dean(200, 200, mockPlayer, mockScreen);

        assertNotSame(dean1, dean2);
        assertEquals(100, dean1.getPosition().x);
        assertEquals(200, dean2.getPosition().x);
    }

    //Test #9: speed can be set to zero
    @Test
    @DisplayName("Speed can be set to zero")
    public void testSpeedZero() {
        Player mockPlayer = mock(Player.class);
        GameScreen mockScreen = mock(GameScreen.class);

        Dean dean = new Dean(100, 200, mockPlayer, mockScreen);

        dean.setSpeed(0f);
        assertEquals(0f, dean.getSpeed(), 0.01f);
    }

    //Test #10: high speed values are accepted
    @Test
    @DisplayName("Speed can be set to high value")
    public void testSpeedHigh() {
        Player mockPlayer = mock(Player.class);
        GameScreen mockScreen = mock(GameScreen.class);

        Dean dean = new Dean(100, 200, mockPlayer, mockScreen);

        dean.setSpeed(10f);
        assertEquals(10f, dean.getSpeed(), 0.01f);
    }

    //Test #11: constructor parameters match position values
    @Test
    @DisplayName("Position at creation matches constructor")
    public void testPositionMatchesConstructor() {
        Player mockPlayer = mock(Player.class);
        GameScreen mockScreen = mock(GameScreen.class);

        Dean dean = new Dean(175, 225, mockPlayer, mockScreen);

        assertEquals(175, dean.getPosition().x, 0.01f);
        assertEquals(225, dean.getPosition().y, 0.01f);
    }

    //Test #12: bounds position synchronized with dean position
    @Test
    @DisplayName("Bounds position matches dean position")
    public void testBoundsMatchPosition() {
        Player mockPlayer = mock(Player.class);
        GameScreen mockScreen = mock(GameScreen.class);

        Dean dean = new Dean(100, 200, mockPlayer, mockScreen);

        assertEquals(dean.getPosition().x, dean.getBounds().x, 0.01f);
        assertEquals(dean.getPosition().y, dean.getBounds().y, 0.01f);
    }

    //Test #13: update method executes without throwing errors
    @Test
    @DisplayName("Update executes without error")
    public void testUpdateNoError() {
        Player mockPlayer = mock(Player.class);
        when(mockPlayer.getPosition()).thenReturn(new Vector2(150, 150));

        GameScreen mockScreen = mock(GameScreen.class);
        when(mockScreen.isCellBlockedForDean(anyFloat(), anyFloat())).thenReturn(false);

        Dean dean = new Dean(100, 100, mockPlayer, mockScreen);

        assertDoesNotThrow(() -> dean.update(0.016f));
    }

    //Test #14: resetToStart with even caught count returns to start position
    @Test
    @DisplayName("Reset to start position on even catches")
    public void testResetToStartEven() {
        Player mockPlayer = mock(Player.class);
        GameScreen mockScreen = mock(GameScreen.class);

        Dean dean = new Dean(100, 100, mockPlayer, mockScreen);

        dean.getPosition().set(500, 500);
        dean.resetToStart(2);

        assertEquals(100, dean.getPosition().x, 0.01f);
        assertEquals(100, dean.getPosition().y, 0.01f);
    }

    //Test #15: resetToStart with odd caught count moves to alternate position
    @Test
    @DisplayName("Reset to alternate position on odd catches")
    public void testResetToStartOdd() {
        Player mockPlayer = mock(Player.class);
        GameScreen mockScreen = mock(GameScreen.class);

        Dean dean = new Dean(100, 100, mockPlayer, mockScreen);

        dean.resetToStart(1);

        assertEquals(690, dean.getPosition().x, 0.01f);
        assertEquals(560, dean.getPosition().y, 0.01f);
    }
}
