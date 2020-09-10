package steve6472.polyground.commands.coms;

import com.mojang.brigadier.CommandDispatcher;
import steve6472.polyground.commands.Command;
import steve6472.polyground.commands.CommandSource;
import steve6472.polyground.entity.player.EnumGameMode;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 10.09.2020
 * Project: CaveGame
 *
 ***********************/
public class GamemodeCommand extends Command
{
	public GamemodeCommand(CommandDispatcher<CommandSource> dispatcher)
	{
		super(dispatcher);
	}

	@Override
	protected void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(literal("gmc").executes(c ->
		{
			c.getSource().getPlayer().gamemode = EnumGameMode.CREATIVE;
			return 0;
		}));
		dispatcher.register(literal("gms").executes(c ->
		{
			c.getSource().getPlayer().gamemode = EnumGameMode.SURVIVAL;
			return 0;
		}));
	}
}
