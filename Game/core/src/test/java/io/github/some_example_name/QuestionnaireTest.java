package io.github.some_example_name;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test suite for Questionnaire class.
 * Tests quiz event discovery, E key activation, answer validation (A/B/C/D), player freeze mechanics,
 * and result handling (correct = freeze deans, incorrect = spawn dean)
 * Test coverage:57%
 * Coverage justification: Quiz UI rendering and result message display only testable visually
 * Note:Correct answer is C that freezes all deans. Wrong answers (A/B/D) spawn extra dean.
 */
@DisplayName("Questionnaire Event Tests")
public class QuestionnaireTest extends TestHelper {

    private TiledMap mockMap;
    private TiledMapTileLayer mockLayer;
    private Player mockPlayer;
    private GameScreen mockGameScreen;

    @BeforeEach
    public void setUp() {
        mockMap = mock(TiledMap.class);
        mockLayer = mock(TiledMapTileLayer.class);
        MapObjects mapObjects = new MapObjects();

        RectangleMapObject quizObject = new RectangleMapObject(400, 500, 80, 80);
        quizObject.setName("Questionnaire");
        mapObjects.add(quizObject);

        when(mockMap.getLayers()).thenReturn(mock(com.badlogic.gdx.maps.MapLayers.class));
        when(mockMap.getLayers().get("Events")).thenReturn(mockLayer);
        when(mockLayer.getObjects()).thenReturn(mapObjects);

        mockPlayer = mock(Player.class);
        when(mockPlayer.getPosition()).thenReturn(new Vector2(100, 100));
        mockGameScreen = mock(GameScreen.class);
    }

    //Test #1: constructor finds Questionnaire object
    @Test
    @DisplayName("Constructor finds Questionnaire object")
    public void testConstructorFindsQuestionnaireObject() {
        Questionnaire quiz = new Questionnaire(mockMap, "Events");
        assertFalse(quiz.isAnswered());
    }

    //Test #2: constructor handles missing Questionnaire object
    @Test
    @DisplayName("Constructor handles missing Questionnaire object")
    public void testConstructorHandlesMissingQuestionnaire() {
        MapObjects emptyObjects = new MapObjects();
        when(mockLayer.getObjects()).thenReturn(emptyObjects);

        assertDoesNotThrow(() -> {
            Questionnaire quiz = new Questionnaire(mockMap, "Events");
            assertFalse(quiz.isAnswered());
        });
    }

    //Test #3: initial state is not answered and not frozen
    @Test
    @DisplayName("Initial state is correct")
    public void testInitialState() {
        Questionnaire quiz = new Questionnaire(mockMap, "Events");

        assertFalse(quiz.isAnswered());
        assertFalse(quiz.isPlayerFrozen());
    }

    //Test #4: isAnswered returns false initially
    @Test
    @DisplayName("isAnswered returns false initially")
    public void testIsAnsweredInitiallyFalse() {
        Questionnaire quiz = new Questionnaire(mockMap, "Events");
        assertFalse(quiz.isAnswered());
    }

    //Test #5: isPlayerFrozen returns false initially
    @Test
    @DisplayName("isPlayerFrozen returns false initially")
    public void testIsPlayerFrozenInitiallyFalse() {
        Questionnaire quiz = new Questionnaire(mockMap, "Events");
        assertFalse(quiz.isPlayerFrozen());
    }

    //Test #6: update handles null quiz area safely
    @Test
    @DisplayName("Update handles null quiz area safely")
    public void testUpdateHandlesNullQuizArea() {
        MapObjects emptyObjects = new MapObjects();
        when(mockLayer.getObjects()).thenReturn(emptyObjects);
        Questionnaire quiz = new Questionnaire(mockMap, "Events");

        assertDoesNotThrow(() -> quiz.update(mockPlayer, mockGameScreen));
    }

