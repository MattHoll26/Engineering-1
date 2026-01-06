package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;

/**
 * NEW
 *
 * <code> Freeze_Dean </code> is a positive hidden event that allows the player to
 * temporarily freeze all dean enemies for a fixed duration when interacting with
 * the "Materials" area in the Tiled map.
 *
 * <p>The event is triggered when the player overlaps the with the area assigned to
 * "Materials" in the specific object layer and presses <code>E</code>. Once used,
 * it cannot be used again.</p>
 */
public class Freeze_Dean {
    private Rectangle materialsArea;
    private boolean discovered = false;
    private boolean usedMaterials = false;
    private boolean freezeActive = false;
    private float freezeTimer = 0f;

    /**
     * Constructor for <code> Freeze_Dean </code>, locating the "Materials" rectangle
     * object from the given Tiled map layer.
     *
     * @param tiledMap The Tiled map containing the event object.
     * @param layerName The Name of the object layer to search for "Materials".
     */
    public Freeze_Dean(TiledMap tiledMap, String layerName) {
        for (MapObject obj : tiledMap.getLayers().get(layerName).getObjects()) {
            if (obj instanceof RectangleMapObject && "Materials".equalsIgnoreCase(obj.getName())) {
                materialsArea = ((RectangleMapObject) obj).getRectangle();
                break;
            }
        }
    }

    /**
     * Update the freeze event state each frame. If the player is overlapping with the
     * materials area, the event becomes discoverable and will activate when the
     * player presses <code>E</code>.
     *
     * <p>If the freeze is active, a countdown is applied and the deans are unfrozen
     * automatically when the timer expires.</p>
     *
     * @param player Player character used for overlap checks.
     * @param gameScreen Game screen used to freeze/unfreeze all deans.
     */
    public void update(Player player, GameScreen gameScreen) {
        // Check that the event was already used or missing from map
        if (materialsArea == null || usedMaterials) {
            return;
        }

        Rectangle playerRect = new Rectangle(player.getPosition().x, player.getPosition().y, 16, 16);

        // Check's whether the materials have been discovered when player enters the area
        if (playerRect.overlaps(materialsArea)) {
            discovered = true;
            // Activates the freeze
            if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                useFreeze(gameScreen);
            }
        }

        // Freeze countdown
        if (freezeActive) {
            freezeTimer -= Gdx.graphics.getDeltaTime();
            // Unfreezes when the timer is finished
            if (freezeTimer <= 0) {
                freezeActive = false;
                gameScreen.unfreezeDeans();
            }
        }
    }

    /**
     * Activates the freeze by freezing all deans for 30 seconds and marking
     * this event as used as it's a one time event.
     *
     * @param gameScreen The game screen used to freeze all deans.
     */
    private void useFreeze(GameScreen gameScreen) {
        gameScreen.freezeAllDeans();
        freezeActive = true;
        freezeTimer = 30f;
        usedMaterials = true;
    }

    /**
     * Renders the on-screen prompts and status text for the freeze event.
     *
     * @param batch The SpriteBatch used to render UI text.
     * @param font The Font used to draw text to the screen.
     */
    public void render(SpriteBatch batch, BitmapFont font) {
        if (materialsArea == null) return;

        if (discovered && !usedMaterials) {
            font.draw(batch, "[E] Freeze Dean (30s)", materialsArea.x - 80, materialsArea.y + 40);
        }
        if (freezeActive) {
            font.draw(batch, "Deans Frozen for 30 seconds", materialsArea.x, materialsArea.y);
        }
    }

    /**
     * Returns whether the freeze event has been used already.
     *
     * @return True if the player has activated the freeze event, false otherwise.
     */
    public boolean isUsed() {
        return usedMaterials;
    }
}
