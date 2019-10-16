package com.steve6472.polyground.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.steve6472.polyground.block.Block;
import com.steve6472.polyground.block.registry.BlockRegistry;
import com.steve6472.polyground.commands.Command;
import com.steve6472.polyground.commands.CommandSource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 22.08.2019
 * Project: SJP
 *
 ***********************/
public class BlockArgument implements ArgumentType<Block>
{
	public static BlockArgument block()
	{
		return new BlockArgument();
	}

	public static Block getBlock(CommandContext<CommandSource> source, String name)
	{
		return source.getArgument(name, Block.class);
	}

	@Override
	public Block parse(StringReader reader) throws CommandSyntaxException
	{
		String name = reader.readString();
		return BlockRegistry.getBlockByName(name);
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder)
	{
		List<String> names = new ArrayList<>();
		for (Block b : BlockRegistry.getAllBlocks())
		{
			names.add(b.getName());
		}
		return Command.suggest(builder, names);
	}

	@Override
	public Collection<String> getExamples()
	{
		return null;
	}
}
