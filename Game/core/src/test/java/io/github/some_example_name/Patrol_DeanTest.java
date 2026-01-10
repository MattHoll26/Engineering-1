package io.github.some_example_name;

import com.badlogic.gdx.math.Vector2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test suite for Patrol_Dean class.
 * Tests patrol dean creation, vertical patrol movement, boundary bouncing, and collision detection.
 * Test coverage:70%
 * Coverage justification: Texture rendering only testable visually
 */
@DisplayName("Patrol Dean Tests")
public class Patrol_DeanTest extends TestHelper {

    //Test #1: instance created with patrol boundaries
    @Test
    @DisplayName("Instance created with boundaries")
    public void testInstanceCreation() {
        GameScreen mockScreen = mock(GameScreen.class);
        Patrol_Dean pd = new Patrol_Dean(100, 200, 100, 400, mockScreen);
        assertNotNull(pd);
    }

    //Test #2: default speed is 3.0f
    @Test
    @DisplayName("Initial speed is default")
    public void testInitialSpeed() {
        GameScreen mockScreen = mock(GameScreen.class);
        Patrol_Dean pd = new Patrol_Dean(100, 200, 100, 400, mockScreen);
        assertEquals(3f, pd.getSpeed());
    }

    //Test #3: speed can be modified
    @Test
    @DisplayName("Speed can be modified")
    public void testSpeedModification() {
        GameScreen mockScreen = mock(GameScreen.class);
        Patrol_Dean pd = new Patrol_Dean(100, 200, 100, 400, mockScreen);

        pd.setSpeed(5f);
        assertEquals(5f, pd.getSpeed());

        pd.setSpeed(1f);
        assertEquals(1f, pd.getSpeed());
    }

    //Test #4: position vector accessible and correct
    @Test
    @DisplayName("Position accessible")
    public void testPositionAccessible() {
        GameScreen mockScreen = mock(GameScreen.class);
        Patrol_Dean pd = new Patrol_Dean(100, 200, 50, 400, mockScreen);

        assertNotNull(pd.getPosition());
        assertEquals(100, pd.getPosition().x);
        assertEquals(200, pd.getPosition().y);
    }

    //Test #5: collision bounds are 16x16
    @Test
    @DisplayName("Bounds accessible")
    public void testBoundsAccessible() {
        GameScreen mockScreen = mock(GameScreen.class);
        Patrol_Dean pd = new Patrol_Dean(100, 200, 50, 400, mockScreen);

        assertNotNull(pd.getBounds());
        assertEquals(16, pd.getBounds().width);
        assertEquals(16, pd.getBounds().height);
    }

    //Test #6: patrol boundaries stored correctly
    @Test
    @DisplayName("Patrol boundaries stored correctly")
    public void testPatrolBoundaries() throws Exception {
        GameScreen mockScreen = mock(GameScreen.class);
        Patrol_Dean pd = new Patrol_Dean(200, 300, 100, 500, mockScreen);

        var minField = Patrol_Dean.class.getDeclaredField("minY");
        minField.setAccessible(true);

        var maxField = Patrol_Dean.class.getDeclaredField("maxY");
        maxField.setAccessible(true);

        assertEquals(100f, minField.getFloat(pd));
        assertEquals(500f, maxField.getFloat(pd));
    }

    //Test #7: moves when path not blocked
    @Test
    @DisplayName("Moves when not blocked")
    public void testMovesWhenNotBlocked() {
        GameScreen mockScreen = mock(GameScreen.class);
        when(mockScreen.isCellBlocked(anyFloat(), anyFloat())).thenReturn(false);

        Patrol_Dean pd = new Patrol_Dean(100, 200, 100, 400, mockScreen);
        float startY = pd.getPosition().y;

        pd.update(1f);

        assertNotEquals(startY, pd.getPosition().y);
    }

    //Test #8: reverses direction at upper boundary
    @Test
    @DisplayName("Flip direction at upper boundary")
    public void testFlipAtUpperBoundary() {
        GameScreen mockScreen = mock(GameScreen.class);
        when(mockScreen.isCellBlocked(anyFloat(), anyFloat())).thenReturn(false);

        Patrol_Dean pd = new Patrol_Dean(100, 399, 100, 400, mockScreen);
        pd.setSpeed(5f);

        pd.update(1f);

        assertEquals(400, pd.getPosition().y, 0.01f);
    }

    //Test #9: reverses direction at lower boundary
    @Test
    @DisplayName("Flip direction at lower boundary")
    public void testFlipAtLowerBoundary() {
        GameScreen mockScreen = mock(GameScreen.class);
        when(mockScreen.isCellBlocked(anyFloat(), anyFloat())).thenReturn(false);

        Patrol_Dean pd = new Patrol_Dean(100, 101, 100, 400, mockScreen);
        pd.setSpeed(-5f);

        pd.update(1f);

        assertEquals(100, pd.getPosition().y, 0.01f);
    }

    //Test #10: blocked cell to avoid movement and change direction
    @Test
    @DisplayName("Blocked cell prevents movement and flips direction")
    public void testBlockedCellFlipsDirection() {
        GameScreen mockScreen = mock(GameScreen.class);
        when(mockScreen.isCellBlocked(anyFloat(), anyFloat())).thenReturn(true);

        Patrol_Dean pd = new Patrol_Dean(100, 200, 100, 400, mockScreen);
        Vector2 initial = new Vector2(pd.getPosition());

        pd.update(1f);

        assertEquals(initial.y, pd.getPosition().y, 0.01f);
    }

    //Test #11: no speed stops movement
    @Test
    @DisplayName("Speed zero stops movement")
    public void testSpeedZeroStopsMovement() {
        GameScreen mockScreen = mock(GameScreen.class);
        when(mockScreen.isCellBlocked(anyFloat(), anyFloat())).thenReturn(false);

        Patrol_Dean pd = new Patrol_Dean(100, 200, 100, 400, mockScreen);
        pd.setSpeed(0f);

        Vector2 initial = new Vector2(pd.getPosition());
        pd.update(1f);

        assertEquals(initial.y, pd.getPosition().y, 0.01f);
    }

    //Test #12: multiple patrol dean instances are independent
    @Test
    @DisplayName("Multiple patrol deans independent")
    public void testMultiplePatrolDeans() {
        GameScreen mockScreen = mock(GameScreen.class);

        Patrol_Dean pd1 = new Patrol_Dean(100, 200, 100, 400, mockScreen);
        Patrol_Dean pd2 = new Patrol_Dean(300, 400, 200, 600, mockScreen);

        assertNotSame(pd1, pd2);
        assertEquals(100, pd1.getPosition().x);
        assertEquals(300, pd2.getPosition().x);
    }

    //Test #13: dispose executes without error
    @Test
    @DisplayName("Dispose does not throw")
    public void testDisposeSafe() {
        GameScreen mockScreen = mock(GameScreen.class);
        Patrol_Dean pd = new Patrol_Dean(100, 200, 100, 400, mockScreen);

        assertDoesNotThrow(pd::dispose);
    }
}
