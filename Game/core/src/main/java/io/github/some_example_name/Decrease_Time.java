package io.github.some_example_name;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * NEW
 *
 * <code> tree </code> is an interactable game object, that acts as the
 * negative game event by allowing the player to collide with a tree,
 * giving a permanent time reduction.
 */

public class Decrease_Time {
    private Texture texture;
    private TextureRegion cropped;
    private Vector2 position;
    private Rectangle bounds;
    private boolean searched = false;
    private boolean showMessage = false;
    private boolean hitTree = false;
    private float messageTimer = 0f;
    private final float messageDuration = 5f;
    private final float timeDecreaseAmount = -30;
    private float scale = 2f;
    private BitmapFont font;

    /**
     * Constructor for <code> tree </code>, with a set of coordinates.
     * @param x Horizontal position for tree to spawn in.
     * @param y Vertical position for tree to spawn in.
     */
    public Decrease_Time(float x, float y) {
        texture = new Texture("objects.png");
        cropped = new TextureRegion(texture, 160, 115, 28, 28);
        position = new Vector2(x, y);
        bounds = new Rectangle(x, y, cropped.getRegionWidth() * scale, cropped.getRegionHeight() * scale);
        font = new BitmapFont();
    }

    /**
     * Update attributes of tree, and decrement timer on speed decrease and
     * label timer, showing label if the label timer is still active.
     * @param player Player character.
     * @param timer GameTimer updates the timer
     * @param delta Time elapsed since last frame.
     */
    public void update(Player player, io.github.some_example_name.GameTimer timer, float delta) {
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
                hitTree = true;
                messageTimer = 0f;
                timer.addTime(timeDecreaseAmount);
            }
        }

        if (showMessage) {
            messageTimer += delta;
            if (messageTimer > messageDuration) {
                showMessage = false;
            }
        }
    }

    /**
     * Convenience method to be called by the game screen's <code> render()
     * </code> method, to draw the tree, and it's label using a
     * SpriteBatch at its coordinates.
     * @param batch SpriteBatch used by application to render all sprites.
     * @see com.badlogic.gdx.graphics.g2d.SpriteBatch SpriteBatch
     * @see com.badlogic.gdx.Screen#render Screen.render().
     */
    public void render(SpriteBatch batch) {
       batch.draw(cropped, position.x, position.y, cropped.getRegionWidth() * scale, cropped.getRegionHeight() * scale);
        if (showMessage) {
            font.draw(
                batch,
                "You hit a tree, a rogue squirrel \nremoved some of your time xD",
                position.x - 100, position.y + cropped.getRegionHeight() * scale + 40
            );
        }
    }

    /**
     * Convenience method to be called by application to dispose of texture
     * and font's tree's sprites when the application's dispose method is called.
     * @see com.badlogic.gdx.Screen#dispose Screen.dispose().
     */
    public void dispose() {
        texture.dispose();
        font.dispose();
    }

    /**
     * Return if the tree has been hit for updating the counter
     * @return True/False value.
     */
    public boolean hitTree(){
        return hitTree;
    }
}
