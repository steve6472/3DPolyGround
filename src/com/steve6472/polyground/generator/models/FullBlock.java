package com.steve6472.polyground.generator.models;

public class FullBlock implements IModel
{
	private static String fullBlockJson = "{\"parent\": \"block/templates/block_full\",\"textures\":{\"texture\" : \"TEXTURE_HERE\"}}";

	private String texture;

	public FullBlock(String texture)
	{
		this.texture = texture;
	}

	@Override
	public String build()
	{
		return fullBlockJson.replace("TEXTURE_HERE", texture);
	}
}