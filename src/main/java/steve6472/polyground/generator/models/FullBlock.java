package steve6472.polyground.generator.models;

public class FullBlock implements IModel
{
	private String texture;

	public FullBlock(String texture)
	{
		this.texture = texture;
	}

	@Override
	public String build()
	{
		return BlockModelBuilder.create().addCube(CubeBuilder.create().fullBlock().face(FaceBuilder.create().texture(texture))).build().build();
	}
}