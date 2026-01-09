package io.github.some_example_name;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.MockedConstruction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

/**
 * Test suite for MyGame class.
 * Tests player name management, full name formatting, null/whitespace handling, and screen creation.
 * Test coverage:100%
 * Fully automated
 */
@DisplayName("MyGame Tests")
public class MyGameTest extends TestHelper{

    private MyGame game;

    @BeforeEach
    public void setUp() {
        game = new MyGame();
    }

    @Test //Test #1
    @DisplayName("TEST_MYGAME_01: Set and get first name")
    public void testSetFirstName() {
        game.setPlayerName("Daneena", "");
        assertEquals("Daneena", game.getPlayerFirstName());
    }

    @Test //Test #2
    @DisplayName("TEST_MYGAME_02: Set and get last name")
    public void testSetLastName() {
        game.setPlayerName("Daneena","Roy");
        assertEquals("Roy",game.getPlayerLastName());
    }

    @Test //Test #3
    @DisplayName("TEST_MYGAME_03: Get full name, both")
    public void testGetFullNameWithBoth() {
        game.setPlayerName("Daneena", "Roy");
        assertEquals("Daneena Roy", game.getPlayerFullName());
    }

    @Test //Test #4
    @DisplayName("TEST_MYGAME_04: Get full name, with only first name")
    public void testGetFullNameFirstOnly() {
        game.setPlayerName("Daneena", "");
        assertEquals("Daneena", game.getPlayerFullName());
    }

    @Test //Test #5
    @DisplayName("TEST_MYGAME_05: Null first name")
    public void testNullFirstName() {
        game.setPlayerName(null, "Roy");
        assertEquals("", game.getPlayerFirstName());
    }

    @Test //Test #6
    @DisplayName("TEST_MYGAME_06: Null last name")
    public void testNullLastName() {
        game.setPlayerName("Daneena", null);
        assertEquals("", game.getPlayerLastName());
    }

    @Test //Test #7
    @DisplayName("TEST_MYGAME_07: Trim whitespace from names")
    public void testTrimWhitespace() {
        game.setPlayerName("  Daneena  ","  Roy  ");
        assertEquals("Daneena", game.getPlayerFirstName());
        assertEquals("Roy", game.getPlayerLastName());
        assertEquals("Daneena Roy", game.getPlayerFullName());
    }

    //Test #8
    @Test
    @DisplayName("create() sets MenuScreen")
    void testCreateSetsMenuScreen() {
        try (MockedConstruction<MenuScreen> mockedMenuScreen = mockConstruction(MenuScreen.class)) {

            MyGame game = spy(new MyGame());

            game.create();

            assertEquals(1, mockedMenuScreen.constructed().size(),
                "MenuScreen should be constructed once");

            verify(game, times(1)).setScreen(any(MenuScreen.class));
        }
    }
}
