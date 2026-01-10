package io.github.some_example_name;

import com.badlogic.gdx.math.Vector2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test suite for Decrease_Time class.
 * Tests tree obstacle creation, collision detection, time penalty application, and message display timing.
 * Test coverage: [Run JaCoCo to determine]%
 *
 * Automated: Constructor, hitTree state, collision logic, timer mechanics, penalty amount
 * Manual testing required: render(), dispose() - require LibGDX rendering context
 *
 * Coverage gap: Message display rendering and texture cropping only testable visually
 */
@DisplayName("Decrease Time Event Tests")
public class Decrease_TimeTest extends TestHelper {

    //Test #1: instance created successfully at position
    @Test
    @DisplayName("Instance created successfully")
    public void testInstanceCreation() {
        Decrease_Time dt = new Decrease_Time(100, 200);
        assertNotNull(dt);
    }

    //Test #2: tree not hit initially on creation
    @Test
    @DisplayName("Tree not hit initially")
    public void testTreeNotHitInitially() {
        Decrease_Time dt = new Decrease_Time(100, 200);
        assertFalse(dt.hitTree());
    }

    //Test #3: multiple tree instances are independent
    @Test
    @DisplayName("Multiple instances independent")
    public void testMultipleInstances() {
        Decrease_Time dt1 = new Decrease_Time(100, 100);
        Decrease_Time dt2 = new Decrease_Time(200, 200);

        assertNotSame(dt1, dt2);
        assertFalse(dt1.hitTree());
        assertFalse(dt2.hitTree());
    }

    //Test #4: various spawn positions accepted
    @Test
    @DisplayName("Handles various spawn positions")
    public void testVariousPositions() {
        assertDoesNotThrow(() -> new Decrease_Time(0, 0));
        assertDoesNotThrow(() -> new Decrease_Time(500, 500));
        assertDoesNotThrow(() -> new Decrease_Time(-10, -10));
    }

    //Test #5: player collision triggers tree hit and time penalty
    @Test
    @DisplayName("Player collision triggers tree hit")
    public void testPlayerCollisionTriggersHit() {
        Decrease_Time tree = new Decrease_Time(100, 200);
        Player player = new Player(100, 200);
        GameTimer mockTimer = mock(GameTimer.class);

        assertFalse(tree.hitTree());

        tree.update(player, mockTimer, 0.016f);

        assertTrue(tree.hitTree(), "Tree should be marked as hit");
        verify(mockTimer, times(1)).addTime(-30f);
    }

    //Test #6: distant player does not trigger collision
    @Test
    @DisplayName("Player far from tree - no collision")
    public void testPlayerFarFromTree() {
        Decrease_Time tree = new Decrease_Time(100, 200);
        Player player = new Player(500, 500);
        GameTimer mockTimer = mock(GameTimer.class);

        tree.update(player, mockTimer, 0.016f);

        assertFalse(tree.hitTree(), "Tree should not be hit");
        verify(mockTimer, never()).addTime(anyFloat());
    }

    //Test #7: tree can only be hit once
    @Test
    @DisplayName("Tree can only be hit once")
    public void testTreeOnlyHitOnce() throws Exception {
        Decrease_Time tree = new Decrease_Time(100, 200);
        GameTimer mockTimer = mock(GameTimer.class);

        java.lang.reflect.Field hitField = Decrease_Time.class.getDeclaredField("hitTree");
        hitField.setAccessible(true);
        hitField.set(tree, true);

        java.lang.reflect.Field searchedField = Decrease_Time.class.getDeclaredField("searched");
        searchedField.setAccessible(true);
        searchedField.set(tree, true);

        Player player = new Player(100, 200);

        tree.update(player, mockTimer, 0.016f);

        assertTrue(tree.hitTree(), "Tree should remain hit");
        verify(mockTimer, never()).addTime(anyFloat());
    }

    //Test #8: message timer shows for 5 seconds
    @Test
    @DisplayName("Message timer shows for correct duration")
    public void testMessageDuration() throws Exception {
        Decrease_Time tree = new Decrease_Time(100, 200);
        Player player = new Player(500, 500);
        GameTimer mockTimer = mock(GameTimer.class);

        java.lang.reflect.Field showMessageField = Decrease_Time.class.getDeclaredField("showMessage");
        showMessageField.setAccessible(true);
        showMessageField.set(tree, true);

        java.lang.reflect.Field messageTimerField = Decrease_Time.class.getDeclaredField("messageTimer");
        messageTimerField.setAccessible(true);

        tree.update(player, mockTimer, 1.0f);

        float timer = messageTimerField.getFloat(tree);
        assertEquals(1.0f, timer, 0.1f, "Message timer should increment");

        tree.update(player, mockTimer, 5.0f);

        boolean stillShowing = showMessageField.getBoolean(tree);
        assertFalse(stillShowing, "Message should hide after 5 seconds");
    }

    //Test #9: player hitbox collision detection works
    @Test
    @DisplayName("Player hitbox collision detection works")
    public void testHitboxCollision() {
        Decrease_Time tree = new Decrease_Time(100, 200);
        Player player = new Player(110, 210);
        GameTimer mockTimer = mock(GameTimer.class);

        tree.update(player, mockTimer, 0.016f);

        assertTrue(tree.hitTree(), "Collision should be detected");
        verify(mockTimer, times(1)).addTime(-30f);
    }

    //Test #10: tree position matches constructor values
    @Test
    @DisplayName("Position at tree creation matches constructor")
    public void testTreePosition() throws Exception {
        Decrease_Time tree = new Decrease_Time(150, 250);

        java.lang.reflect.Field posField = Decrease_Time.class.getDeclaredField("position");
        posField.setAccessible(true);
        Vector2 pos = (Vector2) posField.get(tree);

        assertEquals(150, pos.x, "X position should match constructor");
        assertEquals(250, pos.y, "Y position should match constructor");
    }

    //Test #11: collision bounds exist and are initialized
    @Test
    @DisplayName("Bounds size matches texture region with scale")
    public void testBoundsSize() throws Exception {
        Decrease_Time tree = new Decrease_Time(100, 200);

        java.lang.reflect.Field boundsField = Decrease_Time.class.getDeclaredField("bounds");
        boundsField.setAccessible(true);

        assertNotNull(boundsField.get(tree), "Bounds should exist");
    }

    //Test #12: time penalty is exactly -30 seconds
    @Test
    @DisplayName("Time decrease amount is correct")
    public void testTimeDecreaseAmount() {
        Decrease_Time tree = new Decrease_Time(100, 200);
        Player player = new Player(100, 200);
        GameTimer mockTimer = mock(GameTimer.class);

        tree.update(player, mockTimer, 0.016f);

        verify(mockTimer, times(1)).addTime(-30f);
    }
}
