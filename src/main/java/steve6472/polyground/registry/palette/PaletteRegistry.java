package steve6472.polyground.registry.palette;

import steve6472.polyground.gfx.Palette;

import java.util.HashMap;
import java.util.Map;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 06.10.2019
 * Project: SJP
 *
 ***********************/
public class PaletteRegistry
{
	private static final Map<String, Palette> PALETTES = new HashMap<>();

	public static final Palette RAINBOW = register("rainbow");

	public static Palette register(String path, String name)
	{
		final Palette palette = new Palette(path);
		PALETTES.put(name, palette);
		return palette;
	}

	public static Palette register(String name)
	{
		return register(name, name);
	}

	public static Palette getPalette(String name)
	{
		return PALETTES.get(name);
	}

	public static Palette getOrRegister(String name)
	{
		Palette palette = PALETTES.get(name);
		if (palette != null)
			return palette;

		palette = register(name);

		return palette;
	}

	public static void init()
	{

	}

	public static void reload()
	{
		PALETTES.forEach((k, v) -> v.reloadPalette());
	}
}
