package steve6472.polyground.block.special;

import org.joml.Math;
import org.json.JSONObject;
import steve6472.polyground.EnumFace;
import steve6472.polyground.block.blockdata.BlockData;
import steve6472.polyground.block.blockdata.IBlockData;
import steve6472.polyground.block.blockdata.PaintBucketData;
import steve6472.polyground.block.model.elements.Bakery;
import steve6472.polyground.block.properties.enums.EnumFlowerColor;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.entity.player.EnumGameMode;
import steve6472.polyground.entity.player.Player;
import steve6472.polyground.item.itemdata.BrushData;
import steve6472.polyground.world.ModelBuilder;
import steve6472.polyground.world.World;
import steve6472.polyground.world.chunk.ModelLayer;
import steve6472.sge.main.KeyList;
import steve6472.sge.main.events.MouseEvent;
import steve6472.sge.main.util.ColorUtil;
import steve6472.sge.main.util.MathUtil;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 26.09.2020
 * Project: CaveGame
 *
 ***********************/
public class PaintBucketBlock extends CustomBlock implements IBlockData
{
	public PaintBucketBlock(JSONObject json)
	{
		super(json);
	}

	@Override
	public BlockData createNewBlockEntity(BlockState state)
	{
		return new PaintBucketData();
	}

	@Override
	public void onClick(BlockState state, World world, Player player, EnumFace clickedOn, MouseEvent click, int x, int y, int z)
	{
		if (click.getButton() == KeyList.RMB && click.getAction() == KeyList.PRESS)
		{
			PaintBucketData data = (PaintBucketData) world.getData(x, y, z);
			if (data == null)
				return;

			player.processNextBlockPlace = false;

			if (player.heldItem != null)
			{
				if (player.heldItem.item.name().equals("brush"))
				{
					if (player.heldItem.itemData instanceof BrushData bd)
					{
						bd.color = ColorUtil.getColor(data.red, data.green, data.blue, 1);
					}
					player.processNextBlockPlace = false;
					return;
				}

				boolean foundColorMatch = false;
				for (EnumFlowerColor fc : EnumFlowerColor.getValues())
				{
					if (player.heldItem.item.name().toUpperCase().startsWith(fc.name().toUpperCase()) && player.heldItem.item.name().contains("powder"))
					{
						foundColorMatch = true;
						break;
					}
				}

				if (!foundColorMatch)
				{
					return;
				}

				EnumFlowerColor c = EnumFlowerColor.valueOf(player.heldItem.item.name().split("_")[0].toUpperCase());

				float r = c.r / 255f / 10f;
				float g = c.g / 255f / 10f;
				float b = c.b / 255f / 10f;

				if (c == EnumFlowerColor.BLACK)
				{
					r = -0.1f;
					g = -0.1f;
					b = -0.1f;
				}

				data.red = MathUtil.clamp(0, 1, data.red + r);
				data.green = MathUtil.clamp(0, 1, data.green + g);
				data.blue = MathUtil.clamp(0, 1, data.blue + b);

				if (player.gamemode != EnumGameMode.CREATIVE)
				{
					world.getEntityManager().removeEntity(player.heldItem);
					player.heldItem = null;
				}

				world.getSubChunkFromBlockCoords(x, y, z).rebuild();
			}
		}
	}

	@Override
	public boolean isPickable(BlockState state, World world, int x, int y, int z, Player player)
	{
		return true;
	}

	@Override
	public int createModel(int x, int y, int z, World world, BlockState state, ModelBuilder buildHelper, ModelLayer modelLayer)
	{
		if (modelLayer != ModelLayer.NORMAL)
			return 0;

		PaintBucketData data = (PaintBucketData) world.getData(x, y, z);
		int c = ColorUtil.getColor(data.red, data.green, data.blue, 1);

		int tris = 0;
		tris += CustomBlock.model(x, y, z, world, state, buildHelper, modelLayer);

		for (int i = 4; i < 12; i++)
		{
			for (int j = 4; j < 12; j++)
			{
				double a = i - 7.5;
				double b = j - 7.5;
				if (Math.sqrt(a * a + b * b) < 4)
				{
					tris += Bakery.coloredCube_1x1(i, 8, j, c, 47);
				}
			}
		}

		return tris;
	}
}
