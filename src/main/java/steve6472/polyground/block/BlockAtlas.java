package steve6472.polyground.block;

import steve6472.polyground.gfx.atlas.Atlas;

import java.awt.*;
import java.io.File;
import java.util.HashMap;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 18.09.2019
 * Project: SJP
 *
 ***********************/
public class BlockAtlas
{
	private static final HashMap<Integer, String> usedTextures = new HashMap<>();
	private static final HashMap<String, Integer> usedTexturesReference = new HashMap<>();
	private static Rectangle[] textures;
	private static Atlas atlas;

	public static void compileTextures(int overrideSize)
	{
		// Correctly works only if all textures are 16x16 or less
		int size;
		if (overrideSize == 0)
			size = getNextPowerOfTwo((int) Math.ceil(Math.sqrt(usedTextures.size()))) * 16;
		else
			size = overrideSize;

		if (atlas != null)
			atlas.clean();

		atlas = new Atlas(size);
		for (int i = 0; i < usedTextures.size(); i++)
		{
			try
			{
				atlas.add(usedTextures.get(i), new File("game/textures/" + usedTextures.get(i) + ".png"));
			} catch (NullPointerException ex)
			{
				System.err.println(usedTextures.get(i));
				ex.printStackTrace();
			} catch (RuntimeException ex)
			{
				int newSize = size << 1;
				System.out.println("Image did not fit, incresing size to " + newSize);
				compileTextures(newSize);
				return;
			}
		}
		atlas.finish();

		textures = new Rectangle[atlas.getRects().size()];

		atlas.getRects().forEach((key, rectangle) -> textures[usedTexturesReference.get(key)] = rectangle);
	}

	public static int getNextPowerOfTwo(int value)
	{
		--value;
		value |= value >> 16;
		value |= value >> 8;
		value |= value >> 4;
		value |= value >> 2;
		value |= value >> 1;
		return value + 1;
	}

	public static void putTexture(String name)
	{
		try
		{
			if (!usedTexturesReference.containsKey(name))
			{
				if (textures != null)
				{
					throw new IllegalStateException("Textures already compiled");
				}
//				System.out.println("New texture: '" + name + "' id: " + usedTexturesReference.size());
				usedTexturesReference.put(name, usedTexturesReference.size());
				usedTextures.put(usedTexturesReference.get(name), name);
			}
		} catch (Exception ex)
		{
			ex.printStackTrace();
			System.exit(64);
		}
	}

	public static int getTextureId(String name)
	{
		return usedTexturesReference.get(name);
	}

	public static Rectangle getTexture(int texture)
	{
		return textures[texture];
	}

	public static Atlas getAtlas()
	{
		return atlas;
	}
}
