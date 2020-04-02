package steve6472.polyground.commands.coms;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import steve6472.polyground.commands.Command;
import steve6472.polyground.commands.CommandSource;
import steve6472.polyground.entity.AIEntity;
import steve6472.polyground.entity.EntityBase;
import steve6472.polyground.entity.registry.EntityRegistry;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 04.09.2019
 * Project: SJP
 *
 ***********************/
public class AICommand extends Command
{
	public AICommand(CommandDispatcher<CommandSource> dispatcher)
	{
		super(dispatcher);
	}

	@Override
	protected void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(literal("ai").executes(c ->
		{

			spawn(c);

			return 1;
		}));
	}

	private void spawn(CommandContext<CommandSource> source)
	{
		EntityBase e = EntityRegistry.createEntity("ai");
		e.addPosition(22, 1, 19);
		((AIEntity) e).calcPath(new AIEntity.Node(17, 1, -15));

		source.getSource().getWorld().addEntity(e);
	}
}
