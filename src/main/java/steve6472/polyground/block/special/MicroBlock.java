package steve6472.polyground.block.special;

import steve6472.polyground.CaveGame;
import steve6472.polyground.EnumFace;
import steve6472.polyground.HitResult;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.blockdata.BlockData;
import steve6472.polyground.block.blockdata.IBlockData;
import steve6472.polyground.block.blockdata.MicroBlockData;
import steve6472.polyground.block.model.faceProperty.LayerFaceProperty;
import steve6472.polyground.block.model.Cube;
import steve6472.polyground.registry.BlockRegistry;
import steve6472.polyground.entity.Player;
import steve6472.polyground.world.BuildHelper;
import steve6472.polyground.world.Cull;
import steve6472.polyground.world.chunk.ModelLayer;
import steve6472.polyground.world.chunk.SubChunk;
import org.joml.AABBf;
import org.joml.Intersectionf;
import org.joml.Vector2f;
import steve6472.sge.main.KeyList;
import steve6472.sge.main.events.MouseEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 20.12.2019
 * Project: SJP
 *
 ***********************/
public class MicroBlock extends Block implements IBlockData
{

	public MicroBlock(File f, int id)
	{
		super(f, id);
		isFull = false;
	}

	@Override
	public void onPlace(SubChunk subChunk, BlockData blockData, Player player, EnumFace placedOn, int x, int y, int z)
	{
		if (!(blockData instanceof MicroBlockData))
		{
			throw new IllegalStateException("MicroBlockData expected. Found '" + (blockData == null ? null : blockData.getClass().getSimpleName()) + "'");
		}

		MicroBlockData mbd = (MicroBlockData) blockData;
		for (int i = 0; i < 16; i++)
		{
			for (int j = 0; j < 16; j++)
			{
				mbd.setBlock(BlockRegistry.getBlockIdByName("stone"), i, 0, j);
			}
		}

		subChunk.forceRebuild();
	}

	@Override
	public void onClick(SubChunk subChunk, BlockData blockData, Player player, EnumFace clickedOn, MouseEvent click, int x, int y, int z)
	{
		player.processNextBlockBreak = false;
		if (!(blockData instanceof MicroBlockData))
		{
			throw new IllegalStateException("MicroBlockData expected. Found '" + (blockData == null ? null : blockData.getClass().getSimpleName()) + "'");
		}

		HitResult hr = player.getHitResult();

		if ((click.getButton() == KeyList.RMB || click.getButton() == KeyList.LMB) && click.getAction() == KeyList.PRESS)
		{
			if (CaveGame.itemInHand.getName().equals("micro_block"))
			{
				System.out.println("no");
				return;
			}

			if (CaveGame.itemInHand.getBlockToPlace() == null && CaveGame.itemInHand.getBlockToPlace() != Block.air)
				return;

			MicroBlockData mbd = (MicroBlockData) blockData;

			float cx = (clickedOn == EnumFace.NORTH) ? -0.001f : 0;
			float cy = (clickedOn == EnumFace.UP) ? -0.001f : 0;
			float cz = (clickedOn == EnumFace.EAST) ? -0.001f : 0;

			int mx = (int) Math.floor((hr.getPx() - x + cx) * 16.0f);
			int my = (int) Math.floor((hr.getPy() - y + cy) * 16.0f);
			int mz = (int) Math.floor((hr.getPz() - z + cz) * 16.0f);

			if (click.getButton() == KeyList.RMB)
				mbd.setBlock(CaveGame.itemInHand.getBlockToPlace().getId(), mx + clickedOn.getXOffset(), my + clickedOn.getYOffset(), mz + clickedOn.getZOffset());
			else
				mbd.setBlock(Block.air.getId(), mx, my, mz);

			player.processNextBlockPlace = false;
		}

		subChunk.forceRebuild();
	}

