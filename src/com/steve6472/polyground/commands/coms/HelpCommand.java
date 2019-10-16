package com.steve6472.polyground.commands.coms;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.CommandNode;
import com.steve6472.polyground.commands.Command;
import com.steve6472.polyground.commands.CommandSource;

import java.util.Map;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 19.08.2019
 * Project: SJP
 *
 ***********************/
public class HelpCommand extends Command
{
	public HelpCommand(CommandDispatcher<CommandSource> dispatcher)
	{
		super(dispatcher);
	}

	public void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(
			literal("help")
				.executes(c -> {

					Map<CommandNode<CommandSource>, String> smartUsage = dispatcher.getSmartUsage(dispatcher.getRoot(), c.getSource());
					smartUsage.values().forEach(System.out::println);

					return 1;
				})
		);
	}
}
