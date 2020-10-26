package steve6472.polyground.registry;

import steve6472.polyground.audio.SoundMaster;

import java.util.HashMap;
import java.util.Map;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 25.10.2020
 * Project: CaveGame
 *
 ***********************/
public class SoundRegistry
{
	private static final Map<String, Integer> sounds = new HashMap<>();

	public static int WOODEN_DOOR_CLOSE = register("wooden_door_close", "wooden_door_close1");
	public static int WOODEN_DOOR_OPEN = register("wooden_door_open", "wooden_door_open1");

	public static int register(String soundId, String path)
	{
		int sound = SoundMaster.loadSound("game/sounds/" + path + ".ogg");
		sounds.put(soundId, sound);
		return sound;
	}

	public static int getSound(String soundId)
	{
		return sounds.get(soundId);
	}

	public static void init()
	{

	}
}