	@Override
	public int createModel(int x, int y, int z, SubChunk sc, BlockData blockData, BuildHelper buildHelper, ModelLayer modelLayer)
	{
		int tris = 0;

		buildHelper.setSubChunk(sc);
		for (Cube c : getCubesForRender(x + sc.getX() * 16, y + sc.getLayer() * 16, z + sc.getZ() * 16))
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

	private List<Cube> getCubesForRender(int x, int y, int z)
	{
		SubChunk sc = CaveGame.getInstance().getWorld().getSubChunkFromBlockCoords(x, y, z);
		BlockData blockData = sc.getBlockData(Math.floorMod(x, 16), Math.floorMod(y, 16), Math.floorMod(z, 16));

		if (!(blockData instanceof MicroBlockData))
		{
			throw new IllegalStateException("MicroBlockData expected. Found '" + (blockData == null ? null : blockData.getClass().getSimpleName()) + "'");
		}

		MicroBlockData mbd = (MicroBlockData) blockData;

		if (mbd.getPallete().isEmpty())
			return getBlockModel().getCubes();

		List<Cube> cubes = new ArrayList<>();

		for (MicroBlockData.PalleteEntry value : mbd.getPallete().values())
		{
			for (int i = 0; i < value.getCount(); i++)
			{
				for (Cube m : value.getBlock().getBlockModel().getCubes())
				{
					Cube c = m.copy();
					scale(c.getAabb(), value.getPos().get(i));
					cubes.add(c);
				}
			}
		}

		return cubes;
	}

	@Override
	public List<Cube> getCubes(int x, int y, int z)
	{
		SubChunk sc = CaveGame.getInstance().getWorld().getSubChunkFromBlockCoords(x, y, z);
		BlockData blockData = sc.getBlockData(Math.floorMod(x, 16), Math.floorMod(y, 16), Math.floorMod(z, 16));

		if (!(blockData instanceof MicroBlockData))
		{
			throw new IllegalStateException("MicroBlockData expected. Found '" + (blockData == null ? null : blockData.getClass().getSimpleName()) + "'");
		}

		MicroBlockData mbd = (MicroBlockData) blockData;

		if (mbd.getPallete().isEmpty())
			return getBlockModel().getCubes();

		Player player = CaveGame.getInstance().getPlayer();
		Vector2f hr = new Vector2f();
		float closest = Float.MAX_VALUE;
		HitResult hitResult = new HitResult();

		List<Cube> cubes = new ArrayList<>();

		boolean hit = false;

		for (MicroBlockData.PalleteEntry value : mbd.getPallete().values())
		{
			for (int i = 0; i < value.getCount(); i++)
			{
				for (Cube m : value.getBlock().getBlockModel().getCubes())
				{
					Cube c = m.copy();
					scale(c.getAabb(), value.getPos().get(i));

					if (!c.isHitbox())
						continue;

					if (Intersectionf.intersectRayAab(player.getX(), player.getY() + player.eyeHeight, player.getZ(), player.viewDir.x, player.viewDir.y, player.viewDir.z, c.getAabb().minX + x, c.getAabb().minY + y, c.getAabb().minZ + z, c.getAabb().maxX + x, c.getAabb().maxY + y, c.getAabb().maxZ + z, hr))
					{
						if (hr.x <= closest)
						{
							hit = true;
							short pos = value.getPos().get(i);

							closest = hr.x;
							hitResult.setBlockCoords(i, pos, value.getBlock().getId());
							hitResult.setAabb(c.getAabb());
						}
					}
				}
			}
		}

		if (hit)
		{
			for (MicroBlockData.PalleteEntry value : mbd.getPallete().values())
			{
				if (hitResult.getZ() != value.getBlock().getId())
					continue;

				for (int i = 0; i < value.getCount(); i++)
				{
					if (hitResult.getX() != i)
						continue;

					for (Cube m : value.getBlock().getBlockModel().getCubes())
					{
						Cube c = m.copy();
						scale(c.getAabb(), (short) hitResult.getY());
						cubes.add(c);
					}
				}
			}
		}

		return cubes;
	}

	private void scale(AABBf b, short pos)
	{
		float cx = (b.minX + b.maxX) / 2f;
		float cy = (b.minY + b.maxY) / 2f;
		float cz = (b.minZ + b.maxZ) / 2f;

		short x = (short) (pos >> 8);
		short y = (short) ((pos >> 4) & 0xf);
		short z = (short) (pos & 0xf);

		float lmx = (b.minX - cx) / 16f + cx + (x / 16.0f) - 1 / 32f * 15f;
		float lmy = (b.minY - cy) / 16f + cy + (y / 16.0f) - 1 / 32f * 15f;
		float lmz = (b.minZ - cz) / 16f + cz + (z / 16.0f) - 1 / 32f * 15f;

		float lax = (b.maxX - cx) / 16f + cx + (x / 16.0f) - 1 / 32f * 15f;
		float lay = (b.maxY - cy) / 16f + cy + (y / 16.0f) - 1 / 32f * 15f;
		float laz = (b.maxZ - cz) / 16f + cz + (z / 16.0f) - 1 / 32f * 15f;

		b.setMin(lmx, lmy, lmz);
		b.setMax(lax, lay, laz);
		b.correctBounds();
	}

	@Override
	public BlockData createNewBlockEntity()
	{
		return new MicroBlockData();
	}
}
