package io.github.some_example_name;

import com.badlogic.gdx.math.Vector2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for Slow_Down class.
 * Tests bush obstacle creation, collision detection, speed debuff mechanics, and message timing.
 * Test coverage:75%
 * Coverage justification: Bush texture rendering and cropped region only testable visually
 * Note: Applies -200f speed debuff for 20 seconds on collision (negative event)
 */

@DisplayName("Slow Down Tests")
public class Slow_DownTest extends TestHelper {

    //Test #1: instance created successfully
    @Test
    @DisplayName("Instance created successfully")
    public void testInstanceCreation() {
        Slow_Down sd = new Slow_Down(100, 200);
        assertNotNull(sd);
    }

    //Test #2: debuff not active initially
    @Test
    @DisplayName("Initial boost state inactive")
    public void testInitialBoostState() {
        Slow_Down sd = new Slow_Down(100, 200);
        assertFalse(sd.isBoostActive());
    }

    //Test #3: bush not fallen into initially
    @Test
    @DisplayName("Bush fall state false initially")
    public void testBushFallInitially() {
        Slow_Down sd = new Slow_Down(100, 200);
        assertFalse(sd.bushFall());
    }

    //Test #4: multiple bush instances are independent
    @Test
    @DisplayName("Multiple instances work")
    public void testMultipleInstances() {
        Slow_Down sd1 = new Slow_Down(100, 100);
        Slow_Down sd2 = new Slow_Down(300, 300);

        assertNotSame(sd1, sd2);
        assertFalse(sd1.isBoostActive());
        assertFalse(sd2.isBoostActive());
    }

    //Test #5: player collision triggers bush fall and debuff
    @Test
    @DisplayName("Player collision triggers bush fall")
    public void testPlayerCollisionTriggersFall() {
        Slow_Down bush = new Slow_Down(100, 200);
        Player player = new Player(100, 200);

        assertFalse(bush.bushFall());
        assertFalse(bush.isBoostActive());

        bush.update(player, 0.016f);

        assertTrue(bush.bushFall(), "Bush should be marked as fallen");
        assertTrue(bush.isBoostActive(), "Speed debuff should be active");
    }

    //Test #6: distant player does not trigger collision
    @Test
    @DisplayName("Player far from bush - no collision")
    public void testPlayerFarFromBush() {
        Slow_Down bush = new Slow_Down(100, 200);
        Player player = new Player(500, 500);

        bush.update(player, 0.016f);

        assertFalse(bush.bushFall(), "Bush should not be fallen");
        assertFalse(bush.isBoostActive(), "Boost should not be active");
    }

    //Test #7: debuff timer decrements over time
    @Test
    @DisplayName("Speed debuff timer decrements over time")
    public void testDebuffTimerDecrement() throws Exception {
        Slow_Down bush = new Slow_Down(100, 200);
        Player player = new Player(500, 500);

        java.lang.reflect.Field timerField = Slow_Down.class.getDeclaredField("speedBoostTimer");
        timerField.setAccessible(true);
        timerField.set(bush, 20f);

        assertTrue(bush.isBoostActive());

        bush.update(player, 1.0f);
        assertTrue(bush.isBoostActive());

        bush.update(player, 20.0f);
        assertFalse(bush.isBoostActive());
    }

    //Test #8: bush can only be fallen into once
    @Test
    @DisplayName("Bush can only be fallen into once")
    public void testBushOnlyFallOnce() throws Exception {
        Slow_Down bush = new Slow_Down(100, 200);

        java.lang.reflect.Field searchedField = Slow_Down.class.getDeclaredField("searched");
        searchedField.setAccessible(true);
        searchedField.set(bush, true);

        java.lang.reflect.Field fallenField = Slow_Down.class.getDeclaredField("fallen");
        fallenField.setAccessible(true);
        fallenField.set(bush, true);

        Player player = new Player(100, 200);

        bush.update(player, 0.016f);

        assertTrue(bush.bushFall());
    }

    //Test #9: message displays for 5 seconds
    @Test
    @DisplayName("Message timer shows for correct duration")
    public void testMessageDuration() throws Exception {
        Slow_Down bush = new Slow_Down(100, 200);
        Player player = new Player(500, 500);

        java.lang.reflect.Field showMessageField = Slow_Down.class.getDeclaredField("showMessage");
        showMessageField.setAccessible(true);
        showMessageField.set(bush, true);

        java.lang.reflect.Field messageTimerField = Slow_Down.class.getDeclaredField("messageTimer");
        messageTimerField.setAccessible(true);

        bush.update(player, 1.0f);

        float timer = messageTimerField.getFloat(bush);
        assertEquals(1.0f, timer, 0.1f, "Message timer should increment");

        bush.update(player, 5.0f);

        boolean stillShowing = showMessageField.getBoolean(bush);
        assertFalse(stillShowing, "Message should hide after 5 seconds");
    }

    //Test #10: debuff expires after 20 seconds
    @Test
    @DisplayName("Speed debuff expires after 20 seconds")
    public void testDebuffExpiration() throws Exception {
        Slow_Down bush = new Slow_Down(100, 200);
        Player player = new Player(500, 500);

        java.lang.reflect.Field timerField = Slow_Down.class.getDeclaredField("speedBoostTimer");
        timerField.setAccessible(true);
        timerField.set(bush, 20f);

        assertTrue(bush.isBoostActive());

        for (int i = 0; i < 21; i++) {
            bush.update(player, 1.0f);
        }

        assertFalse(bush.isBoostActive(), "Debuff should expire after 20 seconds");
    }

    //Test #11: player hitbox collision detection works
    @Test
    @DisplayName("Player hitbox collision detection works")
    public void testHitboxCollision() {
        Slow_Down bush = new Slow_Down(100, 200);

        Player player = new Player(110, 210);

        bush.update(player, 0.016f);

        assertTrue(bush.bushFall(), "Collision should be detected");
        assertTrue(bush.isBoostActive());
    }

    //Test #12: position matches constructor parameters
    @Test
    @DisplayName("Position at bush creation matches constructor")
    public void testBushPosition() throws Exception {
        Slow_Down bush = new Slow_Down(150, 250);

        java.lang.reflect.Field posField = Slow_Down.class.getDeclaredField("position");
        posField.setAccessible(true);
        Vector2 pos = (Vector2) posField.get(bush);

        assertEquals(150, pos.x, "X position should match constructor");
        assertEquals(250, pos.y, "Y position should match constructor");
    }
}
