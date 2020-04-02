package steve6472.polyground.generator.models;

public class TintedFullBlock implements IModel
{
	private static String json = "{\"parent\": \"block/templates/tintable_stone_ore_full\",\"textures\": {\"overlay\": \"#overlay\",\"texture\": \"#texture\",},\"tints\": {\"red\": \"#red\",\"green\": \"#green\",\"blue\": \"#blue\"}}";

	float red, green, blue;
	private String texture, overlay;

	public TintedFullBlock(String texture, String overlay, float red, float green, float blue)
	{
		this.texture = texture;
		this.overlay = overlay;
		this.red = red;
		this.blue = blue;
		this.green = green;
	}

	@Override
	public String build()
	{
		return json.replace("#texture", texture).replace("#overlay", overlay)

			.replace("#red", "" + red).replace("#green", "" + green).replace("#blue", "" + blue);
	}
}