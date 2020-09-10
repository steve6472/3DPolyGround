package steve6472.polyground.block.special;

import org.json.JSONObject;
import steve6472.polyground.EnumFace;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.entity.player.Player;
import steve6472.polyground.gfx.light.EnumLightSource;
import steve6472.polyground.gfx.light.LightManager;
import steve6472.polyground.world.World;
import steve6472.sge.main.util.ColorUtil;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 23.11.2019
 * Project: SJP
 *
 ***********************/
public class LightSourceBlock extends Block implements ILightBlock
{
	private int color;
	private float constant, linear, quadratic;
	private float xOffset, yOffset, zOffset;
	private float dirX, dirY, dirZ, cutOff;

	public LightSourceBlock(JSONObject json)
	{
		super(json);
	}

	@Override
	public void load(JSONObject json)
	{
		System.out.println(name + " " + json);
		if (json.has("color"))
			color = (int) Long.parseLong(json.getString("color"), 16);
		else
			color = 0xffffff;

		constant = json.getFloat("constant");
		linear = json.getFloat("linear");
		quadratic = json.getFloat("quadratic");

		isFull = json.optBoolean("isFull", isFull);

		xOffset = json.optFloat("lightXOffset", 0) / 16f;
		yOffset = json.optFloat("lightYOffset", 0) / 16f;
		zOffset = json.optFloat("lightZOffset", 0) / 16f;

		dirX = json.optFloat("dirX", 0);
		dirY = json.optFloat("dirY", -1);
		dirZ = json.optFloat("dirZ", 0);
		cutOff = json.optFloat("cutOff", -60);
	}

	@Override
	public void onBlockAdded(BlockState state, World world, BlockState oldState, int x, int y, int z)
	{
		if (world.getBlock(x, y, z) != this)
			return;

		spawnLight(state, world, x, y, z);
	}

	@Override
	public void onPlayerBreak(BlockState state, World world, Player player, EnumFace breakedFrom, int x, int y, int z)
	{
		super.onPlayerBreak(state, world, player, breakedFrom, x, y, z);
		LightManager.removeLight(EnumLightSource.BLOCK, x + 0.5f + xOffset, y + 0.5f + yOffset, z + 0.5f + zOffset);
	}

	@Override
	public void spawnLight(BlockState state, World world, int x, int y, int z)
	{
		float[] col = ColorUtil.getColors(color);
		LightManager.replaceIdeal(EnumLightSource.BLOCK, x + 0.5f + xOffset, y + 0.5f + yOffset, z + 0.5f + zOffset, col[0], col[1], col[2], constant, linear, quadratic, dirX, dirY, dirZ, cutOff);
	}

	/*
		 * Must be in interval <0;15>
		 * Can update chunks only in 3x3x3 cube
		 */
/*
		System.out.println(String.format("Light from %d/%d/%d inv: %b", x, y, z, isInverted));

		if (radius > 15)
			throw new IllegalArgumentException("Radius can not be bigger than 15! (" + radius + ") Block: " + getName());


		boolean[][][] checked = new boolean[radius * 2 + 1][radius * 2 + 1][radius * 2 + 1];
		List<Node> queue = new ArrayList<>();
		List<Node> toAdd = new ArrayList<>();

		sc.setLight(x, y, z, color);
		checked[radius][radius][radius] = true;

		for (EnumFace f : EnumFace.getFaces())
		{
			int lx = radius + f.getXOffset();
			int ly = radius + f.getYOffset();
			int lz = radius + f.getZOffset();

			Block block = sc.getBlockEfficiently(lx - radius + x, ly - radius + y, lz - radius + z);
			if (block == null || !block.isFull)
				toAdd.add(new Node(lx, ly, lz, 0));
		}

		while (!toAdd.isEmpty())
		{
			queue.addAll(toAdd);
			toAdd.clear();

			for (Iterator<Node> iterator = queue.iterator(); iterator.hasNext(); )
			{
				Node i = iterator.next();
				iterator.remove();

				double distance = i.distance;

				if (!isInverted)
					sc.setLight(i.x - radius + x, i.y - radius + y, i.z - radius + z, ColorUtil.dim(color, Math.max(1 - (distance / ((double) radius)), 0)));
				else
					sc.removeLight(i.x - radius + x, i.y - radius + y, i.z - radius + z, ColorUtil.dim(color, Math.max(1 - (distance / ((double) radius)), 0)));

				Block block = sc.getBlockEfficiently(i.x - radius + x, i.y - radius + y, i.z - radius + z);
				if (block != null && block.isFull)
					continue;

				for (EnumFace f : EnumFace.getFaces())
				{
					int lx = i.x + f.getXOffset();
					int ly = i.y + f.getYOffset();
					int lz = i.z + f.getZOffset();

					if (lx < 0 || lx >= checked.length || ly < 0 || ly >= checked[lx].length || lz < 0 || lz >= checked[lx][ly].length)
						continue;

					if (checked[lx][ly][lz])
						continue;

					checked[lx][ly][lz] = true;

					toAdd.add(new Node(lx, ly, lz, i.distance + 1));
				}
			}
		}

	private class Node
	{
		public int x, y, z;
		int distance;

		Node(int x, int y, int z, int distance)
		{
			this.x = x;
			this.y = y;
			this.z = z;
			this.distance = distance;
		}
	}*/
}
