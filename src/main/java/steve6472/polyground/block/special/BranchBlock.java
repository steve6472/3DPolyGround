package steve6472.polyground.block.special;

import org.joml.AABBf;
import org.json.JSONObject;
import steve6472.polyground.EnumFace;
import steve6472.polyground.block.model.CubeHitbox;
import steve6472.polyground.block.model.elements.Bakery;
import steve6472.polyground.block.properties.BooleanProperty;
import steve6472.polyground.block.properties.IProperty;
import steve6472.polyground.block.properties.IntProperty;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.block.states.States;
import steve6472.polyground.block.tree.Tree;
import steve6472.polyground.entity.player.Player;
import steve6472.polyground.registry.Blocks;
import steve6472.polyground.world.ModelBuilder;
import steve6472.polyground.world.World;
import steve6472.polyground.world.chunk.ModelLayer;
import steve6472.sge.main.KeyList;
import steve6472.sge.main.events.MouseEvent;

import java.util.ArrayList;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 09.10.2020
 * Project: CaveGame
 *
 ***********************/
public class BranchBlock extends CustomBlock
{
	public static BooleanProperty LEAVES = States.HAS_LEAVES;
	public static IntProperty RADIUS = States.RADIUS_0_7;

	public BranchBlock(JSONObject json)
	{
		super(json);
		setDefaultState(getDefaultState().with(LEAVES, false).with(RADIUS, 0).get());
	}


	@Override
	public void onClick(BlockState state, World world, Player player, EnumFace clickedOn, MouseEvent click, int x, int y, int z)
	{
		if (click.getAction() == KeyList.PRESS)
		{
			if (click.getButton() == KeyList.LMB)
			{
//				if (state.get(LEAVES))
//				{
//					world.setState(state.with(LEAVES, false).get(), x, y, z);
//					player.processNextBlockBreak = false;
//				}
			} else if (click.getButton() == KeyList.MMB)
			{
				int radius = state.get(RADIUS);
				if (radius == 7)
				{
					world.setState(state.with(LEAVES, state.get(LEAVES)).with(RADIUS, 0).get(), x, y, z);
				} else
				{
					world.setState(state.with(LEAVES, state.get(LEAVES)).with(RADIUS, radius + 1).get(), x, y, z);
				}
			}
		}
	}

	@Override
	public int createModel(int x, int y, int z, World world, BlockState state, ModelBuilder buildHelper, ModelLayer modelLayer)
	{
		if (modelLayer != ModelLayer.NORMAL)
			return 0;

		int tris = 0;

		buildHelper.setSubChunk(world.getSubChunkFromBlockCoords(x, y, z));

		if (state.get(LEAVES))
			tris += CustomBlock.model(Blocks.getBlockByName("oak_leaves").getDefaultState().getBlockModel(world, x, y, z).getElements(), x, y, z, world, buildHelper, modelLayer);

		int radius = state.get(RADIUS) + 1;
		int diameter = (state.get(RADIUS) + 1) * 2;
		tris += Bakery.autoTexturedCube(8 - radius, 8 - radius, 8 - radius, diameter, diameter, diameter, "block/wood/log/oak_log", 0);

		if (radius == 8)
			return tris;

		if (check(world.getState(x + 1, y, z), state))
			tris += Bakery.autoTexturedCube(8 - radius + diameter, 8 - radius, 8 - radius, 8 - radius, diameter, diameter, "block/wood/log/oak_log", 0);

		if (check(world.getState(x, y, z + 1), state))
			tris += Bakery.autoTexturedCube(8 - radius, 8 - radius, 8 - radius + diameter, diameter, diameter, 8 - radius, "block/wood/log/oak_log", 0);

		if (check(world.getState(x - 1, y, z), state))
			tris += Bakery.autoTexturedCube(0, 8 - radius, 8 - radius, 8 - radius, diameter, diameter, "block/wood/log/oak_log", 0);

		if (check(world.getState(x, y, z - 1), state))
			tris += Bakery.autoTexturedCube(8 - radius, 8 - radius, 0, diameter, diameter, 8 - radius, "block/wood/log/oak_log", 0);

		if (check(world.getState(x, y + 1, z), state) || world.getState(x, y + 1, z).getBlock() instanceof LeavesBlock)
			tris += Bakery.autoTexturedCube(8 - radius, 8 - radius + diameter, 8 - radius, diameter, 8 - radius, diameter, "block/wood/log/oak_log", 0);

		if (check(world.getState(x, y - 1, z), state) || world.getState(x, y - 1, z).getBlock() instanceof RootBlock)
			tris += Bakery.autoTexturedCube(8 - radius, 0, 8 - radius, diameter, 8 - radius, diameter, "block/wood/log/oak_log", 0);

		return tris;
	}

