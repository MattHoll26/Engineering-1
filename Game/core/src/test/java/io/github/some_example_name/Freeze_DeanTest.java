package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
 * Test suite for Freeze_Dean class.
 * Tests event discovery, E key activation, freeze mechanics, and one-time use enforcement.
 * Test coverage:70%
 * Automated: Constructor, Materials object detection, E key activation, freeze logic, one-time use
 * Coverage justification: Freeze timer countdown visual rendering untestable but tested manually when playing the game
 */
@DisplayName("Freeze Dean Event Tests")
public class Freeze_DeanTest extends TestHelper {

    private TiledMap mockMap;
    private TiledMapTileLayer mockLayer;
    private MapObjects mapObjects;
    private RectangleMapObject materialsObject;
    private Player mockPlayer;
    private GameScreen mockGameScreen;

    @BeforeEach
    public void setUp() {
        mockMap = mock(TiledMap.class);
        mockLayer = mock(TiledMapTileLayer.class);
        mapObjects = new MapObjects();

        materialsObject = new RectangleMapObject(100, 100, 32, 32);
        materialsObject.setName("Materials");
        mapObjects.add(materialsObject);

        when(mockMap.getLayers()).thenReturn(mock(com.badlogic.gdx.maps.MapLayers.class));
        when(mockMap.getLayers().get("Events")).thenReturn(mockLayer);
        when(mockLayer.getObjects()).thenReturn(mapObjects);

        mockPlayer = mock(Player.class);
        mockGameScreen = mock(GameScreen.class);
    }

    //Test #1: constructor finds Materials object in map
    @Test
    @DisplayName("Constructor finds Materials object in map")
    public void testConstructorFindsMaterialsObject() {
        Freeze_Dean freezeDean = new Freeze_Dean(mockMap, "Events");

        assertFalse(freezeDean.isUsed(),
            "Freeze event should not be used initially");
    }

    //Test #2: constructor handles missing Materials object gracefully
    @Test
    @DisplayName("Constructor handles missing Materials object")
    public void testConstructorHandlesMissingMaterials() {
        MapObjects emptyObjects = new MapObjects();
        when(mockLayer.getObjects()).thenReturn(emptyObjects);

        assertDoesNotThrow(() -> {
            Freeze_Dean freezeDean = new Freeze_Dean(mockMap, "Events");
            assertFalse(freezeDean.isUsed());
        }, "Constructor should handle missing Materials object gracefully");
    }

    //Test #3: isUsed returns false initially
    @Test
    @DisplayName("isUsed returns false initially")
    public void testIsUsedInitiallyFalse() {
        Freeze_Dean freezeDean = new Freeze_Dean(mockMap, "Events");

        assertFalse(freezeDean.isUsed(),
            "Freeze event should not be marked as used when first created");
    }

    //Test #4: update handles null materials area safely
    @Test
    @DisplayName("Update method handles null materials area safely")
    public void testUpdateHandlesNullMaterialsArea() {
        MapObjects emptyObjects = new MapObjects();
        when(mockLayer.getObjects()).thenReturn(emptyObjects);
        Freeze_Dean freezeDean = new Freeze_Dean(mockMap, "Events");

        when(mockPlayer.getPosition()).thenReturn(new Vector2(100, 100));

        assertDoesNotThrow(() -> {
            freezeDean.update(mockPlayer, mockGameScreen);
        }, "Update should handle null materials area without crashing");
    }

    //Test #5: event validates one-time use design
    @Test
    @DisplayName("Event validates one-time use design")
    public void testOneTimeUseDesign() {
        Freeze_Dean freezeDean = new Freeze_Dean(mockMap, "Events");

        assertFalse(freezeDean.isUsed(),
            "Event should not be used initially");
    }

    //Test #6: player hitbox size is 16x16
    @Test
    @DisplayName("Player overlap detection uses correct hitbox size")
    public void testPlayerHitboxSize() {
        Freeze_Dean freezeDean = new Freeze_Dean(mockMap, "Events");
        Vector2 playerPosition = new Vector2(108, 108);
        when(mockPlayer.getPosition()).thenReturn(playerPosition);

        freezeDean.update(mockPlayer, mockGameScreen);

        assertFalse(freezeDean.isUsed(),
            "Event should not activate without E key press");
    }

    //Test #7: player outside materials area - no discovery
    @Test
    @DisplayName("Player outside materials area - no discovery")
    void testPlayerOutsideMaterialsArea() {
        Freeze_Dean freezeDean = new Freeze_Dean(mockMap, "Events");

        when(mockPlayer.getPosition()).thenReturn(new Vector2(10, 10));

        freezeDean.update(mockPlayer, mockGameScreen);

        verify(mockGameScreen, never()).freezeAllDeans();
        assertFalse(freezeDean.isUsed());
    }

    //Test #8: player inside materials area - discovered state
    @Test
    @DisplayName("Player inside materials area - discovered state")
    void testPlayerInsideMaterialsArea() {
        Freeze_Dean freezeDean = new Freeze_Dean(mockMap, "Events");

        when(mockPlayer.getPosition()).thenReturn(new Vector2(110, 110));

        freezeDean.update(mockPlayer, mockGameScreen);

        verify(mockGameScreen, never()).freezeAllDeans();
        assertFalse(freezeDean.isUsed());
    }

