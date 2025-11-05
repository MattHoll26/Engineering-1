package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
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

public class GameScreen implements Screen {
    private final MyGame game;

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
    private Locker locker;
    private BitmapFont font;
    private boolean canPickUpTicket = false;

    private Rectangle busInteractionArea;
    private boolean canEndGame = false;

    private final int MAP_WIDTH = 640;
    private final int MAP_HEIGHT = 640;

    private Dean dean;
    private int timesCaughtByDean = 0;
    private BitmapFont catchCounterFont;

    private NPC friend; //the NPC that talks to the player

    public GameScreen(MyGame game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, MAP_WIDTH, MAP_HEIGHT);
        camera.zoom=0.5f;
        camera.update();

        tiledMap = new TmxMapLoader().load("Tile Maps/Final Game Map - Maze.tmx");
        
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        viewport = new FitViewport(MAP_WIDTH, MAP_HEIGHT, camera);

        batch = new SpriteBatch();
        player = new Player(145, 70); //the player's starting position in their accommodation
        locker = new Locker(495, 575); //the locker's position in the computer science building on the map
        dean = new Dean(390, 400, player, this); //dean positioned somewhere walkable within the map
        friend = new NPC(560, 300); //npc positioned in the classroom

        catchCounterFont = new BitmapFont();
        catchCounterFont.getData().setScale(1.5f); //get's the fon'ts internal data and scales it to 150% of its original size
        font = new BitmapFont();
        
        MapObject ticketObject = tiledMap.getLayers().get("Events").getObjects().get("BusTicket");
        if (ticketObject != null && ticketObject instanceof RectangleMapObject) {
            RectangleMapObject rect = (RectangleMapObject) ticketObject;
            busTicket = new BusTicket(rect.getRectangle().x, rect.getRectangle().y);
        }

         MapObject busObject = tiledMap.getLayers().get("Events").getObjects().get("Bus");
        if (busObject != null && busObject instanceof RectangleMapObject) {
            this.busInteractionArea = ((RectangleMapObject) busObject).getRectangle();
        }
	
	uiSkin = new Skin(Gdx.files.internal("ui/uiskin.json"));
	uiStage = new Stage(new FitViewport(MAP_WIDTH, MAP_HEIGHT));	 
	uiTable = new Table();
	uiTable.setFillParent(true);
	uiStage.addActor(uiTable);
	gameTimer = new GameTimer(uiSkin, uiTable);
	uiTable.top();
	uiTable.right();
	uiTable.pad(10,0,0,10);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1); //clear the screen to black
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        handleInput();

        //update the NPC 'friend'
        friend.update(player);
        
        //update the dean
        dean.update(delta);

        //each time the dean is updated check if there has been a collision with the player
        if (player.getPosition().dst(dean.getPosition()) < 16f) { //this line checks whether the player and dean are close enough to collide (less than 16 units apart)
            player.getPosition().set(145,70); //puts the player back to their acccommodation coords
            timesCaughtByDean ++;
        }

        //update the locker logic with the message and boost timer, update each fram to constantly check for a key press of e
        locker.update(player, delta);

        if (busTicket != null) {
            if (!busTicket.isCollected()) {
                // Check for ticket pickup
                if (player.getPosition().dst(busTicket.getPosition()) < 16) {
                    busTicket.discover();
                    canPickUpTicket = true;
                } else {
                    canPickUpTicket = false;
                }
            } else {
                // If ticket IS collected, check for bus interaction
                Rectangle playerRect = new Rectangle(player.getPosition().x, player.getPosition().y, 16, 16);
                if (busInteractionArea != null && playerRect.overlaps(busInteractionArea)) {
                    canEndGame = true;
                } else {
                    canEndGame = false;
                }
            }
        }

        // Make the camera follow the player
        camera.position.set(player.getPosition().x, player.getPosition().y, 0);
        camera.update();

        mapRenderer.setView(camera);
        mapRenderer.render();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        if (busTicket != null) {
            busTicket.render(batch);
        }

        if (canPickUpTicket) {
            font.draw(batch, "Press E to pick up", player.getPosition().x - 50, player.getPosition().y + 30);
        }

        if (canEndGame) {
            font.draw(batch, "Press E to use ticket", player.getPosition().x - 50, player.getPosition().y + 30);
        }

        locker.render(batch); //this will draw the locker and the message before drawing the player -> the message will appear on top
        dean.render(batch);
        friend.render(batch);
        player.render(batch);

        //display the catch counter
        //not added but potential future feature -> catchCounterFont.draw(batch, "Caught: " + timesCaughtByDean, camera.position.x - 150, camera.position.y + 100);

        if (busTicket != null && busTicket.isCollected()) {
            busTicket.renderAsIcon(batch, camera);
        }

        batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MenuScreen(game));
        }

        gameTimer.decrementTimer(Gdx.graphics.getRawDeltaTime());
        if (gameTimer.getTimeLeft() == 0) { 
            gameTimer.onTimeUp();
                    game.setScreen(new MenuScreen(game));
        }
        uiStage.act(Gdx.graphics.getRawDeltaTime());
        uiStage.draw();
    }

    private void handleInput() {
        float moveSpeed = 1f;
        //speed is different and should be quicker if the positive event has been triggered
        if (locker != null && locker.isBoostActive()){
            moveSpeed = 2f; //this will double the player's speed whilst the boost is active
        }

        float newX = player.getPosition().x;
        float newY = player.getPosition().y;

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

        if (canPickUpTicket && Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            busTicket.collect();
            canPickUpTicket = false; //prevents picking the bus ticket up again
        }

        if (canEndGame && Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            game.setScreen(new WinScreen(game));
        }

        if (!isCellBlocked(newX, newY)) {
            player.getPosition().set(newX, newY);
        }
    }

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

    @Override
    public void resize(int width, int height) {
		uiStage.getViewport().update(width, height, true);
		uiStage.getViewport().apply();
        viewport.update(width, height);
		viewport.apply();
    }

    @Override
    public void dispose() {
        tiledMap.dispose();
        mapRenderer.dispose();
        batch.dispose();
        player.dispose();
        if (busTicket != null) {
            busTicket.dispose();
        }
        locker.dispose();
        font.dispose();
		uiStage.dispose();
        dean.dispose();
        catchCounterFont.dispose();
        friend.dispose();
    }

    @Override
    public void show() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}
}
