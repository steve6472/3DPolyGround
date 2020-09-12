package steve6472.polyground.item.special;

import org.json.JSONObject;
import steve6472.polyground.CaveGame;
import steve6472.polyground.entity.player.EnumSlot;
import steve6472.polyground.entity.player.Player;
import steve6472.polyground.item.Item;
import steve6472.sge.main.KeyList;
import steve6472.sge.main.events.MouseEvent;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 06.10.2019
 * Project: SJP
 *
 ***********************/
public class RegeneratorItem extends Item
{
	public RegeneratorItem(JSONObject json, int id)
	{
		super(json, id);
	}

	@Override
	public void onClick(Player player, EnumSlot slot, MouseEvent click)
	{
		if (click.getAction() == KeyList.PRESS)
		{
			if (click.getButton() == KeyList.RMB)
			{
				CaveGame.getInstance().inGameGui.chat.addText("[#ff0000]", "Regenerator Item is no longer working");
//				CaveGame.getInstance().world.getChunks().forEach(c ->
//				{
//					for (SubChunk chunk : c.getSubChunks())
//					{
//						chunk.generate();
//					}
//				});
//
//				CaveGame.getInstance().world.getChunks().forEach(Chunk::rebuild);
			}
		}
	}
}
