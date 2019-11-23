package com.steve6472.polyground.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.steve6472.polyground.entity.Player;
import com.steve6472.polyground.commands.Command;
import com.steve6472.polyground.commands.CommandSource;
import com.steve6472.polyground.world.chunk.Chunk;
import org.joml.Vector2i;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 22.08.2019
 * Project: SJP
 *
 ***********************/
public class ChunkArgument implements ArgumentType<Vector2i>
{
	public static ChunkArgument chunkArgument()
	{
		return new ChunkArgument();
	}

	public static Vector2i getChunkCoords(CommandContext<CommandSource> source, String name)
	{
		return source.getArgument(name, Vector2i.class);
	}

	public static Chunk getChunk(CommandContext<CommandSource> source, String name)
	{
		Vector2i v = source.getArgument(name, Vector2i.class);
		return source.getSource().getWorld().getChunk(v.x, v.y);
	}

	@Override
	public Vector2i parse(StringReader reader) throws CommandSyntaxException
	{
		int x = reader.readInt();
		reader.skipWhitespace();
		int z = reader.readInt();
		return new Vector2i(x, z);
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder)
	{
		Player p = ((CommandSource) context.getSource()).getPlayer();
		return Command.suggest(builder, (int) Math.floor(p.getX()) >> 4, (int) Math.floor(p.getZ()) >> 4);
	}

	@Override
	public Collection<String> getExamples()
	{
		return null;
	}
}
