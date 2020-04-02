package steve6472.polyground.commands.coms;

import com.mojang.brigadier.CommandDispatcher;
import steve6472.polyground.block.model.Cube;
import steve6472.polyground.registry.BlockRegistry;
import steve6472.polyground.commands.Command;
import steve6472.polyground.commands.CommandSource;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 19.08.2019
 * Project: SJP
 *
 ***********************/
public class BlocksCommand extends Command
{
	public BlocksCommand(CommandDispatcher<CommandSource> dispatcher)
	{
		super(dispatcher);
	}

	public void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(literal("blocks").executes(c ->
		{

			BlockRegistry.getAllBlocks().forEach(b ->
			{
				System.out.println(b.getName() + " " + b.getId());

				if (b.getBlockModel() != null)
				{
					if (b.getCubes() != null)
					{
						for (Cube u : b.getCubes())
						{
							System.out.println(u);
						}
						System.out.println();
					}
				}
			});

			return 1;
		}));
	}
}
