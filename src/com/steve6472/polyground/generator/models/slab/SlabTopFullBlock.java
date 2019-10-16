package com.steve6472.polyground.generator.models.slab;

import com.steve6472.polyground.generator.models.IModel;

public class SlabTopFullBlock implements IModel
{
	private static String json = "{ \"parent\": \"block/templates/slab_top_full\", \"textures\": {  \"texture\" : \"TEXTURE_HERE\" }}";

	private String texture;

	public SlabTopFullBlock(String texture)
	{
		this.texture = texture;
	}

	@Override
	public String build()
	{
		return json.replace("TEXTURE_HERE", texture);
	}
}