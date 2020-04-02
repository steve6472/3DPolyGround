package steve6472.polyground.generator.models.slab;

import steve6472.polyground.generator.models.IModel;

public class SlabBottomSidedBlock implements IModel
{
	private static String json = "{\"parent\": \"block/templates/slab_bottom\", \"textures\": { \"up\" : \"#top\", \"down\" : \"#bottom\", \"north\" : \"#side\", \"east\" : \"#side\", \"south\" : \"#side\", \"west\" : \"#side\"}}";

	private String top, bottom, side;

	public SlabBottomSidedBlock(String top, String bottom, String side)
	{
		this.top = top;
		this.bottom = bottom;
		this.side = side;
	}

	@Override
	public String build()
	{
		return json.replace("#top", top).replace("#bottom", bottom).replace("#side", side);
	}
}