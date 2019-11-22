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
							float speed = getFloat(c, "speed");

							c.getSource().getPlayer().flySpeed = speed;

							c.getSource().sendFeedback("Speed set to ", "[#55FF55]", speed);

							return 1;
						})
				)
		);
	}
}