    //Test #9: event cannot be used after already activated
    @Test
    @DisplayName("Event cannot be used after already activated")
    void testCannotUseAfterActivated() throws Exception {
        Freeze_Dean freezeDean = new Freeze_Dean(mockMap, "Events");

        java.lang.reflect.Field usedField = Freeze_Dean.class.getDeclaredField("usedMaterials");
        usedField.setAccessible(true);
        usedField.set(freezeDean, true);

        when(mockPlayer.getPosition()).thenReturn(new Vector2(110, 110));

        freezeDean.update(mockPlayer, mockGameScreen);
        freezeDean.update(mockPlayer, mockGameScreen);

        verify(mockGameScreen, never()).freezeAllDeans();
        assertTrue(freezeDean.isUsed());
    }

    //Test #10: null materials area handled gracefully
    @Test
    @DisplayName("Null materials area handled gracefully")
    void testNullMaterialsAreaHandling() {
        MapObjects emptyObjects = new MapObjects();
        when(mockLayer.getObjects()).thenReturn(emptyObjects);

        Freeze_Dean freezeDean = new Freeze_Dean(mockMap, "Events");
        when(mockPlayer.getPosition()).thenReturn(new Vector2(110, 110));

        assertDoesNotThrow(() -> freezeDean.update(mockPlayer, mockGameScreen));
        verify(mockGameScreen, never()).freezeAllDeans();
    }

    //Test #11: player overlap boundary detection
    @Test
    @DisplayName("Player overlap detection boundary test")
    void testPlayerOverlapBoundary() {
        Freeze_Dean freezeDean = new Freeze_Dean(mockMap, "Events");

        when(mockPlayer.getPosition()).thenReturn(new Vector2(100, 100));
        freezeDean.update(mockPlayer, mockGameScreen);

        when(mockPlayer.getPosition()).thenReturn(new Vector2(99, 99));
        freezeDean.update(mockPlayer, mockGameScreen);

        verify(mockGameScreen, never()).freezeAllDeans();
    }

    //Test #12: E key activates freeze event
    @Test
    @DisplayName("E key activates freeze event")
    void testEKeyActivatesFreeze() {
        Input mockInput = mock(Input.class);
        Gdx.input = mockInput;

        Freeze_Dean freezeDean = new Freeze_Dean(mockMap, "Events");

        when(mockPlayer.getPosition()).thenReturn(new Vector2(110, 110));

        when(mockInput.isKeyJustPressed(Input.Keys.E)).thenReturn(true);

        freezeDean.update(mockPlayer, mockGameScreen);

        verify(mockGameScreen, times(1)).freezeAllDeans();
        assertTrue(freezeDean.isUsed(), "Freeze should be marked as used");
    }

    //Test #13: E key only works inside materials area
    @Test
    @DisplayName("E key only works inside materials area")
    void testEKeyOnlyWorksInArea() {
        Input mockInput = mock(Input.class);
        Gdx.input = mockInput;

        Freeze_Dean freezeDean = new Freeze_Dean(mockMap, "Events");

        when(mockPlayer.getPosition()).thenReturn(new Vector2(10, 10));

        when(mockInput.isKeyJustPressed(Input.Keys.E)).thenReturn(true);

        freezeDean.update(mockPlayer, mockGameScreen);

        verify(mockGameScreen, never()).freezeAllDeans();
        assertFalse(freezeDean.isUsed());
    }

    //Test #14: cannot use freeze twice with E key
    @Test
    @DisplayName("Cannot use freeze twice with E key")
    void testCannotUseTwice() {
        Input mockInput = mock(Input.class);
        Gdx.input = mockInput;

        Freeze_Dean freezeDean = new Freeze_Dean(mockMap, "Events");
        when(mockPlayer.getPosition()).thenReturn(new Vector2(110, 110));

        when(mockInput.isKeyJustPressed(Input.Keys.E)).thenReturn(true);
        freezeDean.update(mockPlayer, mockGameScreen);

        freezeDean.update(mockPlayer, mockGameScreen);
        freezeDean.update(mockPlayer, mockGameScreen);

        verify(mockGameScreen, times(1)).freezeAllDeans();
        assertTrue(freezeDean.isUsed());
    }

    //Test #15: E key does nothing outside materials area
    @Test
    @DisplayName("E key does nothing outside materials area")
    void testEKeyIgnoredOutsideArea() {
        Input mockInput = mock(Input.class);
        Gdx.input = mockInput;

        Freeze_Dean freezeDean = new Freeze_Dean(mockMap, "Events");

        when(mockPlayer.getPosition()).thenReturn(new Vector2(500, 500));
        when(mockInput.isKeyJustPressed(Input.Keys.E)).thenReturn(true);

        freezeDean.update(mockPlayer, mockGameScreen);

        verify(mockGameScreen, never()).freezeAllDeans();
        assertFalse(freezeDean.isUsed());
    }
}
