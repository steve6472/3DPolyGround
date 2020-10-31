package steve6472.polyground.generator.biome.feature;

import org.json.JSONArray;
import org.json.JSONObject;
import steve6472.polyground.generator.BlockBuilder;
import steve6472.polyground.generator.DataGenerator;
import steve6472.sge.main.util.Pair;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 17.08.2020
 * Project: CaveGame
 *
 ***********************/
public class FeatureBuilder
{
	private final String featureName;
	private final List<Pair<String, BlockStateBuilder>> states;
	private final List<Pair<String, BlockStateBuilder[]>> stateMatches;
	private final List<Pair<String, String[]>> tagMatches;
	private final List<Pair<String, String[]>> blockMatches;
	private final List<String> alwaysTrueMatch;
	private final List<Pair<String, Object>> other;

	private final List<Pair<String, BlockStateBuilder[]>> simpleStateProviders;
	private String name, path;

	private FeatureBuilder(String featureName)
	{
		this.featureName = featureName;
		this.states = new ArrayList<>();
		this.other = new ArrayList<>();
		this.tagMatches = new ArrayList<>();
		this.blockMatches = new ArrayList<>();
		this.stateMatches = new ArrayList<>();
		this.alwaysTrueMatch = new ArrayList<>();
		this.simpleStateProviders = new ArrayList<>();
	}

	public static FeatureBuilder create(String featureName)
	{
		return new FeatureBuilder(featureName);
	}

	public FeatureBuilder name(String name)
	{
		this.name = name;
		return this;
	}

	public FeatureBuilder path(String path)
	{
		this.path = path;
		return this;
	}

	public FeatureBuilder integer(String propertyName, int value)
	{
		other.add(new Pair<>(propertyName, value));
		return this;
	}

	public FeatureBuilder doubleArg(String propertyName, double value)
	{
		other.add(new Pair<>(propertyName, value));
		return this;
	}

	public FeatureBuilder floatArg(String propertyName, float value)
	{
		other.add(new Pair<>(propertyName, value));
		return this;
	}

	public FeatureBuilder bool(String propertyName, boolean value)
	{
		other.add(new Pair<>(propertyName, value));
		return this;
	}

	public FeatureBuilder blockState(String propertyName, BlockStateBuilder state)
	{
		this.states.add(new Pair<>(propertyName, state));
		return this;
	}

	public FeatureBuilder matchTags(String propertyName, String... tags)
	{
		tagMatches.add(new Pair<>(propertyName, tags));
		return this;
	}

	public FeatureBuilder matchBlocks(String propertyName, String... blocks)
	{
		blockMatches.add(new Pair<>(propertyName, blocks));
		return this;
	}

	public FeatureBuilder matchStates(String propertyName, BlockStateBuilder... states)
	{
		this.stateMatches.add(new Pair<>(propertyName, states));
		return this;
	}

	public FeatureBuilder matchAlwaysTrue(String propertyName)
	{
		this.alwaysTrueMatch.add(propertyName);
		return this;
	}

	public FeatureBuilder provideStates(String propertyName, BlockStateBuilder... states)
	{
		this.simpleStateProviders.add(new Pair<>(propertyName, states));
		return this;
	}

	public String getName()
	{
		return name;
	}

	private void strings(JSONObject main, String name, List<Pair<String, String[]>> match)
	{
		for (Pair<String, String[]> pair : match)
		{
			JSONObject object = new JSONObject();
			JSONArray array = new JSONArray();
			for (String string : pair.getB())
			{
				array.put(string);
			}
			object.put(name, array);
			main.put(pair.getA(), object);
		}
	}

	private void states(JSONObject main, String name, List<Pair<String, BlockStateBuilder[]>> providers)
	{
		for (Pair<String, BlockStateBuilder[]> states : providers)
		{
			JSONObject object = new JSONObject();
			JSONArray array = new JSONArray();

			for (BlockStateBuilder state : states.getB())
			{
				JSONObject obj = new JSONObject();
				obj.put("block", state.block);

				if (!state.states.isEmpty())
				{
					JSONObject properties = new JSONObject();

					state.states.forEach((k, v) -> properties.put(k.getName(), v.toString()));
					obj.put("properties", properties);
				}
				array.put(obj);
			}

			object.put(name, array);
			main.put(states.getA(), object);
		}
	}

	public JSONObject build()
	{
		JSONObject main = new JSONObject();
		main.put("feature", featureName);

		strings(main, "tag", tagMatches);
		strings(main, "block", blockMatches);
		states(main, "state", stateMatches);

		states(main, "simple", simpleStateProviders);

		for (String s : alwaysTrueMatch)
		{
			main.put(s, new JSONObject().put("always_true", new JSONArray()));
		}

		for (Pair<String, BlockStateBuilder> state : states)
		{
			JSONObject s = new JSONObject();
			s.put("block", state.getB().block);

			if (!state.getB().states.isEmpty())
			{
				JSONObject properties = new JSONObject();

				state.getB().states.forEach((k, v) -> properties.put(k.getName(), v.toString()));
				s.put("properties", properties);
			}
			main.put(state.getA(), s);
		}

		for (Pair<String, Object> pair : other)
		{
			if (pair.getB() instanceof FeatureBuilder fb)
				main.put(pair.getA(), fb.build());
			else
				main.put(pair.getA(), pair.getB());
		}

		return main;
	}

	public void generate()
	{
		System.out.println("Generating feature " + name);
		File feature;
		if (path != null)
		{
			feature = new File(DataGenerator.FEATURES, path);
			if (feature.mkdirs())
				System.out.println("Created path '" + path + "'");
			feature = new File(feature,  name + ".json");
		} else
		{
			feature = new File(DataGenerator.FEATURES,  name + ".json");
		}
		try
		{
			if (feature.createNewFile())
			{
				System.out.println("Created feature " + feature.getPath());
			}
		} catch (IOException e)
		{
			System.err.println("Error while creating feature " + name);
			e.printStackTrace();
		}

		BlockBuilder.save(feature, build());
	}
}
