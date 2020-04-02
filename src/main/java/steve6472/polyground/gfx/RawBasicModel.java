package steve6472.polyground.gfx;

import steve6472.sge.gfx.VertexObjectCreator;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 30.03.2020
 * Project: CaveGame
 *
 ***********************/
public class RawBasicModel
{
	private final int vao;
	private final int vboVert, vboColor;
	private final int vertCount;

	public RawBasicModel(int vertCount)
	{
		this.vertCount = vertCount;

		vao = VertexObjectCreator.createVAO();
		vboVert = VertexObjectCreator.createVBO();
		vboColor = VertexObjectCreator.createVBO();
	}

	public int getVao()
	{
		return vao;
	}

	public int getVboVert()
	{
		return vboVert;
	}

	public int getVboColor()
	{
		return vboColor;
	}

	public int getVertCount()
	{
		return vertCount;
	}
}
