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

		HitResult hr = CaveGame.getInstance().hitPicker.getHitResult();
		place(block.getStateForPlacement(CaveGame.getInstance().getWorld(), player, face, hr.getX() + face.getXOffset(), hr.getY() + face.getYOffset(), hr.getZ() + face.getZOffset()), face, player);
	}

	public static void place(BlockState state, EnumFace face, Player player)
	{
		if (state == null)
			return;

		place(state, face, player, face.getXOffset(), face.getYOffset(), face.getZOffset());
	}

	public static void place(BlockState state, EnumFace face, Player player, int xOffset, int yOffset, int zOffset)
	{
		if (state == null)
			return;

		HitResult hr = CaveGame.getInstance().hitPicker.getHitResult();
		World world = CaveGame.getInstance().world;

		SubChunk subChunk = world.getSubChunkFromBlockCoords(hr.getX() + xOffset, hr.getY() + yOffset, hr.getZ() + zOffset);
		if (subChunk == null)
			return;

		world.setState(state, hr.getX() + xOffset, hr.getY() + yOffset, hr.getZ() + zOffset, blockState -> blockState.getBlock().isReplaceable(blockState));

		state.getBlock().onPlace(world, state, player, face, hr.getX() + xOffset, hr.getY() + yOffset, hr.getZ() + zOffset);
		updateAll(subChunk, hr.getX() + xOffset, hr.getY() + yOffset, hr.getZ() + zOffset);
	}

	public static void place(BlockState state, World world, int x, int y, int z)
	{
		if (state == null || world == null)
			return;

		world.setState(state, x, y, z, blockState -> blockState.getBlock().isReplaceable(blockState));

		state.getBlock().onPlace(world, state, null, null, x, y, z);
//		updateAll(world.getSubChunkFromBlockCoords(x, y, z), x, y, z);
	}

	/* Replacing Block */
	public static void replace(Block block, EnumFace face, Player player)
	{
		if (block == null)
			return;

		replace(block, face, player, face.getXOffset(), face.getYOffset(), face.getZOffset());
	}
	public static void replace(BlockState state, EnumFace face, Player player)
	{
		if (state == null)
			return;

		replace(state, face, player, face.getXOffset(), face.getYOffset(), face.getZOffset());
	}

	public static void replace(Block block, EnumFace face, Player player, int xOffset, int yOffset, int zOffset)
	{
		if (block == null)
			return;

		HitResult hr = CaveGame.getInstance().hitPicker.getHitResult();
		World world = CaveGame.getInstance().world;

		SubChunk subChunk = world.getSubChunkFromBlockCoords(hr.getX() + xOffset, hr.getY() + yOffset, hr.getZ() + zOffset);

		world.setBlock(block, hr.getX() + xOffset, hr.getY() + yOffset, hr.getZ() + zOffset);
		BlockState state = world.getState(hr.getX() + xOffset, hr.getY() + yOffset, hr.getZ() + zOffset);

		block.onPlace(world, state, player, face, hr.getX() + xOffset, hr.getY() + yOffset, hr.getZ() + zOffset);
		updateAll(subChunk, hr.getX() + xOffset, hr.getY() + yOffset, hr.getZ() + zOffset);
	}

	public static void replace(BlockState state, EnumFace face, Player player, int xOffset, int yOffset, int zOffset)
	{
		if (state == null)
			return;

		HitResult hr = CaveGame.getInstance().hitPicker.getHitResult();
		World world = CaveGame.getInstance().world;

		SubChunk subChunk = world.getSubChunkFromBlockCoords(hr.getX() + xOffset, hr.getY() + yOffset, hr.getZ() + zOffset);

		world.setState(state, hr.getX() + xOffset, hr.getY() + yOffset, hr.getZ() + zOffset);

		state.getBlock().onPlace(world, state, player, face, hr.getX() + xOffset, hr.getY() + yOffset, hr.getZ() + zOffset);
		updateAll(subChunk, hr.getX() + xOffset, hr.getY() + yOffset, hr.getZ() + zOffset);
	}

	/* Breaking Block */
	public static void breakBlock(Player player)
	{
		HitResult hr = CaveGame.getInstance().hitPicker.getHitResult();
		World world = CaveGame.getInstance().world;

		SubChunk subChunk = world.getSubChunkFromBlockCoords(hr.getX(), hr.getY(), hr.getZ());

		BlockState state = subChunk.getState(hr.getCx(), hr.getCy(), hr.getCz());

		world.getBlock(hr.getX(), hr.getY(), hr.getZ()).onBreak(world, state, player, hr.getFace(), hr.getX(), hr.getY(), hr.getZ());
		world.setBlock(Block.air, hr.getX(), hr.getY(), hr.getZ());
		updateAll(subChunk, hr.getX(), hr.getY(), hr.getZ());
	}

	/* Updating Block */
	public static void updateAll(SubChunk subChunk, int x, int y, int z)
	{
		update(subChunk, EnumFace.NONE, x, y, z);
		for (EnumFace face : EnumFace.getFaces())
		{
			update(subChunk.getWorld().getSubChunk((x + face.getXOffset()) >> 4, (y+ face.getYOffset()) >> 4, (z+ face.getZOffset()) >> 4), face, x + face.getXOffset(), y + face.getYOffset(), z + face.getZOffset());
		}
	}

	public static void update(SubChunk subChunk, EnumFace updateFrom, int x, int y, int z)
	{
		if (subChunk == null)
			return;

		subChunk.rebuild();

//		BlockState state = subChunk.getState(Math.floorMod(x, 16), Math.floorMod(y, 16), Math.floorMod(z, 16));
//		subChunk.setState(state.getBlock().getStateForPlacement(subChunk, null, null, Math.floorMod(x, 16), Math.floorMod(y, 16), Math.floorMod(z, 16)), Math.floorMod(x, 16), Math.floorMod(y, 16), Math.floorMod(z, 16));
		subChunk.addScheduledUpdate(Math.floorMod(x, 16), Math.floorMod(y, 16), Math.floorMod(z, 16));

		//		Block block = world.getBlock(x, y, z);
		//
		//		BlockData blockData = subChunk.getBlockData(x, y, z);
		//
		//		block.onUpdate(subChunk, blockData, updateFrom, x, y, z);
	}
}
