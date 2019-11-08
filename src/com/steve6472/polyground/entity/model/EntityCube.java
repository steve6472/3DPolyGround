package com.steve6472.polyground.entity.model;

import com.steve6472.polyground.EnumFace;
import com.steve6472.polyground.CaveGame;
import com.steve6472.polyground.tessellators.EntityTessellator;
import com.steve6472.sge.gfx.Tessellator3D;

import java.util.Arrays;

import static com.steve6472.sge.gfx.VertexObjectCreator.*;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 23.08.2019
 * Project: SJP
 *
 ***********************/
public class EntityCube extends ModelPart
{
	private float width, height, depth;

	public EntityCube(float width, float height, float depth)
	{
		super();

		this.width = width;
		this.height = height;
		this.depth = depth;
	}

	@Override
	public void build()
	{
		vao = createVAO();

		EntityTessellator tess = CaveGame.getInstance().entityTessellator;

		renderBox();

		positionVbo = storeFloatDataInAttributeList(0, 3, tess.getPosArr());
		colorVbo = storeFloatDataInAttributeList(1, 4, tess.getColorArr());
		textureVbo = storeFloatDataInAttributeList(2, 2, tess.getTextureArr());

		unbindVAO();
	}

	public void render()
	{
		glBindVertexArray(getVao());

		for (int l = 0; l < 3; l++)
			glEnableVertexAttribArray(l);

		glDrawArrays(Tessellator3D.TRIANGLES, 0, getTriangleCount() * 3);

		for (int l = 0; l < 3; l++)
			glDisableVertexAttribArray(l);
	}

	private void renderBox()
	{
		float minX = -width / 2f;
		float minY = -height / 2f;
		float minZ = -depth / 2f;

		float maxX = width / 2f;
		float maxY = height / 2f;
		float maxZ = depth / 2f;

		EntityTessellator tess = CaveGame.getInstance().entityTessellator;
		tess.begin(36);

		EntityModelFace face = getFace(EnumFace.SOUTH);
		tess.color(face.getShade(), face.getShade(), face.getShade(), 1.0f);

		tess.pos(minX, maxY, minZ).texture(face.getMinU(), face.getMinV()).endVertex();
		tess.pos(minX, minY, minZ).texture(face.getMinU(), face.getMaxV()).endVertex();
		tess.pos(minX, minY, maxZ).texture(face.getMaxU(), face.getMaxV()).endVertex();

		tess.pos(minX, minY, maxZ).texture(face.getMaxU(), face.getMaxV()).endVertex();
		tess.pos(minX, maxY, maxZ).texture(face.getMaxU(), face.getMinV()).endVertex();
		tess.pos(minX, maxY, minZ).texture(face.getMinU(), face.getMinV()).endVertex();

		face = getFace(EnumFace.NORTH);
		tess.color(face.getShade(), face.getShade(), face.getShade(), 1.0f);

		tess.pos(maxX, maxY, maxZ).texture(face.getMinU(), face.getMinV()).endVertex();
		tess.pos(maxX, minY, maxZ).texture(face.getMinU(), face.getMaxV()).endVertex();
		tess.pos(maxX, minY, minZ).texture(face.getMaxU(), face.getMaxV()).endVertex();

		tess.pos(maxX, minY, minZ).texture(face.getMaxU(), face.getMaxV()).endVertex();
		tess.pos(maxX, maxY, minZ).texture(face.getMaxU(), face.getMinV()).endVertex();
		tess.pos(maxX, maxY, maxZ).texture(face.getMinU(), face.getMinV()).endVertex();

		face = getFace(EnumFace.UP);
		tess.color(face.getShade(), face.getShade(), face.getShade(), 1.0f);

		tess.pos(maxX, maxY, minZ).texture(face.getMinU(), face.getMinV()).endVertex();
		tess.pos(minX, maxY, minZ).texture(face.getMinU(), face.getMaxV()).endVertex();
		tess.pos(minX, maxY, maxZ).texture(face.getMaxU(), face.getMaxV()).endVertex();

		tess.pos(minX, maxY, maxZ).texture(face.getMaxU(), face.getMaxV()).endVertex();
		tess.pos(maxX, maxY, maxZ).texture(face.getMaxU(), face.getMinV()).endVertex();
		tess.pos(maxX, maxY, minZ).texture(face.getMinU(), face.getMinV()).endVertex();

		face = getFace(EnumFace.DOWN);
		tess.color(face.getShade(), face.getShade(), face.getShade(), 1.0f);

		tess.pos(minX, minY, maxZ).texture(face.getMaxU(), face.getMinV()).endVertex();
		tess.pos(minX, minY, minZ).texture(face.getMinU(), face.getMinV()).endVertex();
		tess.pos(maxX, minY, minZ).texture(face.getMinU(), face.getMaxV()).endVertex();

		tess.pos(maxX, minY, minZ).texture(face.getMinU(), face.getMaxV()).endVertex();
		tess.pos(maxX, minY, maxZ).texture(face.getMaxU(), face.getMaxV()).endVertex();
		tess.pos(minX, minY, maxZ).texture(face.getMaxU(), face.getMinV()).endVertex();

		face = getFace(EnumFace.WEST);
		tess.color(face.getShade(), face.getShade(), face.getShade(), 1.0f);


		tess.pos(maxX, maxY, minZ).texture(face.getMinU(), face.getMinV()).endVertex();
		tess.pos(maxX, minY, minZ).texture(face.getMinU(), face.getMaxV()).endVertex();
		tess.pos(minX, minY, minZ).texture(face.getMaxU(), face.getMaxV()).endVertex();

		tess.pos(minX, minY, minZ).texture(face.getMaxU(), face.getMaxV()).endVertex();
		tess.pos(minX, maxY, minZ).texture(face.getMaxU(), face.getMinV()).endVertex();
		tess.pos(maxX, maxY, minZ).texture(face.getMinU(), face.getMinV()).endVertex();

		face = getFace(EnumFace.EAST);
		tess.color(face.getShade(), face.getShade(), face.getShade(), 1.0f);

		tess.pos(minX, maxY, maxZ).texture(face.getMinU(), face.getMinV()).endVertex();
		tess.pos(minX, minY, maxZ).texture(face.getMinU(), face.getMaxV()).endVertex();
		tess.pos(maxX, minY, maxZ).texture(face.getMaxU(), face.getMaxV()).endVertex();

		tess.pos(maxX, minY, maxZ).texture(face.getMaxU(), face.getMaxV()).endVertex();
		tess.pos(maxX, maxY, maxZ).texture(face.getMaxU(), face.getMinV()).endVertex();
		tess.pos(minX, maxY, maxZ).texture(face.getMinU(), face.getMinV()).endVertex();
	}

