package com.steve6472.polyground.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.steve6472.polyground.commands.Command;
import com.steve6472.polyground.commands.CommandSource;
import com.steve6472.polyground.world.chunk.ModelLayer;

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
public class ModelLayerArgument implements ArgumentType<ModelLayer>
{
	public static ModelLayerArgument modelLayer()
	{
		return new ModelLayerArgument();
	}

	public static ModelLayer getModelLayer(CommandContext<CommandSource> source, String name)
	{
		return source.getArgument(name, ModelLayer.class);
	}

	@Override
	public ModelLayer parse(StringReader reader) throws CommandSyntaxException
	{
		String name = reader.readString();
		return ModelLayer.valueOf(name);
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder)
	{
		List<String> names = new ArrayList<>();
		for (ModelLayer b : ModelLayer.values())
		{
			names.add(b.name());
		}
		return Command.suggest(builder, names);
	}

	@Override
	public Collection<String> getExamples()
	{
		return null;
	}
}
