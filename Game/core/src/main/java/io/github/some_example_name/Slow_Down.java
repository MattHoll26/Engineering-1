package io.github.some_example_name;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * <code> bush </code> is an interactable game object, that acts as the
 * negative game event by allowing the player to fall into a bush,
 * giving a temporary speed reduction.
 */

public class Slow_Down {
    private Texture texture;
    private Vector2 position;
    private Rectangle bounds;
    private boolean searched = false;
    private boolean showMessage = false;
    private boolean fallen = false;
    private float messageTimer = 0f;
    private final float messageDuration = 5f;
    private final float speedBoostAmount = -200f;
    private final float speedBoostDuration = 20f;
    private float speedBoostTimer = 0f;
    private BitmapFont font;

    /**
     * Constructor for <code> bush </code>, with a set of coordinates.
     * @param x Horizontal position for bush to spawn in.
     * @param y Vertical position for bush to spawn in.
     */
    public Slow_Down(float x, float y) {
        texture = new Texture("locker.png");
        position = new Vector2(x, y);
        bounds = new Rectangle(x,y,texture.getWidth(), texture.getHeight());
        font = new BitmapFont();
    }

    /**
     * Update attributes of bush, and decrement timer on speed decrease and
     * label timer, showing label if the label timer is still active.
     * @param player Player character.
     * @param delta Time elapsed since last frame.
     */
    public void update(Player player, float delta) {
        if (!searched && bounds.contains(player.getPosition())){
            if (player.getPosition().dst(position) < 50f) {
                searched = true;
                showMessage = true;
                fallen = true;
                messageTimer = 0f;
                speedBoostTimer = speedBoostDuration;
            }
        }

        if (speedBoostTimer > 0){
            speedBoostTimer -= delta;
        }

        if (showMessage) {
            messageTimer += delta;
            if (messageTimer > messageDuration){
                showMessage = false;
            }
        }
    }

    /**
     * Convenience method to be called by the game screen's <code> render()
     * </code> method, to draw the bush and it's label using a
     * SpriteBatch at it's coordinates.
     * @param batch SpriteBatch used by application to render all sprites.
     * @see com.badlogic.gdx.graphics.g2d.SpriteBatch SpriteBatch
     * @see com.badlogic.gdx.Screen#render Screen.render().
     */
    public void render(SpriteBatch batch){
        batch.draw(texture, position.x, position.y);
        if (showMessage) {
            font.draw(
                batch,
                "You fell in a bush,\n you have poison damage :(",
                position.x - 100, position.y + texture.getHeight() + 40
            );
        }
    }

    /**
     * Convenience method to be called by application to dispose of texture
     * and font's bush's sprites when the application's dispose method is called.
     * @see com.badlogic.gdx.Screen#dispose Screen.dispose().
     */
    public void dispose(){
        texture.dispose();
        font.dispose();
    }

    /**
     * Return if speed decrease is still active.
     * @return True/False value.
     */
    public boolean isBoostActive() {
        return speedBoostTimer > 0;
    }

    /**
     * Return if the bush has been fallen in for updating the counter
     * @return True/False value.
     */
    public boolean bushFall() {return fallen; }
    }

