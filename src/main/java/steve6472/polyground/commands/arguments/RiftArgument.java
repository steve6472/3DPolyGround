package steve6472.polyground.commands.arguments;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import steve6472.polyground.CaveGame;
import steve6472.polyground.commands.Command;
import steve6472.polyground.commands.CommandSource;
import steve6472.polyground.rift.Rift;

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
public class RiftArgument implements ArgumentType<Rift>
{
	private static final DynamicCommandExceptionType RIFT_NOT_FOUND = new DynamicCommandExceptionType(name -> new LiteralMessage("Rift '" + name + "' not found"));

	public static RiftArgument rift()
	{
		return new RiftArgument();
	}

	public static Rift getRift(CommandContext<CommandSource> source, String name)
	{
		return source.getArgument(name, Rift.class);
	}

	@Override
	public Rift parse(StringReader reader) throws CommandSyntaxException
	{
		String name = reader.readString();
		Rift r = CaveGame.getInstance().getRifts().getRift(name);
		if (r == null)
			throw RIFT_NOT_FOUND.create(name);
		return r;
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder)
	{
		List<String> names = new ArrayList<>();
		for (Rift rift : CaveGame.getInstance().getRifts().getRifts())
		{
			names.add(rift.getName());
		}
		return Command.suggest(builder, names);
	}

	@Override
	public Collection<String> getExamples()
	{
		return null;
	}
}
