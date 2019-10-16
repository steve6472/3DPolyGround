package com.steve6472.polyground.generator.models;

public class GrassBlock implements IModel
{
	private static String json = "{\"parent\": \"block/templates/tintable_grass\",\"textures\": {\"side\": \"dirt_cut\",\"down\": \"dirt\",\"overlay_side\": \"grass_block_side_overlay\",\"overlay_up\": \"grass_block_top\"},\"tints\":{\"red\": \"0.5686275\",\"green\": \"0.7411765\",\"blue\": \"0.34901962\"}}";

	public GrassBlock()
	{
	}

	@Override
	public String build()
	{
		return json;
	}
}