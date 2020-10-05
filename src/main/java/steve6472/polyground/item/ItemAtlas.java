package steve6472.polyground.item;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import steve6472.polyground.CaveGame;
import steve6472.polyground.PolyUtil;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.BlockAtlas;
import steve6472.polyground.block.model.BlockModel;
import steve6472.polyground.block.model.IElement;
import steve6472.polyground.gfx.MainRender;
import steve6472.polyground.registry.Blocks;
import steve6472.polyground.tessellators.ItemTessellator;
import steve6472.polyground.world.ModelBuilder;
import steve6472.polyground.world.World;
import steve6472.polyground.world.biomes.Biomes;
import steve6472.polyground.world.chunk.ModelLayer;
import steve6472.polyground.world.chunk.SubChunk;
import steve6472.polyground.world.generator.generators.IBiomeGenerator;
import steve6472.polyground.world.generator.generators.IHeightMapGenerator;
import steve6472.polyground.world.generator.generators.impl.flat.FlatHeightMapGen;
import steve6472.polyground.world.generator.generators.impl.flat.SingleBiomeGen;
import steve6472.polyground.world.generator.generators.impl.world.SurfaceGenerator;
import steve6472.sge.gfx.*;
import steve6472.sge.gfx.shaders.Shader;
import steve6472.sge.main.events.WindowSizeEvent;
import steve6472.sge.main.game.Camera;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 21.09.2019
 * Project: SJP
 *
 ***********************/
@SuppressWarnings("removal")
public class ItemAtlas
{
	CaveGame caveGame;

	public FrameBuffer itemAtlas;

	public DepthFrameBuffer textureBuffer;
	private ModelBuilder buildHelper;
	private Matrix4f viewMatrix, transformationMatrix, projectionMatrix;
	private Camera camera;
	private ItemTessellator itemTessellator;
	private Sprite itemTexture;
	private final World dummyWorld;

	private List<Vector3f> vertices, normal;
	private List<Vector4f> colors;
	private List<Vector2f> textures;

	public int totalSize;

	public int itemSize = 32;
	private int s;

	public ItemAtlas(CaveGame caveGame)
	{
		this.caveGame = caveGame;

		IBiomeGenerator biom = new SingleBiomeGen(0, Biomes.AIR_BIOME);
		IHeightMapGenerator heig = new FlatHeightMapGen(biom, 0);
		dummyWorld = new World(caveGame, 0, biom, heig, (c) -> new SurfaceGenerator(heig, c));

		vertices = new ArrayList<>();
		textures = new ArrayList<>();
		colors = new ArrayList<>();
		normal = new ArrayList<>();

		itemTexture = new Sprite();

		itemTessellator = new ItemTessellator((int) Math.pow(2, 16));

		s = Atlas.getNextPowerOfTwo((int) Math.ceil(Math.sqrt(Blocks.getAllBlocks().length)));
		totalSize = s * itemSize;
		itemAtlas = new FrameBuffer(totalSize, totalSize, true);
		textureBuffer = new DepthFrameBuffer(itemSize, itemSize, true);
		buildHelper = new ModelBuilder();
		camera = new Camera();
		camera.calculateOrbit(0, 0, 0, 10f);

		buildHelper.atlasSize = BlockAtlas.getAtlas().getTileCount();
		buildHelper.texel = 1f / (float) BlockAtlas.getAtlas().getTileCount();
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

		MainRender.shaders.flatTexturedShader.bind();
		MainRender.shaders.flatTexturedShader.setView(camera.getViewMatrix());
		MainRender.shaders.flatTexturedShader.setTransformation(transformationMatrix);
		MainRender.shaders.flatTexturedShader.setProjection(projectionMatrix);

		BlockAtlas.getAtlas().getSprite().bind();
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
		normal.clear();

		buildHelper.load(vertices, colors, textures, normal);
		int tris = model(block, modelLayer);

		itemTessellator.begin(tris * 3);

		for (int i = 0; i < vertices.size(); i++)
		{
			Vector3f vert = vertices.get(i);
			Vector4f col = colors.get(i);
			Vector2f text = textures.get(i);

			itemTessellator.pos(vert.x, vert.y, vert.z).color(col.x, col.y, col.z, col.w).texture(text.x, text.y).endVertex();
		}

		itemTessellator.loadPos(0);
		itemTessellator.loadColor(1);
		itemTessellator.loadTexture(2);

		itemTessellator.draw(Tessellator.TRIANGLES);

		itemTessellator.disable(0, 1, 2);
	}

	private int model(Block block, ModelLayer modelLayer)
	{
		int tris = 0;

		BlockModel model = block.getDefaultState().getBlockModel(dummyWorld, 0, 0, 0);

		if (model.getElements() != null)
		{
			for (IElement c : model.getElements())
			{
				tris += c.build(buildHelper, modelLayer, dummyWorld, block.getDefaultState(), 0, 0, 0);
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
			SpriteRender.renderSpriteInverted(x, y, itemSize, itemSize, 0, textureId);
		else
			SpriteRender.renderSprite(x, y, itemSize, itemSize, 0, textureId);


		itemAtlas.unbindCurrentFrameBuffer(caveGame);
	}

	public int getTileCount()
	{
		return s;
	}
}
