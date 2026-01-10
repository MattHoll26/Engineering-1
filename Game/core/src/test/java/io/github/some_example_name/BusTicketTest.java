package io.github.some_example_name;
import com.badlogic.gdx.math.Vector2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for BusTicket class.
 * Tests ticket creation, position handling, collection state management, and discovery mechanics.
 * Test coverage: 32%
 * Automated: Constructor, position management, collection or discovery state logic, edge cases
 */
@DisplayName("Bus Ticket Tests")
public class BusTicketTest extends TestHelper {

    //Test #1: instance created at specified position
    @Test
    @DisplayName("Instance created at position")
    public void testInstanceCreation() {
        BusTicket ticket = new BusTicket(100, 200);
        assertNotNull(ticket);
    }

    //Test #2: ticket not collected initially
    @Test
    @DisplayName("Not collected initially")
    public void testNotCollectedInitially() {
        BusTicket ticket = new BusTicket(100, 200);
        assertFalse(ticket.isCollected());
    }

    //Test #3: position vector accessible and correct
    @Test
    @DisplayName("Position accessible")
    public void testPositionAccessible() {
        BusTicket ticket = new BusTicket(150, 250);

        assertNotNull(ticket.getPosition());
        assertEquals(150, ticket.getPosition().x);
        assertEquals(250, ticket.getPosition().y);
    }

    //Test #4: collect method sets collected to true
    @Test
    @DisplayName("Collect sets collected to true")
    public void testCollect() {
        BusTicket ticket = new BusTicket(100, 200);

        assertFalse(ticket.isCollected());
        ticket.collect();
        assertTrue(ticket.isCollected());
    }

    //Test #5: discover method executes without error
    @Test
    @DisplayName("Discover method works")
    public void testDiscover() {
        BusTicket ticket = new BusTicket(100, 200);

        assertDoesNotThrow(ticket::discover);
    }

    //Test #6: multiple ticket instances are independent
    @Test
    @DisplayName("Multiple tickets independent")
    public void testMultipleTickets() {
        BusTicket t1 = new BusTicket(100, 100);
        BusTicket t2 = new BusTicket(200, 200);

        assertNotSame(t1, t2);

        t1.collect();
        assertTrue(t1.isCollected());
        assertFalse(t2.isCollected());
    }

    //Test #7: collection state persists after a lot of collect calls
    @Test
    @DisplayName("Cannot uncollect after collecting")
    public void testCannotUncollect() {
        BusTicket ticket = new BusTicket(100, 200);

        ticket.collect();
        assertTrue(ticket.isCollected());

        // collect again should not change state
        ticket.collect();
        assertTrue(ticket.isCollected());
    }

    //Test #8: discover can be called a lot of times safely
    @Test
    @DisplayName("Discover can be called multiple times")
    public void testDiscoverMultipleTimes() {
        BusTicket ticket = new BusTicket(100, 200);

        assertDoesNotThrow(() -> {
            ticket.discover();
            ticket.discover();
            ticket.discover();
        });
    }

    //Test #9: constructor parameters match position values
    @Test
    @DisplayName("Position at creation matches constructor")
    public void testPositionMatchesConstructor() {
        BusTicket ticket = new BusTicket(175, 225);

        assertEquals(175, ticket.getPosition().x);
        assertEquals(225, ticket.getPosition().y);
    }

    //Test #10: position vector can be modified
    @Test
    @DisplayName("Position is mutable")
    public void testPositionMutable() {
        BusTicket ticket = new BusTicket(100, 100);

        Vector2 pos = ticket.getPosition();
        pos.set(200, 300);

        assertEquals(200, ticket.getPosition().x);
        assertEquals(300, ticket.getPosition().y);
    }

    //Test #11: ticket can be placed at origin coordinates
    @Test
    @DisplayName("Ticket at origin")
    public void testTicketAtOrigin() {
        BusTicket ticket = new BusTicket(0, 0);

        assertEquals(0, ticket.getPosition().x);
        assertEquals(0, ticket.getPosition().y);
        assertFalse(ticket.isCollected());
    }

    //Test #12: negative coordinates are accepted
    @Test
    @DisplayName("Ticket at negative coordinates")
    public void testTicketNegativeCoordinates() {
        BusTicket ticket = new BusTicket(-50, -100);

        assertEquals(-50, ticket.getPosition().x);
        assertEquals(-100, ticket.getPosition().y);
    }

    //Test #13: collection state independent when position changes
    @Test
    @DisplayName("Collection state independent of position")
    public void testCollectionIndependentOfPosition() {
        BusTicket ticket = new BusTicket(100, 100);

        ticket.getPosition().set(200, 200);
        assertFalse(ticket.isCollected(), "Position change should not affect collection");

        ticket.collect();
        assertTrue(ticket.isCollected());

        ticket.getPosition().set(300, 300);
        assertTrue(ticket.isCollected(), "Collection persists after position change");
    }

    //Test #14: discover and collect methods work independently
    @Test
    @DisplayName("Discover before and after collection")
    public void testDiscoverWithCollection() {
        BusTicket ticket = new BusTicket(100, 200);

        ticket.discover();
        assertFalse(ticket.isCollected());

        ticket.collect();
        assertTrue(ticket.isCollected());

        ticket.discover();
        assertTrue(ticket.isCollected(), "Collection state should persist");
    }
}
