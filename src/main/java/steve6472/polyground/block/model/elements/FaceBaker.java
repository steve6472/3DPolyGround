package steve6472.polyground.block.model.elements;

import org.joml.Vector3f;
import steve6472.polyground.EnumFace;
import steve6472.polyground.block.properties.enums.EnumAxis;
import steve6472.polyground.world.chunk.ModelLayer;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 19.10.2020
 * Project: CaveGame
 *
 ***********************/
public class FaceBaker
{
	String texture;
	boolean biomeTint;
	Vector3f tint;
	ModelLayer modelLayer;
	UvBuilder uv;
	boolean bothSides;
	boolean autoUv;

	public FaceBaker(String texture, boolean biomeTint, Vector3f tint, ModelLayer modelLayer, UvBuilder uv, boolean bothSides, boolean autoUv)
	{
		this.texture = texture;
		this.biomeTint = biomeTint;
		this.tint = tint;
		this.modelLayer = modelLayer;
		this.uv = uv;
		this.bothSides = bothSides;
		this.autoUv = autoUv;
	}

	public String getTexture()
	{
		return texture;
	}

	public boolean isBiomeTint()
	{
		return biomeTint;
	}

	public Vector3f getTint()
	{
		return tint;
	}

	public ModelLayer getModelLayer()
	{
		return modelLayer;
	}

	public UvBuilder getUv()
	{
		return uv;
	}

	public boolean isBothSides()
	{
		return bothSides;
	}

	public boolean isAutoUv()
	{
		return autoUv;
	}

	public int add(CubeBaker cubeBaker, EnumFace face)
	{
		int tris = 0;
		tris += Bakery.face(face, cubeBaker, this, (byte) 0);
		if (isBothSides())
		{
			byte flip = 0;
			flip |= (face.getAxis() == EnumAxis.X ? 1 : 0);
			flip |= (face.getAxis() == EnumAxis.Y ? 2 : 0);
			flip |= (face.getAxis() == EnumAxis.Z ? 4 : 0);
			tris += Bakery.face(face, cubeBaker, this, flip);
		}
		return tris;
	}
}
