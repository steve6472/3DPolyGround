package com.steve6472.polyground.block.model.faceProperty.condition;

import com.steve6472.polyground.block.BlockTextureHolder;
import com.steve6472.polyground.block.model.CubeFace;
import com.steve6472.polyground.block.model.faceProperty.AutoUVFaceProperty;
import com.steve6472.polyground.block.model.faceProperty.FaceProperty;
import com.steve6472.polyground.block.model.faceProperty.TextureFaceProperty;
import com.steve6472.polyground.block.model.faceProperty.UVFaceProperty;
import com.steve6472.polyground.block.model.registry.face.FaceEntry;
import com.steve6472.polyground.block.model.registry.face.FaceRegistry;
import com.steve6472.polyground.world.chunk.SubChunk;
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
		for (Result c : results)
		{
			if (c.hasProperty(FaceRegistry.texture))
			{
				TextureFaceProperty texture = c.getProperty(FaceRegistry.texture);

				BlockTextureHolder.putTexture(texture.getTexture());
				texture.setTextureId(BlockTextureHolder.getTextureId(texture.getTexture()));
			}
		}
	}

	public static boolean editProperties(ConditionFaceProperty conditions, CubeFace cubeFace, int x, int y, int z, SubChunk sc)
	{
		for (int i = 0; i < conditions.results.size() - 1; i++)
		{
			Result result = conditions.results.get(i);

			boolean flag = result.testBlock(x + sc.getX() * 16, y, z + sc.getZ() * 16, sc);

			if (flag)
			{
				for (FaceEntry<? extends FaceProperty> e : FaceRegistry.getEntries())
				{
					if (e.getInstance() instanceof ConditionFaceProperty || e.getInstance() instanceof CondProperty)
						continue;
					cubeFace.removeProperty(e);
				}

				for (FaceProperty p : result.properties)
				{
					if (p instanceof ConditionFaceProperty || p instanceof CondProperty)
						continue;
					cubeFace.addProperty(p.createCopy());
				}

				if (cubeFace.hasProperty(FaceRegistry.texture))
				{
					TextureFaceProperty texture = cubeFace.getProperty(FaceRegistry.texture);
					if (!texture.isReference())
					{
						texture.setTextureId(BlockTextureHolder.getTextureId(texture.getTexture()));
					}
				}

				if (!cubeFace.hasProperty(FaceRegistry.uv))
					cubeFace.addProperty(new UVFaceProperty());

				if (AutoUVFaceProperty.check(cubeFace))
				{
					cubeFace.getProperty(FaceRegistry.uv).autoUV(cubeFace.getParent(), cubeFace.getFace());
				}
				/*
				for (FaceProperty p : result.properties)
				{
					if (p instanceof ConditionFaceProperty || p instanceof CondProperty)
						continue;
					cubeFace.removeProperty(FaceRegistry.getEntry(p.getId()));
					cubeFace.addProperty(p.createCopy());
				}*/

				if (result.hasProperty(FaceRegistry.isVisible))
				{
					return result.getProperty(FaceRegistry.isVisible).isVisible();
				}
				return true;
			}
		}

		if (cubeFace.hasProperty(FaceRegistry.texture))
			cubeFace.getProperty(FaceRegistry.texture).setTextureId(conditions.results.get(conditions.results.size() - 1).getLastTexture());

		return conditions.results.get(conditions.results.size() - 1).getLast();
	}

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

	private static class Result
	{
		private List<FaceProperty> properties;

		Result(JSONObject conditionJson)
		{
			properties = new ArrayList<>();
			loadFromJSON(conditionJson);
		}

		boolean testBlock(int x, int y, int z, SubChunk subChunk)
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

		boolean getLast()
		{
			if (hasProperty(FaceRegistry.isVisible))
			{
				return getProperty(FaceRegistry.isVisible).isVisible();
			}

			return false;
		}

		int getLastTexture()
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
