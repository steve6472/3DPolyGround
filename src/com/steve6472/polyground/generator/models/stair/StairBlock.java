package com.steve6472.polyground.generator.models.stair;

import com.steve6472.polyground.generator.models.IModel;

public class StairBlock implements IModel
{
	private static String json = "{\"parent\": \"block/templates/stair_full\",\"textures\":{\"texture\":\"#TEXTURE\"}}";

	private String texture;

	public StairBlock(String texture)
	{
		this.texture = texture;
	}

	@Override
	public String build()
	{
		return json.replace("#TEXTURE", texture);
	}
}