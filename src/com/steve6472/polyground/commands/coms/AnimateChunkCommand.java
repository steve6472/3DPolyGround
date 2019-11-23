package com.steve6472.polyground.commands.coms;

import com.mojang.brigadier.CommandDispatcher;
import com.steve6472.polyground.commands.Command;
import com.steve6472.polyground.commands.CommandSource;
import com.steve6472.polyground.commands.arguments.ChunkArgument;
import com.steve6472.polyground.world.chunk.SubChunk;

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
				.executes(c -> {

					c.getSource().getWorld().getChunks().forEach(ch ->
					{
						for (SubChunk subChunk : ch.getSubChunks())
						{
							subChunk.renderTime = 0;
						}
					});

					return 1;
				})
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
