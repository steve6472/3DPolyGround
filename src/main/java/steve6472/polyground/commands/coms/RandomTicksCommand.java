package steve6472.polyground.commands.coms;

import com.mojang.brigadier.CommandDispatcher;
import steve6472.polyground.commands.Command;
import steve6472.polyground.commands.CommandSource;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 13.08.2020
 * Project: CaveGame
 *
 ***********************/
public class RandomTicksCommand extends Command
{
	public RandomTicksCommand(CommandDispatcher<CommandSource> dispatcher)
	{
		super(dispatcher);
	}

	@Override
	protected void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(literal("rt").then(argument("rt", integer(0)).executes(c -> c.getSource().getWorld().getGame().options.randomTicks = getInteger(c, "rt"))));
	}
}
