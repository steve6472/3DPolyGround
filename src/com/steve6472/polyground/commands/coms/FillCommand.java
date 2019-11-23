package com.steve6472.polyground.commands.coms;

import com.mojang.brigadier.CommandDispatcher;
import com.steve6472.polyground.commands.Command;
import com.steve6472.polyground.commands.CommandSource;
import com.steve6472.polyground.commands.arguments.BlockArgument;
import com.steve6472.polyground.commands.arguments.Vec3Argument;
import com.steve6472.polyground.world.chunk.Chunk;
import com.steve6472.polyground.world.World;
import org.joml.Vector3i;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 23.08.2019
 * Project: SJP
 *
 ***********************/
public class FillCommand extends Command
{
	public FillCommand(CommandDispatcher<CommandSource> dispatcher)
	{
		super(dispatcher);
	}

	@Override
	protected void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(
			literal("fill")
				.then(
					argument("min", Vec3Argument.vec3())
						.then(
							argument("max", Vec3Argument.vec3())
								.then(
									argument("block", BlockArgument.block())
										.executes(c -> {

											World w = c.getSource().getWorld();
											Vector3i min = Vec3Argument.getCoords(c, "min");
											Vector3i max = Vec3Argument.getCoords(c, "max");
											int id = BlockArgument.getBlock(c, "block").getId();

											for (int x = min.x; x <= max.x; x++)
											{
												for (int y = min.y; y <= max.y; y++)
												{
													for (int z = min.z; z <= max.z; z++)
													{
														w.setBlock(x, y, z, id, false);
													}
												}
											}

											for (int x = min.x >> 4; x <= max.x >> 4; x++)
											{
												for (int z = min.z >> 4; z <= max.z >> 4; z++)
												{
													Chunk chunk = w.getChunk(x, z);
													if (chunk != null)
														chunk.update();
												}
											}

											return 1;
										})
								)
						)
				)
		);
	}
}
