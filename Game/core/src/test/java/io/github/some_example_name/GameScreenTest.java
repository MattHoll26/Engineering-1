package io.github.some_example_name;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Field;

/**
 * Test suite for GameScreen class.
 * Tests the only testable calculation method: calculateTotalPenalty().
 * Test coverage: <5%
 * Automated: calculateTotalPenalty() - penalty scoring logic only
 * Coverage justification: GameScreen is primarily a UI, as GameScreen has been tested manually with ID GS
 */
@DisplayName("GameScreen Penalty Calculation Tests")
public class GameScreenTest {

    private static final sun.misc.Unsafe unsafe;
    static {
        try {
            Field f = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            unsafe = (sun.misc.Unsafe) f.get(null);
        } catch (Exception e) {
            throw new RuntimeException("Cannot access Unsafe", e);
        }
    }

    private GameScreen createGameScreenWithoutConstructor() throws Exception {
        return (GameScreen) unsafe.allocateInstance(GameScreen.class);
    }

    private void setField(GameScreen screen, String fieldName, int value) throws Exception {
        Field field = GameScreen.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(screen, value);
    }

    //Test #1: no penalties returns zero
    @Test
    @DisplayName("calculateTotalPenalty() with no penalties should return 0")
    void testCalculateTotalPenaltyNoPenalties() throws Exception {
        GameScreen screen = createGameScreenWithoutConstructor();
        setField(screen, "timesCaughtByDean", 0);
        setField(screen, "timesDrowned", 0);
        setField(screen, "timesCaughtByPatrol", 0);

        int penalty = screen.calculateTotalPenalty();

        assertEquals(0, penalty,
            "No penalties should result in 0 total penalty");
    }

    //Test #2: dean catches apply 5 points each
    @Test
    @DisplayName("calculateTotalPenalty() with dean catches should apply 5 points per catch")
    void testCalculateTotalPenaltyDeanCatches() throws Exception {
        GameScreen screen = createGameScreenWithoutConstructor();
        setField(screen, "timesCaughtByDean", 3);
        setField(screen, "timesDrowned", 0);
        setField(screen, "timesCaughtByPatrol", 0);

        int penalty = screen.calculateTotalPenalty();

        assertEquals(15, penalty,
            "3 dean catches should result in 15 point penalty");
    }

    //Test #3: drowning applies 10 points per occurrence
    @Test
    @DisplayName("calculateTotalPenalty() with drowning should apply 10 points per drown")
    void testCalculateTotalPenaltyDrowning() throws Exception {
        GameScreen screen = createGameScreenWithoutConstructor();
        setField(screen, "timesCaughtByDean", 0);
        setField(screen, "timesDrowned", 2);
        setField(screen, "timesCaughtByPatrol", 0);

        int penalty = screen.calculateTotalPenalty();

        assertEquals(20, penalty,
            "2 drownings should result in 20 point penalty");
    }

    //Test #4: patrol catches apply 5 points each
    @Test
    @DisplayName("calculateTotalPenalty() with patrol catches should apply 5 points per catch")
    void testCalculateTotalPenaltyPatrolCatches() throws Exception {
        GameScreen screen = createGameScreenWithoutConstructor();
        setField(screen, "timesCaughtByDean", 0);
        setField(screen, "timesDrowned", 0);
        setField(screen, "timesCaughtByPatrol", 4);

        int penalty = screen.calculateTotalPenalty();

        assertEquals(20, penalty,
            "4 patrol catches should result in 20 point penalty");
    }

    //Test #5: multiple penalty types sum correctly
    @Test
    @DisplayName("calculateTotalPenalty() with multiple penalty types should sum correctly")
    void testCalculateTotalPenaltyMultipleTypes() throws Exception {
        GameScreen screen = createGameScreenWithoutConstructor();
        setField(screen, "timesCaughtByDean", 2);
        setField(screen, "timesDrowned", 1);
        setField(screen, "timesCaughtByPatrol", 3);

        int penalty = screen.calculateTotalPenalty();

        assertEquals(35, penalty,
            "Mixed penalties should sum correctly: 2*5 + 1*10 + 3*5 = 35");
    }

    //Test #6: high dean catch count
    @Test
    @DisplayName("calculateTotalPenalty() with high dean catches")
    void testCalculateTotalPenaltyHighDeanCatches() throws Exception {
        GameScreen screen = createGameScreenWithoutConstructor();
        setField(screen, "timesCaughtByDean", 10);
        setField(screen, "timesDrowned", 0);
        setField(screen, "timesCaughtByPatrol", 0);

        int penalty = screen.calculateTotalPenalty();

        assertEquals(50, penalty,
            "10 dean catches should result in 50 point penalty");
    }

