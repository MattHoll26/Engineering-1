package io.github.some_example_name;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;


/**
 * NEW
 *
 * <code>Save_Leaderboard</code> handles persistence for the game's leaderboard.
 * Scores are stored locally in a JSON file and loaded/saved using LibGDX utilities.
 * The leaderboard is limited to the top 5 scores by default.
 */
public class Save_Leaderboard {
    private static final String Leaderboard_file = "leaderboard.json";
    private final Json leaderboardJSON = new Json();
    private static final int maxScore = 5;

    /**
     * <code>ScoreEntry</code> represents a single leaderboard record consisting of
     * a player's full name and a numeric score.
     */
    public static class ScoreEntry {
        /** Player full name stored in the leaderboard. */
        public String fullName;
        /** Player score stored in the leaderboard. */
        public int score;

        /**
         * Default constructor required for LibGDX JSON serialization.
         */
        public ScoreEntry() {} // required in order for JSON serialisation

        /**
         * Construct a leaderboard entry with a name and score.
         * @param fullName Full name of the player.
         * @param score Score achieved by the player.
         */
        public ScoreEntry(String fullName, int score) {
            this.fullName = fullName;
            this.score = score;
        }
    }

    /**
     * Loads the leaderboard scores from the local JSON file.
     * If the file does not exist or cannot be parsed, an empty list is returned.
     * @return Array of loaded leaderboard scores
     */
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

    /**
     * Save leaderboard scores to the local JSON file. Existing file contents are overwritten.
     * @param scores Array of scores to store.
     */
    private void saveScores(Array<ScoreEntry> scores) {
        FileHandle scoreFile = Gdx.files.local(Leaderboard_file);
        scoreFile.writeString(leaderboardJSON.prettyPrint(scores), false);
    }

    /**
     * Add a new score to the leaderboard, sort the leaderboard in descending order,
     * and keep only the top {@value #maxScore} entries.
     * @param fullName Full name of the player.
     * @param score Score achieved by the player.
     */
    public void addScore(String fullName, int score) {
        Array<ScoreEntry> finalScores = loadScores();
        // Add new entry, then sort descending by score
        finalScores.add(new ScoreEntry(fullName, score));
        finalScores.sort((entryA, entryB) -> Integer.compare(entryB.score, entryA.score));
        // Trim any extra entries beyond the maximum allowed.
        while (finalScores.size > maxScore) finalScores.removeIndex(finalScores.size - 1);
        saveScores(finalScores);
    }

    /**
     * Return the current leaderboard scores (top 5).
     * @return Array of high scores loaded from storage.
     */
    public Array<ScoreEntry> getHighScores() {
        return loadScores();
    }
}
