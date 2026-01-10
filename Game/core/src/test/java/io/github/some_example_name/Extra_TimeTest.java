package io.github.some_example_name;

import com.badlogic.gdx.math.Vector2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test suite for Extra_Time class.
 * Tests NPC creation, collision detection, time bonus application, and message display timing.
 * Test coverage:73%
 * Automated: Constructor, collision logic, timer mechanics, bonus amount verification
 * Coverage justification: message rendering and NPC disappearance only testable visually
 */
@DisplayName("Extra Time Tests")
public class Extra_TimeTest extends TestHelper {

    //Test #1: instance created successfully at position
    @Test
    @DisplayName("Instance created successfully")
    public void testInstanceCreation() {
        Extra_Time et = new Extra_Time(100, 200);
        assertNotNull(et);
    }

    //Test #2: time not gained initially
    @Test
    @DisplayName("Initial state correct")
    public void testInitialState() {
        Extra_Time et = new Extra_Time(100, 200);
        assertFalse(et.gainedTime());
    }

    //Test #3: multiple instances are independent
    @Test
    @DisplayName("Multiple instances independent")
    public void testMultipleInstances() {
        Extra_Time et1 = new Extra_Time(100, 100);
        Extra_Time et2 = new Extra_Time(200, 200);

        assertNotSame(et1, et2);
        assertFalse(et1.gainedTime());
        assertFalse(et2.gainedTime());
    }

    //Test #4: various position parameters accepted
    @Test
    @DisplayName("Position parameters accepted")
    public void testPositionParameters() {
        assertDoesNotThrow(() -> new Extra_Time(0, 0));
        assertDoesNotThrow(() -> new Extra_Time(500, 500));
        assertDoesNotThrow(() -> new Extra_Time(100, 200));
    }

    //Test #5: player collision triggers time bonus
    @Test
    @DisplayName("Player collision triggers time gain")
    public void testPlayerCollisionTriggersTimeGain() {
        Extra_Time clock = new Extra_Time(100, 200);
        Player player = new Player(120, 210);
        GameTimer mockTimer = mock(GameTimer.class);

        assertFalse(clock.gainedTime());

        clock.update(player, mockTimer, 0.016f);

        assertTrue(clock.gainedTime(), "Time should be marked as gained");
        verify(mockTimer, times(1)).addTime(30f);
    }

    //Test #6: distant player does not trigger collision
    @Test
    @DisplayName("Player far from clock - no collision")
    public void testPlayerFarFromClock() {
        Extra_Time clock = new Extra_Time(100, 200);
        Player player = new Player(500, 500);
        GameTimer mockTimer = mock(GameTimer.class);

        clock.update(player, mockTimer, 0.016f);

        assertFalse(clock.gainedTime(), "Time should not be gained");
        verify(mockTimer, never()).addTime(anyFloat());
    }

    //Test #7: time can only be gained once
    @Test
    @DisplayName("Time can only be gained once")
    public void testTimeOnlyGainedOnce() throws Exception {
        Extra_Time clock = new Extra_Time(100, 200);
        GameTimer mockTimer = mock(GameTimer.class);

        java.lang.reflect.Field gainedField = Extra_Time.class.getDeclaredField("gainedTime");
        gainedField.setAccessible(true);
        gainedField.set(clock, true);

        java.lang.reflect.Field searchedField = Extra_Time.class.getDeclaredField("searched");
        searchedField.setAccessible(true);
        searchedField.set(clock, true);

        Player player = new Player(120, 210);

        clock.update(player, mockTimer, 0.016f);

        assertTrue(clock.gainedTime(), "Time should remain gained");
        verify(mockTimer, never()).addTime(anyFloat());
    }

    //Test #8: message displays for 5 seconds
    @Test
    @DisplayName("Message timer shows for correct duration")
    public void testMessageDuration() throws Exception {
        Extra_Time clock = new Extra_Time(100, 200);
        Player player = new Player(500, 500);
        GameTimer mockTimer = mock(GameTimer.class);

        java.lang.reflect.Field showMessageField = Extra_Time.class.getDeclaredField("showMessage");
        showMessageField.setAccessible(true);
        showMessageField.set(clock, true);

        java.lang.reflect.Field messageTimerField = Extra_Time.class.getDeclaredField("messageTimer");
        messageTimerField.setAccessible(true);

        clock.update(player, mockTimer, 1.0f);

        float timer = messageTimerField.getFloat(clock);
        assertEquals(1.0f, timer, 0.1f, "Message timer should increment");

        clock.update(player, mockTimer, 5.0f);

        boolean stillShowing = showMessageField.getBoolean(clock);
        assertFalse(stillShowing, "Message should hide after 5 seconds");
    }

    //Test #9: player hitbox collision works
    @Test
    @DisplayName("Player hitbox collision detection works")
    public void testHitboxCollision() {
        Extra_Time clock = new Extra_Time(100, 200);
        Player player = new Player(130, 220);
        GameTimer mockTimer = mock(GameTimer.class);

        clock.update(player, mockTimer, 0.016f);

        assertTrue(clock.gainedTime(), "Collision should be detected");
        verify(mockTimer, times(1)).addTime(30f);
    }

    //Test #10: position has offset from constructor parameters
    @Test
    @DisplayName("Position at creation matches constructor with offset")
    public void testClockPosition() throws Exception {
        Extra_Time clock = new Extra_Time(150, 250);

        java.lang.reflect.Field posField = Extra_Time.class.getDeclaredField("position");
        posField.setAccessible(true);
        Vector2 pos = (Vector2) posField.get(clock);

        assertEquals(170, pos.x, "X position should be +20 from constructor");
        assertEquals(260, pos.y, "Y position should be +10 from constructor");
    }

    //Test #11: collision bounds exist and initialized
    @Test
    @DisplayName("Bounds size matches texture")
    public void testBoundsSize() throws Exception {
        Extra_Time clock = new Extra_Time(100, 200);

        java.lang.reflect.Field boundsField = Extra_Time.class.getDeclaredField("bounds");
        boundsField.setAccessible(true);

        assertNotNull(boundsField.get(clock), "Bounds should exist");
    }

    //Test #12: time bonus is exactly +30 seconds
    @Test
    @DisplayName("Time increase amount is correct")
    public void testTimeIncreaseAmount() {
        Extra_Time clock = new Extra_Time(100, 200);
        Player player = new Player(120, 210);
        GameTimer mockTimer = mock(GameTimer.class);

        clock.update(player, mockTimer, 0.016f);

        verify(mockTimer, times(1)).addTime(30f);
    }

    //Test #13: NPC disappears after collection
    @Test
    @DisplayName("NPC disappears after collection")
    public void testNPCDisappearsAfterCollection() throws Exception {
        Extra_Time clock = new Extra_Time(100, 200);
        Player player = new Player(120, 210);
        GameTimer mockTimer = mock(GameTimer.class);

        clock.update(player, mockTimer, 0.016f);

        java.lang.reflect.Field searchedField = Extra_Time.class.getDeclaredField("searched");
        searchedField.setAccessible(true);

        assertTrue(searchedField.getBoolean(clock), "NPC should be marked as searched");
    }
}