    //Test #7: high drowning count
    @Test
    @DisplayName("calculateTotalPenalty() with high drowning count")
    void testCalculateTotalPenaltyHighDrowning() throws Exception {
        GameScreen screen = createGameScreenWithoutConstructor();
        setField(screen, "timesCaughtByDean", 0);
        setField(screen, "timesDrowned", 5);
        setField(screen, "timesCaughtByPatrol", 0);

        int penalty = screen.calculateTotalPenalty();

        assertEquals(50, penalty,
            "5 drownings should result in 50 point penalty");
    }

    //Test #8: maximum penalty scenario
    @Test
    @DisplayName("calculateTotalPenalty() with maximum penalties")
    void testCalculateTotalPenaltyMaximum() throws Exception {
        GameScreen screen = createGameScreenWithoutConstructor();
        setField(screen, "timesCaughtByDean", 20);
        setField(screen, "timesDrowned", 10);
        setField(screen, "timesCaughtByPatrol", 15);

        int penalty = screen.calculateTotalPenalty();

        assertEquals(275, penalty,
            "Maximum penalties should sum correctly: 20*5 + 10*10 + 15*5 = 275");
    }

    //Test #9: penalty weights are correct
    @Test
    @DisplayName("calculateTotalPenalty() penalty weights are correct")
    void testCalculateTotalPenaltyWeights() throws Exception {
        GameScreen screen = createGameScreenWithoutConstructor();
        setField(screen, "timesCaughtByDean", 1);
        setField(screen, "timesDrowned", 1);
        setField(screen, "timesCaughtByPatrol", 1);

        int penalty = screen.calculateTotalPenalty();

        assertEquals(20, penalty,
            "One of each penalty: 1*5 + 1*10 + 1*5 = 20");
    }

    //Test #10: drowning penalty is worth more than catches
    @Test
    @DisplayName("calculateTotalPenalty() drowning is worth more than catches")
    void testCalculateTotalPenaltyDrowningVsCatches() throws Exception {
        GameScreen screen1 = createGameScreenWithoutConstructor();
        setField(screen1, "timesCaughtByDean", 0);
        setField(screen1, "timesDrowned", 1);
        setField(screen1, "timesCaughtByPatrol", 0);

        GameScreen screen2 = createGameScreenWithoutConstructor();
        setField(screen2, "timesCaughtByDean", 1);
        setField(screen2, "timesDrowned", 0);
        setField(screen2, "timesCaughtByPatrol", 0);

        int drowningPenalty = screen1.calculateTotalPenalty();
        int catchPenalty = screen2.calculateTotalPenalty();

        assertTrue(drowningPenalty > catchPenalty,
            "Drowning penalty (10) should be greater than catch penalty (5)");
        assertEquals(10, drowningPenalty);
        assertEquals(5, catchPenalty);
    }

    //Test #11: dean and patrol catches have equal weight
    @Test
    @DisplayName("calculateTotalPenalty() dean and patrol catches have equal weight")
    void testCalculateTotalPenaltyDeanVsPatrol() throws Exception {
        GameScreen screen1 = createGameScreenWithoutConstructor();
        setField(screen1, "timesCaughtByDean", 3);
        setField(screen1, "timesDrowned", 0);
        setField(screen1, "timesCaughtByPatrol", 0);

        GameScreen screen2 = createGameScreenWithoutConstructor();
        setField(screen2, "timesCaughtByDean", 0);
        setField(screen2, "timesDrowned", 0);
        setField(screen2, "timesCaughtByPatrol", 3);

        int deanPenalty = screen1.calculateTotalPenalty();
        int patrolPenalty = screen2.calculateTotalPenalty();

        assertEquals(deanPenalty, patrolPenalty,
            "Dean catches and patrol catches should have equal weight (5 points each)");
        assertEquals(15, deanPenalty);
        assertEquals(15, patrolPenalty);
    }

    //Test #12: realistic gameplay scenario
    @Test
    @DisplayName("calculateTotalPenalty() realistic gameplay scenario")
    void testCalculateTotalPenaltyRealisticScenario() throws Exception {
        GameScreen screen = createGameScreenWithoutConstructor();
        setField(screen, "timesCaughtByDean", 2);
        setField(screen, "timesDrowned", 1);
        setField(screen, "timesCaughtByPatrol", 1);

        int penalty = screen.calculateTotalPenalty();

        assertEquals(25, penalty,
            "Realistic scenario: 2 dean + 1 drown + 1 patrol = 25 points");
    }
}
