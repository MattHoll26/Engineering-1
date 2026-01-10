package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.Array;


/**
 * EXTENDED
 *
 * <code> GameScreen </code> implements the main gameplay logic and rendering as one class,
 * to process user input, and redraw the frames and update the game asset states as
 * the game progresses.
 *
 * <p>This screen is responsible for:</p>
 * <ul>
 * <li>Loading and rendering the main Tiled map.</li>
 * <li>Updating the player, enemies, and events.</li>
 * <li>Tracking the timer, score penalties, and achievement conditions.</li>
 * <li>Switching to win/lose/menu screens based on game state.</li>
 * </ul>
 *
 * @see com.badlogic.gdx.Screen Screen.
 */

public class GameScreen implements Screen {
    private final MyGame game;
    private boolean isPaused = false;

    TiledMap tiledMap;
    OrthogonalTiledMapRenderer mapRenderer;
    OrthographicCamera camera;
    FitViewport viewport;

    private SpriteBatch batch;
    private Player player;

    private final Stage uiStage;
    private final Table uiTable;
    private final Skin uiSkin;
    private final GameTimer gameTimer;

    private BusTicket busTicket;
    private Drown drown;
    private Locker locker;
    private Slow_Down bush;
    private Decrease_Time tree;
    private Extra_Time extraTime;
    private Teleport labEquipment;
    private BitmapFont font;
    private boolean canPickUpTicket = false;
    private boolean hasDrowned = false;
    private int timesDrowned = 0;

    private Rectangle busInteractionArea;
    private boolean canEndGame = false;

    private final int MAP_WIDTH = 640;
    private final int MAP_HEIGHT = 640;

    private Dean dean;
    private Patrol_Dean patrolDean1;
    private Patrol_Dean patrolDean2;
    private Patrol_Dean patrolDean3;
    private Patrol_Dean extraDean;

    private Questionnaire questionnaire;

    private Freeze_Dean freezeDean;

    private NPC friend;
    private int timesCaughtByDean = 0;
    private int timesCaughtByPatrol = 0;
    private BitmapFont catchCounterFont;

    /**
     * EXTENDED
     *
     * Constructor for <code> GameScreen </code>, using the game creator
     * in <code> MyGame </code> to create all main game and UI assets.
     * @param game Game creator.
     */
    public GameScreen(MyGame game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, MAP_WIDTH, MAP_HEIGHT);
        camera.zoom=0.5f;
        camera.update();

        // Load the TMX map created in Tiled.
        tiledMap = new TmxMapLoader().load("Tile Maps/Final Game Map - Maze.tmx");

        drown = new Drown(
            tiledMap,
            "Events", // same object layer as Bus & BusTicket
            560,      // respawn X (same as dean reset)
            180       // respawn Y
        );

        questionnaire = new Questionnaire(
            tiledMap,
            "Events" // same object layer as Bus & BusTicket and Drown
        );

        freezeDean = new Freeze_Dean(
            tiledMap,
            "Events" // same object layer as Bus & BusTicket & Drown & questionnaire
        );


        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        viewport = new FitViewport(MAP_WIDTH, MAP_HEIGHT, camera);

        batch = new SpriteBatch();
        // Spawn entities/events at fixed world coordinates.
        player = new Player(560, 180);
        locker = new Locker(495, 895);
        bush = new Slow_Down(770, 545);
        tree = new Decrease_Time(270, 9);
        labEquipment = new Teleport(750,610, this);
        extraTime = new Extra_Time(300, 120);
        dean = new Dean(300, 310,     player, this);
        patrolDean1 = new Patrol_Dean(140, 190, 90, 260, this);
        patrolDean2 = new Patrol_Dean(170, 130, 90, 260, this);
        patrolDean3 = new Patrol_Dean(200, 100, 90, 260, this);
        friend = new NPC(560, 600);

        catchCounterFont = new BitmapFont();
        catchCounterFont.getData().setScale(1.5f);
        font = new BitmapFont();

        // Pull the named objects from the Tiled "Events" layer.
        MapObjects eventObjects = tiledMap.getLayers().get("Events").getObjects();

        // Create the ticket using the rectangle named "BusTicket".
        MapObject ticketObject = eventObjects.get("BusTicket");
        if (ticketObject != null && ticketObject instanceof RectangleMapObject) {
            RectangleMapObject rect = (RectangleMapObject) ticketObject;
            busTicket = new BusTicket(rect.getRectangle().x, rect.getRectangle().y);
        }

