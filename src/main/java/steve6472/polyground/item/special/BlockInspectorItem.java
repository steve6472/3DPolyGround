package steve6472.polyground.item.special;

import org.json.JSONObject;
import steve6472.polyground.CaveGame;
import steve6472.polyground.EnumFace;
import steve6472.polyground.block.properties.IProperty;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.entity.player.EnumSlot;
import steve6472.polyground.entity.player.Player;
import steve6472.polyground.events.InGameGuiEvent;
import steve6472.polyground.gui.floatingdialogs.BlockStateDialog;
import steve6472.polyground.item.Item;
import steve6472.polyground.world.World;
import steve6472.sge.gfx.font.Font;
import steve6472.sge.gui.floatingdialog.FloatingDialog;
import steve6472.sge.main.KeyList;
import steve6472.sge.main.events.Event;
import steve6472.sge.main.events.MouseEvent;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 06.10.2019
 * Project: SJP
 *
 ***********************/
public class BlockInspectorItem extends Item
{
	public BlockInspectorItem(JSONObject json, int id)
	{
		super(json, id);
	}

	@Override
	public void onClick(World world, BlockState state, Player player, EnumSlot slot, EnumFace clickedOn, MouseEvent click, int x, int y, int z)
	{
		if (click.getButton() == KeyList.RMB && click.getAction() == KeyList.PRESS)
		{
			FloatingDialog dialog = new BlockStateDialog(world.getGame());
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

			int yAdd = 0;

			if (state.getProperties() != null)
			{
				text.append("\nStates:\n");
				for (IProperty<?> property : state.getProperties().keySet())
				{
					text.append(property.getName()).append(" = ").append(state.getProperties().get(property)).append("\n");
				}
				yAdd += state.getProperties().size() + 2;
			}

			if (!state.getTags().isEmpty())
			{
				text.append("\nTags:\n");
				for (String tag : state.getTags())
				{
					text.append(tag).append("\n");
				}
				yAdd += state.getTags().size() + 2;
			}

			Font.render(text.toString(), 5, CaveGame.getInstance().getHeight() - 10 - 10 * yAdd);
		}
	}
}
