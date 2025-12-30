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

/** <code> GameScreen </code> implements the main gameplay logic and rendering as one class,
 * to process user input, and redraw the frames and update the game asset states as
 * the game progresses.
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

    private NPC friend;
    private int timesCaughtByDean = 0;
    private int timesCaughtByPatrol = 0;
    private BitmapFont catchCounterFont;

    /**
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


        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        viewport = new FitViewport(MAP_WIDTH, MAP_HEIGHT, camera);

        batch = new SpriteBatch();
        player = new Player(560, 180);
        locker = new Locker(495, 895);
        bush = new Slow_Down(560, 270);
        tree = new Decrease_Time(270, 9);
        labEquipment = new Teleport(800,620, this);
        extraTime = new Extra_Time(300, 120);
        dean = new Dean(325, 335,     player, this);
        patrolDean1 = new Patrol_Dean(140, 190, 100, 260, this);
        patrolDean2 = new Patrol_Dean(170, 130, 100, 260, this);
        patrolDean3 = new Patrol_Dean(200, 100, 100, 260, this);
        dean = new Dean(325, 335,     player, this);
        friend = new NPC(560, 600);

        catchCounterFont = new BitmapFont();
        catchCounterFont.getData().setScale(1.5f);
        font = new BitmapFont();

        MapObjects eventObjects = tiledMap.getLayers().get("Events").getObjects();

        MapObject ticketObject = eventObjects.get("BusTicket");
        if (ticketObject != null && ticketObject instanceof RectangleMapObject) {
            RectangleMapObject rect = (RectangleMapObject) ticketObject;
            busTicket = new BusTicket(rect.getRectangle().x, rect.getRectangle().y);
        }

        MapObject busObject = eventObjects.get("Bus");
        if (busObject != null && busObject instanceof RectangleMapObject) {
            this.busInteractionArea = ((RectangleMapObject) busObject).getRectangle();
        }

        uiSkin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        uiStage = new Stage(new FitViewport(MAP_WIDTH, MAP_HEIGHT));
        uiTable = new Table();
        uiTable.setFillParent(true);
        uiStage.addActor(uiTable);
        gameTimer = new GameTimer(uiSkin, uiTable);
        uiTable.top().right().pad(10,0,0,10);
    }

    /**
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

        friend.update(player);
        dean.update(delta);
        patrolDean1.update(delta);
        patrolDean2.update(delta);
        patrolDean3.update(delta);
        dean.update(delta);
        if (extraDean != null) {
            extraDean.update(delta);
            if (player.getPosition().dst(extraDean.getPosition()) < 16f) {
                player.getPosition().set(560, 180);
                timesCaughtByPatrol++;
            }
        }

        if (player.getPosition().dst(dean.getPosition()) < 16f) {
            player.getPosition().set(560, 180);
            timesCaughtByDean++;
            dean.resetToStart(timesCaughtByDean); //send the dean back to his starting position or other side of the map to ensure he can't spawn camp the player
        }

        if (player.getPosition().dst(patrolDean1.getPosition()) < 16f ||
            player.getPosition().dst(patrolDean2.getPosition()) < 16f ||
            player.getPosition().dst(patrolDean3.getPosition()) < 16f) {

            player.getPosition().set(560, 180);
            timesCaughtByPatrol++;
        }


        if (drown.update(player)) {
            hasDrowned = true;
            timesDrowned++;
        }

        locker.update(player, delta);
        bush.update(player, delta);
        tree.update(player, gameTimer, delta);
        extraTime.update(player, gameTimer, delta);
        labEquipment.update(player, delta);

        if (questionnaire != null) {
            questionnaire.update(player, this);
        }


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

        camera.position.set(player.getPosition().x, player.getPosition().y, 0);
        camera.update();

        mapRenderer.setView(camera);
        mapRenderer.render();


        batch.setProjectionMatrix(camera.combined);
        batch.begin();

//switch to screen coordinates for the UI elements
        batch.setProjectionMatrix(uiStage.getCamera().combined);

//draw the three events encountered checklists in the top left hand corner of the screen
//events get updates using a ternary operator which is like a condensed if/else statement -> it is set out like: (condition ? vali_if_true : value_if_false)

        int positiveEvents = 0;
        if (locker.lockerSearched()) positiveEvents++;
        if (extraTime.gainedTime()) positiveEvents++;

        font.draw(batch, "Positive Events Encountered = " + positiveEvents + "/2", 35, 630);//this means if the locker boost is active (the bus ticket has been picked up) display that the event 1/1 has been enocuntered otherwide 0/1

        int negativeEvents = 0;
        if (timesCaughtByDean > 0) negativeEvents++;
        if (timesCaughtByPatrol > 0) negativeEvents ++;
        if (hasDrowned) negativeEvents++;
        if (bush.bushFall()) negativeEvents++;
        if (tree.hitTree()) negativeEvents++;

        font.draw(batch, "Negative Events Encountered = " + negativeEvents + "/4" , 35, 610);
        font.draw(batch, "Negative Events Encountered = " + negativeEvents + "/5" , 35, 610);

        int hiddenEvents = 0;
        if (busTicket.isCollected()) hiddenEvents++;
        if (labEquipment.teleportHappened()) hiddenEvents++;

        font.draw(batch, "Hidden Events Encountered = " + hiddenEvents + "/2", 35, 590);



//switch back to the game coordinates for game objects
        batch.setProjectionMatrix(camera.combined);

        if (busTicket != null) {
            busTicket.render(batch);
        }

        if (canPickUpTicket) {
            font.draw(
                batch,
                "Press E to pick up",
                player.getPosition().x - 50,
                player.getPosition().y + 30
            );
        }

        if (canEndGame) {
            font.draw(
                batch,
                "Press E to use ticket",
                player.getPosition().x - 50,
                player.getPosition().y + 30
            );
        }

//Messages will appear on top by rendering player last.
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


        player.render(batch);

        if (busTicket != null && busTicket.isCollected()) {
            busTicket.renderAsIcon(batch, camera);
        }

        batch.end();

// Decrement the timer only if the game is not paused
        if (!isPaused) {
            gameTimer.decrementTimer(delta);
        }

        if (gameTimer.getTimeLeft() == 0) {
            gameTimer.onTimeUp();
            game.setScreen(new MenuScreen(game));
        }


        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MenuScreen(game));
        }

        if (gameTimer.getTimeLeft() == 0) {
            gameTimer.onTimeUp();
            game.setScreen(new GameOverScreen(game));
        }
        uiStage.act(delta);
        uiStage.draw();
    }

    /**
     * Move the player and interacting with the world and menus every frame when
     * the corrosponding keys are pressed:
     * <ul>
     * <li> WASD - Move Character Up/Left/Down/Right.</li>
     * <li> E - Interact with items.</li>
     * <li> Esc - Pause Game.</li>
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

        else if (canEndGame && Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            int finalScore = calculateFinalScore();
            int totalPenalty = calculateTotalPenalty();
            int timeRemaining = (int) gameTimer.getTimeLeft();
            int timesCaught = getTimesCaughtByDean();
            String playerName = game.getPlayerFullName(); // get the full player name for the leaderboard

            Save_Leaderboard leaderboard = new Save_Leaderboard();
            leaderboard.addScore(playerName, finalScore); // add score to the leaderboard if its in the top scores
            game.setScreen(new WinScreen(game, finalScore, timeRemaining, totalPenalty));
        }

        float oldX = player.getPosition().x;
        float oldY = player.getPosition().y;

        if (!isCellBlocked(newX, newY)) {
            player.getPosition().set(newX, newY);
        }

        else if (!isCellBlocked(newX, oldY)) {
            player.getPosition().set(newX, oldY);
        }

        else if (!isCellBlocked(oldX, newY)) {
            player.getPosition().set(oldX, newY);
        }

    }

    /**
     * Returns if the cell at a given coordinate in the world allows an entity
     * to move onto it.Useful for checking collisions when moving player or another
     * entity.
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

    // Only Dean treats doors as blocked
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
     * Calculate the player's final score
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


    public int calculateTotalPenalty() {
        int deanPenalty = timesCaughtByDean * 5;
        int drownedPenalty = timesDrowned * 10;
        return deanPenalty + drownedPenalty;
    }


    /**
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
     * Get the number of times the player is caught by the Dean
     */
    public int getTimesCaughtByDean() {
        return timesCaughtByDean;
    }

    /**
     * Dipose of all assets and UI elements when game screen is left i.e
     * when the player wins the game or quits.
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

    public void freezeAllDeans() {
        if (dean != null) dean.setSpeed(0f);
        if (patrolDean1 != null) patrolDean1.setSpeed(0f);
        if (patrolDean2 != null) patrolDean2.setSpeed(0f);
        if (patrolDean3 != null) patrolDean3.setSpeed(0f);
        if (extraDean != null) {
            extraDean.setSpeed(0f);
        }
    }

    public void spawnSecondDean() {
        // Extra dean spawns in top-right area
        extraDean = new Patrol_Dean(550, 450, 800, 600, this);
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
