package steve6472.polyground.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import steve6472.polyground.CaveGame;
import steve6472.polyground.commands.Command;
import steve6472.polyground.commands.CommandSource;
import steve6472.polyground.teleporter.Teleporter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 24.07.2020
 * Project: CaveGame
 *
 ***********************/
public class TeleporterArgument implements ArgumentType<Teleporter>
{
	public static TeleporterArgument teleporter()
	{
		return new TeleporterArgument();
	}

	public static Teleporter getTeleporter(CommandContext<CommandSource> source, String name)
	{
		return source.getArgument(name, Teleporter.class);
	}

	@Override
	public Teleporter parse(StringReader reader) throws CommandSyntaxException
	{
		String name = reader.readString();
		for (Teleporter t : CaveGame.getInstance().getWorld().teleporters.getTeleporters())
		{
			if (t.getName().equals(name))
				return t;
		}
		return null;
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder)
	{
		List<String> names = new ArrayList<>();
		for (Teleporter t : CaveGame.getInstance().getWorld().teleporters.getTeleporters())
		{
			names.add(t.getName());
		}

		return Command.suggest(builder, names);
	}
}
