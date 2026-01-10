package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test suite for Locker class.
 * Tests locker creation, E key activation, speed boost mechanics, and distance-based interaction.
 * Test coverage:75%
 * Automated: Constructor, E key activation within 50 units, boost timer mechanics, message display
 * Coverage Justification: Visual rendering and texture display only testable through gameplay
 */
@DisplayName("Locker Event Tests")
public class LockerTest extends TestHelper {

    //Test #1: instance created successfully
    @Test
    @DisplayName("Instance created successfully")
    public void testInstanceCreation() {
        Locker locker = new Locker(100, 200);
        assertNotNull(locker);
    }

    //Test #2: boost not active initially
    @Test
    @DisplayName("Boost not active initially")
    public void testBoostNotActiveInitially() {
        Locker locker = new Locker(100, 200);
        assertFalse(locker.isBoostActive());
    }

    //Test #3: locker not searched initially
    @Test
    @DisplayName("Locker not searched initially")
    public void testLockerNotSearchedInitially() {
        Locker locker = new Locker(100, 200);
        assertFalse(locker.lockerSearched());
    }

    //Test #4: distant player does not activate locker
    @Test
    @DisplayName("Player far from locker - no activation")
    public void testPlayerFarFromLocker() {
        Locker locker = new Locker(100, 200);
        Player player = new Player(500, 500);

        locker.update(player, 0.016f);

        assertFalse(locker.lockerSearched());
        assertFalse(locker.isBoostActive());
    }

    //Test #5: multiple locker instances are independent
    @Test
    @DisplayName("Multiple lockers independent")
    public void testMultipleLockers() {
        Locker l1 = new Locker(100, 100);
        Locker l2 = new Locker(200, 200);

        assertNotSame(l1, l2);
        assertFalse(l1.isBoostActive());
        assertFalse(l2.isBoostActive());
    }

    //Test #6: boost timer decrements over time
    @Test
    @DisplayName("Boost timer decrements over time")
    public void testBoostTimerDecrement() throws Exception {
        Locker locker = new Locker(100, 200);
        Player player = new Player(500, 500);

        java.lang.reflect.Field timerField = Locker.class.getDeclaredField("speedBoostTimer");
        timerField.setAccessible(true);
        timerField.set(locker, 10f);

        assertTrue(locker.isBoostActive());

        locker.update(player, 1.0f);
        assertTrue(locker.isBoostActive());

        locker.update(player, 10.0f);
        assertFalse(locker.isBoostActive());
    }

    //Test #7: cannot search locker twice
    @Test
    @DisplayName("Cannot search locker twice")
    public void testCannotSearchTwice() throws Exception {
        Locker locker = new Locker(100, 200);
        Player player = new Player(120, 220);

        java.lang.reflect.Field searchedField = Locker.class.getDeclaredField("searched");
        searchedField.setAccessible(true);
        searchedField.set(locker, true);

        java.lang.reflect.Field lockerSearchedField = Locker.class.getDeclaredField("lockerSearched");
        lockerSearchedField.setAccessible(true);
        lockerSearchedField.set(locker, true);

        assertTrue(locker.lockerSearched());

        locker.update(player, 0.016f);
        assertTrue(locker.lockerSearched());
    }

    //Test #8: E key activates locker within 50 units
    @Test
    @DisplayName("E key activates locker when player is within 50 units")
    public void testEKeyActivatesWithinRange() {
        Input mockInput = mock(Input.class);
        Gdx.input = mockInput;

        Locker locker = new Locker(100, 200);
        Player player = new Player(120, 220);

        when(mockInput.isKeyJustPressed(Input.Keys.E)).thenReturn(true);

        locker.update(player, 0.016f);

        assertTrue(locker.lockerSearched(), "Locker should be searched");
        assertTrue(locker.isBoostActive(), "Boost should be active");
    }

    //Test #9: E key ignored beyond 50 units
    @Test
    @DisplayName("E key does nothing when player is beyond 50 units")
    public void testEKeyIgnoredOutsideRange() {
        Input mockInput = mock(Input.class);
        Gdx.input = mockInput;

        Locker locker = new Locker(100, 200);
        Player player = new Player(200, 300);

        when(mockInput.isKeyJustPressed(Input.Keys.E)).thenReturn(true);

        locker.update(player, 0.016f);

        assertFalse(locker.lockerSearched(), "Locker should not activate outside range");
        assertFalse(locker.isBoostActive());
    }

