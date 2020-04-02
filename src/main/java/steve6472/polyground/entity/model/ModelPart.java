package steve6472.polyground.entity.model;

import steve6472.polyground.EnumFace;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 07.09.2019
 * Project: SJP
 *
 ***********************/
public abstract class ModelPart
{
	EntityModelFace[] faces;

	public float rot, center, pos;
	public float size;

	int vao, positionVbo, colorVbo, textureVbo;

	public ModelPart()
	{
		size = 1f;

		initFaces();
	}

	protected void initFaces()
	{
		faces = new EntityModelFace[6];
		for (int i = 0; i < 6; i++)
		{
			faces[i] = new EntityModelFace(this, EnumFace.values()[i]);
		}
	}

	public void setVao(int vao)
	{
		this.vao = vao;
	}

	public int getVao()
	{
		return vao;
	}

	public abstract void build();

	public abstract int getTriangleCount();
}
