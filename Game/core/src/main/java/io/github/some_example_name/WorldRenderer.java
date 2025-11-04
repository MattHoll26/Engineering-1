package io.github.some_example_name;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * <code> WorldRenderer </code> handles drawing and updating of all the 
 * graphics for the game, including the tile map for the actual world, 
 * and a set of given sprites characters, and moving the sprites appropriately. 
 */
public class WorldRenderer extends ApplicationAdapter {
    TiledMap tiledMap;
    OrthogonalTiledMapRenderer mapRenderer;
    OrthographicCamera camera;
    FitViewport viewport;

    private SpriteBatch batch;
    private Player player;

    private int MAP_HEIGHT = 640;
    private int MAP_WIDTH = 640;

    /** 
     * Called immediately after window is created, to instantiate the renderers 
     * used to draw the different assets to the game including: 
     * <ul>
     * <li> The games camera and viewport. </li>
     * <li> The background tilemap. </li>
     * <li> The sprite for the player. </li>
     * </ul>
     * @see {@link com.badlogic.gdx.ApplicationAdapter}
     */
    @Override
    public void create() 
    {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, MAP_WIDTH, MAP_HEIGHT);
        camera.update();

        tiledMap = new TmxMapLoader().load("Tile Maps/Game Map Maze Version.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        viewport = new FitViewport(MAP_WIDTH, MAP_HEIGHT, camera);

        batch = new SpriteBatch();
        player = new Player(145,120); 
    }

    /** 
     * Handle how the player is moved when the input to the window changes, 
     * including checks if the player can move to the tile they are 
     * attempting to move into.  
     * @see {@link com.badlogic.gdx.input.isKeyPressed}
     */
    private void handleInput() 
    {
        float moveSpeed = 150f; 

        float newX = player.getPosition().x;
        float newY = player.getPosition().y;

        if (Gdx.input.isKeyPressed(Input.Keys.W))
        {
            newY += moveSpeed * Gdx.graphics.getDeltaTime();
            player.setDirection(Player.Direction.UP);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)) 
        {
            newX -= moveSpeed * Gdx.graphics.getDeltaTime();
            player.setDirection(Player.Direction.LEFT);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S)) 
        {
            newY -= moveSpeed * Gdx.graphics.getDeltaTime();
            player.setDirection(Player.Direction.DOWN);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D)) 
        {
            newX += moveSpeed * Gdx.graphics.getDeltaTime();
            player.setDirection(Player.Direction.RIGHT);
        }

        if (!isCellBlocked(newX, newY))
        {
            player.getPosition().set(newX, newY);
        }
    }

    /** 
     * Checks if the cell with the given coordinates can be traversed onto by
     * the player.  
     * @param x absolute position of tile horizontally. 
     * @param y absolute position of tile vertically. 
     * @return True/False if the given cell is blocked. 
     */
    private boolean isCellBlocked(float x, float y)
    {
        for (int i = 0; i < tiledMap.getLayers().getCount(); i++) {
            if (tiledMap.getLayers().get(i) instanceof TiledMapTileLayer) {
                TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get(i);
                
                int tileX = (int) ((x + 8) / layer.getTileWidth());
                int tileY = (int) ((y + 8) / layer.getTileHeight());

                TiledMapTileLayer.Cell cell = layer.getCell(tileX, tileY);

                if (cell == null || cell.getTile() == null) {
                    continue;
                }

                if (cell.getTile().getProperties().containsKey("collidable")) {
                    return true; 
                }

                if (layer.getProperties().containsKey("collidable")) {
                    return true; 
                }
            }
        }
        return false; 
    }
	
    /**
     * Called at the end of every frame when the application is in focus,
     * to re-render all assets on display, and refit the game to the new 
     * viewport, if the window size has changed.
     * @see {@link com.badlogic.gdx.ApplicationAdapter}
     * @see {@link com.badlogic.gdx.FitViewport}
     */
    @Override
    public void render() 
    {
        handleInput();

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        mapRenderer.setView(camera);
        mapRenderer.render();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        player.render(batch);
        batch.end();
    } 

    /** 
     * Called after the window is closed, to ensure all rendering assets are 
     * dispoed of.
     */
    @Override
    public void dispose()
    {
        tiledMap.dispose();
        mapRenderer.dispose();
        batch.dispose();
        player.dispose();
    }
}
