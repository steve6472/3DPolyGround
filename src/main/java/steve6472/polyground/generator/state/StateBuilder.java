package steve6472.polyground.generator.state;

import org.json.JSONArray;
import org.json.JSONObject;
import steve6472.polyground.generator.DataGenerator;
import steve6472.polyground.generator.models.BlockModelBuilder;
import steve6472.sge.main.util.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 03.07.2020
 * Project: CaveGame
 *
 ***********************/
public class StateBuilder
{
	private BlockModelBuilder singleModel;
	List<Pair<PropertyBuilder, BlockModelBuilder>> models;
	private JSONArray tags;

	public static StateBuilder create()
	{
		return new StateBuilder();
	}

	private StateBuilder()
	{
		models = new ArrayList<>();
	}

	public List<Pair<PropertyBuilder, BlockModelBuilder>> getModels()
	{
		return models;
	}

	public StateBuilder singleModel(BlockModelBuilder model)
	{
		singleModel = model;
		tags = new JSONArray();
		return this;
	}

	public StateBuilder addState(PropertyBuilder state, BlockModelBuilder model)
	{
		singleModel = null;
		models.add(new Pair<>(state, model));
		return this;
	}

	public StateBuilder tag(String tag)
	{
		if (this.tags == null)
			throw new NullPointerException("Tags are null. Tags can be added in StateBuilder only if there is only single model! and 'singleModel' was called first");
		this.tags.put(tag);
		return this;
	}

	public StateBuilder tags(String... tags)
	{
		if (this.tags == null)
			throw new NullPointerException("Tags are null. Tags can be added in StateBuilder only if there is only single model! and 'singleModel' was called first");
		for (String t : tags)
			this.tags.put(t);
		return this;
	}

	public void generate(String blockName)
	{
		JSONObject main = new JSONObject();
		if (singleModel != null)
		{
			main.put("model", "block/" + singleModel.getModelPath() + singleModel.getModelName());
			// Save block state
			if (this.tags.isEmpty())
				save(new File(DataGenerator.BLOCK_STATES, blockName + ".json"), main.toString(4));
			else
				save(new File(DataGenerator.BLOCK_STATES, blockName + ".json"), main.put("tags", this.tags).toString(4));

			// Save block model
			createModelFile(singleModel.getModelPath());
			save(new File(DataGenerator.BLOCK_MODELS, singleModel.getModelPath() + singleModel.getModelName() + ".json"), new JSONObject(singleModel.build().build()).toString(4));
		} else
		{
			JSONArray modelArray = new JSONArray();

			// Save models & generate model array
			for (Pair<PropertyBuilder, BlockModelBuilder> s : models)
			{
				JSONObject model = new JSONObject();
				model.put("state", s.getA().build());
				if (s.getA().getRotation() != 0)
					model.put("rotation", s.getA().getRotation());
				if (!s.getA().getTags().isEmpty())
				{
					JSONArray tags = new JSONArray();
					for (String tag : s.getA().getTags())
						tags.put(tag);
					model.put("tags", tags);
				}
				model.put("model", "block/" + s.getB().getModelPath() + s.getB().getModelName());
				modelArray.put(model);

				if (s.getB().generateModel())
				{
					createModelFile(s.getB().getModelPath());
					save(new File(DataGenerator.BLOCK_MODELS, s.getB().getModelPath() + s.getB().getModelName() + ".json"), new JSONObject(s.getB().build().build()).toString(4));
				}
			}

			// Save block state
			main.put("models", modelArray);
			save(new File(DataGenerator.BLOCK_STATES, blockName + ".json"), main.toString(4));
		}
	}

	private void createModelFile(String modelPath)
	{
		if (modelPath != null && !modelPath.isBlank())
		{
			System.out.println("\tWith path " + modelPath.substring(0, modelPath.length() - 1));
			File f = new File(DataGenerator.BLOCK_MODELS, modelPath.substring(0, modelPath.length() - 1));
			if (!f.exists())
			{
				if (f.mkdirs())
				{
					System.out.println("\tCreated new directory");
				}
			}
		}
	}

	private void save(File file, String s)
	{
		try (PrintWriter out = new PrintWriter(file))
		{
			out.println(s);

		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}
}
