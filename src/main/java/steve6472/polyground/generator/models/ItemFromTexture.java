package steve6472.polyground.generator.models;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 25.09.2019
 * Project: SJP
 *
 ***********************/
public class ItemFromTexture implements IModel
{
	private static String itemJson = "{\"type\": \"texture\",\"texture\": \"TEXTURE_HERE\"}";

	private String texture;

	public ItemFromTexture(String texture)
	{
		this.texture = texture;
	}

	@Override
	public String build()
	{
		return itemJson.replace("TEXTURE_HERE", texture);
	}
}