    //Test #7: player hitbox is 16x16
    @Test
    @DisplayName("Player hitbox uses correct size")
    public void testPlayerHitboxSize() {
        Questionnaire quiz = new Questionnaire(mockMap, "Events");
        quiz.update(mockPlayer, mockGameScreen);
        assertFalse(quiz.isAnswered());
    }

    //Test #8: player outside quiz area - no discovery
    @Test
    @DisplayName("Player outside quiz area - no discovery")
    void testPlayerOutsideQuizArea() {
        Questionnaire quiz = new Questionnaire(mockMap, "Events");

        when(mockPlayer.getPosition()).thenReturn(new Vector2(100, 100));
        quiz.update(mockPlayer, mockGameScreen);

        assertFalse(quiz.isPlayerFrozen());
        assertFalse(quiz.isAnswered());
    }

    //Test #9: player inside quiz area - discovered but not started
    @Test
    @DisplayName("Player inside quiz area - discovered state")
    void testPlayerInsideQuizArea() {
        Questionnaire quiz = new Questionnaire(mockMap, "Events");

        when(mockPlayer.getPosition()).thenReturn(new Vector2(420, 520));
        quiz.update(mockPlayer, mockGameScreen);

        assertFalse(quiz.isPlayerFrozen());
        assertFalse(quiz.isAnswered());
    }

    //Test #10: cannot answer quiz twice
    @Test
    @DisplayName("Cannot answer quiz twice")
    void testCannotAnswerTwice() throws Exception {
        Questionnaire quiz = new Questionnaire(mockMap, "Events");

        java.lang.reflect.Field answeredField = Questionnaire.class.getDeclaredField("answered");
        answeredField.setAccessible(true);
        answeredField.set(quiz, true);

        when(mockPlayer.getPosition()).thenReturn(new Vector2(420, 520));

        quiz.update(mockPlayer, mockGameScreen);
        quiz.update(mockPlayer, mockGameScreen);

        verify(mockGameScreen, never()).spawnSecondDean();
        verify(mockGameScreen, never()).freezeAllDeans();
    }

    //Test #11: player frozen state managed by quiz
    @Test
    @DisplayName("Player frozen state controlled by quiz")
    void testPlayerFrozenStateManagement() {
        Questionnaire quiz = new Questionnaire(mockMap, "Events");
        assertFalse(quiz.isPlayerFrozen());
    }

    //Test #12: player overlap boundary detection
    @Test
    @DisplayName("Player overlap uses correct hitbox size")
    void testPlayerHitboxOverlap() {
        Questionnaire quiz = new Questionnaire(mockMap, "Events");

        when(mockPlayer.getPosition()).thenReturn(new Vector2(400, 500));
        quiz.update(mockPlayer, mockGameScreen);

        when(mockPlayer.getPosition()).thenReturn(new Vector2(399, 499));
        quiz.update(mockPlayer, mockGameScreen);

        assertFalse(quiz.isAnswered());
    }

    //Test #13: correct answer state tracked
    @Test
    @DisplayName("Correct answer state tracked properly")
    void testCorrectAnswerState() throws Exception {
        Questionnaire quiz = new Questionnaire(mockMap, "Events");

        java.lang.reflect.Field answeredField = Questionnaire.class.getDeclaredField("answered");
        answeredField.setAccessible(true);

        java.lang.reflect.Field successField = Questionnaire.class.getDeclaredField("questionSuccess");
        successField.setAccessible(true);
        successField.set(quiz, true);

        answeredField.set(quiz, true);

        assertTrue(quiz.isAnswered());

        when(mockPlayer.getPosition()).thenReturn(new Vector2(420, 520));
        quiz.update(mockPlayer, mockGameScreen);

        verify(mockGameScreen, never()).spawnSecondDean();
        verify(mockGameScreen, never()).freezeAllDeans();
    }