	/* Textures */
	public void setTop(float minU, float minV, float maxU, float maxV)
	{
		faces[0].setUV(minU, minV, maxU, maxV);
	}

	public void setBottom(float minU, float minV, float maxU, float maxV)
	{
		faces[1].setUV(minU, minV, maxU, maxV);
	}

	public void setFront(float minU, float minV, float maxU, float maxV)
	{
		faces[2].setUV(minU, minV, maxU, maxV);
	}

	public void setBack(float minU, float minV, float maxU, float maxV)
	{
		faces[3].setUV(minU, minV, maxU, maxV);
	}

	public void setRight(float minU, float minV, float maxU, float maxV)
	{
		faces[4].setUV(minU, minV, maxU, maxV);
	}

	public void setLeft(float minU, float minV, float maxU, float maxV)
	{
		faces[5].setUV(minU, minV, maxU, maxV);
	}

	public EntityModelFace[] getFaces()
	{
		return faces;
	}

	public EntityModelFace getFace(EnumFace face)
	{
		return switch (face)
			{
				case UP -> faces[0];
				case DOWN -> faces[1];
				case NORTH ->faces[2];
				case SOUTH -> faces[3];
				case EAST -> faces[4];
				case WEST -> faces[5];
				case NONE -> null;
			};
	}

	@Override
	public int getTriangleCount()
	{
		return 36;
	}

	@Override
	public String toString()
	{
		return "EntityCube{" + "width=" + width + ", height=" + height + ", depth=" + depth + ", faces=" + Arrays.toString(faces) + ", rot=" + rot + ", center=" + center + ", pos=" + pos + '}';
	}
}
