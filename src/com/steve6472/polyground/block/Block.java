package com.steve6472.polyground.block;

import com.steve6472.polyground.EnumFace;
import com.steve6472.polyground.block.blockdata.BlockData;
import com.steve6472.polyground.block.model.BlockModel;
import com.steve6472.polyground.block.model.CubeFace;
import com.steve6472.polyground.block.model.faceProperty.AutoUVFaceProperty;
import com.steve6472.polyground.block.model.faceProperty.LayerFaceProperty;
import com.steve6472.polyground.block.model.faceProperty.TextureFaceProperty;
import com.steve6472.polyground.block.model.registry.Cube;
import com.steve6472.polyground.block.special.SnapBlock;
import com.steve6472.polyground.entity.Player;
import com.steve6472.polyground.world.BuildHelper;
import com.steve6472.polyground.world.Cull;
import com.steve6472.polyground.world.SubChunk;
import com.steve6472.sge.main.events.MouseEvent;
import org.joml.AABBf;

import java.io.File;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 23.08.2019
 * Project: SJP
 *
 ***********************/
public class Block
{
	public static Block air, error;

	public boolean isFull;

	private final int id;

	public String name;
	public BlockModel blockModel;

	public static Block createAir()
	{
		return air = new Block("air", 0, new BlockModel());
	}

	public static Block createError()
	{
		Cube cube = new Cube(new AABBf(0, 0, 0, 1, 1, 1));
		cube.setCollisionBox(true);
		cube.setHitbox(true);

		for (EnumFace face : EnumFace.getFaces())
		{
			CubeFace cubeFace = new CubeFace(cube, face);

			TextureFaceProperty texture = new TextureFaceProperty();
			texture.setTexture("null");
			BlockTextureHolder.putTexture("null");
			texture.setTextureId(BlockTextureHolder.getTextureId(texture.getTexture()));
			cubeFace.addProperty(texture);

			cubeFace.addProperty(new AutoUVFaceProperty(true));

			cube.setFace(face, cubeFace);
		}

		BlockModel model = new BlockModel(cube);

		error = new Block("error", 1, model);
		error.addTag("error");
		error.addTag("solid");

		return error;
	}

	private Block(String name, int id, BlockModel blockModel)
	{
		this.name = name;
		this.id = id;
		isFull = false;
		this.blockModel = blockModel;
	}

	public Block(File f, int id)
	{
		isFull = true;
		name = f.getName().substring(0, f.getName().length() - 4);
		blockModel = new BlockModel(f);

		this.id = id;
	}

	public Cube getCube(int i)
	{
		return blockModel.getCube(i);
	}

	public BlockModel getBlockModel()
	{
		return blockModel;
	}

	public String getName()
	{
		return name;
	}

	public int getId()
	{
		return id;
	}

	public List<Cube> getCubes(int x, int y, int z)
	{
		return blockModel.getCubes();
	}

	public List<Cube> getCubes()
	{
		return blockModel.getCubes();
	}

	/* Tags */

	public boolean hasTag(String tag)
	{
		return getBlockModel().hasTag(tag);
	}

	public void addTag(String tag)
	{
		getBlockModel().addTag(tag);
	}

	/* Other Something */

	public boolean rebuildChunkOnPlace() { return true; }
	public boolean isTickable() { return false; }
	public boolean isReplaceable() { return this == air; }

	/* Something */

	public int createModel(int x, int y, int z, SubChunk sc, BlockData blockData, BuildHelper buildHelper, int modelLayer)
	{
		int tris = 0;

		buildHelper.setSubChunk(sc);
		for (Cube c : blockModel.getCubes())
		{
			buildHelper.setCube(c);
			for (EnumFace face : EnumFace.getFaces())
			{
				/* Check if face is in correct (Chunk) Model Layer */
				if (LayerFaceProperty.getModelLayer(c.getFace(face)) == modelLayer)
					if (Cull.renderFace(x, y, z, c, face, this, sc))
						tris += buildHelper.face(face);
			}
		}

		return tris;
	}

	public void tick(SubChunk subChunk, BlockData blockData, int x, int y, int z) {}
	public void postLoad() {}

	/* Events */

	public void onPlace(SubChunk subChunk, BlockData blockData, Player player, EnumFace placedOn, int x, int y, int z) {}
	public void onBreak(SubChunk subChunk, BlockData blockData, Player player, EnumFace breakedFrom, int x, int y, int z) { SnapBlock.activate(this, subChunk, x, y, z); }

	/**
	 *
	 * Runs on block in the world
	 *
	 * @param subChunk Sub Chunk
	 * @param blockData Data of block
	 * @param player Player
	 * @param clickedOn Side the player has clicked on
	 * @param click event
	 * @param x x position of block
	 * @param y y position of block
	 * @param z z position of block
	 */
	public void onClick(SubChunk subChunk, BlockData blockData, Player player, EnumFace clickedOn, MouseEvent click, int x, int y, int z) {}

	public void onUpdate(SubChunk subChunk, BlockData blockData, EnumFace updateFrom, int x, int y, int z) { }
}
