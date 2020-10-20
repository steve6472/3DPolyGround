package steve6472.polyground.block.model.elements;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 19.10.2020
 * Project: CaveGame
 *
 ***********************/
public class UvBuilder
{
	float u0, v0, u1, v1;

	public static UvBuilder uv(float u0, float v0, float u1, float v1)
	{
		UvBuilder builder = new UvBuilder();
		builder.u0 = u0;
		builder.v0 = v0;
		builder.u1 = u1;
		builder.v1 = v1;
		return builder;
	}

	public UvBuilder div(float d)
	{
		float inv = 1f / d;
		u0 *= inv;
		v0 *= inv;
		u1 *= inv;
		v1 *= inv;
		return this;
	}

	public UvBuilder flipY()
	{
		float temp = v0;
		v0 = v1;
		v1 = temp;
		return this;
	}

	public UvBuilder flipX()
	{
		float temp = u0;
		u0 = u1;
		u1 = temp;
		return this;
	}
}
