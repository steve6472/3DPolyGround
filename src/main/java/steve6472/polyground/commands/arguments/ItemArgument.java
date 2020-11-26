package steve6472.polyground.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import steve6472.polyground.commands.Command;
import steve6472.polyground.commands.CommandSource;
import steve6472.polyground.item.Item;
import steve6472.polyground.registry.Items;

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
public class ItemArgument implements ArgumentType<Item>
{
	public static ItemArgument item()
	{
		return new ItemArgument();
	}

	public static Item getItem(CommandContext<CommandSource> source, String name)
	{
		return source.getArgument(name, Item.class);
	}

	@Override
	public Item parse(StringReader reader) throws CommandSyntaxException
	{
		String name = reader.readString();
		return Items.getItemByName(name);
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder)
	{
		List<String> names = new ArrayList<>();
		for (Item b : Items.getAllItems())
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
