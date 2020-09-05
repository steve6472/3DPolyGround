package steve6472.polyground.commands.coms;

import com.mojang.brigadier.CommandDispatcher;
import steve6472.polyground.commands.Command;
import steve6472.polyground.commands.CommandSource;
import steve6472.polyground.gfx.light.EnumLightSource;
import steve6472.polyground.gfx.light.LightManager;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 05.09.2020
 * Project: CaveGame
 *
 ***********************/
public class LightCommand extends Command
{
	public LightCommand(CommandDispatcher<CommandSource> dispatcher)
	{
		super(dispatcher);
	}

	@Override
	protected void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(literal("light")
		.then(literal("c").then(argument("cutOff", floatArg()).executes(c -> {

			LightManager.lights.stream().filter(p -> p.getSource() != EnumLightSource.SKY).forEach(l -> l.setSpotlight(l.getSpotlight().x, l.getSpotlight().y, l.getSpotlight().z, getFloat(c, "cutOff")));

			return 0;
		}))));
	}
}
