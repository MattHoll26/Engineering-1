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
 * @see {@link com.badlogic.gdx.Screen} Screen.
 */
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
	private NPC friend; 
	private int timesCaughtByDean = 0;
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

		mapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
		viewport = new FitViewport(MAP_WIDTH, MAP_HEIGHT, camera);

		batch = new SpriteBatch();
		player = new Player(145, 70); 
		locker = new Locker(495, 575); 
		dean = new Dean(390, 400, player, this); 
		friend = new NPC(560, 300); 

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
	 * @see {@link com.badlogic.gdx.Screen#render} Screen.render().
	 */
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1); 
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		handleInput();
		friend.update(player);
		dean.update(delta);

		if (player.getPosition().dst(dean.getPosition()) < 16f) {
		    player.getPosition().set(145,70); 
		    timesCaughtByDean ++;
		}

		locker.update(player, delta);

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

			if (
				busInteractionArea != null && 
				playerRect.overlaps(busInteractionArea)
			) {
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
			
		// Messages will appear on top by rendering player last. 
		locker.render(batch); 		
		dean.render(batch);
		friend.render(batch);
		player.render(batch);

		if (busTicket != null && busTicket.isCollected()) {
			busTicket.renderAsIcon(batch, camera);
		}

		batch.end();

		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			game.setScreen(new MenuScreen(game));
		}

		gameTimer.decrementTimer(delta);
		if (gameTimer.getTimeLeft() == 0) { 
			gameTimer.onTimeUp();
			game.setScreen(new MenuScreen(game));
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
		float moveSpeed = 1f;
		if (locker != null && locker.isBoostActive()){
		    moveSpeed = 2f; 
		}

		float newX = player.getPosition().x;
		float newY = player.getPosition().y;

		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
		    newY += moveSpeed;
		    player.setDirection(Player.Direction.UP);
		} else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
		    newY -= moveSpeed;
		    player.setDirection(Player.Direction.DOWN);
		} else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
		    newX -= moveSpeed;
		    player.setDirection(Player.Direction.LEFT);
		} else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
		    newX += moveSpeed;
		    player.setDirection(Player.Direction.RIGHT);
		} else if (canPickUpTicket && Gdx.input.isKeyJustPressed(Input.Keys.E)) {
		    busTicket.collect();
		    canPickUpTicket = false; 
		} else if (canEndGame && Gdx.input.isKeyJustPressed(Input.Keys.E)) {
		    game.setScreen(new WinScreen(game));
		}

		if (!isCellBlocked(newX, newY)) {
		    player.getPosition().set(newX, newY);
		}
	}


	/**
	 * Returns if the cell at a given coordinate in the world allows an entity
	 * to move onto it.Useful for checking collisions when moving player or another
	 * entity.
	 * @param x Horizontal position of cell in the world.
	 * @param x Vertical position of cell in the world.
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
	 * Resize UI and game map viewports when the window size is changed.
	 * @param width Current width of window. 
	 * @param height Current height of window. 
	 * @see {@link com.badlogic.gdx.Screen#resize} Screen.resize().
	 */
	@Override
	public void resize(int width, int height) {
		uiStage.getViewport().update(width, height, true);
		uiStage.getViewport().apply();
		viewport.update(width, height);
		viewport.apply();
	}

	/**
	 * Dipose of all assets and UI elements when game screen is left i.e 
	 * when the player wins the game or quits.
	 * @see {@link com.badlogic.gdx.Screen#dispose} Screen.dispose().
	 */
	@Override
	public void dispose() {
		tiledMap.dispose();
		mapRenderer.dispose();
		batch.dispose();
		player.dispose();
		locker.dispose();
		font.dispose();
		uiStage.dispose();
		dean.dispose();
		catchCounterFont.dispose();
		friend.dispose();
		if (busTicket != null) { busTicket.dispose(); }
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
