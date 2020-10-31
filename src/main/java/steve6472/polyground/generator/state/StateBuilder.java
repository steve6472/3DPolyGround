package steve6472.polyground.generator.state;

import org.json.JSONArray;
import org.json.JSONObject;
import steve6472.polyground.generator.BlockBuilder;
import steve6472.polyground.generator.DataGenerator;
import steve6472.polyground.generator.models.BlockModelBuilder;
import steve6472.sge.main.util.Pair;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
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
	private boolean isEmpty;
	private boolean custom;

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

	public StateBuilder emptyModel()
	{
		tags = new JSONArray();
		isEmpty = true;
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

	public StateBuilder custom(boolean custom)
	{
		if (this.tags == null)
			throw new NullPointerException("Custom can be selected in StateBuilder only if there is only single model! and 'singleModel' was called first");
		this.custom = custom;
		return this;
	}

	public void generate(String blockName)
	{
		JSONObject main = new JSONObject();
		if (isEmpty)
		{
			main.put("empty", true);


			// Save block state
			if (this.tags.isEmpty())
				BlockBuilder.save(new File(DataGenerator.BLOCK_STATES, blockName + ".json"), main);
			else
				BlockBuilder.save(new File(DataGenerator.BLOCK_STATES, blockName + ".json"), main.put("tags", this.tags));
		}
		else if (singleModel != null)
		{
			if (singleModel.length == 1)
			{
				main.put("model", "block/" + singleModel[0].getModelPath() + singleModel[0].getModelName());
				if (custom) main.put("custom", true);
				// Save block state
				if (this.tags.isEmpty())
					BlockBuilder.save(new File(DataGenerator.BLOCK_STATES, blockName + ".json"), main);
				else
					BlockBuilder.save(new File(DataGenerator.BLOCK_STATES, blockName + ".json"), main.put("tags", this.tags));

				// Save block model
				save(singleModel[0]);
			} else
			{
				JSONArray models = new JSONArray();

				for (BlockModelBuilder blockModelBuilder : singleModel)
				{
					models.put("block/" + blockModelBuilder.getModelPath() + blockModelBuilder.getModelName());

					// Save block model
					save(blockModelBuilder);
				}

				main.put("models", models);
				// Save block state
				if (this.tags.isEmpty())
					BlockBuilder.save(new File(DataGenerator.BLOCK_STATES, blockName + ".json"), main);
				else
					BlockBuilder.save(new File(DataGenerator.BLOCK_STATES, blockName + ".json"), main.put("tags", this.tags));
			}
		} else
		{
			JSONArray modelArray = new JSONArray();

			// Save models & generate model array
			for (Pair<PropertyBuilder, BlockModelBuilder> s : models)
			{
				JSONObject model = new JSONObject();
				model.put("state", s.getA().build());
				if (s.getA().hasRotation())
					model.put("rotation", new JSONArray().put(s.getA().getRotX()).put(s.getA().getRotY()).put(s.getA().getRotZ()));
				if (s.getA().isUvLock())
					model.put("uvlock", true);
				if (s.getA().isCustom())
					model.put("custom", true);
				if (!s.getA().getTags().isEmpty())
				{
					JSONArray tags = new JSONArray();
					for (String tag : s.getA().getTags())
						tags.put(tag);
					model.put("tags", tags);
				}
				model.put("model", "block/" + s.getB().getModelPath() + s.getB().getModelName());
				modelArray.put(model);

				save(s.getB());
			}

			// Save block state
			main.put("models", modelArray);
			BlockBuilder.save(new File(DataGenerator.BLOCK_STATES, blockName + ".json"), main);
		}
	}

	private void save(BlockModelBuilder builder)
	{
		if (builder.getExternalPath() != null)
		{
			try
			{
				System.out.println("Copying model from " + builder.getExternalPath());
				File f = new File(DataGenerator.BLOCK_MODELS, builder.getModelPath());
				if (f.mkdirs())
					System.out.println("Created file " + f.getPath());
				Files.copy(new File(builder.getExternalPath()).toPath(), new File(DataGenerator.BLOCK_MODELS, builder.getModelPath() + builder.getModelName() + ".json").toPath(), StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		} else if (builder.generateModel())
		{
			createModelFile(builder.getModelPath());
			BlockBuilder.save(new File(DataGenerator.BLOCK_MODELS, builder.getModelPath() + builder.getModelName() + ".json"), new JSONObject(builder.build().build()));
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
