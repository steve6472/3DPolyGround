package com.steve6472.polyground.generator.creator;

import com.steve6472.polyground.AABBUtil;
import com.steve6472.polyground.EnumFace;
import com.steve6472.polyground.PolyUtil;
import com.steve6472.polyground.block.model.BlockModel;
import com.steve6472.polyground.block.model.CubeFace;
import com.steve6472.polyground.block.model.faceProperty.UVFaceProperty;
import com.steve6472.polyground.block.model.faceProperty.VisibleFaceProperty;
import com.steve6472.polyground.block.model.registry.Cube;
import com.steve6472.polyground.block.model.registry.TintedCube;
import com.steve6472.polyground.block.model.registry.face.FaceRegistry;
import com.steve6472.polyground.shaders.MainShader;
import com.steve6472.polyground.shaders.world.WorldShader;
import com.steve6472.polyground.tessellators.BasicTessellator;
import com.steve6472.polyground.tessellators.ItemTessellator;
import com.steve6472.polyground.world.BuildHelper;
import com.steve6472.sge.gfx.Atlas;
import com.steve6472.sge.gfx.DepthFrameBuffer;
import com.steve6472.sge.gfx.Tessellator;
import com.steve6472.sge.main.KeyList;
import com.steve6472.sge.main.game.Camera;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 10.10.2019
 * Project: SJP
 *
 ***********************/
public class BlockPreview
{
	private List<Float> vertices, textures, colors, emissive;

	private BuildHelper buildHelper;
	private DepthFrameBuffer preview;
	private ItemTessellator itemTessellator;
	private BasicTessellator basicTessellator;
	private Camera camera;

	private WorldShader worldShader;
	private MainShader mainShader;

	private Atlas atlas;

	private BlockCreatorGui creatorGui;

	public BlockPreview(BlockCreatorGui creatorGui)
	{
		this.creatorGui = creatorGui;

		preview = new DepthFrameBuffer(16 * 70, 9 * 70);

		vertices = new ArrayList<>();
		textures = new ArrayList<>();
		colors = new ArrayList<>();
		emissive = new ArrayList<>();

		buildHelper = new BuildHelper();
		itemTessellator = new ItemTessellator();
		basicTessellator = new BasicTessellator();

		worldShader = new WorldShader();
		Matrix4f projectionMatrix = PolyUtil.createProjectionMatrix(16 * 70, 9 * 70, 3f, 80f);

		Matrix4f transformationMatrix = new Matrix4f();
		transformationMatrix.translate(0.5f, 0.5f, 0.5f);
		transformationMatrix.scale(1f);
		transformationMatrix.translate(-0.5f, -0.5f, -0.5f);

		worldShader.getShader().bind();
		worldShader.setProjection(projectionMatrix);
		worldShader.setTransformation(transformationMatrix);

		mainShader = new MainShader();
		mainShader.getShader().bind();
		mainShader.setProjection(projectionMatrix);
		mainShader.setTransformation(transformationMatrix);

		camera = new Camera();
		camera.calculateOrbit(0.5f, 0.5f, 0.5f, 1.5f);
		camera.updateViewMatrix();

		update(null);
	}

	public void update(Atlas atlas)
	{
		this.atlas = atlas;
		if (atlas == null)
		{
			buildHelper.atlasSize = 1;
			buildHelper.texel = 1f;
		} else
		{
			buildHelper.atlasSize = atlas.getTileCount();
			buildHelper.texel = 1f / (float) atlas.getTileCount();
		}
	}

	private boolean flag;

	public void tick()
	{
		if (creatorGui.getMouseHandler().getButton() == KeyList.RMB && creatorGui.isCursorInComponent(creatorGui.getMouseHandler(), 220, 45, 680, 540))
		{
			if (!flag)
			{
				camera.oldx = creatorGui.getMouseHandler().getMouseX();
				camera.oldy = creatorGui.getMouseHandler().getMouseY();
				flag = true;
			}

			camera.headOrbit(
				creatorGui.getMouseHandler().getMouseX(), creatorGui.getMouseHandler().getMouseY(), 0.5f,
				0.5f, 0.5f, 0.5f, 1.5f);

			camera.updateViewMatrix();
		} else
		{
			flag = false;
		}
	}