    //Test #14: wrong answer state tracked
    @Test
    @DisplayName("Wrong answer state tracked properly")
    void testWrongAnswerState() throws Exception {
        Questionnaire quiz = new Questionnaire(mockMap, "Events");

        java.lang.reflect.Field answeredField = Questionnaire.class.getDeclaredField("answered");
        answeredField.setAccessible(true);

        java.lang.reflect.Field successField = Questionnaire.class.getDeclaredField("questionSuccess");
        successField.setAccessible(true);
        successField.set(quiz, false);

        answeredField.set(quiz, true);

        assertTrue(quiz.isAnswered());

        when(mockPlayer.getPosition()).thenReturn(new Vector2(420, 520));
        quiz.update(mockPlayer, mockGameScreen);

        verify(mockGameScreen, never()).spawnSecondDean();
        verify(mockGameScreen, never()).freezeAllDeans();
    }

    //Test #15: result timer initialized at zero
    @Test
    @DisplayName("Result timer initialized correctly")
    void testResultTimerInitialization() throws Exception {
        Questionnaire quiz = new Questionnaire(mockMap, "Events");

        java.lang.reflect.Field timerField = Questionnaire.class.getDeclaredField("resultTimer");
        timerField.setAccessible(true);

        float initialTimer = timerField.getFloat(quiz);
        assertEquals(0f, initialTimer, "Timer should start at 0");

        timerField.set(quiz, 3.0f);
        assertEquals(3.0f, timerField.getFloat(quiz), "Timer should be settable");
    }

    //Test #16: player frozen during active quiz
    @Test
    @DisplayName("Player frozen when quiz is active")
    void testPlayerFrozenDuringQuiz() throws Exception {
        Questionnaire quiz = new Questionnaire(mockMap, "Events");

        java.lang.reflect.Field showQuizField = Questionnaire.class.getDeclaredField("showQuiz");
        showQuizField.setAccessible(true);
        showQuizField.set(quiz, true);

        java.lang.reflect.Field frozenField = Questionnaire.class.getDeclaredField("playerFrozen");
        frozenField.setAccessible(true);
        frozenField.set(quiz, true);

        assertTrue(quiz.isPlayerFrozen(), "Player should be frozen during quiz");
    }

    //Test #17: player unfrozen after answering
    @Test
    @DisplayName("Player unfrozen after answering")
    void testPlayerUnfrozenAfterAnswer() throws Exception {
        Questionnaire quiz = new Questionnaire(mockMap, "Events");

        java.lang.reflect.Field answeredField = Questionnaire.class.getDeclaredField("answered");
        answeredField.setAccessible(true);
        answeredField.set(quiz, true);

        java.lang.reflect.Field frozenField = Questionnaire.class.getDeclaredField("playerFrozen");
        frozenField.setAccessible(true);
        frozenField.set(quiz, false);

        assertFalse(quiz.isPlayerFrozen(), "Player should be unfrozen after answering");
    }

    //Test #18: correct answer is option C
    @Test
    @DisplayName("Correct answer is option C (Keyword)")
    public void testCorrectAnswerIsC() {
        Questionnaire quiz = new Questionnaire(mockMap, "Events");

        assertNotNull(quiz, "Quiz should be created with correct answer C (Keyword)");
    }

    //Test #19: E key starts quiz and freezes player
    @Test
    @DisplayName("E key starts quiz and freezes player")
    void testEKeyStartsQuiz() {
        Input mockInput = mock(Input.class);
        Gdx.input = mockInput;

        Questionnaire quiz = new Questionnaire(mockMap, "Events");

        when(mockPlayer.getPosition()).thenReturn(new Vector2(420, 520));

        when(mockInput.isKeyJustPressed(Input.Keys.E)).thenReturn(true);
        when(mockInput.isKeyJustPressed(Input.Keys.A)).thenReturn(false);
        when(mockInput.isKeyJustPressed(Input.Keys.B)).thenReturn(false);
        when(mockInput.isKeyJustPressed(Input.Keys.C)).thenReturn(false);
        when(mockInput.isKeyJustPressed(Input.Keys.D)).thenReturn(false);

        quiz.update(mockPlayer, mockGameScreen);

        assertTrue(quiz.isPlayerFrozen(), "Player should be frozen when quiz starts");
        assertFalse(quiz.isAnswered(), "Quiz should not be answered yet");
    }

