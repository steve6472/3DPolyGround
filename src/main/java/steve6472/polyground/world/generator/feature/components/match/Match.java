package steve6472.polyground.world.generator.feature.components.match;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.function.Supplier;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 21.08.2020
 * Project: CaveGame
 *
 ***********************/
public class Match
{
	private static final HashMap<String, Supplier<IBlockMatch>> MATCHES;

	static {
		MATCHES = new HashMap<>();

		MATCHES.put("tag", TagMatch::new);
		MATCHES.put("state", StateMatch::new);
		MATCHES.put("block", BlockMatch::new);
		MATCHES.put("always_true", AlwaysTrueMatch::new);
	}

	public static IBlockMatch match(JSONObject block)
	{
		for (String key : MATCHES.keySet())
		{
			if (block.has(key))
			{
				IBlockMatch match = MATCHES.get(key).get();
				match.load(block.getJSONArray(key));
				return match;
			}
		}

		throw new IllegalArgumentException("Could not match state '" + block.toString(4) + "'");
	}
}
