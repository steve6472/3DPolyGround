package steve6472.polyground.item.special;

import steve6472.polyground.CaveGame;
import steve6472.polyground.entity.Player;
import steve6472.polyground.item.Item;
import steve6472.polyground.world.chunk.Chunk;
import steve6472.polyground.world.chunk.SubChunk;
import steve6472.sge.main.KeyList;
import steve6472.sge.main.events.MouseEvent;

import java.io.File;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 06.10.2019
 * Project: SJP
 *
 ***********************/
public class RegeneratorItem extends Item
{
	public RegeneratorItem(File f, int id)
	{
		super(f, id);
	}

	@Override
	public void onClick(Player player, MouseEvent click)
	{
		if (click.getAction() == KeyList.PRESS)
		{
			if (click.getButton() == KeyList.RMB)
			{
				CaveGame.getInstance().world.getChunks().forEach(c ->
				{
					for (SubChunk chunk : c.getSubChunks())
					{
						chunk.generate();
					}
				});

				CaveGame.getInstance().world.getChunks().forEach(Chunk::update);
			}
		}
	}
}