	@Override
	public CubeHitbox[] getHitbox(World world, BlockState state, int x, int y, int z)
	{
		AABBf union = new AABBf();

		if (state.get(LEAVES) || state.get(RADIUS) == 8)
		{
			union.setMin(0, 0, 0);
			union.setMax(1, 1, 1);
			return new CubeHitbox[] { new CubeHitbox(union) };
		}

		float radius = (state.get(RADIUS) + 1) / 16f;
		float diameter = ((state.get(RADIUS) + 1) * 2) / 16f;

		List<AABBf> collisionBox = new ArrayList<>();

		collisionBox.add(fromWidth(0.5f - radius, 0.5f - radius, 0.5f - radius, diameter, diameter, diameter));

		if (check(world.getState(x + 1, y, z), state))
			collisionBox.add(fromWidth(0.5f - radius + diameter, 0.5f - radius, 0.5f - radius, 0.5f - radius, diameter, diameter));

		if (check(world.getState(x, y, z + 1), state))
			collisionBox.add(fromWidth(0.5f - radius, 0.5f - radius, 0.5f - radius + diameter, diameter, diameter, 0.5f - radius));

		if (check(world.getState(x - 1, y, z), state))
			collisionBox.add(fromWidth(0, 0.5f - radius, 0.5f - radius, 0.5f - radius, diameter, diameter));

		if (check(world.getState(x, y, z - 1), state))
			collisionBox.add(fromWidth(0.5f - radius, 0.5f - radius, 0, diameter, diameter, 0.5f - radius));

		if (check(world.getState(x, y + 1, z), state) || world.getState(x, y + 1, z).getBlock() instanceof LeavesBlock)
			collisionBox.add(fromWidth(0.5f - radius, 0.5f - radius + diameter, 0.5f - radius, diameter, 0.5f - radius, diameter));

		BlockState down = world.getState(x, y - 1, z);
		if (check(down, state) || down.getBlock() instanceof RootBlock || down.getBlock().getName().equals("grass"))
			collisionBox.add(fromWidth(0.5f - radius, 0, 0.5f - radius, diameter, 0.5f - radius, diameter));

		CubeHitbox[] hitboxes = new CubeHitbox[collisionBox.size() + 1];

		for (int i = 0; i < collisionBox.size(); i++)
		{
			union.union(collisionBox.get(i));
			CubeHitbox hb = new CubeHitbox(collisionBox.get(i));
			hb.setVisible(true);
			hb.setHitbox(false);
			hitboxes[i] = hb;
		}

		CubeHitbox hitbox = new CubeHitbox(union);
		hitbox.setCollisionBox(false);

		hitboxes[hitboxes.length - 1] = hitbox;

		return hitboxes;
	}

	private AABBf fromWidth(float x, float y, float z, float w, float h, float d)
	{
		return new AABBf(x, y, z, x + w, y + h, z + d);
	}

	private boolean check(BlockState world, BlockState itself)
	{
		return world.getBlock() == this || world.getBlock() == Blocks.getBlockByName("oak_log") ||
			(itself.get(RADIUS) == 0 && world.getBlock() instanceof LeavesBlock); // Branch can connect to leaves
	}

	@Override
	public void randomTick(BlockState state, World world, int x, int y, int z)
	{
		Tree tree = new Tree();
		tree.analyze(world, x, y, z);
		tree.grow(world);
	}

	@Override
	public boolean randomTickable()
	{
		return true;
	}

	@Override
	public void fillStates(List<IProperty<?>> properties)
	{
		properties.add(LEAVES);
		properties.add(RADIUS);
	}
}
