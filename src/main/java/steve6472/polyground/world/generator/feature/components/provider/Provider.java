package steve6472.polyground.world.generator.feature.components.provider;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.function.Supplier;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 21.08.2020
 * Project: CaveGame
 *
 ***********************/
public class Provider
{
	private static final HashMap<String, Supplier<IBlockProvider>> MATCHES;

	static {
		MATCHES = new HashMap<>();

		MATCHES.put("simple", SimpleStateProvider::new);
	}

	public static IBlockProvider provide(JSONObject block)
	{
		for (String key : MATCHES.keySet())
		{
			if (block.has(key))
			{
				IBlockProvider match = MATCHES.get(key).get();
				match.load(block.getJSONArray(key));
				return match;
			}
		}

		throw new IllegalArgumentException("Could not match provider '" + block.toString(4) + "'");
	}
}
