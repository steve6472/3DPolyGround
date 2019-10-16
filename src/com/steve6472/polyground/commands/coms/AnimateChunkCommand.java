package com.steve6472.polyground.commands.coms;

import com.mojang.brigadier.CommandDispatcher;
import com.steve6472.polyground.commands.Command;
import com.steve6472.polyground.commands.CommandSource;
import com.steve6472.polyground.commands.arguments.ChunkArgument;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 19.08.2019
 * Project: SJP
 *
 ***********************/
public class AnimateChunkCommand extends Command
{
	public AnimateChunkCommand(CommandDispatcher<CommandSource> dispatcher)
	{
		super(dispatcher);
	}

	public void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(
			literal("animatechunk")
				.then(argument("chunk", ChunkArgument.chunkArgument())
					.then(argument("layer", integer(0, 4))
						.executes(c -> {

							ChunkArgument.getChunk(c, "chunk").getSubChunk(getInteger(c, "layer")).renderTime = 0f;

							return 1;
						})
					)
				)
		);
	}
}
