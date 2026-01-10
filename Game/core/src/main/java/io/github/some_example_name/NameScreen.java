package io.github.some_example_name;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * NEW
 *
 * <code> NameScreen </code> implements a name entry screen using a simple
 * on-screen keyboard. The player enters a first name (mandatory) and an optional
 * last name, then starts the game.
 *
 * <p>Input options:</p>
 * <ul>
 * <li>Mouse click - selects keys on the on-screen keyboard.</li>
 * <li>TAB - switches between first name and last name.</li>
 * <li>SPACE - confirms and continues (first name must not be empty).</li>
 * <li>DEL / BACKSPACE - deletes the last character of the active field.</li>
 * <li>ESC - returns to the tutorial screen.</li>
 * </ul>
 *
 * @see com.badlogic.gdx.Screen Screen
 */
public class NameScreen implements Screen {
    private final MyGame game;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private BitmapFont font;
    private FitViewport viewport;

    private String firstName = "";
    private String lastName = "";
    private boolean typingFirstName = true;

    /**
     * Layout for the on-screen keyboard. Each string represents the label/action
     * for a key.
     */
    private final String[][] onscreenKeyboard = {
        {"A", "B", "C", "D", "E", "F", "G", "H"},
        {"I", "J", "K", "L", "M", "N", "O", "P"},
        {"Q", "R", "S", "T", "U", "V", "W", "X"},
        {"Y", "Z", "DEL", "SPACE", "TAB",}
    };

    private Rectangle[][] keyboardRects;  // Button hitboxes
    private Texture keyboardTexture;
    private final int KEY_WIDTH = 60;
    private final int KEY_HEIGHT = 50;
    private final int NAME_MENU_WIDTH = 640;
    private final int NAME_MENU_HEIGHT = 480;

    /**
     * Constructor for <code> NameScreen </code>, creating the camera, rendering objects,
     * and generating the on-screen keyboard layout.
     *
     * @param game Game creator used to store the player's name and change screens
     */
    public NameScreen(MyGame game) {
        this.game = game;
        screenRender();
        createKeyboard();
    }


