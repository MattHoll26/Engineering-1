package io.github.some_example_name;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.*;


public class Questionnaire {
    private Rectangle quizArea;
    private boolean discovered = false;
    private boolean answered = false;
    private boolean showQuiz = false;
    private boolean playerFrozen = false;
    private boolean questionSuccess = false;
    private String resultText = "";
    private float resultTimer = 0f;

    public Questionnaire(TiledMap tiledMap, String layerName) {
        //
        for (MapObject obj : tiledMap.getLayers().get(layerName).getObjects()) {
            if (obj instanceof RectangleMapObject && "Questionnaire".equalsIgnoreCase(obj.getName())) {
                quizArea = ((RectangleMapObject) obj).getRectangle();
                break;
            }
        }
    }

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

            if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                showQuiz = true;
                playerFrozen = true;
            }

            if (showQuiz) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
                    gameScreen.spawnSecondDean(); // FAIL
                    questionSuccess = false;
                    endQuiz();
                } if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
                    gameScreen.spawnSecondDean(); // FAIL
                    questionSuccess = false;
                    endQuiz();
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
                    gameScreen.freezeAllDeans();  // PASS
                    questionSuccess = true;
                    endQuiz();
                }
                else if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
                    gameScreen.spawnSecondDean(); // FAIL
                    questionSuccess = false;
                    endQuiz();
                }
            }
        }

        if (answered) {
            playerFrozen = false;
        }

        if (resultTimer > 0) {
            resultTimer -= Gdx.graphics.getDeltaTime();
        }
    }

    private void endQuiz() {
        showQuiz = false;
        answered = true;
        playerFrozen = false;
        resultText = questionSuccess ? "CORRECT, freezing Deans Permanently" : "INCORRECT, more Deans";
        resultTimer = 3f;
    }

    public boolean isPlayerFrozen() {
        return playerFrozen;
    }

    public void render(SpriteBatch batch, BitmapFont font) {
        if (quizArea == null) return;

        if (discovered && !answered) {
            font.draw(batch, "[E] Quiz?", quizArea.x - 30, quizArea.y + 100);
        }

        if (showQuiz) {
            font.draw(batch, "Which of these cannot be used", quizArea.x - 100, quizArea.y + 160);
            font.draw(batch, "for a variable name in Java?", quizArea.x - 100, quizArea.y + 140);
            font.draw(batch, "Answer with your keyboard", quizArea.x - 100, quizArea.y + 120);
            font.draw(batch, "[A] Identifier & Keyword", quizArea.x - 60, quizArea.y + 40);
            font.draw(batch, "[B] identifier", quizArea.x - 60, quizArea.y);
            font.draw(batch, "[C] Keyword,", quizArea.x - 60, quizArea.y - 40);
            font.draw(batch, "[D] None of these", quizArea.x - 60, quizArea.y - 80);
        }

        if (resultTimer > 0) {
            font.draw(batch, resultText, quizArea.x - 100, quizArea.y + 160);
        }
    }

    public boolean isAnswered() {
        return answered;
    }
}
