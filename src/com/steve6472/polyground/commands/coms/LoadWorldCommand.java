package com.steve6472.polyground.commands.coms;

import com.mojang.brigadier.CommandDispatcher;
import com.steve6472.polyground.commands.Command;
import com.steve6472.polyground.commands.CommandSource;
import com.steve6472.polyground.world.Saver;

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
		dispatcher.register(
			literal("loadworld")
				.then(
					argument("name", string())
						.executes(c -> {

							Saver.loadWorld(c.getSource().getWorld(), getString(c, "name"));

							return 1;
						})
				)
		);
	}
}
