package com.steve6472.polyground;

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
	public boolean enablePostProcessing = true;


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

	public float mouseSensitivity = 0.2f;

	public Options()
	{

	}
}
