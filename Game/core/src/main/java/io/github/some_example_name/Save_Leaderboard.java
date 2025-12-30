package io.github.some_example_name;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;


public class Save_Leaderboard {
    private static final String Leaderboard_file = "leaderboard.json";
    private final Json leaderboardJSON = new Json();
    private static final int maxScore = 5;

    public static class ScoreEntry {
        public String fullName;
        public int score;

        public ScoreEntry() {} // required in order for JSON serialisation

        public ScoreEntry(String fullName, int score) {
            this.fullName = fullName;
            this.score = score;
        }
    }

    private Array<ScoreEntry> loadScores() {
        try {
            FileHandle scoreFile = Gdx.files.local(Leaderboard_file);
            if (scoreFile.exists()) {
                String LeaderboardJSONData = scoreFile.readString();
                return leaderboardJSON.fromJson(Array.class, ScoreEntry.class, LeaderboardJSONData);
            }
        } catch (Exception error) {
            // Rather the file doesn't exist or there is an error code; return empty
        }
        return new Array<>();
    }

    private void saveScores(Array<ScoreEntry> scores) {
        FileHandle scoreFile = Gdx.files.local(Leaderboard_file);
        scoreFile.writeString(leaderboardJSON.prettyPrint(scores), false);
    }

    public void addScore(String fullName, int score) {
        Array<ScoreEntry> finalScores = loadScores();
        finalScores.add(new ScoreEntry(fullName, score));
        finalScores.sort((entryA, entryB) -> Integer.compare(entryB.score, entryA.score));
        while (finalScores.size > maxScore) finalScores.removeIndex(finalScores.size - 1);
        saveScores(finalScores);
    }

    public Array<ScoreEntry> getHighScores() {
        return loadScores();
    }
}
