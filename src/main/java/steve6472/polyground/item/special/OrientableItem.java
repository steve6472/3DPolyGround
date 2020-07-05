package steve6472.polyground.item.special;

import steve6472.SSS;
import steve6472.polyground.BasicEvents;
import steve6472.polyground.CaveGame;
import steve6472.polyground.EnumFace;
import steve6472.polyground.HitResult;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.entity.Player;
import steve6472.polyground.item.Item;
import steve6472.polyground.registry.BlockRegistry;
import steve6472.polyground.world.chunk.SubChunk;
import steve6472.sge.main.KeyList;
import steve6472.sge.main.events.MouseEvent;

import java.io.File;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 01.07.2020
 * Project: CaveGame
 *
 ***********************/
public class OrientableItem extends Item
{
	private Block north, east, south, west;

	public OrientableItem(File f, int id)
	{
		super(f, id);
		if (f.isFile())
		{
			SSS sss = new SSS(f);

			try
			{
				north = BlockRegistry.getBlockByName(sss.getString("north"));
				east = BlockRegistry.getBlockByName(sss.getString("east"));
				south = BlockRegistry.getBlockByName(sss.getString("south"));
				west = BlockRegistry.getBlockByName(sss.getString("west"));
			} catch (Exception ex)
			{
				System.err.println(getName());
				ex.printStackTrace();
				System.exit(1);
			}
		}
	}

	@Override
	public void onClick(SubChunk subChunk, BlockState state, Player player, EnumFace clickedOn, MouseEvent click, int x, int y, int z)
	{
		if (!player.processNextBlockPlace)
			return;

		if (!(click.getAction() == KeyList.PRESS && click.getButton() == KeyList.RMB))
			return;

		HitResult hitResult = CaveGame.getInstance().hitPicker.getHitResult();

		//TODO: Change to radians
		double yaw = Math.toDegrees(player.getCamera().getYaw());
		if (yaw < 45 || yaw > 315)
			BasicEvents.place(east, hitResult.getFace(), player);
		else if (yaw < 315 && yaw > 225)
			BasicEvents.place(south, hitResult.getFace(), player);
		else if (yaw < 225 && yaw > 135)
			BasicEvents.place(west, hitResult.getFace(), player);
		else
			BasicEvents.place(north, hitResult.getFace(), player);

		player.processNextBlockPlace = false;
	}
}