    /**
     * Initialise core rendering objects used by this screen, including the camera,
     * batch, font, viewport, and the keyboard button texture.
     */
    private void screenRender() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, NAME_MENU_WIDTH, NAME_MENU_HEIGHT);
        batch = new SpriteBatch();
        font = new BitmapFont(); //uses default font of Arial
        font.getData().setScale(1f); //this makes the text bigger

        viewport = new FitViewport(NAME_MENU_WIDTH, NAME_MENU_HEIGHT, camera);

        // The White button texture
        Pixmap whiteButton = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        whiteButton.setColor(0.8f, 0.8f, 0.9f, 1f);
        whiteButton.drawPixel(0, 0);
        keyboardTexture = new Texture(whiteButton);
        whiteButton.dispose();
    }

    /**
     * Create the hitboxes for each key on the on-screen keyboard.
     * The positions are generated in a grid formation.
     */
    private void createKeyboard() {
        keyboardRects = new Rectangle[onscreenKeyboard.length][onscreenKeyboard[0].length];
        int startX = 50, startY = 255;

        for (int row = 0; row < onscreenKeyboard.length; row++) {
            for (int col = 0; col < onscreenKeyboard[row].length; col++) {
                keyboardRects[row][col] = new Rectangle(
                    startX + col * (KEY_WIDTH + 5),
                    startY - row * (KEY_HEIGHT + 5),
                    KEY_WIDTH, KEY_HEIGHT
                );
            }
        }
    }


    /**
     * Render the name screen, including the on-screen keyboard and current input.
     * In addition, it processes the mouse clicks and
     * the key shortcuts for editing/confirming the name.
     *
     * @param delta Time in seconds since the last frame finished rendering
     * @see com.badlogic.gdx.Screen#render Screen.render().
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // Name display
        font.draw(batch, "Enter Your Full Name:", 50, 460);
        font.draw(batch, "([MANDATORY] You must type at least a character in First Name)", 50, 430);
        font.draw(batch, (typingFirstName ? "--> " : "  ") + "First: " + firstName, 50, 410);
        font.draw(batch, (!typingFirstName ? "--> " : "  ") + "Last:  " + lastName, 50, 380);

        // Instructions
        font.draw(batch, "Use the Onscreen Keyboard for name -> TAB = switch | " +
                             "Space = done | " +
                             "Esc = back", 50, 360);

        // Keyboard
        font.draw(batch, "KEYBOARD:", 50, 330);
        for (int row = 0; row < onscreenKeyboard.length; row++) {
            for (int col = 0; col < onscreenKeyboard[row].length; col++) {
                Rectangle rect = keyboardRects[row][col];
                String keyboardButton = onscreenKeyboard[row][col];

                if (!keyboardButton.isEmpty()) {
                    // Draw button
                    batch.draw(keyboardTexture, rect.x, rect.y, rect.width, rect.height);
                    // Draw letter
                    font.draw(batch, keyboardButton, rect.x + 12, rect.y + 35);
                }
            }
        }

        batch.end();

        // Mouse clicks
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            Vector3 mouse = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            viewport.unproject(mouse);

            for (int row = 0; row < onscreenKeyboard.length; row++) {
                for (int col = 0; col < onscreenKeyboard[row].length; col++) {
                    if (keyboardRects[row][col].contains(mouse.x, mouse.y)) {
                        String action = onscreenKeyboard[row][col];
                        handleKeyPress(action);
                        return;
                    }
                }
            }
        }

        // Keyboard shortcuts
        if (Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {
            typingFirstName = !typingFirstName;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && firstName.length() > 0) {
            game.setPlayerName(firstName, lastName);
            game.setScreen(new GameScreen(game));
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new TutorialScreen(game));
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.DEL) || Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)) {
            if (typingFirstName && !firstName.isEmpty()) {
                firstName = firstName.substring(0, firstName.length() - 1);
            } else if (!typingFirstName && !lastName.isEmpty()) {
                lastName = lastName.substring(0, lastName.length() - 1);
            }
        }
    }

    /**
     * This handles the action for a key press from the on-screen keyboard.
     * Supports deleting, switching fields, confirming, or appending letters.
     *
     * @param action Button label/action pressed
     */
    private void handleKeyPress(String action) {
        if (action.equals("DEL")) {
            if (typingFirstName && !firstName.isEmpty()) {
                firstName = firstName.substring(0, firstName.length() - 1);
            } else if (!typingFirstName && !lastName.isEmpty()) {
                lastName = lastName.substring(0, lastName.length() - 1);
            }
        } else if (action.equals("TAB")) {
            typingFirstName = !typingFirstName;
        } else if (action.equals("SPACE") && firstName.length() > 0){
            game.setPlayerName(firstName, lastName);
            game.setScreen(new GameScreen(game));
        } else {
            // Letter
            if (typingFirstName) firstName += action;
            else lastName += action;
        }
    }

    /**
     * Resize the viewport when the window size changes.
     *
     * @param width Current width of window
     * @param height Current height of window
     * @see com.badlogic.gdx.Screen#resize Screen.resize().
     */
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);

    }

    /**
     * Dispose of screen assets when leaving the name screen.
     *
     * @see com.badlogic.gdx.Screen#dispose Screen.dispose()
     */
    @Override public void dispose() {
        batch.dispose(); font.dispose();
        if (keyboardTexture != null) keyboardTexture.dispose();
    }

    /** Unimplemented */
    @Override
    public void pause() {

    }

    /** Unimplemented */
    @Override
    public void resume() {

    }

    /** Unimplemented */
    @Override
    public void hide() {

    }

    /** Unimplemented */
    @Override
    public void show() {

    }

}
