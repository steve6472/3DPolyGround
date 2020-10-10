package steve6472.polyground.commands.coms;

import com.mojang.brigadier.CommandDispatcher;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.BlockAtlas;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.commands.Command;
import steve6472.polyground.commands.CommandSource;
import steve6472.polyground.registry.Blocks;
import steve6472.sge.main.util.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 19.08.2019
 * Project: SJP
 *
 ***********************/
public class DumpCommand extends Command
{
	public DumpCommand(CommandDispatcher<CommandSource> dispatcher)
	{
		super(dispatcher);
	}

	public void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(literal("dump").then(literal("blocks").executes(c ->
		{
			for (Block b : Blocks.getAllBlocks())
			{
				System.out.println(b.getName());
				for (BlockState possibleState : b.getDefaultState().getPossibleStates())
				{
					System.out.println("\t" + possibleState);
				}
			}

			return 1;
		}))
				.then(literal("texture")
					.then(literal("sort")
						.then(literal("id")
							.executes(c ->
							{
								List<Pair<Integer, String>> list = new ArrayList<>();
								BlockAtlas.getUsedTexturesReference().forEach((k, i) -> list.add(new Pair<>(i, k)));

								list.sort(Comparator.comparingInt(Pair::getA));
								list.forEach((p) -> System.out.println(p.getA() + " -> " + p.getB()));

								return 1;
							})
						)
						.then(literal("name")
							.executes(c ->
							{
								List<Pair<Integer, String>> list = new ArrayList<>();
								BlockAtlas.getUsedTexturesReference().forEach((k, i) -> list.add(new Pair<>(i, k)));

								list.sort(Comparator.comparing(Pair::getB));
								list.forEach((p) -> System.out.println(p.getB() + " -> " + p.getA()));

								return 1;
							})
						)
					).executes(c ->
					{
						BlockAtlas.getUsedTexturesReference().forEach((k, i) -> System.out.println(i + " -> " + k));
						return 1;
					})
			)
		);
	}
}
