package steve6472.polyground.world.chunk;

import org.lwjgl.opengl.GL30;
import steve6472.polyground.gfx.ModelData;

import java.nio.FloatBuffer;

import static steve6472.sge.gfx.VertexObjectCreator.bindVAO;
import static steve6472.sge.gfx.VertexObjectCreator.delete;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 06.11.2019
 * Project: SJP
 *
 ***********************/
public class SubChunkModel
{
	/* Model Data */ int vao, positionVbo, colorVbo, textureVbo, vboNorm;
	int triangleCount;

	private final ModelLayer modelLayer;
	private final SubChunk subChunk;

	public boolean rebuildInProgress;

	public SubChunkModel(ModelLayer modelLayer, SubChunk subChunk)
	{
		this.modelLayer = modelLayer;
		this.subChunk = subChunk;
	}

	public void unload()
	{
		delete(vao, positionVbo, colorVbo, textureVbo, vboNorm);
	}

	public void update(ModelData data)
	{
		bindVAO(vao);

		triangleCount = data.triangleCount;
		storeFloatDataInAttributeList(0, 3, positionVbo, data.vert);
		storeFloatDataInAttributeList(1, 4, colorVbo, data.color);
		storeFloatDataInAttributeList(2, 2, textureVbo, data.text);
		storeFloatDataInAttributeList(3, 3, vboNorm, data.normal);
		rebuildInProgress = false;
	}

	public int getVao()
	{
		return vao;
	}

	public SubChunk getSubChunk()
	{
		return subChunk;
	}

	public ModelLayer getLayer()
	{
		return modelLayer;
	}

	private static void storeFloatDataInAttributeList(int attributeNumber, int size, int vboId, FloatBuffer buffer)
	{
		GL30.glBindBuffer(34962, vboId);
		GL30.glBufferData(34962, buffer, 35044);
		GL30.glVertexAttribPointer(attributeNumber, size, 5126, false, 0, 0L);
		GL30.glBindBuffer(34962, 0);
	}
}
