package steve6472.polyground.world.biomes;

import org.json.JSONObject;
import steve6472.polyground.block.model.ModelLoader;
import steve6472.polyground.generator.DataGenerator;
import steve6472.polyground.registry.feature.FeatureRegistry;
import steve6472.polyground.world.generator.Feature;

import java.io.File;
import java.util.HashMap;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 19.08.2020
 * Project: CaveGame
 *
 ***********************/
public class Features
{
	private final HashMap<String, Feature> features;

	public Features()
	{
		features = new HashMap<>();
	}

	public void load()
	{
		loadFromFile(DataGenerator.FEATURES);
	}

	private void loadFromFile(File file)
	{
		File[] featureFiles = file.listFiles();

		if (featureFiles == null)
			return;

		for (File featureFile : featureFiles)
		{
			if (featureFile.isDirectory())
			{
				loadFromFile(featureFile);
				continue;
			}

			JSONObject json = new JSONObject(ModelLoader.read(featureFile));
			features.put(featureFile.getName().substring(0, featureFile.getName().length() - 5), FeatureRegistry.createFeature(json.getString("feature"), json));
		}
	}

	public Feature getFeature(String name)
	{
		Feature r = features.get(name);
		if (r == null)
			System.err.println("Feature with name '" + name + "' not found!");
		return r;
	}
}
