package steve6472.polyground;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 09.09.2019
 * Project: SJP
 *
 ***********************/
public class Options
{
	/*
	 * Gameplay
	 */
	public boolean enablePostProcessing = false;
	public boolean renderCrosshair = true;

	/**
	 * -1 to disable limit
	 */
	public int maxChunkRebuild = -1;

	/**
	 * -1 to disable limit
	 * Don't use this, it causes rapid flickering of water!
	 */
	public int maxWaterTick = -1;

	/**
	 * Radius to generate new chunks around player
	 */
	public int generateDistance = 5;

	/**
	 * true - uses other thread to build chunks and main thread to upload the data to graphics card
	 *        Placing blocks can be slow due to long queue for the thread
	 *        Can be toggled only if no chunks are being built!
	 */
	public boolean fastChunkBuild = true;

	public boolean isGamePaused = true;
	public boolean isInMenu = true;
	public boolean isMouseFree = false;

	/*
	 * Debug
	 */
	public boolean chunkModelDebug = false;
	public boolean renderAtlases = false;
	public boolean lightDebug = false;
	public boolean subChunkBuildTime = false;
	public boolean showGCLog = false;
	public boolean renderTeleporters = false;
	public boolean renderRifts = false;
	public boolean renderChunkOutline = false;
	public boolean renderLights = false;
	public boolean renderSkybox = true;

	public float mouseSensitivity = 0.1f;

	public Options()
	{

	}
}
