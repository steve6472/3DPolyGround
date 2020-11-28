package steve6472.polyground.block.special;

import org.joml.Vector4i;
import org.json.JSONObject;
import steve6472.polyground.EnumFace;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.blockdata.BlockData;
import steve6472.polyground.block.blockdata.ChiselBlockData;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.entity.player.Player;
import steve6472.polyground.item.itemdata.BrushData;
import steve6472.polyground.world.World;
import steve6472.sge.main.KeyList;
import steve6472.sge.main.events.MouseEvent;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 26.09.2020
 * Project: CaveGame
 *
 ***********************/
public class ChiselBlock extends AbstractMicroBlock
{
	public ChiselBlock(JSONObject json)
	{
		super(json);
	}

	@Override
	public BlockData createNewBlockEntity(BlockState state)
	{
		return new ChiselBlockData();
	}

	@Override
	public void onClick(BlockState state, World world, Player player, EnumFace clickedOn, MouseEvent click, int x, int y, int z)
	{
		if (click.getButton() == KeyList.RMB && click.getAction() == KeyList.PRESS)
		{
			ChiselBlockData data = (ChiselBlockData) world.getData(x, y, z);
			if (data == null)
				return;

			Vector4i c = getLookedAtPiece(world, player, x, y, z);

			if (c != null && player.holdsItem())
			{
				if (player.getItemDataInHand() instanceof BrushData bd)
				{
					int cx = c.x;
					int cy = c.y;
					int cz = c.z;
					data.grid[cy][cx + cz * 16] = bd.color;

					data.updateModel();
					world.getSubChunkFromBlockCoords(x, y, z).rebuild();
					return;

				}
			}
		}

		if (click.getButton() != KeyList.MMB && click.getAction() == KeyList.PRESS)
		{
			ChiselBlockData data = (ChiselBlockData) world.getData(x, y, z);
			if (data == null)
				return;

			Vector4i c = getLookedAtPiece(world, player, x, y, z);

			if (c != null && player.holdsItem() && player.getItemInHand().name().equals("chisel_tool"))
			{
				if (click.getButton() == KeyList.LMB)
				{
					data.grid[c.y][c.x + c.z * 16] = 0;
					data.pieceCount--;
					player.processNextBlockBreak = false;
				} else
				{
					EnumFace f = EnumFace.getFaces()[c.w];
					int cx = c.x + f.getXOffset();
					int cy = c.y + f.getYOffset();
					int cz = c.z + f.getZOffset();
					if (cx >= 0 && cx < 16 && cy >= 0 && cy < 16 && cz >= 0 && cz < 16)
					{
						data.grid[cy][cx + cz * 16] = 0x303030;
						data.pieceCount++;
					}
					player.processNextBlockPlace = false;
				}

				if (data.pieceCount == 0)
				{
					world.setBlock(Block.AIR, x, y, z);
					world.getSubChunkFromBlockCoords(x, y, z).rebuild();
					return;
				}

				data.updateModel();
				world.getSubChunkFromBlockCoords(x, y, z).rebuild();
			}
		}
	}

	@Override
	public boolean isPickable(BlockState state, Player player)
	{
		return !player.holdsItem() || !player.getItemInHand().name().equals("chisel_tool");
	}
}
