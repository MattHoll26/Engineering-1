package io.github.some_example_name;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * NEW
 *
 * <code> Teleport </code> is an interactable game object, that acts as the
 * hidden game event by allowing the player to teleport randomly using some
 * science lab equipment
 */

public class Teleport {
    private Texture texture;
    private Vector2 position;
    private Rectangle bounds;
    private boolean searched = false;
    private boolean teleportHappened = false;
    private boolean showMessage = false;
    private float messageTimer = 0f;
    private BitmapFont font;
    private GameScreen gameScreen;
    private float countdownTimer = 0f;

    /**
     * Constructor for <code> Teleport </code>, with a set of coordinates.
     * @param x Horizontal position for locker to spawn in.
     * @param y Vertical position for locker to spawn in.
     * @param gameScreen Screen instance controlling gameplay and collision checks.
     */
    public Teleport(float x, float y, GameScreen gameScreen) {
        texture = new Texture("locker.png");
        position = new Vector2(x, y);
        bounds = new Rectangle(x,y,texture.getWidth(), texture.getHeight());
        font = new BitmapFont();
        this.gameScreen = gameScreen;
    }

    /**
     * Update attributes of teleport
     * Showing label if the label timer is still active.
     * @param player Player character.
     * @param delta Time elapsed since last frame.
     */
    public void update(Player player, float delta) {
        if (!searched) {
            Rectangle playerRect = new Rectangle(
                player.getPosition().x,
                player.getPosition().y,
                player.currentFrame.getRegionWidth(),   // width of player sprite
                player.currentFrame.getRegionHeight()   // height of player sprite
            );
            if (bounds.overlaps(playerRect)) {
                searched = true;
                showMessage = true;
                countdownTimer = 0f;
            }
        }

        if (showMessage && !teleportHappened) {
            countdownTimer += delta;
            if (countdownTimer >= 3.6f) {
                Vector2 newPos = getRandomSafePosition();
                player.getPosition().set(newPos);
                teleportHappened = true;
            }
            if (teleportHappened) {
                messageTimer += delta;
                if (messageTimer > 0.8f) {
                    showMessage = false;
                }
            }
        }
    }

    private Vector2 getRandomSafePosition() {
        // Specific map locations
        Vector2[] safeSpots = new Vector2[] {
            new Vector2(560, 180),
            new Vector2(300, 300),
            new Vector2(150, 400),
            new Vector2(450, 100),
            new Vector2(360, 250),
            new Vector2(50, 200)
        };

        // Random spot
        int index = MathUtils.random(0, safeSpots.length - 1);

        // Return a new copy
        return new Vector2(safeSpots[index]);
    }

    /**
     * Convenience method to be called by the game screen's <code> render()
     * </code> method, to draw the teleportation device, and it's label using a
     * SpriteBatch at its coordinates.
     * @param batch SpriteBatch used by application to render all sprites.
     * @see com.badlogic.gdx.graphics.g2d.SpriteBatch SpriteBatch
     * @see com.badlogic.gdx.Screen#render Screen.render().
     */
    public void render(SpriteBatch batch){
        batch.draw(texture, position.x, position.y);
        if (showMessage) {
            String text = "";
            if (!teleportHappened) {
                if (countdownTimer < 0.8f) text = "3..";
                else if (countdownTimer < 1.6f) text = "2..";
                else if (countdownTimer < 2.4f) text = "1..";
                else if (countdownTimer < 3.6f) text = "Teleporting...";
            } else {
                text = "Byeeeee....";
                showMessage = false;
            }

            font.draw(batch, text, position.x, position.y + texture.getHeight() + 20);
        }
    }



    /**
     * Convenience method to be called by application to dispose of texture
     * and font's teleport's sprites when the application's dispose method is called.
     * @see com.badlogic.gdx.Screen#dispose Screen.dispose().
     */
    public void dispose(){
        texture.dispose();
        font.dispose();
    }

    /**
     * Return if the teleport has happened for updating the counter
     * @return True/False value.
     */
    public boolean teleportHappened() {
        return teleportHappened;
    }
}