        // Store the bus interaction zone rectangle named "Bus".
        MapObject busObject = eventObjects.get("Bus");
        if (busObject != null && busObject instanceof RectangleMapObject) {
            this.busInteractionArea = ((RectangleMapObject) busObject).getRectangle();
        }

        // Scene2D UI setup for the timer HUD.
        uiSkin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        uiStage = new Stage(new FitViewport(MAP_WIDTH, MAP_HEIGHT));
        uiTable = new Table();
        uiTable.setFillParent(true);
        uiStage.addActor(uiTable);
        gameTimer = new GameTimer(uiSkin, uiTable);
        uiTable.top().right().pad(10,0,0,10);
    }

    /**
     * EXTENDED
     *
     * Update game state from last frame, and render a new frame for the Screen
     * using updated assets.
     * @param delta Time in seconds since last frame finished rendering.
     * @see com.badlogic.gdx.Screen#render Screen.render().
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        handleInput();

        // If paused, render a frozen frame and return early
        if (isPaused) {
            // Render the current frame
            camera.update();
            mapRenderer.setView(camera);
            mapRenderer.render();

            batch.setProjectionMatrix(camera.combined);
            batch.begin();

            // Render the "Game Paused" message
            font.draw(batch, "Game Paused", camera.position.x - 50, camera.position.y + 50);

            // Render the player and other static elements
            player.render(batch);
            if (busTicket != null && busTicket.isCollected()) {
                busTicket.renderAsIcon(batch, camera);
            }

            batch.end();

            // Render the UI stage
            uiStage.act(delta);
            uiStage.draw();

            return; // Skip the rest of the game logic
        }

        // Update entities that depend on player position.
        friend.update(player);
        // Update all deans.
        dean.update(delta);
        patrolDean1.update(delta);
        patrolDean2.update(delta);
        patrolDean3.update(delta);
        dean.update(delta);
        // Extra dean (spawned as a penalty) behaves like a patrol dean.
        if (extraDean != null) {
            extraDean.update(delta);
            if (player.getPosition().dst(extraDean.getPosition()) < 16f) {
                player.getPosition().set(560, 180);
                timesCaughtByPatrol++;
            }
        }

        // Collision check for main chasing dean.
        if (player.getPosition().dst(dean.getPosition()) < 16f) {
            player.getPosition().set(560, 180);
            timesCaughtByDean++;
            dean.resetToStart(timesCaughtByDean); //send the dean back to his starting position or other side of the map to ensure he can't spawn camp the player
        }

        // Collision check for patrol deans.
        if (player.getPosition().dst(patrolDean1.getPosition()) < 16f ||
            player.getPosition().dst(patrolDean2.getPosition()) < 16f ||
            player.getPosition().dst(patrolDean3.getPosition()) < 16f) {

            player.getPosition().set(560, 180);
            timesCaughtByPatrol++;
        }

        // Drown Check
        if (drown.update(player)) {
            hasDrowned = true;
            timesDrowned++;
        }


        // Update map events
        locker.update(player, delta);
        bush.update(player, delta);
        tree.update(player, gameTimer, delta);
        extraTime.update(player, gameTimer, delta);
        labEquipment.update(player, delta);

        // Hidden quiz event
        if (questionnaire != null) {
            questionnaire.update(player, this);
        }

        // Freeze event
        freezeDean.update(player, this);

        // The logic behind the ticket pick up and the end game bus
        if (busTicket != null) {
            if (!busTicket.isCollected()) {
                if (player.getPosition().dst(busTicket.getPosition()) < 16) {
                    busTicket.discover();
                    canPickUpTicket = true;
                } else {
                    canPickUpTicket = false;
                }
            } else {
                Rectangle playerRect = new Rectangle(
                    player.getPosition().x,
                    player.getPosition().y,
                    16,
                    16
                );

                if (busInteractionArea != null && playerRect.overlaps(busInteractionArea)) {
                    canEndGame = true;
                } else {
                    canEndGame = false;
                }
            }
        }

        // Camera follows the player
        camera.position.set(player.getPosition().x, player.getPosition().y, 0);
        camera.update();

        mapRenderer.setView(camera);
        mapRenderer.render();


        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        //switch to screen coordinates for the UI elements
        batch.setProjectionMatrix(uiStage.getCamera().combined);

        //draw the three events encountered checklists in the top left hand corner of the screen
        //events get updates using a ternary operator which is like a condensed if/else statement -> it is set out like: (condition ? value_if_true : value_if_false)

        int positiveEvents = 0;
        if (locker.lockerSearched()) positiveEvents++;
        if (extraTime.gainedTime()) positiveEvents++;
        if (freezeDean.isUsed()) positiveEvents++;

        font.draw(batch, "Positive Events Encountered = " + positiveEvents + "/3", 35, 630);//this means if the locker boost is active (the bus ticket has been picked up) display that the event 1/1 has been encountered otherwise 0/1

        int negativeEvents = 0;
        if (timesCaughtByDean > 0) negativeEvents++;
        if (timesCaughtByPatrol > 0) negativeEvents ++;
        if (hasDrowned) negativeEvents++;
        if (bush.bushFall()) negativeEvents++;
        if (tree.hitTree()) negativeEvents++;

        font.draw(batch, "Negative Events Encountered = " + negativeEvents + "/5" , 35, 610);

        int hiddenEvents = 0;
        if (busTicket.isCollected()) hiddenEvents++;
        if (labEquipment.teleportHappened()) hiddenEvents++;
        if (questionnaire.isAnswered()) hiddenEvents++;

        font.draw(batch, "Hidden Events Encountered = " + hiddenEvents + "/3", 35, 590);



        //switch back to the game coordinates for game objects
        batch.setProjectionMatrix(camera.combined);

        if (busTicket != null) {
            busTicket.render(batch);
        }

        // Interaction prompt for ticket pickup.
        if (canPickUpTicket) {
            font.draw(
                batch,
                "Press E to pick up",
                player.getPosition().x - 50,
                player.getPosition().y + 30
            );
        }

        // Interaction prompt for using ticket at the bus.
        if (canEndGame) {
            font.draw(
                batch,
                "Press E to use ticket",
                player.getPosition().x - 50,
                player.getPosition().y + 30
            );
        }

        // The messages will appear on top by rendering player last.
        locker.render(batch);
        bush.render(batch);
        tree.render(batch);
        extraTime.render(batch);
        labEquipment.render(batch);
        dean.render(batch);
        friend.render(batch);
        patrolDean1.render(batch);
        patrolDean2.render(batch);
        patrolDean3.render(batch);
        dean.render(batch);
        friend.render(batch);
        if (extraDean != null) {
            extraDean.render(batch);
        }

        if (questionnaire != null) {
            questionnaire.render(batch, font);
        }

        freezeDean.render(batch, font);


        player.render(batch);

        if (busTicket != null && busTicket.isCollected()) {
            busTicket.renderAsIcon(batch, camera);
        }

        batch.end();

        // Decrement the timer only if the game is not paused
        if (!isPaused) {
            gameTimer.decrementTimer(delta);
        }

        // When time runs out, trigger time-up behaviour.
        if (gameTimer.getTimeLeft() == 0) {
            gameTimer.onTimeUp();
            game.setScreen(new MenuScreen(game));
        }

        // Escape key returns to menu.
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MenuScreen(game));
        }

        // Time-up to the game over screen
        if (gameTimer.getTimeLeft() == 0) {
            gameTimer.onTimeUp();
            game.setScreen(new GameOverScreen(game));
        }
        uiStage.act(delta);
        uiStage.draw();
    }

    /**
     * NEW
     *
     * Calculate the achievements earned by the player based on gameplay state.
     *
     * <p>Achievements are displayed on the win screen and can award positive or
     * negative bonus score.</p>
     *
     * @return Array of achievements earned during this run.
     */
    public Array<Achievement> calculateAchievements() {
        Array<Achievement> earnedAchievement = new Array<>();

        // Hidden events hunter --> Collected ticket and used teleporter
        if (busTicket != null && busTicket.isCollected() &&
            labEquipment != null && labEquipment.teleportHappened() &&
            questionnaire != null && questionnaire.isAnswered()) {
            earnedAchievement.add(new Achievement("Secret Hunter", "Found all hidden events", 100));
        }

        // All Positive Events
        if (locker.lockerSearched() && extraTime.gainedTime() && freezeDean.isUsed()) {
            earnedAchievement.add(new Achievement("Positive Master", "Found all positive events", 80));
        }

        // All Negative Events
        if (timesCaughtByDean > 0 && timesCaughtByPatrol > 0 && hasDrowned && bush.bushFall() && tree.hitTree()) {
            earnedAchievement.add(new Achievement("Masochist", "Found all negative events", -30));
        }

        // All Events
        if (locker.lockerSearched() && extraTime.gainedTime() && freezeDean.isUsed() &&
            timesCaughtByDean > 0 && timesCaughtByPatrol > 0 && hasDrowned &&
            bush.bushFall() && tree.hitTree() && busTicket.isCollected() &&
            labEquipment.teleportHappened() && questionnaire.isAnswered()) {
            earnedAchievement.add(new Achievement("Completionist", "Found all Events!", 200));
        }

        // Speedster --> Never caught by any Dean
        if (timesCaughtByDean == 0 && timesCaughtByPatrol == 0) {
            earnedAchievement.add(new Achievement("Speedster", "Never caught by a Dean", 150));
        }

        // Class Clown -->  more than 3 times
        if (timesCaughtByDean + timesCaughtByPatrol >= 3) {
            earnedAchievement.add(new Achievement("Class Clown", "Caught 3+ times", -50));
        }

        // Quiz Taker --> Attempted the Quiz (Pass or Fail)
        if (questionnaire != null && questionnaire.isAnswered()) {
            earnedAchievement.add(new Achievement("Quiz Taker", "Attempted the Technical Quiz", 50));
        }

        // Natural Barrier Victim --> Natural Barrier Victim  Drowned or hit obstacles
        if (hasDrowned || (bush != null && bush.bushFall()) || (tree != null && tree.hitTree())) {
            earnedAchievement.add(new Achievement("Natural Barrier Victim", "Hit an natural obstacle", -20));
        }

        return earnedAchievement;
    }



    /**
     * EXTENDED
     *
     * Move the player and interacting with the world and menus every frame when
     * the corresponding keys are pressed:
     * <ul>
     * <li> WASD - Move Character Up/Left/Down/Right.</li>
     * <li> E - Interact with items.</li>
     * <li> P - Pause Game.</li>
     * </ul>
     */
    private void handleInput() {
        if (questionnaire != null && questionnaire.isPlayerFrozen()){
            return; // freeze the character during the quiz
        }

        float moveSpeed = 1f;
        if (locker != null && locker.isBoostActive()) {
            moveSpeed = 2f;
        }
        if (bush != null && bush.isBoostActive()) {
            moveSpeed = 0.5f;
        }

        float newX = player.getPosition().x;
        float newY = player.getPosition().y;

        // independent key checks
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            newY += moveSpeed;
            player.setDirection(Player.Direction.UP);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            newY -= moveSpeed;
            player.setDirection(Player.Direction.DOWN);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            newX -= moveSpeed;
            player.setDirection(Player.Direction.LEFT);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            newX += moveSpeed;
            player.setDirection(Player.Direction.RIGHT);
        }

        // Ticket pickup event
        else if (canPickUpTicket && Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            busTicket.collect();
            canPickUpTicket = false;
        }

        // Change pause functionality to use the P key, include this in docstrings
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            isPaused = !isPaused; // Toggle pause state
            return; // Skip other input handling when toggling pause
        }

        if (isPaused) {
            return; // Do not process input if the game is paused
        }

        // End game (win condition) interaction when standing in the bus zone with the ticket, calculating the score with the achievement.
        else if (canEndGame && Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            int baseScore = calculateFinalScore();
            int totalPenalty = calculateTotalPenalty();
            int timeRemaining = (int) gameTimer.getTimeLeft();
            int timesCaught = getTimesCaughtByDean();

            Array<Achievement> achievements = calculateAchievements();

            int totalAchievementScore = 0;
            for (Achievement currentAchievement : achievements) {
                // Add the specific bonus score (positive or negative) of the current achievement to our total
                totalAchievementScore = totalAchievementScore + currentAchievement.bonusScore;
            }

            int finalScore = Math.max(0, baseScore + totalAchievementScore);

            String playerName = game.getPlayerFullName(); // get the full player name for the leaderboard
            Save_Leaderboard leaderboard = new Save_Leaderboard();
            leaderboard.addScore(playerName, finalScore); // add score to the leaderboard if it is in the top scores
            game.setScreen(new WinScreen(game, finalScore, timeRemaining, totalPenalty, achievements));
        }

        float oldX = player.getPosition().x;
        float oldY = player.getPosition().y;

        if (!isCellBlocked(newX, newY) && !isBounds(newX, newY)) {
            player.getPosition().set(newX, newY);
        }

        else if (!isCellBlocked(newX, oldY) && !isBounds(newX, oldY)) {
            player.getPosition().set(newX, oldY);
        }

        else if (!isCellBlocked(oldX, newY) && !isBounds(oldX, newY)) {
            player.getPosition().set(oldX, newY);
        }

    }

    /**
     * EXTENDED JAVADOC
     * Returns if the cell at a given coordinate in the world allows an entity
     * to move onto it. Useful for checking collisions when moving player or another
     * entity.
     *
     * <p>This method checks tile properties (and/or layer properties) for a
     * <code>collidable</code> flag.</p>
     *
     * @param x Horizontal position of cell in the world.
     * @param y Vertical position of cell in the world.
     * @return True if cell blocks entities to move onto it, False if entities can move onto it.
     */
    public boolean isCellBlocked(float x, float y) {
        for (int i = 0; i < tiledMap.getLayers().getCount(); i++) {
            if (tiledMap.getLayers().get(i) instanceof TiledMapTileLayer) {
                TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get(i);
                int tileX = (int) ((x + 8) / layer.getTileWidth());
                int tileY = (int) ((y + 8) / layer.getTileHeight());
                TiledMapTileLayer.Cell cell = layer.getCell(tileX, tileY);

                if (cell != null && cell.getTile() != null) {
                    if (cell.getTile().getProperties().containsKey("collidable") || layer.getProperties().containsKey("collidable")) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * NEW
     *
     * Return whether a cell blocks movement for the dean.
     *
     * <p>This is similar to {@link #isCellBlocked(float, float)} but also treats
     * any layer with a <code>door</code> property as blocked for the dean.</p>
     *
     * @param x The horizontal position of cell in the world.
     * @param y The vertical position of cell in the world.
     * @return True if the cell blocks dean movement, false otherwise.
     */
    public boolean isCellBlockedForDean(float x, float y) {
        for (int i = 0; i < tiledMap.getLayers().getCount(); i++) {
            if (tiledMap.getLayers().get(i) instanceof TiledMapTileLayer) {
                TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get(i);
                int tileX = (int) ((x + 8) / layer.getTileWidth());
                int tileY = (int) ((y + 8) / layer.getTileHeight());
                TiledMapTileLayer.Cell cell = layer.getCell(tileX, tileY);

                if (cell != null && cell.getTile() != null) {
                    if (layer.getProperties().containsKey("collidable") ||
                        layer.getProperties().containsKey("door")) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    /**
     * NEW
     *
     * Checks if the player's future position overlaps with the "Bounds" object layer of the Tiled map.
     * @param x The future x-coordinate of the player.
     * @param y The future y-coordinate of the player.
     * @return {@code true} if a collision is detected (movement blocked), {@code false} otherwise.
     */
    public boolean isBounds(float x, float y) {
        // Get the bounds layer unless it does not exist (then return false which is no collision)
        if (tiledMap.getLayers().get("Bounds") == null) {
            return false;
        }

        // Get the player width and height to create a temporary collision object
        float playerWidth = player.currentFrame.getRegionWidth();
        float playerHeight = player.currentFrame.getRegionHeight();
        Rectangle playerFutureRect = new Rectangle(x, y, playerWidth, playerHeight);

        // Check all the objects in the Bounds layer
        MapObjects wallBounds = tiledMap.getLayers().get("Bounds").getObjects();
        for (RectangleMapObject wallObject : wallBounds.getByType(RectangleMapObject.class)) {
            Rectangle wallHitbox = wallObject.getRectangle();

            // Register if the player hit the wall
            if (playerFutureRect.overlaps(wallHitbox)) {
                return true;
            }
        }
        return false;
    }

    /**
     * EXTENDED
     *
     * Calculate the player's base score for this run.
     *
     * <p>Score is derived from time remaining subtract the penalties from deaths/catches.
     * Score is set to have at least a minimum of 0.</p>
     *
     * @return Base score after penalties
     */
    public int calculateFinalScore() {
        //convert the time remaining into seconds to have as the player's score
        int timeRemainingSeconds = (int) gameTimer.getTimeLeft();

        int minutes = timeRemainingSeconds / 60;
        int seconds = timeRemainingSeconds % 60;
        int timeScore = (minutes * 100) + seconds; //this means 3:24 left on the clock gives a score of 324 before penalties are taken into account

        int totalPenalty = calculateTotalPenalty();

        //make sure the score can't go below 0 which could happen if the dean catches you enough times
        return Math.max(0, timeScore - totalPenalty);
    }

    /**
     * NEW
     *
     * Calculate the total score penalty based on negative events.
     *
     * <p>Penalties:</p>
     * <ul>
     * <li>Dean catches: 5 points per caught time</li>
     * <li>Patrol dean catches: 5 points per caught time</li>
     * <li>Drowning: 10 points per time drowned</li>
     * </ul>
     *
     * @return Total penalty score to subtract from the base time score.
     */
    public int calculateTotalPenalty() {
        int deanPenalty = timesCaughtByDean * 5;
        int drownedPenalty = timesDrowned * 10;
        int patrolDeanPenalty = timesCaughtByPatrol * 5;
        return deanPenalty + drownedPenalty + patrolDeanPenalty;
    }


    /**
     * UNCHANGED
     * Resize UI and game map viewports when the window size is changed.
     * @param width Current width of window.
     * @param height Current height of window.
     * @see com.badlogic.gdx.Screen#resize Screen.resize().
     */
    @Override
    public void resize(int width, int height) {
        uiStage.getViewport().update(width, height, true);
        uiStage.getViewport().apply();
        viewport.update(width, height);
        viewport.apply();
    }

    /**
     * EXTENDED JAVADOC
     * Get the number of times the player is caught by the Dean
     *
     * @return Number of times caught by the dean
     */
    public int getTimesCaughtByDean() {
        return timesCaughtByDean;
    }

    /**
     * EXTENDED
     *
     * Dispose of all assets and UI elements when game screen is left.
     * For example. when the player wins the game or quits.
     * @see com.badlogic.gdx.Screen#dispose Screen.dispose().
     */
    @Override
    public void dispose() {
        tiledMap.dispose();
        mapRenderer.dispose();
        batch.dispose();
        player.dispose();
        locker.dispose();
        bush.dispose();
        labEquipment.dispose();
        font.dispose();
        uiStage.dispose();
        dean.dispose();
        patrolDean1.dispose();
        patrolDean2.dispose();
        patrolDean3.dispose();
        dean.dispose();
        catchCounterFont.dispose();
        friend.dispose();
        extraTime.dispose();
        if (busTicket != null) { busTicket.dispose(); }
    }

    /**
     * NEW
     *
     * Freeze all dean enemies by setting their movement speed to zero.
     *
     * <p>Used by the freeze event and potentially other mechanics.</p>
     */
    public void freezeAllDeans() {
        if (dean != null) dean.setSpeed(0f);
        if (patrolDean1 != null) patrolDean1.setSpeed(0f);
        if (patrolDean2 != null) patrolDean2.setSpeed(0f);
        if (patrolDean3 != null) patrolDean3.setSpeed(0f);
        if (extraDean != null) {
            extraDean.setSpeed(0f);
        }
    }

    /**
     * NEW
     *
     * Restore dean movement speeds after a freeze effect ends.
     */
    public void unfreezeDeans() {
        dean.setSpeed(0.7f);
        patrolDean1.setSpeed(0.7f);
        patrolDean2.setSpeed(0.7f);
        patrolDean3.setSpeed(0.7f);
        if (extraDean != null) extraDean.setSpeed(0.7f);
    }

    /**
     * NEW
     *
     * Spawn a second (extra) dean as a difficulty penalty.
     *
     * <p>This is used by the questionnaire fail outcome.</p>
     */
    public void spawnSecondDean() {
        // Extra dean spawns in top-right area
        extraDean = new Patrol_Dean(780, 800, 700, 800, this);
    }

    /** Unimplemented */
    @Override
    public void show() {}

    /** Unimplemented */
    @Override
    public void pause() {}

    /** Unimplemented */
    @Override
    public void resume() {}

    /** Unimplemented */
    @Override
    public void hide() {}
}
