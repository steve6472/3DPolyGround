package steve6472.polyground.world;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
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
	private Vector3f offset;
	private List<Vector3f> vert;
	private List<Vector4f> col;
	private List<Vector2f> text;
	private List<Vector3f> norm;
	private SubChunk sc;

	public ModelBuilder()
	{
		offset = new Vector3f();
	}

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
		setOffset(0, 0, 0);
	}

	public void setOffset(float offsetX, float offsetY, float offsetZ)
	{
		offset.set(offsetX, offsetY, offsetZ);
	}

	public void setSubChunk(SubChunk sc)
	{
		this.sc = sc;
	}

	public void tri(Vector3f v0, Vector3f v1, Vector3f v2)
	{
		vert.add(new Vector3f(v0).add(x, y, z).add(offset));
		vert.add(new Vector3f(v1).add(x, y, z).add(offset));
		vert.add(new Vector3f(v2).add(x, y, z).add(offset));
	}

	/**
	 * Uses v0, v1, v2 directly, adds block position to them
	 * @param v0 v0
	 * @param v1 v1
	 * @param v2 v2
	 */
	public void tri_(Vector3f v0, Vector3f v1, Vector3f v2)
	{
		vert.add(v0.add(x, y, z).add(offset));
		vert.add(v1.add(x, y, z).add(offset));
		vert.add(v2.add(x, y, z).add(offset));
	}

	public void uv(Vector2f uv)
	{
		text.add(uv);
	}

	private final Vector3f DEFAULT_BIOME_COLOR = new Vector3f(92 / 255f, 184 / 255f, 64 / 255f);

	public Vector3f getBiomeTint()
	{
		if (sc == null)
			return DEFAULT_BIOME_COLOR;
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
