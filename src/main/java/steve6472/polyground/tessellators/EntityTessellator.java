package steve6472.polyground.tessellators;

import steve6472.sge.gfx.TessellatorCreator;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 20.12.2018
 * Project: Poly Creator 2.0
 *
 ***********************/
public class EntityTessellator extends TessellatorCreator
{
	private List<Float> posArr, colorArr, textureArr;

	private float x, y, z;
	private float r, g, b, a;
	private float tx, ty;

	public EntityTessellator pos(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}

	public EntityTessellator color(float r, float g, float b, float a)
	{
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
		return this;
	}

	public EntityTessellator texture(float x, float y)
	{
		this.tx = x;
		this.ty = y;
		return this;
	}

	@Override
	public void endVertex()
	{
		Collections.addAll(posArr, x, y, z);
		Collections.addAll(colorArr, r, g, b, a);
		Collections.addAll(textureArr, tx, ty);
		super.endVertex();
	}

	public void loadAll(List<Float> pos, List<Float> color, List<Float> texture)
	{
		posArr.addAll(pos);
		colorArr.addAll(color);
		textureArr.addAll(texture);
	}

	@Override
	public void begin(int vertexCount)
	{
		posArr = new ArrayList<>();
		colorArr = new ArrayList<>();
		textureArr = new ArrayList<>();

		super.begin(vertexCount);
	}

	public List<Float> getPosArr()
	{
		return posArr;
	}

	public List<Float> getColorArr()
	{
		return colorArr;
	}

	public List<Float> getTextureArr()
	{
		return textureArr;
	}

	public void loadPos(int index)
	{
		loadBuffer(newBuffer(posArr), index, 3);
	}

	public void loadColor(int index)
	{
		loadBuffer(newBuffer(colorArr), index, 4);
	}

	public void loadTexture(int index)
	{
		loadBuffer(newBuffer(textureArr), index, 2);
	}

	private FloatBuffer newBuffer(List<Float> array)
	{
		FloatBuffer f = createBuffer(array.size());
		put(f, array);
		return f;
	}

	@Override
	@Deprecated
	public void draw(int mode)
	{
		super.draw(mode);
		System.exit(0);
	}
}