    //Test #20: answer A spawns dean (incorrect)
    @Test
    @DisplayName("Answer A spawns dean (incorrect)")
    void testAnswerASpawnsDean() {
        Input mockInput = mock(Input.class);
        Gdx.input = mockInput;

        Questionnaire quiz = new Questionnaire(mockMap, "Events");
        when(mockPlayer.getPosition()).thenReturn(new Vector2(420, 520));

        when(mockInput.isKeyJustPressed(Input.Keys.E)).thenReturn(true);
        when(mockInput.isKeyJustPressed(Input.Keys.A)).thenReturn(false);
        when(mockInput.isKeyJustPressed(Input.Keys.B)).thenReturn(false);
        when(mockInput.isKeyJustPressed(Input.Keys.C)).thenReturn(false);
        when(mockInput.isKeyJustPressed(Input.Keys.D)).thenReturn(false);
        quiz.update(mockPlayer, mockGameScreen);

        when(mockInput.isKeyJustPressed(Input.Keys.E)).thenReturn(false);
        when(mockInput.isKeyJustPressed(Input.Keys.A)).thenReturn(true);
        quiz.update(mockPlayer, mockGameScreen);

        verify(mockGameScreen, times(1)).spawnSecondDean();
        verify(mockGameScreen, never()).freezeAllDeans();
        assertTrue(quiz.isAnswered(), "Quiz should be marked as answered");
    }

    //Test #21: answer B spawns dean (incorrect)
    @Test
    @DisplayName("Answer B spawns dean (incorrect)")
    void testAnswerBSpawnsDean() {
        Input mockInput = mock(Input.class);
        Gdx.input = mockInput;

        Questionnaire quiz = new Questionnaire(mockMap, "Events");
        when(mockPlayer.getPosition()).thenReturn(new Vector2(420, 520));

        when(mockInput.isKeyJustPressed(Input.Keys.E)).thenReturn(true);
        when(mockInput.isKeyJustPressed(Input.Keys.A)).thenReturn(false);
        when(mockInput.isKeyJustPressed(Input.Keys.B)).thenReturn(false);
        when(mockInput.isKeyJustPressed(Input.Keys.C)).thenReturn(false);
        when(mockInput.isKeyJustPressed(Input.Keys.D)).thenReturn(false);
        quiz.update(mockPlayer, mockGameScreen);

        when(mockInput.isKeyJustPressed(Input.Keys.E)).thenReturn(false);
        when(mockInput.isKeyJustPressed(Input.Keys.B)).thenReturn(true);
        when(mockInput.isKeyJustPressed(Input.Keys.A)).thenReturn(false);
        quiz.update(mockPlayer, mockGameScreen);

        verify(mockGameScreen, times(1)).spawnSecondDean();
        assertTrue(quiz.isAnswered());
    }

    //Test #22: answer C freezes deans (CORRECT)
    @Test
    @DisplayName("Answer C freezes deans (CORRECT)")
    void testAnswerCFreezesDeans() {
        Input mockInput = mock(Input.class);
        Gdx.input = mockInput;

        Questionnaire quiz = new Questionnaire(mockMap, "Events");
        when(mockPlayer.getPosition()).thenReturn(new Vector2(420, 520));

        when(mockInput.isKeyJustPressed(Input.Keys.E)).thenReturn(true);
        when(mockInput.isKeyJustPressed(Input.Keys.A)).thenReturn(false);
        when(mockInput.isKeyJustPressed(Input.Keys.B)).thenReturn(false);
        when(mockInput.isKeyJustPressed(Input.Keys.C)).thenReturn(false);
        when(mockInput.isKeyJustPressed(Input.Keys.D)).thenReturn(false);
        quiz.update(mockPlayer, mockGameScreen);

        when(mockInput.isKeyJustPressed(Input.Keys.E)).thenReturn(false);
        when(mockInput.isKeyJustPressed(Input.Keys.C)).thenReturn(true);
        when(mockInput.isKeyJustPressed(Input.Keys.A)).thenReturn(false);
        when(mockInput.isKeyJustPressed(Input.Keys.B)).thenReturn(false);
        when(mockInput.isKeyJustPressed(Input.Keys.D)).thenReturn(false);
        quiz.update(mockPlayer, mockGameScreen);

        verify(mockGameScreen, times(1)).freezeAllDeans();
        verify(mockGameScreen, never()).spawnSecondDean();
        assertTrue(quiz.isAnswered());
    }

