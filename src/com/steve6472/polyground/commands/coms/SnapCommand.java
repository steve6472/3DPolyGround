package com.steve6472.polyground.commands.coms;

import com.mojang.brigadier.CommandDispatcher;
import com.steve6472.polyground.EnumFace;
import com.steve6472.polyground.block.Block;
import com.steve6472.polyground.block.registry.BlockRegistry;
import com.steve6472.polyground.block.special.SnapButtonBlock;
import com.steve6472.polyground.block.special.WorldButtonBlock;
import com.steve6472.polyground.commands.Command;
import com.steve6472.polyground.commands.CommandSource;
import com.steve6472.polyground.world.Chunk;
import com.steve6472.polyground.world.SubChunk;
import com.steve6472.sge.main.util.RandomUtil;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 02.09.2019
 * Project: SJP
 *
 ***********************/
public class SnapCommand extends Command
{
	public SnapCommand(CommandDispatcher<CommandSource> dispatcher)
	{
		super(dispatcher);
	}

	@Override
	protected void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(
			literal("snap")
				.executes(c -> {

					for (Chunk chunk : c.getSource().getWorld().getChunks())
					{
						for (SubChunk subChunk : chunk.getSubChunks())
						{
							for (int i = 0; i < 16; i++)
							{
								for (int j = 0; j < 16; j++)
								{
									for (int k = 0; k < 16; k++)
									{
										if (RandomUtil.flipACoin())
										{
											int id = subChunk.getIds()[i][j][k];
											Block b = BlockRegistry.getBlockById(id);
											if (b instanceof WorldButtonBlock || b instanceof SnapButtonBlock) continue;

											b.onBreak(subChunk, subChunk.getBlockData(i, j, k), c.getSource().getPlayer(), EnumFace.NONE, i, j, k);
											subChunk.getIds()[i][j][k] = Block.air.getId();
										}
									}
								}
							}
							subChunk.rebuild();
						}
					}

					return 1;
				})
		);
	}
}
