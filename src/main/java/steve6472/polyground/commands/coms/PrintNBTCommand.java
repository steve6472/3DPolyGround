package steve6472.polyground.commands.coms;

import com.mojang.brigadier.CommandDispatcher;
import net.querz.nbt.io.SNBTUtil;
import org.joml.Vector3i;
import steve6472.polyground.block.blockdata.BlockData;
import steve6472.polyground.commands.Command;
import steve6472.polyground.commands.CommandSource;
import steve6472.polyground.commands.arguments.Vec3iArgument;
import steve6472.polyground.world.World;

import java.io.IOException;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 18.10.2020
 * Project: CaveGame
 *
 ***********************/
public class PrintNBTCommand extends Command
{
	public PrintNBTCommand(CommandDispatcher<CommandSource> dispatcher)
	{
		super(dispatcher);
	}

	@Override
	protected void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(literal("nbt").then(argument("position", Vec3iArgument.vec3()).executes(c ->
		{
			Vector3i position = Vec3iArgument.getCoords(c, "position");
			World world = c.getSource().getWorld();
			BlockData data = world.getData(position.x, position.y, position.z);
			if (data == null)
			{
				c.getSource().sendFeedback("[#FF5555]", "This block does not hold any NBT data!");
				return 0;
			}

			try
			{
				c.getSource().sendFeedback("[#aaaaaa]", data.getClass().getCanonicalName());
				String snbt = SNBTUtil.toSNBT(data.write());
				c.getSource().sendFeedback(snbt);
				System.out.println(snbt);
			} catch (IOException e)
			{
				c.getSource().sendFeedback("#FF1111", "NBT -> SNBT Failed");
				e.printStackTrace();
				return 0;
			}
			return 1;
		})));
	}
}
