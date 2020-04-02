package steve6472.polyground.commands.coms;

import com.mojang.brigadier.CommandDispatcher;
import steve6472.polyground.CaveGame;
import steve6472.polyground.commands.Command;
import steve6472.polyground.commands.CommandSource;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 23.08.2019
 * Project: SJP
 *
 ***********************/
public class SensitivityCommand extends Command
{
	public SensitivityCommand(CommandDispatcher<CommandSource> dispatcher)
	{
		super(dispatcher);
	}

	@Override
	protected void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(literal("sensitivity").then(argument("sensitivity", floatArg()).executes(c ->
		{

			CaveGame.getInstance().options.mouseSensitivity = getFloat(c, "sensitivity");

			return 0;
		})));
	}
}
