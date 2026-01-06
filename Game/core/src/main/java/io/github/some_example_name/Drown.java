package io.github.some_example_name;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * NEW
 *
 * <code>Drown</code> represents a water hazard that resets the player
 * if they step into it. Uses the same rectangle overlap style
 * as bus and ticket interactions.
 */
public class Drown {


    /** Rectangle defining the water hazard area */
    private Rectangle waterArea1;
    private Rectangle waterArea2;

    /** Position the player is reset to after drowning */
    private Vector2 respawnPosition;

    /**
     * Constructor for <code>Drown</code>, loading the water hazard
     * from a specified object layer in a Tiled map.
     *
     * @param tiledMap Tiled map containing the water hazard object
     * @param layerName Name of the object layer containing the water area
     * @param respawnX X-coordinate to respawn the player at
     * @param respawnY Y-coordinate to respawn the player at
     */
    public Drown(TiledMap tiledMap, String layerName, float respawnX, float respawnY) {

        this.respawnPosition = new Vector2(respawnX, respawnY);

        for (MapObject obj : tiledMap.getLayers().get(layerName).getObjects()) {
            if (obj instanceof RectangleMapObject) {
                if ("Water1".equalsIgnoreCase(obj.getName())) {
                    waterArea1 = ((RectangleMapObject) obj).getRectangle();
                } else if ("Water2".equalsIgnoreCase(obj.getName())) {
                    waterArea2 = ((RectangleMapObject) obj).getRectangle();
                }
            }
        }
    }

    /**
     * Check if the player has entered the water hazard and reset their position if so.
     *
     * @param player Player character to test for collision
     * @return True if the player drowned and was respawned, false otherwise
     */
    public boolean update(Player player) {

        Rectangle playerRect = new Rectangle(
            player.getPosition().x,
            player.getPosition().y,
            16,
            16
        );

        if (playerRect.overlaps(waterArea1)) {
            player.getPosition().set(respawnPosition);
            return true; // drowned
        }

        if (playerRect.overlaps(waterArea2)) {
            player.getPosition().set(respawnPosition);
            return true; // drowned
        }

        return false;
    }

}
