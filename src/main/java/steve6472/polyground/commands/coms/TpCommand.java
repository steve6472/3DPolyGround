package steve6472.polyground.commands.coms;

import com.mojang.brigadier.CommandDispatcher;
import steve6472.polyground.commands.Command;
import steve6472.polyground.commands.CommandSource;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 19.08.2019
 * Project: SJP
 *
 ***********************/
public class TpCommand extends Command
{
	public TpCommand(CommandDispatcher<CommandSource> dispatcher)
	{
		super(dispatcher);
	}

	public void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(literal("tp").then(argument("x", floatArg()).then(argument("y", floatArg()).then(argument("z", floatArg()).executes(c ->
		{

			c.getSource().getPlayer().setPosition(getFloat(c, "x"), getFloat(c, "y"), getFloat(c, "z"));
			c.getSource().getPlayer().setMotion(0, 0, 0);

			return 1;
		})))));
	}
}