	public void renderBlock(BlockModel block)
	{
		setupBlockItemRender();

		glEnable(GL_CULL_FACE);
		glEnable(GL_DEPTH_TEST);

		vertices.clear();
		textures.clear();
		colors.clear();
		emissive.clear();

		preview.bindFrameBuffer(16 * 70, 9 * 70);
		DepthFrameBuffer.clearCurrentBuffer();

		buildHelper.load(0, 0, 0, vertices, colors, textures, emissive);
		int tris = model(block);

		itemTessellator.begin(tris * 3);

		for (int i = 0; i < tris; i++)
		{
			float x = vertices.get(i * 3);
			float y = vertices.get(i * 3 + 1);
			float z = vertices.get(i * 3 + 2);

			float r = colors.get(i * 4);
			float g = colors.get(i * 4 + 1);
			float b = colors.get(i * 4 + 2);
			float a = colors.get(i * 4 + 3);

			float u = textures.get(i * 2);
			float v = textures.get(i * 2 + 1);

			itemTessellator.pos(x, y, z).color(r, g, b, a).texture(u, v).endVertex();
		}

		itemTessellator.loadPos(0);
		itemTessellator.loadColor(1);
		itemTessellator.loadTexture(2);

		itemTessellator.draw(Tessellator.TRIANGLES);

		itemTessellator.disable(0, 1, 2);

		renderAxis();

		//Render selected cube outline
		if (creatorGui.getSelectedCube() != null)
			AABBUtil.renderAABBf(creatorGui.getSelectedCube().getAabb(), basicTessellator, mainShader);

		preview.unbindCurrentFrameBuffer(16 * 70, 9 * 70);

		glDisable(GL_DEPTH_TEST);
		glDisable(GL_CULL_FACE);
	}

	private void renderAxis()
	{
		mainShader.getShader().bind();
		mainShader.setView(camera.getViewMatrix());

		basicTessellator.begin(6);
		basicTessellator.pos(0.5f, 0.5f, 0.5f).color(1, 0, 0, 1).endVertex();
		basicTessellator.pos(1.5f, 0.5f, 0.5f).color(1, 0, 0, 1).endVertex();

		basicTessellator.pos(0.5f, 0.5f, 0.5f).color(0, 1, 0, 1).endVertex();
		basicTessellator.pos(0.5f, 1.5f, 0.5f).color(0, 1, 0, 1).endVertex();

		basicTessellator.pos(0.5f, 0.5f, 0.5f).color(0, 0, 1, 1).endVertex();
		basicTessellator.pos(0.5f, 0.5f, 1.5f).color(0, 0, 1, 1).endVertex();

		basicTessellator.loadPos(0);
		basicTessellator.loadColor(1);
		basicTessellator.loadNormal(2);
		basicTessellator.draw(Tessellator.LINES);
		basicTessellator.disable(0, 1, 2);
	}

	private void setupBlockItemRender()
	{
		worldShader.bind();
		worldShader.setView(camera.getViewMatrix());

		if (atlas != null)
			atlas.getSprite().bind();
	}

	private int model(BlockModel model)
	{
		if (model.getCubes() == null) return 0;

		int tris = 0;

		for (Cube c : model.getCubes())
		{
			buildHelper.setCube(c);

			if (c instanceof TintedCube)
			{
				throw new IllegalStateException("Model contains TintedCube which is not supported!");

				/*
				TintedCube tc = (TintedCube) c;
				for (EnumFace face : EnumFace.getFaces())
				{
					tris += buildHelper.face(face);
					CubeFace cubeFace = tc.getFace(face);
					if (cubeFace != null)
					{
						if (!cubeFace.visible) continue;
						tris += buildHelper.face(face);

						recolor(buildHelper, cubeFace.getShade(), tc);
						if (cubeFace.getTexture() == -1)
						{
							buildHelper.replaceLastFaceWithErrorTexture();
						}
					}
				}*/
			} else
			{
				for (EnumFace face : EnumFace.getFaces())
				{
					CubeFace cubeFace = c.getFace(face);

					if (cubeFace != null)
					{
						if (!VisibleFaceProperty.check(cubeFace)) continue;

						if (cubeFace.hasProperty(FaceRegistry.uv))
						{
							UVFaceProperty uv = cubeFace.getProperty(FaceRegistry.uv);
							uv.setUV(uv.getMinU(), uv.getMinV(), uv.getMaxU(), uv.getMaxV());
						}

						tris += buildHelper.face(face);

						if (c.getFace(face).getProperty(FaceRegistry.texture).getTextureId() == -1)
						{
							buildHelper.replaceLastFaceWithErrorTexture();
						}
					}
				}
			}
		}

		return tris;
	}

	private static void recolor(BuildHelper buildHelper, float shade, TintedCube cube)
	{
		for (int j = 0; j < 24; j++)
		{
			buildHelper.getCol().remove(buildHelper.getCol().size() - 1);
		}
		for (int j = 0; j < 6; j++)
		{
			shade(buildHelper, cube.red, cube.green, cube.blue, shade);
		}
	}

	private static void shade(BuildHelper buildHelper, float r, float g, float b, float shade)
	{
		buildHelper.getCol().add(r * shade);
		buildHelper.getCol().add(g * shade);
		buildHelper.getCol().add(b * shade);
		buildHelper.getCol().add(1.0f);
	}

	public int getId()
	{
		return preview.texture;
	}
}
