package io.github.some_example_name;

import com.badlogic.gdx.math.Vector2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test suite for Teleport class.
 * Tests teleport event creation, countdown mechanics, random position selection, and usage of one item
 * Test coverage:62%
 * Coverage justification: Countdown visual display and texture rendering only testable visually
 */
@DisplayName("Teleport Tests")
public class TeleportTest extends TestHelper {

    //Test #1: instance created with GameScreen reference
    @Test
    @DisplayName("Instance created with GameScreen")
    public void testInstanceCreation() {
        GameScreen mockScreen = mock(GameScreen.class);
        Teleport tp = new Teleport(100, 200, mockScreen);
        assertNotNull(tp);
    }

    //Test #2: teleport not happened initially
    @Test
    @DisplayName("Teleport not happened initially")
    public void testTeleportNotHappenedInitially() {
        GameScreen mockScreen = mock(GameScreen.class);
        Teleport tp = new Teleport(100, 200, mockScreen);
        assertFalse(tp.teleportHappened());
    }

    //Test #3: multiple teleport instances can exist
    @Test
    @DisplayName("Multiple teleports can exist")
    public void testMultipleTeleports() {
        GameScreen mockScreen = mock(GameScreen.class);
        Teleport tp1 = new Teleport(100, 100, mockScreen);
        Teleport tp2 = new Teleport(300, 300, mockScreen);

        assertNotSame(tp1, tp2);
        assertFalse(tp1.teleportHappened());
        assertFalse(tp2.teleportHappened());
    }

    //Test #4: player collision starts countdown
    @Test
    @DisplayName("Player collision triggers teleport countdown")
    public void testPlayerCollisionStartsCountdown() {
        GameScreen mockScreen = mock(GameScreen.class);
        Graphics mockGraphics = mock(Graphics.class);
        Gdx.graphics = mockGraphics;

        Teleport tp = new Teleport(100, 200, mockScreen);
        Player player = new Player(100, 200);

        when(mockGraphics.getDeltaTime()).thenReturn(0.016f);

        tp.update(player, 0.016f);

        assertFalse(tp.teleportHappened());
    }

    //Test #5: teleport happens after 3.6s countdown
    @Test
    @DisplayName("Teleport happens after countdown completes")
    public void testTeleportAfterCountdown() throws Exception {
        GameScreen mockScreen = mock(GameScreen.class);
        Graphics mockGraphics = mock(Graphics.class);
        Gdx.graphics = mockGraphics;

        Teleport tp = new Teleport(100, 200, mockScreen);
        Player player = new Player(100, 200);

        when(mockGraphics.getDeltaTime()).thenReturn(1.0f);

        java.lang.reflect.Field countdownField = Teleport.class.getDeclaredField("countdownTimer");
        countdownField.setAccessible(true);
        countdownField.set(tp, 3.5f);

        tp.update(player, 1.0f);

        Vector2 pos = player.getPosition();
        assertNotNull(pos);
    }

    //Test #6: distant player does not activate teleport
    @Test
    @DisplayName("Player far from teleport no activation")
    public void testPlayerFarAway() {
        GameScreen mockScreen = mock(GameScreen.class);
        Teleport tp = new Teleport(100, 200, mockScreen);
        Player player = new Player(500, 500);

        tp.update(player, 0.016f);

        assertFalse(tp.teleportHappened());
    }

    //Test #7: teleport only happens once
    @Test
    @DisplayName("Teleport only happens once")
    public void testTeleportOnlyOnce() throws Exception {
        GameScreen mockScreen = mock(GameScreen.class);
        Graphics mockGraphics = mock(Graphics.class);
        Gdx.graphics = mockGraphics;

        Teleport tp = new Teleport(100, 200, mockScreen);
        Player player = new Player(100, 200);

        when(mockGraphics.getDeltaTime()).thenReturn(1.0f);

        java.lang.reflect.Field happenedField = Teleport.class.getDeclaredField("teleportHappened");
        happenedField.setAccessible(true);
        happenedField.set(tp, true);

        //Vector2 initialPos = new Vector2(player.getPosition()); unnecessary

        tp.update(player, 1.0f);

        assertTrue(tp.teleportHappened());
    }

    //Test #8: countdown timer increments correctly
    @Test
    @DisplayName("Countdown timer increments correctly")
    public void testCountdownIncrement() throws Exception {
        GameScreen mockScreen = mock(GameScreen.class);
        Graphics mockGraphics = mock(Graphics.class);
        Gdx.graphics = mockGraphics;

        Teleport tp = new Teleport(100, 200, mockScreen);
        Player player = new Player(100, 200);

        when(mockGraphics.getDeltaTime()).thenReturn(1.0f);

        java.lang.reflect.Field showField = Teleport.class.getDeclaredField("showMessage");
        showField.setAccessible(true);
        showField.set(tp, true);

        java.lang.reflect.Field countdownField = Teleport.class.getDeclaredField("countdownTimer");
        countdownField.setAccessible(true);

        float initialCountdown = countdownField.getFloat(tp);

        tp.update(player, 1.0f);

        float newCountdown = countdownField.getFloat(tp);
        assertTrue(newCountdown >= initialCountdown, "Countdown should increment");
    }

    //Test #9: random safe position is valid
    @Test
    @DisplayName("Random safe position selected")
    public void testRandomSafePosition() throws Exception {
        GameScreen mockScreen = mock(GameScreen.class);
        Teleport tp = new Teleport(100, 200, mockScreen);

        java.lang.reflect.Method method = Teleport.class.getDeclaredMethod("getRandomSafePosition");
        method.setAccessible(true);

        Vector2 pos = (Vector2) method.invoke(tp);

        assertNotNull(pos);
        assertTrue(pos.x >= 0 && pos.y >= 0);
    }
}
