package io.github.some_example_name;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for Save_Leaderboard.ScoreEntry class.
 * Tests score entry creation, field assignment, and edge cases for leaderboard data.
 * Test coverage:100%
 * Note: Only testing the inner ScoreEntry data class, not the parent Save_Leaderboard logic
 */
@DisplayName("Leaderboard ScoreEntry Tests")
public class Save_LeaderboardTest extends TestHelper {

    //Test #1: default constructor creates instance
    @Test
    @DisplayName("Default constructor creates instance")
    public void testDefaultConstructor() {
        Save_Leaderboard.ScoreEntry entry = new Save_Leaderboard.ScoreEntry();
        assertNotNull(entry);
    }

    //Test #2: parameterized constructor assigns fields
    @Test
    @DisplayName("Parameterized constructor assigns fields")
    public void testParameterizedConstructor() {
        Save_Leaderboard.ScoreEntry entry = new Save_Leaderboard.ScoreEntry("Alice", 500);

        assertEquals("Alice", entry.fullName);
        assertEquals(500, entry.score);
    }

    //Test #3: fields are publicly accessible
    @Test
    @DisplayName("Fields are publicly accessible")
    public void testFieldsPublic() {
        Save_Leaderboard.ScoreEntry entry = new Save_Leaderboard.ScoreEntry();

        entry.fullName = "Bob";
        entry.score = 1000;

        assertEquals("Bob", entry.fullName);
        assertEquals(1000, entry.score);
    }

    //Test #4: multiple entries are independent
    @Test
    @DisplayName("Multiple entries independent")
    public void testMultipleEntriesIndependent() {
        Save_Leaderboard.ScoreEntry e1 = new Save_Leaderboard.ScoreEntry("Player1", 100);
        Save_Leaderboard.ScoreEntry e2 = new Save_Leaderboard.ScoreEntry("Player2", 200);

        assertNotSame(e1, e2);
        assertEquals("Player1", e1.fullName);
        assertEquals("Player2", e2.fullName);
        assertEquals(100, e1.score);
        assertEquals(200, e2.score);
    }

    //Test #5: negative scores allowed
    @Test
    @DisplayName("Negative scores allowed")
    public void testNegativeScores() {
        Save_Leaderboard.ScoreEntry entry = new Save_Leaderboard.ScoreEntry("Loser", -50);
        assertEquals(-50, entry.score);
    }

    //Test #6: zero score allowed
    @Test
    @DisplayName("Zero score allowed")
    public void testZeroScore() {
        Save_Leaderboard.ScoreEntry entry = new Save_Leaderboard.ScoreEntry("Newbie", 0);
        assertEquals(0, entry.score);
    }

    //Test #7: large scores allowed
    @Test
    @DisplayName("Large scores allowed")
    public void testLargeScore() {
        Save_Leaderboard.ScoreEntry entry = new Save_Leaderboard.ScoreEntry("Pro", 999999);
        assertEquals(999999, entry.score);
    }

    //Test #8: empty name string accepted
    @Test
    @DisplayName("Empty name allowed")
    public void testEmptyName() {
        Save_Leaderboard.ScoreEntry entry = new Save_Leaderboard.ScoreEntry("", 100);
        assertEquals("", entry.fullName);
    }

    //Test #9: special characters in name preserved
    @Test
    @DisplayName("Special characters in name")
    public void testSpecialCharactersInName() {
        Save_Leaderboard.ScoreEntry entry = new Save_Leaderboard.ScoreEntry("Player★123", 500);
        assertEquals("Player★123", entry.fullName);
    }
}
