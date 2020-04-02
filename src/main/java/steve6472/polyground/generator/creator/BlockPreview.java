package steve6472.polyground.generator.creator;

import steve6472.polyground.AABBUtil;
import steve6472.polyground.EnumFace;
import steve6472.polyground.PolyUtil;
import steve6472.polyground.block.model.BlockModel;
import steve6472.polyground.block.model.CubeFace;
import steve6472.polyground.block.model.faceProperty.VisibleFaceProperty;
import steve6472.polyground.block.model.registry.Cube;
import steve6472.polyground.block.model.registry.face.FaceRegistry;
import steve6472.polyground.gfx.shaders.MainShader;
import steve6472.polyground.gfx.shaders.world.FlatTexturedShader;
import steve6472.polyground.tessellators.BasicTessellator;
import steve6472.polyground.tessellators.ItemTessellator;
import steve6472.polyground.world.BuildHelper;
import org.joml.Matrix4f;
import steve6472.sge.gfx.Atlas;
import steve6472.sge.gfx.DepthFrameBuffer;
import steve6472.sge.gfx.Tessellator;
import steve6472.sge.gfx.shaders.Shader;
import steve6472.sge.main.KeyList;
import steve6472.sge.main.events.WindowSizeEvent;
import steve6472.sge.main.game.Camera;

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
	private List<Float> vertices, textures, colors, normal;

	private BuildHelper buildHelper;
	private DepthFrameBuffer preview;
	private ItemTessellator itemTessellator;
	private BasicTessellator basicTessellator;
	private Camera camera;

	private int w, h;

	private FlatTexturedShader flatTexturedShader;
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
		normal = new ArrayList<>();

		buildHelper = new BuildHelper();
		itemTessellator = new ItemTessellator();
		basicTessellator = new BasicTessellator();

		flatTexturedShader = new FlatTexturedShader();
		Matrix4f projectionMatrix = PolyUtil.createProjectionMatrix(16 * 70, 9 * 70, 3f, 80f);

		Matrix4f transformationMatrix = new Matrix4f();
		transformationMatrix.translate(0.5f, 0.5f, 0.5f);
		transformationMatrix.scale(1f);
		transformationMatrix.translate(-0.5f, -0.5f, -0.5f);

		flatTexturedShader.getShader().bind();
		flatTexturedShader.setProjection(projectionMatrix);
		flatTexturedShader.setTransformation(transformationMatrix);

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

	public void updateSize(WindowSizeEvent e)
	{
		this.w = e.getWidth();
		this.h = e.getHeight();
		preview.resize(w, h);

		Matrix4f projectionMatrix = PolyUtil.createProjectionMatrix(w, h, 3f, 80f);

		flatTexturedShader.getShader().bind();
		flatTexturedShader.setProjection(projectionMatrix);

		mainShader.getShader().bind();
		mainShader.setProjection(projectionMatrix);

		Shader.releaseShader();
	}

	private boolean flag;

	public void tick()
	{
		if (creatorGui.getMouseHandler().getButton() == KeyList.RMB && creatorGui.isCursorInComponent(creatorGui.getMouseHandler(), 220, 45, creatorGui.getMainApp().getWidth() - 440, creatorGui.getMainApp().getHeight() - 90))
		{
			if (!flag)
			{
				camera.oldx = creatorGui.getMouseHandler().getMouseX();
				camera.oldy = creatorGui.getMouseHandler().getMouseY();
				flag = true;
			}

			camera.headOrbit(creatorGui.getMouseHandler().getMouseX(), creatorGui.getMouseHandler().getMouseY(), 0.5f, 0.5f, 0.5f, 0.5f, 1.5f);

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
		normal.clear();

		preview.bindFrameBuffer(w, h);
		DepthFrameBuffer.clearCurrentBuffer();

		buildHelper.load(vertices, colors, textures, normal);
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
			AABBUtil.renderAABBf(creatorGui.getSelectedCube().getAabb(), basicTessellator, 1f, mainShader);

		preview.unbindCurrentFrameBuffer(w, h);

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
		basicTessellator.draw(Tessellator.LINES);
		basicTessellator.disable(0, 1);
	}

	private void setupBlockItemRender()
	{
		flatTexturedShader.bind();
		flatTexturedShader.setView(camera.getViewMatrix());

		if (atlas != null)
			atlas.getSprite().bind();
	}

	private int model(BlockModel model)
	{
		if (model.getCubes() == null)
			return 0;

		int tris = 0;

		for (Cube c : model.getCubes())
		{
			buildHelper.setCube(c);

			for (EnumFace face : EnumFace.getFaces())
			{
				CubeFace cubeFace = c.getFace(face);

				if (cubeFace != null)
				{
					if (!VisibleFaceProperty.check(cubeFace))
						continue;

					tris += buildHelper.face(face);

					if (c.getFace(face).getProperty(FaceRegistry.texture).isReference())
					{
						buildHelper.replaceLastFaceWithErrorTexture(256, 0, 256, 0, 256, 0);
					} else if (c.getFace(face).getProperty(FaceRegistry.texture).getTextureId() == -1)
					{
						buildHelper.replaceLastFaceWithErrorTexture();
					}
				}
			}
		}

		return tris;
	}

	public int getId()
	{
		return preview.texture;
	}
}
