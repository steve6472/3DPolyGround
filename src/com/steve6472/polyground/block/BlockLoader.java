package com.steve6472.polyground.block;

import com.steve6472.sge.gfx.Atlas;
import com.steve6472.sge.main.MainApp;

import java.io.File;
import java.net.URL;
import java.util.HashMap;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 18.09.2019
 * Project: SJP
 *
 ***********************/
public class BlockLoader
{
	private static HashMap<Integer, String> usedTextures = new HashMap<>();
	private static HashMap<String, Integer> usedTexturesReference = new HashMap<>();
	private static Atlas atlas;

	public static void compileTextures()
	{
		atlas = new Atlas(usedTextures.size(), 16);
		for (int i = 0; i < usedTextures.size(); i++)
		{
			URL url = MainApp.class.getResource("/textures/block/" + usedTextures.get(i) + ".png");
			try
			{
				File f = new File(url.getFile());
				atlas.add(f);
			} catch (NullPointerException ex)
			{
				System.err.println(usedTextures.get(i));
				ex.printStackTrace();
			}
		}
		atlas.finish();
	}

	public static void putTexture(String name)
	{
		if (!usedTexturesReference.containsKey(name))
		{
			usedTexturesReference.put(name, usedTexturesReference.size());
			usedTextures.put(usedTexturesReference.get(name), name);
		}
	}

	public static int getTextureId(String name)
	{
		return usedTexturesReference.get(name);
	}

	public static Atlas getAtlas()
	{
		return atlas;
	}
}
