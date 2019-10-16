package com.steve6472.polyground.commands.coms;

import com.mojang.brigadier.CommandDispatcher;
import com.steve6472.polyground.commands.Command;
import com.steve6472.polyground.commands.CommandSource;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 08.09.2019
 * Project: SJP
 *
 ***********************/
public class SetWorldName extends Command
{
	public SetWorldName(CommandDispatcher<CommandSource> dispatcher)
	{
		super(dispatcher);
	}

	@Override
	protected void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(
			literal("setworldname")
				.then(
					argument("name", string())
						.executes(c -> {

							c.getSource().getWorld().clearWorld();

							c.getSource().getWorld().worldName = getString(c, "name");

							return 1;
						})
				).executes(c -> {

					c.getSource().getWorld().worldName = null;
					return 1;
			})
		);
	}
}