    //Test #10: no activation without E key press
    @Test
    @DisplayName("Player within range but no E key - no activation")
    public void testNoActivationWithoutEKey() {
        Input mockInput = mock(Input.class);
        Gdx.input = mockInput;

        Locker locker = new Locker(100, 200);
        Player player = new Player(120, 220);

        when(mockInput.isKeyJustPressed(Input.Keys.E)).thenReturn(false);

        locker.update(player, 0.016f);

        assertFalse(locker.lockerSearched(), "Should not activate without E key");
        assertFalse(locker.isBoostActive());
    }

    //Test #11: exact 50 unit boundary test
    @Test
    @DisplayName("Exact 50 unit boundary test")
    public void testExactBoundary() {
        Input mockInput = mock(Input.class);
        Gdx.input = mockInput;

        Locker locker = new Locker(0, 0);
        Player player = new Player(30, 40);

        when(mockInput.isKeyJustPressed(Input.Keys.E)).thenReturn(true);

        locker.update(player, 0.016f);

        assertFalse(locker.lockerSearched(), "Should not activate at exact 50 units");
    }

    //Test #12: just inside 50 unit range activates
    @Test
    @DisplayName("Just inside 50 unit range activates")
    public void testJustInsideRange() {
        Input mockInput = mock(Input.class);
        Gdx.input = mockInput;

        Locker locker = new Locker(0, 0);
        Player player = new Player(30, 39);

        when(mockInput.isKeyJustPressed(Input.Keys.E)).thenReturn(true);

        locker.update(player, 0.016f);

        assertTrue(locker.lockerSearched(), "Should activate just inside 50 units");
        assertTrue(locker.isBoostActive());
    }

    //Test #13: message timer increments correctly
    @Test
    @DisplayName("Message timer increments correctly")
    public void testMessageTimerIncrement() throws Exception {
        Locker locker = new Locker(100, 200);
        Player player = new Player(500, 500);

        java.lang.reflect.Field messageField = Locker.class.getDeclaredField("showMessage");
        messageField.setAccessible(true);
        messageField.set(locker, true);

        java.lang.reflect.Field timerField = Locker.class.getDeclaredField("messageTimer");
        timerField.setAccessible(true);
        timerField.set(locker, 0f);

        locker.update(player, 1.0f);

        float timer = timerField.getFloat(locker);
        assertEquals(1.0f, timer, 0.1f, "Message timer should increment to 1.0");

        locker.update(player, 5.0f);

        boolean stillShowing = messageField.getBoolean(locker);
        assertFalse(stillShowing, "Message should hide after 5 seconds");
    }

    //Test #14: speed boost expires after 10 seconds
    @Test
    @DisplayName("Speed boost expires after 10 seconds")
    public void testBoostExpiration() throws Exception {
        Locker locker = new Locker(100, 200);
        Player player = new Player(500, 500);

        java.lang.reflect.Field timerField = Locker.class.getDeclaredField("speedBoostTimer");
        timerField.setAccessible(true);
        timerField.set(locker, 10f);

        assertTrue(locker.isBoostActive());

        for (int i = 0; i < 11; i++) {
            locker.update(player, 1.0f);
        }

        assertFalse(locker.isBoostActive(), "Boost should expire after 10 seconds");
    }

    //Test #15: E key only works once per locker
    @Test
    @DisplayName("E key only works once per locker")
    public void testEKeyOnlyWorksOnce() {
        Input mockInput = mock(Input.class);
        Gdx.input = mockInput;

        Locker locker = new Locker(100, 200);
        Player player = new Player(120, 220);

        when(mockInput.isKeyJustPressed(Input.Keys.E)).thenReturn(true);

        locker.update(player, 0.016f);
        assertTrue(locker.lockerSearched());

        locker.update(player, 0.016f);
        locker.update(player, 0.016f);

        assertTrue(locker.lockerSearched());
    }

    //Test #16: player at exact locker position activates
    @Test
    @DisplayName("Player at exact locker position activates with E key")
    public void testPlayerAtExactPosition() {
        Input mockInput = mock(Input.class);
        Gdx.input = mockInput;

        Locker locker = new Locker(100, 200);
        Player player = new Player(100, 200);

        when(mockInput.isKeyJustPressed(Input.Keys.E)).thenReturn(true);

        locker.update(player, 0.016f);

        assertTrue(locker.lockerSearched(), "Should activate at exact position");
        assertTrue(locker.isBoostActive());
    }
}
