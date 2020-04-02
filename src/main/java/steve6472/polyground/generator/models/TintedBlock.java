package steve6472.polyground.generator.models;

public class TintedBlock implements IModel
{
	private static String json = "{\"parent\": \"block/templates/tintable_block\",\"textures\": {\"texture\": \"#TEXTURE\"},\"tints\": {\"red\": \"#RED\",\"green\": \"#GREEN\",\"blue\": \"#BLUE\"}}";

	private float red, green, blue;
	private String texture;

	public TintedBlock(float red, float green, float blue, String texture)
	{
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.texture = texture;
	}

	@Override
	public String build()
	{
		return json.replace("#RED", "" + red).replace("#GREEN", "" + green).replace("#BLUE", "" + blue)

			.replace("#TEXTURE", texture);
	}
}