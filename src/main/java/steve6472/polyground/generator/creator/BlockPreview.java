package steve6472.polyground.generator.creator;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import steve6472.polyground.PolyUtil;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.model.IElement;
import steve6472.polyground.gfx.atlas.Atlas;
import steve6472.polyground.gfx.shaders.MainShader;
import steve6472.polyground.gfx.shaders.world.FlatTexturedShader;
import steve6472.polyground.tessellators.BasicTessellator;
import steve6472.polyground.tessellators.ItemTessellator;
import steve6472.polyground.world.ModelBuilder;
import steve6472.polyground.world.World;
import steve6472.polyground.world.chunk.ModelLayer;
import steve6472.polyground.world.chunk.SubChunk;
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
	private List<Vector3f> vertices, normal;
	private List<Vector4f> colors;
	private List<Vector2f> textures;

	private ModelBuilder buildHelper;
	private DepthFrameBuffer preview;
	private ItemTessellator itemTessellator;
	private BasicTessellator basicTessellator;
	private Camera camera;
	private final World dummyWorld;

	private int w, h;

	private FlatTexturedShader flatTexturedShader;
	private MainShader mainShader;

	private Atlas atlas;

	private steve6472.polyground.generator.creator.BlockCreatorGui creatorGui;

	public BlockPreview(steve6472.polyground.generator.creator.BlockCreatorGui creatorGui)
	{
		this.creatorGui = creatorGui;

		Block.createAir();
		dummyWorld = new World();

		preview = new DepthFrameBuffer(16 * 70, 9 * 70);

		vertices = new ArrayList<>();
		textures = new ArrayList<>();
		colors = new ArrayList<>();
		normal = new ArrayList<>();

		buildHelper = new ModelBuilder();
		itemTessellator = new ItemTessellator((int) Math.pow(2, 16));
		basicTessellator = new BasicTessellator(6);

		flatTexturedShader = new FlatTexturedShader();
		Matrix4f projectionMatrix = PolyUtil.createProjectionMatrix(16 * 70, 9 * 70, 3f, 80f);

		Matrix4f transformationMatrix = new Matrix4f();
//		transformationMatrix.translate(0.5f, 0.5f, 0.5f);
//		transformationMatrix.scale(1f);
//		transformationMatrix.translate(-0.5f, -0.5f, -0.5f);

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

		Matrix4f projectionMatrix = PolyUtil.createProjectionMatrix(w, h, 16f, 80f);

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

	public void renderBlock(List<IElement> elements)
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
		int tris = 0;
		for (int i = SubChunk.getModelCount() - 1; i >= 0; i--)
		{
			tris += model(elements, ModelLayer.values()[i]);
		}

		itemTessellator.begin(tris * 3);

		for (int i = 0; i < tris * 3; i++)
		{
			Vector3f vert = vertices.get(i);
			Vector4f col = colors.get(i);
			Vector2f text = textures.get(i);

			itemTessellator.pos(vert.x / 16f, vert.y / 16f, vert.z / 16f).color(col.x, col.y, col.z, col.w).texture(text.x, text.y).endVertex();
		}

		itemTessellator.loadPos(0);
		itemTessellator.loadColor(1);
		itemTessellator.loadTexture(2);

		itemTessellator.draw(Tessellator.TRIANGLES);

		itemTessellator.disable(0, 1, 2);

		renderAxis();

		//Render selected cube outline
//		if (creatorGui.getSelectedCube() != null)
//			AABBUtil.renderAABBf(creatorGui.getSelectedCube().getAabb(), basicTessellator, 1f, mainShader);

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
		{
			atlas.getSprite().bind();
		} else
		{
			System.err.println("Atlas is null!");
		}
	}

	private int model(List<IElement> elements, ModelLayer layer)
	{
		int tris = 0;

		for (IElement c : elements)
		{
			tris += c.build(buildHelper, layer, dummyWorld, null, 0, 0, 0);
		}

		return tris;
	}

	public int getId()
	{
		return preview.texture;
	}
}
