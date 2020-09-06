package steve6472.polyground.commands.coms;

import com.mojang.brigadier.CommandDispatcher;
import steve6472.polyground.Palette;
import steve6472.polyground.commands.Command;
import steve6472.polyground.commands.CommandSource;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 06.09.2020
 * Project: CaveGame
 *
 ***********************/
public class SpawnPaletteCommand extends Command
{
	public SpawnPaletteCommand(CommandDispatcher<CommandSource> dispatcher)
	{
		super(dispatcher);
	}

	@Override
	protected void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(literal("palette").executes(c -> {

			Palette p = new Palette(null);
			p.setPosition(c.getSource().getPlayer().getPosition());
			c.getSource().getWorld().getEntityManager().addEntity(p);

			return 1;
		}));
	}
}
