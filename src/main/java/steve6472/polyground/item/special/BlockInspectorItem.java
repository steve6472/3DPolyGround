package steve6472.polyground.item.special;

import steve6472.polyground.CaveGame;
import steve6472.polyground.EnumFace;
import steve6472.polyground.block.properties.IProperty;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.entity.Player;
import steve6472.polyground.events.InGameGuiEvent;
import steve6472.polyground.gui.floatingdialogs.BlockStateDialog;
import steve6472.polyground.item.Item;
import steve6472.polyground.world.chunk.SubChunk;
import steve6472.sge.gfx.font.Font;
import steve6472.sge.gui.floatingdialog.FloatingDialog;
import steve6472.sge.main.KeyList;
import steve6472.sge.main.events.Event;
import steve6472.sge.main.events.MouseEvent;

import java.io.File;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 06.10.2019
 * Project: SJP
 *
 ***********************/
public class BlockInspectorItem extends Item
{
	public BlockInspectorItem(File f, int id)
	{
		super(f, id);
	}

	@Override
	public void onClick(SubChunk subChunk, BlockState state, Player player, EnumFace clickedOn, MouseEvent click, int x, int y, int z)
	{
		if (click.getButton() == KeyList.RMB && click.getAction() == KeyList.PRESS)
		{
			FloatingDialog dialog = new BlockStateDialog(subChunk.getWorld().getGame());
			dialog.setPosition(x + .5f + clickedOn.getXOffset(), y + .5f + clickedOn.getYOffset(), z + .5f + clickedOn.getZOffset());
			CaveGame.getInstance().mainRender.dialogManager.addDialog(dialog);
		}
	}

	@Event
	public void renderBlock(InGameGuiEvent.PostRender e)
	{
		if (CaveGame.itemInHand == this && CaveGame.getInstance().hitPicker.hit)
		{
			StringBuilder text = new StringBuilder();
			BlockState state = CaveGame.getInstance().hitPicker.getHitResult().getState();
			text.append("Block: ").append(state.getBlock().getName()).append("\n");

			if (state.getProperties() != null)
			{
				for (IProperty<?> property : state.getProperties().keySet())
				{
					text.append(property.getName()).append(" = ").append(state.getProperties().get(property)).append("\n");
				}

				Font.render(text.toString(), 5, CaveGame.getInstance().getHeight() - 10 - 10 * (state.getProperties() == null ? 0 : state.getProperties().size()));
			} else
			{
				Font.render(text.toString(), 5, CaveGame.getInstance().getHeight() - 15);
			}
		}
	}
}
