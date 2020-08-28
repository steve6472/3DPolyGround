package steve6472.polyground.world;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import steve6472.polyground.block.model.CubeHitbox;
import steve6472.polyground.world.chunk.SubChunk;

import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 19.08.2019
 * Project: SJP
 *
 ***********************/
public final class ModelBuilder
{
	public int atlasSize;

	public float texel = 1f / 16f;

	private int x;
	private int y;
	private int z;
	private List<Vector3f> vert;
	private List<Vector4f> col;
	private List<Vector2f> text;
	private List<Vector3f> norm;
	private CubeHitbox cube;
	private SubChunk sc;

	public void load(List<Vector3f> vert, List<Vector4f> col, List<Vector2f> text, List<Vector3f> norm)
	{
		this.vert = vert;
		this.col = col;
		this.text = text;
		this.norm = norm;
	}

	public void load(int x, int y, int z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void setCube(CubeHitbox cube)
	{
		this.cube = cube;
	}

	public void setSubChunk(SubChunk sc)
	{
		this.sc = sc;
	}

	public void quad(Vector3f v0, Vector3f v1, Vector3f v2, Vector3f v3, Vector3f v4, Vector3f v5)
	{
		vert.add(v0.add(x, y, z));
		vert.add(v1.add(x, y, z));
		vert.add(v2.add(x, y, z));
		vert.add(v3);
		vert.add(v4.add(x, y, z));
		vert.add(v5);
	}

	public void tri(Vector3f v0, Vector3f v1, Vector3f v2)
	{
		vert.add(new Vector3f(v0).add(x, y, z));
		vert.add(new Vector3f(v1).add(x, y, z));
		vert.add(new Vector3f(v2).add(x, y, z));
	}

	public void uv(Vector2f uv)
	{
		text.add(uv);
	}

	public void uv(float u, float v)
	{
		text.add(new Vector2f(u, v));
	}

	private final Vector3f ONE = new Vector3f(92 / 255f, 184 / 255f, 64 / 255f);

	public Vector3f getBiomeTint()
	{
		if (sc == null)
			return ONE;
		else
			return sc.getWorld().biomes.getBiome(sc.getBiomeId(x, y, z)).getColor();
	}

	public void colorTri(float r, float g, float b)
	{
		for (int i = 0; i < 3; i++)
		{
			getCol().add(new Vector4f(r, g, b, 1.0f));
		}
	}

	public void normal(float x, float y, float z)
	{
		Vector3f v = new Vector3f(x, y, z);
		for (int i = 0; i < 6; i++)
		{
			norm.add(v);
		}
	}

	public void normalTri(Vector3f normal)
	{
		for (int i = 0; i < 3; i++)
		{
			norm.add(normal);
		}
	}


	public List<Vector3f> getVert()
	{
		return vert;
	}

	public List<Vector4f> getCol()
	{
		return col;
	}

	public List<Vector2f> getText()
	{
		return text;
	}


}
