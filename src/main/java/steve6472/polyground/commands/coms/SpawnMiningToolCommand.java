package steve6472.polyground.commands.coms;

import com.mojang.brigadier.CommandDispatcher;
import steve6472.polyground.commands.Command;
import steve6472.polyground.commands.CommandSource;
import steve6472.polyground.entity.MiningTool;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 06.09.2020
 * Project: CaveGame
 *
 ***********************/
public class SpawnMiningToolCommand extends Command
{
	public SpawnMiningToolCommand(CommandDispatcher<CommandSource> dispatcher)
	{
		super(dispatcher);
	}

	@Override
	protected void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(literal("mining_tool").executes(c -> {

			MiningTool p = new MiningTool();
			p.setPosition(c.getSource().getPlayer().getPosition());
			c.getSource().getWorld().getEntityManager().addEntity(p);

			return 1;
		}));
	}
}
