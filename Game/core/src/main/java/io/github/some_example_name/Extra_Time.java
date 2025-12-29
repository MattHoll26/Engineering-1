package io.github.some_example_name;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;


public class Extra_Time {
    private Texture texture;
    private TextureRegion cropped;
    private Vector2 position;
    private Rectangle bounds;
    private boolean searched = false;
    private boolean showMessage = false;
    private boolean hitTree = false;
    private float messageTimer = 0f;
    private final float messageDuration = 5f;
    private final float timeExtraAmount = -30;
    private float scale = 2f;
    private BitmapFont font;

    public Extra_Time(float x, float y) {
        texture = new Texture("objects.png");
        cropped = new TextureRegion(texture, 160, 115, 28, 28);
        position = new Vector2(x, y);
        bounds = new Rectangle(x, y, cropped.getRegionWidth() * scale, cropped.getRegionHeight() * scale);
        font = new BitmapFont();
    }

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
                timer.addTime(timeExtraAmount);
            }
        }

        if (showMessage) {
            messageTimer += delta;
            if (messageTimer > messageDuration) {
                showMessage = false;
            }
        }
    }

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

    public void dispose() {
        texture.dispose();
        font.dispose();
    }

    public boolean hitTree(){
        return hitTree;
    }
}
