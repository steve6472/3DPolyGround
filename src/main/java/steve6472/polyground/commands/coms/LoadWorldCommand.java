package steve6472.polyground.commands.coms;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import steve6472.polyground.commands.Command;
import steve6472.polyground.commands.CommandSource;
import steve6472.polyground.world.WorldSerializer;

import java.io.IOException;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 08.09.2019
 * Project: SJP
 *
 ***********************/
public class LoadWorldCommand extends Command
{
	public LoadWorldCommand(CommandDispatcher<CommandSource> dispatcher)
	{
		super(dispatcher);
	}

	@Override
	protected void register(CommandDispatcher<CommandSource> dispatcher)
	{
		LiteralCommandNode<CommandSource> node = dispatcher.register(literal("loadworld").then(argument("name", string()).executes(c ->
		{
			c.getSource().getWorld().worldName = getString(c, "name");

			try
			{
				WorldSerializer.deserialize(c.getSource().getWorld());
			} catch (IOException e)
			{
				e.printStackTrace();
			}

			return 1;
		})));

		dispatcher.register(literal("lw").redirect(node));
	}
}
