package steve6472.polyground.commands.coms;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import steve6472.polyground.commands.Command;
import steve6472.polyground.commands.CommandSource;
import steve6472.polyground.commands.arguments.EntityArgument;
import steve6472.polyground.entity.EntityBase;
import steve6472.polyground.entity.registry.EntityRegistry;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 04.09.2019
 * Project: SJP
 *
 ***********************/
public class SpawnCommand extends Command
{
	public SpawnCommand(CommandDispatcher<CommandSource> dispatcher)
	{
		super(dispatcher);
	}

	@Override
	protected void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(literal("spawn").then(argument("entity", EntityArgument.entity()).executes(c ->
		{

			spawn(c);

			return 1;
		}).then(argument("count", integer(0)).executes(c ->
		{

			for (int i = 0; i < getInteger(c, "count"); i++)
			{
				spawn(c);
			}

			return 1;
		}))));
	}

	private void spawn(CommandContext<CommandSource> source)
	{
		EntityBase e = EntityRegistry.createEntity(EntityArgument.getEntityName(source, "entity"));
		e.addPosition(source.getSource().getPlayer().getPosition());

		source.getSource().getWorld().addEntity(e);
	}
}
