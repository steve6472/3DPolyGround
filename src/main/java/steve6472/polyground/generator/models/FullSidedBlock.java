package steve6472.polyground.generator.models;

public class FullSidedBlock implements IModel
{
	private static String fullBlockJson = "{\"parent\": \"block/templates/block\", \"textures\": { \"up\" : \"#top\", \"down\" : \"#bottom\", \"north\" : \"#side\", \"east\" : \"#side\", \"south\" : \"#side\", \"west\" : \"#side\"}}";

	private String top, bottom, side;

	public FullSidedBlock(String top, String bottom, String side)
	{
		this.top = top;
		this.bottom = bottom;
		this.side = side;
	}

	@Override
	public String build()
	{
		return fullBlockJson.replace("#top", top).replace("#bottom", bottom).replace("#side", side);
	}
}