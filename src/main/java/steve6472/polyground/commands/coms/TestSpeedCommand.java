package steve6472.polyground.commands.coms;

import com.mojang.brigadier.CommandDispatcher;
import steve6472.polyground.commands.Command;
import steve6472.polyground.commands.CommandSource;
import steve6472.polyground.entity.parkour.ParkourTest;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 19.08.2019
 * Project: SJP
 *
 ***********************/
public class TestSpeedCommand extends Command
{
	public TestSpeedCommand(CommandDispatcher<CommandSource> dispatcher)
	{
		super(dispatcher);
	}

	public void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(literal("ts")
		.then(argument("speed", integer(0)).executes(c -> {

			ParkourTest.TEST_SPEED = getInteger(c, "speed");

			return 1;
		})));
	}
}
