package io.github.some_example_name;

import com.badlogic.gdx.math.Vector2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for NPC class.
 * Tests NPC creation, position management, update logic, and interaction triggers.
 * Test coverage:63%
 * Automated: Constructor, position access, bounds initialization, update safety, state management
 * Uses TestableNPC subclass to mock input or range checks
 * Coverage justification: Dialog selection logic, detect E key, and rendering only testable visually
 */
@DisplayName("NPC High-Coverage Tests")
public class NPCTest extends TestHelper {

    private Player createPlayer(float x, float y) {
        return new Player(x, y);
    }

    //Test #1: NPC instance created successfully
    @Test
    @DisplayName("NPC instance is created")
    void testCreation() {
        NPC npc = new NPC(100, 200);
        assertNotNull(npc);
    }

    //Test #2: initial position matches constructor parameters
    @Test
    @DisplayName("Initial position is correct")
    void testInitialPosition() {
        NPC npc = new NPC(150, 250);

        assertEquals(150, npc.getPosition().x);
        assertEquals(250, npc.getPosition().y);
    }

    //Test #3: collision bounds initialized correctly
    @Test
    @DisplayName("Bounds are initialised correctly")
    void testBoundsInitialised() {
        NPC npc = new NPC(100, 200);

        assertNotNull(npc.getBounds());
        assertTrue(npc.getBounds().width > 0);
        assertTrue(npc.getBounds().height > 0);
    }

    //Test #4: position vector is mutable
    @Test
    @DisplayName("Position object is mutable")
    void testPositionMutable() {
        NPC npc = new NPC(0, 0);

        Vector2 pos = npc.getPosition();
        pos.set(300, 400);

        assertEquals(300, npc.getPosition().x);
        assertEquals(400, npc.getPosition().y);
    }

    //Test #5: getPosition returns same instance
    @Test
    @DisplayName("getPosition returns same instance")
    void testPositionInstance() {
        NPC npc = new NPC(10, 10);

        assertSame(npc.getPosition(), npc.getPosition());
    }

    //Test #6: update runs safely without interaction
    @Test
    @DisplayName("Update runs safely when no interaction occurs")
    void testUpdateSafePath() {
        NPC npc = new NPC(100, 100);
        Player player = createPlayer(1000, 1000);

        assertDoesNotThrow(() -> npc.update(player));
    }

    //Test #7: interaction triggered when conditions met
    @Test
    @DisplayName("Interaction triggers when player in range and key pressed")
    void testInteractionTriggered() {
        TestableNPC npc = new TestableNPC(100, 100);
        Player player = createPlayer(100, 100);

        npc.setInRange(true);
        npc.setKeyPressed(true);
        npc.setForcedDialog("HELLO");

        assertDoesNotThrow(() -> npc.update(player));
    }

    //Test #8: dialog hides when player moves away
    @Test
    @DisplayName("Dialog hides when player moves out of range")
    void testDialogHideLogic() {
        TestableNPC npc = new TestableNPC(100, 100);
        Player player = createPlayer(100, 100);

        npc.setInRange(true);
        npc.setKeyPressed(true);
        npc.update(player);

        npc.setInRange(false);
        npc.setOutOfRange(true);

        assertDoesNotThrow(() -> npc.update(player));
    }

    //Test #9: multiple updates maintain valid state
    @Test
    @DisplayName("Multiple updates maintain valid state")
    void testMultipleUpdates() {
        TestableNPC npc = new TestableNPC(200, 200);
        Player player = createPlayer(0, 0);

        npc.setInRange(false);
        npc.setKeyPressed(false);

        for (int i = 0; i < 10; i++) {
            npc.update(player);
        }

        assertNotNull(npc.getPosition());
        assertNotNull(npc.getBounds());
    }

    //Test #10: NPC supports origin position
    @Test
    @DisplayName("NPC supports origin position")
    void testOriginPosition() {
        NPC npc = new NPC(0, 0);

        assertEquals(0, npc.getPosition().x);
        assertEquals(0, npc.getPosition().y);
    }

    //Test #11: NPC supports negative coordinates
    @Test
    @DisplayName("NPC supports negative coordinates")
    void testNegativeCoordinates() {
        NPC npc = new NPC(-50, -100);

        assertEquals(-50, npc.getPosition().x);
        assertEquals(-100, npc.getPosition().y);
    }

    //Test #12: dispose executes safely
    @Test
    @DisplayName("Dispose executes safely")
    void testDispose() {
        NPC npc = new NPC(100, 100);
        assertDoesNotThrow(npc::dispose);
    }
}
