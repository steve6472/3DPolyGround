package steve6472.polyground.commands.coms;

import com.mojang.brigadier.CommandDispatcher;
import steve6472.polyground.commands.Command;
import steve6472.polyground.commands.CommandSource;
import steve6472.polyground.teleporter.Teleporter;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 17.05.2020
 * Project: CaveGame
 *
 ***********************/
public class TeleCommand extends Command
{
	public TeleCommand(CommandDispatcher<CommandSource> dispatcher)
	{
		super(dispatcher);
	}

	@Override
	protected void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(literal("tele").then(literal("print").executes(c ->
		{
			for (Teleporter t : c.getSource().getWorld().teleporters.getTeleporters())
			{
				System.out.printf("Uuid: %s, Other: %s, AABB: %s\n", t.uuid, t.getOther().getUuid(), t.getAabb());
			}
			return 1;
		})));
	}
}
