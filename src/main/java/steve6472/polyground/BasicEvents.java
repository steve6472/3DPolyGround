package steve6472.polyground;

import steve6472.polyground.block.Block;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.entity.Player;
import steve6472.polyground.world.World;
import steve6472.polyground.world.chunk.SubChunk;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 18.09.2019
 * Project: SJP
 *
 ***********************/
public class BasicEvents
{
	/* Placing Block */
	public static void place(Block block, EnumFace face, Player player)
	{
		if (block == null)
			return;

		place(block, face, player, face.getXOffset(), face.getYOffset(), face.getZOffset());
	}

	public static void place(Block block, EnumFace face, Player player, int xOffset, int yOffset, int zOffset)
	{
		if (block == null)
			return;

		HitResult hr = CaveGame.getInstance().hitPicker.getHitResult();
		World world = CaveGame.getInstance().world;

		SubChunk subChunk = world.getSubChunkFromBlockCoords(hr.getX() + xOffset, hr.getY() + yOffset, hr.getZ() + zOffset);
		if (subChunk == null)
			return;

		world.setBlock(block, hr.getX() + xOffset, hr.getY() + yOffset, hr.getZ() + zOffset, Block::isReplaceable);
		BlockState state = subChunk.getState(hr.getCx() + xOffset, hr.getCy() + yOffset, hr.getCz() + zOffset);

		block.onPlace(subChunk, state, player, face, hr.getX() + xOffset, hr.getY() + yOffset, hr.getZ() + zOffset);
		updateAll(subChunk, hr.getX() + xOffset, hr.getY() + yOffset, hr.getZ() + zOffset);
	}

	/* Replacing Block */
	public static void replace(Block block, EnumFace face, Player player)
	{
		if (block == null)
			return;

		replace(block, face, player, face.getXOffset(), face.getYOffset(), face.getZOffset());
	}

	public static void replace(Block block, EnumFace face, Player player, int xOffset, int yOffset, int zOffset)
	{
		if (block == null)
			return;

		HitResult hr = CaveGame.getInstance().hitPicker.getHitResult();
		World world = CaveGame.getInstance().world;

		SubChunk subChunk = world.getSubChunkFromBlockCoords(hr.getX() + xOffset, hr.getY() + yOffset, hr.getZ() + zOffset);

		world.setBlock(block, hr.getX() + xOffset, hr.getY() + yOffset, hr.getZ() + zOffset);
		BlockState state = subChunk.getState(hr.getCx() + xOffset, hr.getCy() + yOffset, hr.getCz() + zOffset);

		block.onPlace(subChunk, state, player, face, hr.getX() + xOffset, hr.getY() + yOffset, hr.getZ() + zOffset);
		updateAll(subChunk, hr.getX() + xOffset, hr.getY() + yOffset, hr.getZ() + zOffset);
	}

	/* Breaking Block */
	public static void breakBlock(Player player)
	{
		HitResult hr = CaveGame.getInstance().hitPicker.getHitResult();
		World world = CaveGame.getInstance().world;

		SubChunk subChunk = world.getSubChunkFromBlockCoords(hr.getX(), hr.getY(), hr.getZ());

		BlockState state = subChunk.getState(hr.getCx(), hr.getCy(), hr.getCz());

		world.getBlock(hr.getX(), hr.getY(), hr.getZ()).onBreak(subChunk, state, player, hr.getFace(), hr.getX(), hr.getY(), hr.getZ());
		world.setBlock(Block.air, hr.getX(), hr.getY(), hr.getZ());
		updateAll(subChunk, hr.getX(), hr.getY(), hr.getZ());
	}

	/* Updating Block */
	public static void updateAll(SubChunk subChunk, int x, int y, int z)
	{
		update(subChunk, EnumFace.NONE, x, y, z);
		for (EnumFace face : EnumFace.getFaces())
		{
			update(subChunk.getNeighbouringSubChunk(Math.floorMod(x, 16) + face.getXOffset(), Math.floorMod(y, 16) + face.getYOffset(), Math.floorMod(z, 16) + face.getZOffset()), face, x + face.getXOffset(), y + face.getYOffset(), z + face.getZOffset());
		}
	}

	public static void update(SubChunk subChunk, EnumFace updateFrom, int x, int y, int z)
	{
		if (subChunk == null)
			return;

		subChunk.addScheduledUpdate(Math.floorMod(x, 16), Math.floorMod(y, 16), Math.floorMod(z, 16));

		//		Block block = world.getBlock(x, y, z);
		//
		//		BlockData blockData = subChunk.getBlockData(x, y, z);
		//
		//		block.onUpdate(subChunk, blockData, updateFrom, x, y, z);
	}
}