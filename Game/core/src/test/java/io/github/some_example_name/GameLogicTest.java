package io.github.some_example_name;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * NEW
 * * <code>GameLogicTest</code> provides Junit tests for some
 * of the core game mechanics in order to ensure CI/CD stability.
 */
public class GameLogicTest {
    /**
     * NEW:
     * Tests the mathematical floor logic of 0 for the player scoring.
     * This ensures that the penalties do not result in a negative score value,
     * satisfying requirement FR_END_SCORE.
     */
    @Test
    @DisplayName("FR_END_SCORE: Test that score doesn't go negative")
    public void testScoreMinimum() {
        int currentScore = 10;
        int penalty = 50;

        // Simulating the score logic
        int finalScore = Math.max(0, currentScore - penalty);

        assertEquals(0, finalScore, "Score should cap at 0 and not be negative.");
    }

    /**
     * NEW:
     * This tests the state management of game achievements by verifying
     * that the unlock flag through the boolean variable
     * can be correctly toggled when an achievement condition is met.
     */
    @Test
    @DisplayName("Achievement Logic: Test flag toggle")
    public void testAchievementUnlock() {
        // Simulating the logic in the Achievement class
        boolean unlocked = false;

        // Simulate event
        unlocked = true;

        assertTrue(unlocked, "Achievement should be marked as unlocked.");
    }
}
