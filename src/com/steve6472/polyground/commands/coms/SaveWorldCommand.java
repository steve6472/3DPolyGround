package com.steve6472.polyground.commands.coms;

import com.mojang.brigadier.CommandDispatcher;
import com.steve6472.polyground.commands.Command;
import com.steve6472.polyground.commands.CommandSource;
import com.steve6472.polyground.world.WorldSerializer;

import java.io.IOException;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 08.09.2019
 * Project: SJP
 *
 ***********************/
public class SaveWorldCommand extends Command
{
	public SaveWorldCommand(CommandDispatcher<CommandSource> dispatcher)
	{
		super(dispatcher);
	}

	@Override
	protected void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(
			literal("saveworld")
				.then(
					argument("name", string())
						.executes(c -> {

							c.getSource().getWorld().worldName = getString(c, "name");

							try
							{
								WorldSerializer.serialize(c.getSource().getWorld());
							} catch (IOException e)
							{
								System.err.println("Failed to save the world!");
								e.printStackTrace();
							}

							return 1;
						})
				)
		);
	}
}
