package steve6472.polyground.commands.coms;

import com.mojang.brigadier.CommandDispatcher;
import steve6472.polyground.CaveGame;
import steve6472.polyground.commands.Command;
import steve6472.polyground.commands.CommandSource;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 25.11.2019
 * Project: SJP
 *
 ***********************/
public class ParticleCommand extends Command
{
	public ParticleCommand(CommandDispatcher<CommandSource> dispatcher)
	{
		super(dispatcher);
	}

	@Override
	protected void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(literal("particle").then(literal("clear").executes(c ->
		{

			CaveGame.getInstance().mainRender.particles.getMap().forEach((k, v) -> v.forEach(p -> p.forcedDeath = true));

			return 1;
		})));
	}
}
