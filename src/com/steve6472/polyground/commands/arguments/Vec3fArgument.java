package com.steve6472.polyground.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.steve6472.polyground.commands.CommandSource;
import org.joml.Vector3f;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 22.08.2019
 * Project: SJP
 *
 ***********************/
public class Vec3fArgument implements ArgumentType<Vector3f>
{
	public static Vec3fArgument vec3()
	{
		return new Vec3fArgument();
	}

	public static Vector3f getCoords(CommandContext<CommandSource> source, String name)
	{
		return source.getArgument(name, Vector3f.class);
	}

	@Override
	public Vector3f parse(StringReader reader) throws CommandSyntaxException
	{
		float x = reader.readFloat();
		reader.skipWhitespace();
		float y = reader.readFloat();
		reader.skipWhitespace();
		float z = reader.readFloat();
		return new Vector3f(x, y, z);
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder)
	{
		return Suggestions.empty();
	}

	@Override
	public Collection<String> getExamples()
	{
		return null;
	}
}
