package steve6472.polyground.entity;

import org.joml.*;
import steve6472.polyground.block.BlockTextureHolder;
import steve6472.polyground.block.model.IElement;
import steve6472.polyground.entity.interfaces.IRotation;
import steve6472.polyground.gfx.MainRender;
import steve6472.polyground.gfx.ThreadedModelBuilder;
import steve6472.polyground.gfx.shaders.EntityShader;
import steve6472.polyground.world.ModelBuilder;
import steve6472.polyground.world.chunk.ModelLayer;
import steve6472.sge.main.game.mixable.IPosition3f;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 06.09.2020
 * Project: CaveGame
 *
 ***********************/
public class DynamicEntityModel
{
	public static final Quaternionf QUAT = new Quaternionf();
	public static final Matrix4f MAT = new Matrix4f();
	private static final ModelBuilder modelBuilder = new ModelBuilder();

	List<Vector3f> vert;
	List<Vector4f> col;
	List<Vector2f> text;
	List<Vector3f> norm;
	FloatBuffer vertBuffer, colBuffer, textBuffer, normBuffer;

	public DynamicEntityModel()
	{
		vert = new ArrayList<>();
		col = new ArrayList<>();
		text = new ArrayList<>();
		norm = new ArrayList<>();
	}

	int vertexCount;

	public void load(IElement[] elements)
	{
		vertexCount = 0;

		vert.clear();
		col.clear();
		text.clear();
		norm.clear();

		modelBuilder.load(0, 0, 0);
		modelBuilder.load(vert, col, text, norm);

		for (IElement e : elements)
		{
			for (ModelLayer layer : ModelLayer.values())
			{
				vertexCount += e.build(modelBuilder, layer, null, null, 0, 0, 0);
			}
		}

		vertexCount *= 3;

		vertBuffer = ThreadedModelBuilder.toFloatBuffer3(vert);
		colBuffer = ThreadedModelBuilder.toFloatBuffer4(col);
		textBuffer = ThreadedModelBuilder.toFloatBuffer2(text);
		normBuffer = ThreadedModelBuilder.toFloatBuffer3(norm);
	}

	public static Matrix4f createMatrix(IPosition3f position, IRotation rotation, float scale)
	{
		QUAT.identity()
			.rotateXYZ(rotation.getRotations().x, rotation.getRotations().y, rotation.getRotations().z);

		MAT.identity()
			.translate(position.getPosition())
			.translate(rotation.getPivotPoint())
			.rotate(QUAT)
			.scale(scale)
			.translate(-rotation.getPivotPoint().x, -rotation.getPivotPoint().y, -rotation.getPivotPoint().z);

		return MAT;
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

		BlockTextureHolder.getAtlas().getSprite().bind(0);

		loadBuffer(0, 3, vertBuffer);
		loadBuffer(1, 4, colBuffer);
		loadBuffer(2, 2, textBuffer);
		loadBuffer(3, 3, normBuffer);

		glDrawArrays(GL_TRIANGLES, 0, vertexCount);

		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glDisableVertexAttribArray(3);
	}

	private void loadBuffer(int index, int size, FloatBuffer buffer)
	{
		glEnableVertexAttribArray(index);
		glVertexPointer(size, GL_FLOAT, 0, buffer);
		glVertexAttribPointer(index, size, GL_FLOAT, false, 0, buffer);
	}
}
