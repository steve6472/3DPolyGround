package com.steve6472.polyground.block.model.faceProperty.condition;

import com.steve6472.polyground.EnumFace;
import com.steve6472.polyground.block.BlockLoader;
import com.steve6472.polyground.block.model.CubeFace;
import com.steve6472.polyground.block.model.faceProperty.FaceProperty;
import com.steve6472.polyground.block.model.faceProperty.TextureFaceProperty;
import com.steve6472.polyground.block.model.registry.face.FaceEntry;
import com.steve6472.polyground.block.model.registry.face.FaceRegistry;
import com.steve6472.polyground.world.SubChunk;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 15.10.2019
 * Project: SJP
 *
 ***********************/
public class ConditionFaceProperty extends FaceProperty
{
	private List<Result> results;

	public ConditionFaceProperty()
	{
		results = new ArrayList<>();
	}

	@Override
	public void loadFromJSON(JSONObject json)
	{
		JSONArray array = json.getJSONArray(getId());
		for (int i = 0; i < array.length(); i++)
		{
			JSONObject result = array.getJSONObject(i);
			results.add(new Result(result));
		}
	}

	@Override
	public void saveToJSON(JSONObject faceJson)
	{
		System.err.println("Can not save ConditionFaceProperty yet!");
		System.exit(0);
	}

	public void loadTextures()
	{
		for (int i = 0; i < results.size(); i++)
		{
			Result c = results.get(i);
			if (c.hasProperty(FaceRegistry.texture))
			{
				TextureFaceProperty texture = c.getProperty(FaceRegistry.texture);

				BlockLoader.putTexture(texture.getTexture());
				texture.setTextureId(BlockLoader.getTextureId(texture.getTexture()));
			}
		}
	}

	public static boolean editProperties(ConditionFaceProperty conditions, CubeFace cubeFace, EnumFace face, int x, int y, int z, SubChunk sc)
	{
		for (int i = 0; i < conditions.results.size() - 1; i++)
		{
			Result result = conditions.results.get(i);

			boolean flag = result.testBlock(x + sc.getX() * 16, y, z + sc.getZ() * 16, sc);

			if (flag)
			{
//				System.out.println("Condition # " + i + " for " + face);
				for (FaceProperty p : result.properties)
				{
					if (p instanceof ConditionFaceProperty || p instanceof CondProperty)
						continue;
					cubeFace.removeProperty(FaceRegistry.getEntry(p.getId()));
					cubeFace.addProperty(p.createCopy());
				}

				if (result.hasProperty(FaceRegistry.isVisible))
				{
//					System.out.println("--------------------");
					return result.getProperty(FaceRegistry.isVisible).isVisible();
				}
				return true;
			}
		}

//		System.out.println("--------------------");

		if (cubeFace.hasProperty(FaceRegistry.texture))
			cubeFace.getProperty(FaceRegistry.texture).setTextureId(conditions.results.get(conditions.results.size() - 1).getLastTexture());

		return conditions.results.get(conditions.results.size() - 1).getLast();
	}

//	public static int getTexture(ConditionFaceProperty conditions, int x, int y, int z, SubChunk sc)
//	{
//		for (int i = 0; i < conditions.results.size() - 1; i++)
//		{
//			Result result = conditions.results.get(i);
//
//			boolean flag = result.testBlock(x + sc.getX() * 16, y, z + sc.getZ() * 16, sc);
//
//			if (flag)
//			{
//				if (result.hasProperty(FaceRegistry.texture))
//				{
//					return result.getProperty(FaceRegistry.texture).getTextureId();
//				}
//				return -1;
//			}
//		}
//
//		return conditions.results.get(conditions.results.size() - 1).getLastTexture();
//	}

	@Override
	public String getId()
	{
		return "conditionedTexture";
	}

	@Override
	public ConditionFaceProperty createCopy()
	{
		return null;
	}

	private class Result
	{
		private List<FaceProperty> properties;

		public Result(JSONObject conditionJson)
		{
			properties = new ArrayList<>();
			loadFromJSON(conditionJson);
		}

		public boolean testBlock(int x, int y, int z, SubChunk subChunk)
		{
			if (hasProperty(FaceRegistry.condition))
			{
				CondProperty c = getProperty(FaceRegistry.condition);
				return c.test(x, y, z, subChunk);
			} else
			{
				System.err.println("No condition found!");
				return false;
			}
		}

		public boolean getLast()
		{
			if (hasProperty(FaceRegistry.isVisible))
			{
				return getProperty(FaceRegistry.isVisible).isVisible();
			}

			return false;
		}

		public int getLastTexture()
		{
			if (hasProperty(FaceRegistry.texture))
			{
				return getProperty(FaceRegistry.texture).getTextureId();
			}

			return -1;
		}

		public void loadFromJSON(JSONObject faceJson)
		{
			for (String key : faceJson.keySet())
			{
				if (FaceRegistry.contains(key))
				{
					FaceProperty newProperty = FaceRegistry.createProperty(key);
					newProperty.loadFromJSON(faceJson);
					properties.add(newProperty);
				} else
				{
					throw new IllegalArgumentException(key + " is not valid key!");
				}
			}
		}

		public boolean hasProperty(String id)
		{
			for (FaceProperty fp : properties)
			{
				if (fp.getId().equals(id))
					return true;
			}

			return false;
		}

		public boolean hasProperty(FaceEntry property)
		{
			return hasProperty(property.getInstance().getId());
		}

		public <T extends FaceProperty> T getProperty(FaceEntry<T> a)
		{
			for (FaceProperty fp : properties)
			{
				if (fp.getId().equals(a.getInstance().getId()))
				{
					//noinspection unchecked
					return (T) fp;
				}
			}

			return null;
		}
	}
}
