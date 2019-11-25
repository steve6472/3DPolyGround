package com.steve6472.polyground.commands.coms;

import com.mojang.brigadier.CommandDispatcher;
import com.steve6472.polyground.commands.Command;
import com.steve6472.polyground.commands.CommandSource;
import com.steve6472.polyground.commands.arguments.ModelLayerArgument;
import com.steve6472.polyground.world.World;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 02.09.2019
 * Project: SJP
 *
 ***********************/
public class ModelLayerCommand extends Command
{
	public ModelLayerCommand(CommandDispatcher<CommandSource> dispatcher)
	{
		super(dispatcher);
	}

	@Override
	protected void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(
			literal("modellayer")
				.then(
					argument("modelLayer", ModelLayerArgument.modelLayer())
						.executes(c -> {

							World.enabled = ModelLayerArgument.getModelLayer(c, "modelLayer").ordinal();

							return 1;
						})
				)
			.executes(c -> {

				World.enabled = -1;

				return 1;
			})
		);
	}
}
