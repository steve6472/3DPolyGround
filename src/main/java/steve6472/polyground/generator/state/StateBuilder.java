package steve6472.polyground.generator.state;

import org.json.JSONArray;
import org.json.JSONObject;
import steve6472.polyground.generator.DataBuilder;
import steve6472.polyground.generator.DataGenerator;
import steve6472.polyground.generator.models.BlockModelBuilder;
import steve6472.sge.main.util.Pair;

import java.io.File;
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
	private BlockModelBuilder[] singleModel;
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

	public StateBuilder singleModel(BlockModelBuilder... model)
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
			if (singleModel.length == 1)
			{
				main.put("model", "block/" + singleModel[0].getModelPath() + singleModel[0].getModelName());
				// Save block state
				if (this.tags.isEmpty())
					DataBuilder.save(new File(DataGenerator.BLOCK_STATES, blockName + ".json"), main);
				else
					DataBuilder.save(new File(DataGenerator.BLOCK_STATES, blockName + ".json"), main.put("tags", this.tags));

				// Save block model
				createModelFile(singleModel[0].getModelPath());
				DataBuilder.save(new File(DataGenerator.BLOCK_MODELS, singleModel[0].getModelPath() + singleModel[0].getModelName() + ".json"), new JSONObject(singleModel[0].build().build()));
			} else
			{
				JSONArray models = new JSONArray();

				for (BlockModelBuilder blockModelBuilder : singleModel)
				{
					models.put("block/" + blockModelBuilder.getModelPath() + blockModelBuilder.getModelName());

					// Save block model
					createModelFile(blockModelBuilder.getModelPath());
					DataBuilder.save(new File(DataGenerator.BLOCK_MODELS, blockModelBuilder.getModelPath() + blockModelBuilder.getModelName() + ".json"), new JSONObject(blockModelBuilder.build().build()));
				}

				main.put("models", models);
				// Save block state
				if (this.tags.isEmpty())
					DataBuilder.save(new File(DataGenerator.BLOCK_STATES, blockName + ".json"), main);
				else
					DataBuilder.save(new File(DataGenerator.BLOCK_STATES, blockName + ".json"), main.put("tags", this.tags));
			}
		} else
		{
			JSONArray modelArray = new JSONArray();

			// Save models & generate model array
			for (Pair<PropertyBuilder, BlockModelBuilder> s : models)
			{
				JSONObject model = new JSONObject();
				model.put("state", s.getA().build());
				if (s.getA().getRotY() != 0)
					model.put("rot_y", s.getA().getRotY());
				if (s.getA().isUvLock())
					model.put("uvlock", true);
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
					DataBuilder.save(new File(DataGenerator.BLOCK_MODELS, s.getB().getModelPath() + s.getB().getModelName() + ".json"), new JSONObject(s.getB().build().build()));
				}
			}

			// Save block state
			main.put("models", modelArray);
			DataBuilder.save(new File(DataGenerator.BLOCK_STATES, blockName + ".json"), main);
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
}
