package com.steve6472.polyground.item;

import com.steve6472.polyground.CaveGame;
import com.steve6472.polyground.EnumFace;
import com.steve6472.polyground.PolyUtil;
import com.steve6472.polyground.block.Block;
import com.steve6472.polyground.block.BlockTextureHolder;
import com.steve6472.polyground.block.model.faceProperty.LayerFaceProperty;
import com.steve6472.polyground.block.model.registry.Cube;
import com.steve6472.polyground.block.registry.BlockRegistry;
import com.steve6472.polyground.tessellators.ItemTessellator;
import com.steve6472.polyground.world.BuildHelper;
import com.steve6472.polyground.world.chunk.ModelLayer;
import com.steve6472.polyground.world.chunk.SubChunk;
import com.steve6472.sge.gfx.*;
import com.steve6472.sge.main.events.WindowSizeEvent;
import com.steve6472.sge.main.game.Camera;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 21.09.2019
 * Project: SJP
 *
 ***********************/
public class ItemAtlas
{
	CaveGame caveGame;

	public FrameBuffer itemAtlas;

	public DepthFrameBuffer textureBuffer;
	private BuildHelper buildHelper;
	private Matrix4f viewMatrix, transformationMatrix, projectionMatrix;
	private Camera camera;
	private ItemTessellator itemTessellator;
	private Sprite itemTexture;

	private List<Float> vertices, textures, colors, light;

	public int totalSize;

	public int itemSize = 32;
	private int s;

	public ItemAtlas(CaveGame caveGame)
	{
		this.caveGame = caveGame;

		vertices = new ArrayList<>();
		textures = new ArrayList<>();
		colors = new ArrayList<>();
		light = new ArrayList<>();

		itemTexture = new Sprite();

		itemTessellator = new ItemTessellator();

		s = Atlas.getNextPowerOfTwo((int) Math.ceil(Math.sqrt(BlockRegistry.getAllBlocks().size())));
		totalSize = s * itemSize;
		itemAtlas = new FrameBuffer(totalSize, totalSize, true);
		textureBuffer = new DepthFrameBuffer(itemSize, itemSize, true);
		buildHelper = new BuildHelper();
		camera = new Camera();
		camera.calculateOrbit(0, 0, 0, 10f);

		buildHelper.atlasSize = BlockTextureHolder.getAtlas().getTileCount();
		buildHelper.texel = 1f / (float) BlockTextureHolder.getAtlas().getTileCount();
	}

	public void start()
	{
		caveGame.getSpriteRender().basicResizeOrtho(new WindowSizeEvent(totalSize, totalSize));
	}

	public int renderBlock(Block b)
	{
		setupBlockItemRender();

		glEnable(GL_CULL_FACE);
		textureBuffer.bindFrameBuffer(itemSize, itemSize);
		DepthFrameBuffer.clearCurrentBuffer();

		for (int i = SubChunk.getModelCount() - 1; i >= 0; i--)
		{
			renderItem(b, ModelLayer.values()[i]);
		}

		textureBuffer.unbindCurrentFrameBuffer(caveGame);
		glDisable(GL_CULL_FACE);

		insertTexture(textureBuffer.texture, false);

		return index;
	}

	public int renderTexture(String texture)
	{
		itemTexture.deleteSprite();
		itemTexture.load(texture);

		insertTexture(itemTexture.getId(), true);

		return index;
	}

	public void finish()
	{
		caveGame.getSpriteRender().basicResizeOrtho(new WindowSizeEvent(caveGame.getWidth(), caveGame.getHeight()));
		Shader.releaseShader();
	}

	private void setupBlockItemRender()
	{
		projectionMatrix = PolyUtil.createProjectionMatrix(itemSize, itemSize, 3f, 0.1f);
		transformationMatrix = new Matrix4f();
		transformationMatrix.translate(0.5f, 0.5f, 0.5f);
		transformationMatrix.scale(0.00166f);
		transformationMatrix.translate(-0.5f, -0.5f, -0.5f);

		camera.setPosition(0.5f, 0.5f, 0.5f);
		camera.setYaw(toRad(45f));
		camera.setPitch(toRad(-30.0f));
		camera.calculateOrbit(camera.getX(), camera.getY(), camera.getZ(), 1.5f);

		camera.updateViewMatrix();

		CaveGame.shaders.worldShader.bind();
		CaveGame.shaders.worldShader.setView(camera.getViewMatrix());
		CaveGame.shaders.worldShader.setTransformation(transformationMatrix);
		CaveGame.shaders.worldShader.setProjection(projectionMatrix);

		BlockTextureHolder.getAtlas().getSprite().bind();
	}

	private float toRad(float deg)
	{
		return deg * 0.017453292519943295f;
	}

	private void renderItem(Block block, ModelLayer modelLayer)
	{
		vertices.clear();
		textures.clear();
		colors.clear();
		light.clear();

		buildHelper.load(vertices, colors, textures, light);
		int tris = model(block, modelLayer);

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
	}

	private int model(Block block, ModelLayer modelLayer)
	{
		if (block == null || block.getCubes() == null) return 0;

		int tris = 0;

		for (Cube c : block.getBlockModel().getCubes())
		{
			buildHelper.setCube(c);

			for (EnumFace face : EnumFace.getFaces())
			{
				/* Check if face is in correct (Chunk) Model Layer */
				if (LayerFaceProperty.getModelLayer(c.getFace(face)) == modelLayer)
					tris += buildHelper.face(face);
			}
		}

		return tris;
	}

	private int index;

	private void insertTexture(int textureId, boolean invert)
	{
		int x = index % s;
		int y = s - index / s - 1;
		index++;

		x *= itemSize;
		y *= itemSize;

		itemAtlas.bindFrameBuffer(totalSize, totalSize);

		if (invert)
			SpriteRender.renderSpriteInverted(x, y, itemSize, itemSize, 0, textureId, itemSize, itemSize);
		else
			SpriteRender.renderSprite(x, y, itemSize, itemSize, 0, textureId);


		itemAtlas.unbindCurrentFrameBuffer(caveGame);
	}

	public int getTileCount()
	{
		return s;
	}
}
