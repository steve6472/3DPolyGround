package steve6472.polyground.block.model.elements;

import org.joml.Vector3f;
import steve6472.polyground.world.chunk.ModelLayer;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 19.10.2020
 * Project: CaveGame
 *
 ***********************/
public final class FaceBakerBuilder
{
	String texture;
	boolean biomeTint;
	Vector3f tint;
	ModelLayer modelLayer;
	UvBuilder uv;
	boolean bothSides;
	boolean autoUv;

	private FaceBakerBuilder()
	{
	}

	public static FaceBakerBuilder faceBaker()
	{
		return new FaceBakerBuilder();
	}

	public FaceBakerBuilder withTexture(String texture)
	{
		this.texture = texture;
		return this;
	}

	public FaceBakerBuilder withBiomeTint(boolean biomeTint)
	{
		this.biomeTint = biomeTint;
		return this;
	}

	public FaceBakerBuilder withTint(Vector3f tint)
	{
		this.tint = tint;
		this.biomeTint = false;
		return this;
	}

	public FaceBakerBuilder withModelLayer(ModelLayer modelLayer)
	{
		this.modelLayer = modelLayer;
		return this;
	}

	public FaceBakerBuilder withUv(UvBuilder uv)
	{
		this.uv = uv.div(16f);
		return this;
	}

	public FaceBakerBuilder withBothSides(boolean bothSides)
	{
		this.bothSides = bothSides;
		return this;
	}

	public FaceBakerBuilder wituAutoUv(boolean autoUv)
	{
		this.autoUv = autoUv;
		return this;
	}

	public FaceBakerBuilder but()
	{
		return faceBaker()
			.withTexture(texture)
			.withBiomeTint(biomeTint)
			.withTint(tint)
			.withModelLayer(modelLayer)
			.withUv(uv)
			.withBothSides(bothSides)
			.wituAutoUv(autoUv);
	}

	public FaceBaker build()
	{
		return new FaceBaker(texture, biomeTint, tint, modelLayer, uv, bothSides, autoUv);
	}
}
