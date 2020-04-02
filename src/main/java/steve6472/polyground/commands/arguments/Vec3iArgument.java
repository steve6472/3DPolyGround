package steve6472.polyground.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import steve6472.polyground.commands.Command;
import steve6472.polyground.commands.CommandSource;
import steve6472.polyground.entity.Player;
import org.joml.Vector3i;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 22.08.2019
 * Project: SJP
 *
 ***********************/
public class Vec3iArgument implements ArgumentType<Vector3i>
{
	public static Vec3iArgument vec3()
	{
		return new Vec3iArgument();
	}

	public static Vector3i getCoords(CommandContext<CommandSource> source, String name)
	{
		return source.getArgument(name, Vector3i.class);
	}

	//TODO: Return block
	//	public static Chunk getChunk(CommandContext<CommandSource> source, String name)
	//	{
	//		Vector2i v = source.getArgument(name, Vector2i.class);
	//		return source.getSource().getWorld().getChunk(v.x, v.y);
	//	}

	@Override
	public Vector3i parse(StringReader reader) throws CommandSyntaxException
	{
		int x = reader.readInt();
		reader.skipWhitespace();
		int y = reader.readInt();
		reader.skipWhitespace();
		int z = reader.readInt();
		return new Vector3i(x, y, z);
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder)
	{
		Player p = ((CommandSource) context.getSource()).getPlayer();
		return Command.suggest(builder, (int) Math.floor(p.getX()), (int) Math.floor(p.getY()), (int) Math.floor(p.getZ()));
	}

	@Override
	public Collection<String> getExamples()
	{
		return null;
	}
}
