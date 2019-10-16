package com.steve6472.polyground.commands.coms;

import com.mojang.brigadier.CommandDispatcher;
import com.steve6472.polyground.commands.Command;
import com.steve6472.polyground.commands.CommandSource;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 19.08.2019
 * Project: SJP
 *
 ***********************/
public class SpeedCommand extends Command
{
	public SpeedCommand(CommandDispatcher<CommandSource> dispatcher)
	{
		super(dispatcher);
	}

	public void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(
			literal("speed")
				.then(
					argument("speed", floatArg())
						.executes(c ->
						{
							c.getSource().getPlayer().flySpeed = getFloat(c, "speed");

							return 1;
						})
				)
		);
	}
}
