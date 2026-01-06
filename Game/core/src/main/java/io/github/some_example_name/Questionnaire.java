package io.github.some_example_name;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.*;


/**
 * NEW
 *
 * <code>Questionnaire</code> it represent a quiz event area which has been loaded from the Tiled map.
 * When the player enters the quiz area they can press <code>E</code> to start the quiz.
 * The player must answer using keys <code>A</code>, <code>B</code>, <code>C</code>, or <code>D</code>.
 * A correct answer rewards the player by freezing all deans, while an incorrect answer
 * spawns an additional dean as a penalty.
 */
public class Questionnaire {
    private Rectangle quizArea;
    private boolean discovered = false;
    private boolean answered = false;
    private boolean showQuiz = false;
    private boolean playerFrozen = false;
    private boolean questionSuccess = false;
    private String resultText = "";
    private float resultTimer = 0f;

    /**
     * Constructor for <code>Questionnaire</code>, loading the questionnaire area rectangle
     * from an object layer in a Tiled map.
     * @param tiledMap The Tiled map containing the quiz object
     * @param layerName The Name of the object layer containing the questionnaire area
     */
    public Questionnaire(TiledMap tiledMap, String layerName) {
        // Find the questionnaire object in the specified tiled map layer
        for (MapObject obj : tiledMap.getLayers().get(layerName).getObjects()) {
            if (obj instanceof RectangleMapObject && "Questionnaire".equalsIgnoreCase(obj.getName())) {
                quizArea = ((RectangleMapObject) obj).getRectangle();
                break;
            }
        }
    }

    /**
     * Update the quiz event each frame. Handles:
     * <ul>
     * <li>Detecting when the player enters the quiz area</li>
     * <li>Starting the quiz when <code>E</code> is pressed</li>
     * <li>Processing answer keys <code>A-D</code></li>
     * <li>Managing the post-answer result message timer</li>
     * </ul>
     * @param player The player character used for collision detection
     * @param gameScreen The game screen used to trigger rewards/penalties --> freeze or spawn deans
     */
    public void update(Player player, GameScreen gameScreen) {
        if (quizArea == null || answered) return;

        Rectangle playerRect = new Rectangle(
            player.getPosition().x,
            player.getPosition().y,
            16,
            16
        );

        if (playerRect.overlaps(quizArea)) {
            discovered = true;

            // Press E to begin the questionnaire, freeze the player while answering
            if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                showQuiz = true;
                playerFrozen = true;
            }

            if (showQuiz) {
                // Incorrect options spawn an extra dean
                if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
                    gameScreen.spawnSecondDean(); // FAIL
                    questionSuccess = false;
                    endQuestionnaire();
                } if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
                    gameScreen.spawnSecondDean(); // FAIL
                    questionSuccess = false;
                    endQuestionnaire();
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.C)) { // Correct option freezes all deans
                    gameScreen.freezeAllDeans();  // PASS
                    questionSuccess = true;
                    endQuestionnaire();
                }
                else if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
                    gameScreen.spawnSecondDean(); // FAIL
                    questionSuccess = false;
                    endQuestionnaire();
                }
            }
        }

        // Once the question has been answered allow player movement again
        if (answered) {
            playerFrozen = false;
        }

        // decrement the result timer if its active and more than 0
        if (resultTimer > 0) {
            resultTimer -= Gdx.graphics.getDeltaTime();
        }
    }

    /**
     * End the questionnaire and save the result. The questionnaire is answered
     * the player is unfrozen, and sets a short result message.
     */
    private void endQuestionnaire() {
        showQuiz = false;
        answered = true;
        playerFrozen = false;
        resultText = questionSuccess ? "CORRECT, freezing Deans Permanently" : "INCORRECT, more Deans";
        resultTimer = 3f;
    }

    /**
     * Return whether the player should be frozen.
     * @return True if player movement should be blocked, false otherwise
     */
    public boolean isPlayerFrozen() {
        return playerFrozen;
    }

    /**
     * Render the quiz prompt, questionnaire questions, and the result text.
     * @param batch SpriteBatch used to draw text
     * @param font Font used to render quiz UI text
     */
    public void render(SpriteBatch batch, BitmapFont font) {
        if (quizArea == null) return;

        if (discovered && !answered) {
            font.draw(batch, "[E] Quiz?", quizArea.x , quizArea.y + 100);
        }

        if (showQuiz) {
            font.draw(batch, "Which of these cannot be used", quizArea.x , quizArea.y + 160);
            font.draw(batch, "for a variable name in Java?", quizArea.x , quizArea.y + 140);
            font.draw(batch, "Answer with your keyboard", quizArea.x , quizArea.y + 120);
            font.draw(batch, "[A] Identifier & Keyword", quizArea.x , quizArea.y + 40);
            font.draw(batch, "[B] identifier", quizArea.x , quizArea.y);
            font.draw(batch, "[C] Keyword,", quizArea.x , quizArea.y - 40);
            font.draw(batch, "[D] None of these", quizArea.x , quizArea.y - 80);
        }

        if (resultTimer > 0) {
            font.draw(batch, resultText, quizArea.x , quizArea.y + 160);
        }
    }

    /**
     * Return whether the questionnaire has been answered.
     * @return True if the questionnaire is complete, false otherwise
     */
    public boolean isAnswered() {
        return answered;
    }
}
