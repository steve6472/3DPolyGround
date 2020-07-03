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

	public static StateBuilder create()
	{
		return new StateBuilder();
	}

	private StateBuilder()
	{
		models = new ArrayList<>();
	}

	public StateBuilder singleModel(BlockModelBuilder model)
	{
		singleModel = model;
		return this;
	}

	public StateBuilder addState(PropertyBuilder state, BlockModelBuilder model)
	{
		singleModel = null;
		models.add(new Pair<>(state, model));
		return this;
	}

	public void generate(String blockName)
	{
		JSONObject main = new JSONObject();
		if (singleModel != null)
		{
			main.put("model", "block/" + singleModel.getModelPath() + singleModel.getModelName());
			// Save block state
			save(new File(DataGenerator.BLOCK_STATES, blockName + ".json"), main.toString(4));

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
				model.put("model", "block/" + s.getB().getModelPath() + s.getB().getModelName());
				modelArray.put(model);

				createModelFile(s.getB().getModelPath());
				save(new File(DataGenerator.BLOCK_MODELS, s.getB().getModelPath() + s.getB().getModelName() + ".json"), new JSONObject(s.getB().build().build()).toString(4));
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
