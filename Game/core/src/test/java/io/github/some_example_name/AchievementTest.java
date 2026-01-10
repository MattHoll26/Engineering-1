package io.github.some_example_name;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for Achievement class.
 * Updated to ensure proper field assignment, bonus or penalty handling, and edge cases.
 * Test coverage : 100%
 * Fully automated
 */
@DisplayName("Achievement Tests")
public class AchievementTest extends TestHelper {
    //Test #1: field assignment for basic constructor
    @Test
    @DisplayName("Constructor assigns all fields correctly")
    public void testConstructorAssignsFields() {
        Achievement achievement = new Achievement(
            "Speed Runner",
            "Completed the game under 2 minutes, YAHOO!",
            50
        );

        assertEquals("Speed Runner", achievement.name);
        assertEquals("Completed the game under 2 minutes, YAHOO!", achievement.description);
        assertEquals(50, achievement.bonusScore);
    }
    // Test #2: positive bonus scores are stored correctly
    @Test
    @DisplayName("Positive bonus score works")
    public void testPositiveBonusScore() {
        Achievement achievement = new Achievement("Winner", "You won!", 100);

        assertEquals(100, achievement.bonusScore,
            "bonus score should be assigned correctly");
    }

    //Test #3: minus bonus score works correctly
    @Test
    @DisplayName("minus bonus score works (penalty)")
    public void testNegativeBonusScore() {
        Achievement achievement = new Achievement(
            "Caught by Dean",
            "Got caught 5 times",
            -20
        );

        assertEquals(-20, achievement.bonusScore,
            "penalty score should work");
    }

    //Test #4: test for zero scare is valid
    @Test
    @DisplayName("Zero bonus score works")
    public void testZeroBonusScore() {
        Achievement achievement = new Achievement(
            "Participation",
            "You tried",
            0
        );

        assertEquals(0, achievement.bonusScore,
            "Zero bonus score should be allowed");
    }

    //#Test 5:multiple achievement objects doesnt interfere
    @Test
    @DisplayName("Independent multiple instances")
    public void testMultipleInstancesIndependent() {
        Achievement a1 = new Achievement("First", "First achievement", 10);
        Achievement a2 = new Achievement("Second", "Second achievement", 20);

        assertNotSame(a1, a2, "Should be different instances");
        assertEquals("First", a1.name);
        assertEquals("Second", a2.name);
        assertEquals(10, a1.bonusScore);
        assertEquals(20, a2.bonusScore);
    }

    //Test #6:long text descriptions are stored properly
    @Test
    @DisplayName("Long description handled")
    public void testLongDescription() {
        String longDesc = "This is a very long description that explains " +
            "in great detail how this achievement was earned " +
            "and what it means for the player's progression. YEEHEEHEEHA";

        Achievement achievement = new Achievement("Detailed", longDesc, 5);

        assertEquals(longDesc, achievement.description,
            "Long descriptions should be stored correctly");
    }

    //Test #7:empty name strings are accepted
    @Test
    @DisplayName("Empty name handled")
    public void testEmptyName() {
        Achievement achievement = new Achievement("", "No name achievement", 10);

        assertEquals("", achievement.name,
            "Empty name should be allowed (though not recommended)");
    }

    //Test #8:empty description strings are accepted
    @Test
    @DisplayName("Empty description handled")
    public void testEmptyDescription() {
        Achievement achievement = new Achievement("Mystery", "", 15);

        assertEquals("", achievement.description,
            "Empty description should be allowed");
    }

    //Test #9:large bonus values work
    @Test
    @DisplayName("Large bonus score")
    public void testLargeBonusScore() {
        Achievement achievement = new Achievement("Legend", "Maximum points", 1000);

        assertEquals(1000, achievement.bonusScore,
            "Large bonus scores should work");
    }

    //Test #10:large penalty values work
    @Test
    @DisplayName("Large penalty score")
    public void testLargePenalty() {
        Achievement achievement = new Achievement("Disaster", "Everything wrong", -500);

        assertEquals(-500, achievement.bonusScore,
            "Large penalties should work");
    }

    //Test #11:special characters check
    @Test
    @DisplayName("Special characters in name")
    public void testSpecialCharactersInName() {
        Achievement achievement = new Achievement(
            "★Mewer2000★",
            "Got all collectibles!",
            75
        );

        assertEquals("★Mewer2000★", achievement.name,
            "Special characters in name should be allowed");
    }

    //Test #12:special characters in description
    @Test
    @DisplayName("Special characters in description")
    public void testSpecialCharactersInDescription() {
        Achievement achievement = new Achievement(
            "Speedster",
            "Completed in < 90 seconds! (90% faster than average)",
            80
        );

        assertTrue(achievement.description.contains("<"),
            "Special characters in description should be preserved");
        assertTrue(achievement.description.contains("%"),
            "Percentage symbols should be preserved");
    }
}
