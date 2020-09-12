package steve6472.polyground.entity;

import org.joml.*;
import org.lwjgl.opengl.GL30;
import steve6472.polyground.block.BlockAtlas;
import steve6472.polyground.block.model.IElement;
import steve6472.polyground.block.model.ModelLoader;
import steve6472.polyground.entity.interfaces.IRotation;
import steve6472.polyground.gfx.MainRender;
import steve6472.polyground.gfx.ThreadedModelBuilder;
import steve6472.polyground.gfx.shaders.EntityShader;
import steve6472.polyground.world.ModelBuilder;
import steve6472.polyground.world.chunk.ModelLayer;
import steve6472.sge.gfx.Tessellator;
import steve6472.sge.main.game.mixable.IPosition3f;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import static steve6472.sge.gfx.VertexObjectCreator.*;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 06.09.2020
 * Project: CaveGame
 *
 ***********************/
public class StaticEntityModel
{
	private static final Quaternionf quat = new Quaternionf();
	private static final Matrix4f mat = new Matrix4f();

	int vao, vertexCount;

	public void load(ModelBuilder modelBuilder, ModelLoader modelLoader, String path, boolean fixUv)
	{
		load(modelBuilder, modelLoader.loadElements(ModelLoader.load(path, false), 0, 0, 0), fixUv);
	}

	public void load(ModelBuilder modelBuilder, IElement[] elements, boolean fixUv)
	{
		List<Vector3f> vert = new ArrayList<>();
		List<Vector4f> col = new ArrayList<>();
		List<Vector2f> text = new ArrayList<>();
		List<Vector3f> norm = new ArrayList<>();

		modelBuilder.load(0, 0, 0);
		modelBuilder.load(vert, col, text, norm);

		for (IElement e : elements)
		{
			if (fixUv) e.fixUv(modelBuilder.texel);
			vertexCount += e.build(modelBuilder, ModelLayer.NORMAL, null, null, 0, 0, 0);
		}

		vertexCount *= 3;

		vao = createVAO();
		storeFloatDataInAttributeList(0, 3, ThreadedModelBuilder.toFloatBuffer3(vert));
		storeFloatDataInAttributeList(1, 4, ThreadedModelBuilder.toFloatBuffer4(col));
		storeFloatDataInAttributeList(2, 2, ThreadedModelBuilder.toFloatBuffer2(text));
		storeFloatDataInAttributeList(3, 3, ThreadedModelBuilder.toFloatBuffer3(norm));
		unbindVAO();
	}

	public static Matrix4f createMatrix(IPosition3f position, IRotation rotation, float scale)
	{
		quat.identity()
			.rotateXYZ(rotation.getRotations().x, rotation.getRotations().y, rotation.getRotations().z);

		mat.identity()
			.translate(position.getPosition())
			.translate(rotation.getPivotPoint())
			.rotate(quat)
			.scale(scale)
			.translate(-rotation.getPivotPoint().x, -rotation.getPivotPoint().y, -rotation.getPivotPoint().z);

		return mat;
	}

	public void render(Matrix4f viewMatrix, IPosition3f position, IRotation rotation, float scale)
	{
		render(viewMatrix, createMatrix(position, rotation, scale));
	}

	public void render(Matrix4f viewMatrix, Matrix4f mat)
	{
		MainRender.shaders.entityShader.bind(viewMatrix);
		MainRender.shaders.entityShader.setTransformation(mat);
		MainRender.shaders.entityShader.setUniform(EntityShader.NORMAL_MATRIX, new Matrix3f(new Matrix4f(mat).invert().transpose3x3()));

		BlockAtlas.getAtlas().getSprite().bind(0);

		basicRender(vao, 4, vertexCount, Tessellator.TRIANGLES);
	}

	private void storeFloatDataInAttributeList(int attributeNumber, int size, FloatBuffer buffer)
	{
		int vboID = createVBO();
		GL30.glBindBuffer(34962, vboID);
		GL30.glBufferData(34962, buffer, 35044);
		GL30.glVertexAttribPointer(attributeNumber, size, 5126, false, 0, 0L);
		GL30.glBindBuffer(34962, 0);
	}
}
