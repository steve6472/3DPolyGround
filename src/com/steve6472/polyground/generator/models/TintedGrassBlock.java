package com.steve6472.polyground.generator.models;

public class TintedGrassBlock implements IModel
{
	private static String json = "{\"parent\": \"block/templates/tintable_grass\",\"textures\": {\"side\": \"#SIDE\",\"down\": \"#DOWN\",\"overlay_side\": \"#O_SIDE\",\"overlay_up\": \"#O_UP\"},\"tints\":{\"red\": \"#RED\",\"green\": \"#GREEN\",\"blue\": \"#BLUE\"}}";

	private float red, green, blue;
	private String side, down, overlay_side, overlay_up;

	public TintedGrassBlock(float red, float green, float blue, String side, String down, String overlay_side, String overlay_up)
	{
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.side = side;
		this.down = down;
		this.overlay_side = overlay_side;
		this.overlay_up = overlay_up;
	}

	@Override
	public String build()
	{
		return json
			.replace("#RED", "" + red)
			.replace("#GREEN", "" + green)
			.replace("#BLUE", "" + blue)

			.replace("#SIDE", side)
			.replace("#DOWN", down)
			.replace("#O_SIDE", overlay_side)
			.replace("#O_UP", overlay_up)
			;
	}
}