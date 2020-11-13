package steve6472.polyground.block.special;

import org.joml.AABBf;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.json.JSONObject;
import steve6472.polyground.CaveGame;
import steve6472.polyground.EnumFace;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.blockdata.BlockData;
import steve6472.polyground.block.blockdata.IBlockData;
import steve6472.polyground.block.blockdata.KnappingData;
import steve6472.polyground.block.model.CubeHitbox;
import steve6472.polyground.block.model.elements.Bakery;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.entity.item.ItemEntity;
import steve6472.polyground.entity.player.Player;
import steve6472.polyground.knapping.Recipe;
import steve6472.polyground.world.ModelBuilder;
import steve6472.polyground.world.World;
import steve6472.polyground.world.chunk.ModelLayer;
import steve6472.sge.main.KeyList;
import steve6472.sge.main.events.MouseEvent;
import steve6472.sge.main.util.ColorUtil;
import steve6472.sge.main.util.RandomUtil;

import java.util.ArrayList;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 26.09.2020
 * Project: CaveGame
 *
 ***********************/
public class KnappingBlock extends CustomBlock implements IBlockData
{
	public KnappingBlock(JSONObject json)
	{
		super(json);
	}

	@Override
	public BlockData createNewBlockEntity(BlockState state)
	{
		return new KnappingData();
	}

	@Override
	public void onClick(BlockState state, World world, Player player, EnumFace clickedOn, MouseEvent click, int x, int y, int z)
	{
		if (click.getButton() == KeyList.LMB && click.getAction() == KeyList.PRESS && player.heldItem != null && player.heldItem.item.name.startsWith("hammerstone"))
		{
			player.processNextBlockBreak = false;

			if (RandomUtil.decide(30))
			{
				world.getEntityManager().removeEntity(player.heldItem);
				player.heldItem = null;
				return;
			}

			KnappingData data = (KnappingData) world.getData(x, y, z);
			if (data == null)
				return;

			Vector2i c = getLookedAtPiece(world, player, x, y, z);
			if (c != null)
			{
				data.stone[c.x + c.y * 16] = false;
				data.pieceCount--;
			}

			if (data.pieceCount == 0)
			{
				world.setBlock(Block.AIR, x, y, z);
			}

			Recipe match = CaveGame.getInstance().knappingRecipes.getMatch(data.stone);
			if (match != null)
			{
				world.setBlock(Block.AIR, x, y, z);
				ItemEntity e = new ItemEntity(match.getResult(), null, x + 0.5f, y + 0.25f, z + 0.5f);
				world.getEntityManager().addEntity(e);
			}

			world.getSubChunkFromBlockCoords(x, y, z).rebuild();
		}
	}

	private Vector2i getLookedAtPiece(World world, Player player, int x, int y, int z)
	{
		KnappingData data = (KnappingData) world.getData(x, y, z);
		if (data == null)
			return null;

		AABBf box = new AABBf();
		Vector2f res = new Vector2f();

		int cx = 0, cy = 0;
		float closestDistance = 10f;

		for (int i = 0; i < 16; i++)
		{
			for (int j = 0; j < 16; j++)
			{
				if (data.stone[i + j * 16])
				{
					box.setMin(x + i * 1f / 16f, y, z + j * 1f / 16f);
					box.setMax(x + i * 1f / 16f + 1f / 16f, y + 1f / 16f, z + j * 1f / 16f + 1f / 16f);

					if (box.intersectsRay(player.getCamera().getX(), player.getCamera().getY(), player.getCamera().getZ(), player.viewDir.x, player.viewDir.y, player.viewDir.z, res))
					{
						if (res.x < closestDistance)
						{
							closestDistance = res.x;
							cx = i;
							cy = j;
						}
					}
				}
			}
		}

		if (closestDistance == 10f)
			return null;

		return new Vector2i(cx, cy);
	}

	@Override
	public int createModel(int x, int y, int z, World world, BlockState state, ModelBuilder buildHelper, ModelLayer modelLayer)
	{
		if (modelLayer != ModelLayer.NORMAL)
			return 0;

		int tris = 0;
		buildHelper.setSubChunk(world.getSubChunkFromBlockCoords(x, y, z));

		KnappingData data = (KnappingData) world.getData(x, y, z);

		for (int i = 0; i < 16; i++)
		{
			for (int j = 0; j < 16; j++)
			{
				if (data.stone[i + j * 16])
				{
					int flags = Bakery.createFaceFlags(
						i != 15 && data.stone[(i + 1) + j * 16],
						j != 15 && data.stone[i + (j + 1) * 16],
						i != 0 && data.stone[(i - 1) + j * 16],
						j != 0 && data.stone[i + (j - 1) * 16],
						false,
						true
					);

					int a = (int) (hash(world.getSeed(), x * ((i << 4) + 1) - j, y, z * -((j << 5) + 1) + i) % 32) + 20;
					tris += Bakery.coloredCube_1x1(i, 0, j, ColorUtil.getColor(a, a, a), flags);
				}
			}
		}

		return tris;
	}

	private long hash(long seed, int x, int y, int z)
	{
		long h = seed + x * 668265263L + y * 2147483647L + z * 374761393L;
		h = (h ^ (h >> 14)) * 1274126177L;
		return h ^ (h >> 16);
	}

	@Override
	public CubeHitbox[] getHitbox(World world, BlockState state, int x, int y, int z)
	{
		KnappingData data = (KnappingData) world.getData(x, y, z);
		if (data == null)
			return new CubeHitbox[0];

		AABBf box = new AABBf();
		AABBf box_ = new AABBf();

		for (int i = 0; i < 16; i++)
		{
			for (int j = 0; j < 16; j++)
			{
				if (data.stone[i + j * 16])
				{
					box_.setMin(i * 1f / 16f, 0, j * 1f / 16f);
					box_.setMax(i * 1f / 16f + 1f / 16f, 1f / 16f, j * 1f / 16f + 1f / 16f);
					box.union(box_);
				}
			}
		}

		Player player = CaveGame.getInstance().getPlayer();
//		if (box.intersectsRay(player.getCamera().getX(), player.getCamera().getY(), player.getCamera().getZ(), player.viewDir.x, player.viewDir.y, player.viewDir.z))
		{
			Vector2i c = getLookedAtPiece(world, CaveGame.getInstance().getPlayer(), x, y, z);

			if (c != null)
			{
				List<CubeHitbox> hitboxes = new ArrayList<>();
				hitboxes.add(new CubeHitbox(box));
				hitboxes.add(new CubeHitbox(new AABBf(c.x * 1f / 16f, 0f, c.y * 1f / 16f, c.x * 1f / 16f + 1f / 16f, 1f / 16f, c.y * 1f / 16f + 1f / 16f)));

				Recipe r = CaveGame.getInstance().knappingRecipes.getRecipes().get(0);

				for (int i = 0; i < 16; i++)
				{
					for (int j = 0; j < 16; j++)
					{
						if (!r.getPattern()[i][j] && data.stone[i + j * 16])
						{
							hitboxes.add(new CubeHitbox(new AABBf(i * 1f / 16f, 0f, j * 1f / 16f, i * 1f / 16f + 1f / 16f, 1f / 16f, j * 1f / 16f + 1f / 16f)));
						}
					}
				}

				return hitboxes.toArray(new CubeHitbox[0]);
			}
		}
		return new CubeHitbox[] {new CubeHitbox(box)};
	}
}
