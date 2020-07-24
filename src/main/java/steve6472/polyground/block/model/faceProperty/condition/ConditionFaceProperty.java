package steve6472.polyground.block.model.faceProperty.condition;

import org.json.JSONArray;
import org.json.JSONObject;
import steve6472.polyground.block.BlockTextureHolder;
import steve6472.polyground.block.model.CubeFace;
import steve6472.polyground.block.model.faceProperty.*;
import steve6472.polyground.registry.face.FaceEntry;
import steve6472.polyground.registry.face.FaceRegistry;
import steve6472.polyground.world.World;

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
	private static final VisibleFaceProperty INVISIBLE = new VisibleFaceProperty(false);
	private static final VisibleFaceProperty VISIBLE = new VisibleFaceProperty(true);

	private final List<Result> results;

	public ConditionFaceProperty()
	{
		results = new ArrayList<>();
	}

	public void fixBlockId()
	{
		for (Result r : results)
		{
			if (r.andChainCondProperty != null)
				r.andChainCondProperty.fixBlockId();
			if (r.condProperty != null)
				r.condProperty.fixBlockId();
		}
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

	public static boolean editProperties(ConditionFaceProperty conditions, CubeFace cubeFace, int x, int y, int z, World world)
	{
		for (int i = 0; i < conditions.results.size() - 1; i++)
		{
			Result result = conditions.results.get(i);

			if (result.testBlock(x, y, z, world))
			{
				return updateToResult(result, cubeFace);
			}
		}

		Result last = conditions.results.get(conditions.results.size() - 1);

		return updateToResult(last, cubeFace);
	}

	public boolean updateToLastResult(CubeFace cubeFace)
	{
		return updateToResult(results.get(results.size() - 1), cubeFace);
	}

	private static boolean updateToResult(Result result, CubeFace cubeFace)
	{
		for (FaceEntry<? extends FaceProperty> e : FaceRegistry.getEntries())
		{
			if (e.getInstance() instanceof ConditionFaceProperty)
				continue;
			cubeFace.removeProperty(e);
		}

		for (FaceProperty p : result.properties)
		{
			if (p instanceof ConditionFaceProperty)
				continue;
			cubeFace.addProperty(p);
		}

		if (cubeFace.hasProperty(FaceRegistry.texture))
		{
			TextureFaceProperty texture = cubeFace.getProperty(FaceRegistry.texture);
			if (!texture.isReference())
			{
				texture.setTextureId(BlockTextureHolder.getTextureId(texture.getTexture()));
			}
		} else
		{
			cubeFace.addProperty(new TextureFaceProperty(0, ""));
		}

		if (!cubeFace.hasProperty(FaceRegistry.uv))
			cubeFace.addProperty(new UVFaceProperty());

		if (AutoUVFaceProperty.check(cubeFace))
			cubeFace.getProperty(FaceRegistry.uv).autoUV(cubeFace.getParent(), cubeFace.getFace());

		if (cubeFace.hasProperty(FaceRegistry.isVisible))
		{
			return cubeFace.getProperty(FaceRegistry.isVisible).isVisible();
		}

		return true;
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
		private final List<FaceProperty> properties;
		private CondProperty condProperty;
		private AndChainCondProperty andChainCondProperty;

		Result(JSONObject conditionJson)
		{
			properties = new ArrayList<>();
			loadFromJSON(conditionJson);

			if (!hasProperty(FaceRegistry.isVisible))
				properties.add(VISIBLE);
		}

		boolean testBlock(int x, int y, int z, World world)
		{
			if (condProperty != null)
			{
				return condProperty.test(x, y, z, world);
			} else if (andChainCondProperty != null)
			{
				return andChainCondProperty.test(x, y, z, world);
			} else
			{
				System.err.println("No condition found!");
				return false;
			}
		}

		public void loadFromJSON(JSONObject faceJson)
		{
			for (String key : faceJson.keySet())
			{
				if (FaceRegistry.contains(key))
				{
					FaceProperty newProperty = FaceRegistry.createProperty(key);
					newProperty.loadFromJSON(faceJson);
					if (newProperty.getId().equals(FaceRegistry.condition.getInstance().getId()))
					{
						condProperty = (CondProperty) newProperty;
					} else if (newProperty.getId().equals(FaceRegistry.andChain.getInstance().getId()))
					{
						andChainCondProperty = (AndChainCondProperty) newProperty;
					} else if (newProperty.getId().equals(FaceRegistry.conditionedTexture.getInstance().getId()))
					{
						throw new IllegalArgumentException(key + " is not allowed here!");
					}

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

		public boolean hasProperty(FaceEntry<? extends FaceProperty> property)
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