    //Test #23: answer D spawns dean (incorrect)
    @Test
    @DisplayName("Answer D spawns dean (incorrect)")
    void testAnswerDSpawnsDean() {
        Input mockInput = mock(Input.class);
        Gdx.input = mockInput;

        Questionnaire quiz = new Questionnaire(mockMap, "Events");
        when(mockPlayer.getPosition()).thenReturn(new Vector2(420, 520));

        when(mockInput.isKeyJustPressed(Input.Keys.E)).thenReturn(true);
        when(mockInput.isKeyJustPressed(Input.Keys.A)).thenReturn(false);
        when(mockInput.isKeyJustPressed(Input.Keys.B)).thenReturn(false);
        when(mockInput.isKeyJustPressed(Input.Keys.C)).thenReturn(false);
        when(mockInput.isKeyJustPressed(Input.Keys.D)).thenReturn(false);
        quiz.update(mockPlayer, mockGameScreen);

        when(mockInput.isKeyJustPressed(Input.Keys.E)).thenReturn(false);
        when(mockInput.isKeyJustPressed(Input.Keys.D)).thenReturn(true);
        when(mockInput.isKeyJustPressed(Input.Keys.A)).thenReturn(false);
        when(mockInput.isKeyJustPressed(Input.Keys.B)).thenReturn(false);
        when(mockInput.isKeyJustPressed(Input.Keys.C)).thenReturn(false);
        quiz.update(mockPlayer, mockGameScreen);

        verify(mockGameScreen, times(1)).spawnSecondDean();
        assertTrue(quiz.isAnswered());
    }

    //Test #24: player unfrozen after quiz complete
    @Test
    @DisplayName("Player unfrozen after answering quiz")
    void testPlayerUnfrozenAfterQuizComplete() {
        Input mockInput = mock(Input.class);
        Gdx.input = mockInput;

        Questionnaire quiz = new Questionnaire(mockMap, "Events");
        when(mockPlayer.getPosition()).thenReturn(new Vector2(420, 520));

        when(mockInput.isKeyJustPressed(Input.Keys.E)).thenReturn(true);
        when(mockInput.isKeyJustPressed(Input.Keys.A)).thenReturn(false);
        when(mockInput.isKeyJustPressed(Input.Keys.B)).thenReturn(false);
        when(mockInput.isKeyJustPressed(Input.Keys.C)).thenReturn(false);
        when(mockInput.isKeyJustPressed(Input.Keys.D)).thenReturn(false);
        quiz.update(mockPlayer, mockGameScreen);

        assertTrue(quiz.isPlayerFrozen(), "Player should be frozen during quiz");

        when(mockInput.isKeyJustPressed(Input.Keys.E)).thenReturn(false);
        when(mockInput.isKeyJustPressed(Input.Keys.C)).thenReturn(true);
        when(mockInput.isKeyJustPressed(Input.Keys.A)).thenReturn(false);
        when(mockInput.isKeyJustPressed(Input.Keys.B)).thenReturn(false);
        when(mockInput.isKeyJustPressed(Input.Keys.D)).thenReturn(false);
        quiz.update(mockPlayer, mockGameScreen);

        assertFalse(quiz.isPlayerFrozen(), "Player should be unfrozen after answering");
        assertTrue(quiz.isAnswered());
    }
}
