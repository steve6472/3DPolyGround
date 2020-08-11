package steve6472.polyground.commands.coms;

import com.mojang.brigadier.CommandDispatcher;
import org.joml.Vector2i;
import steve6472.polyground.EnumFace;
import steve6472.polyground.commands.Command;
import steve6472.polyground.commands.CommandSource;
import steve6472.polyground.commands.arguments.ChunkArgument;
import steve6472.polyground.world.chunk.Chunk;
import steve6472.polyground.world.chunk.SubChunk;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 19.08.2019
 * Project: SJP
 *
 ***********************/
public class ChunkCommand extends Command
{
	public ChunkCommand(CommandDispatcher<CommandSource> dispatcher)
	{
		super(dispatcher);
	}

	public void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(literal("chunk").then(literal("generate").then(literal("single").then(argument("chunk", ChunkArgument.chunkArgument()).executes(c ->
		{

			Vector2i pos = ChunkArgument.getChunkCoords(c, "chunk");

			c.getSource().getWorld().generateNewChunk(pos.x, pos.y);

			return 1;
		}))).then(literal("radius").then(argument("radius", integer(0)).executes(c ->
		{

			int r = getInteger(c, "radius");

			for (int x = -r; x <= r; x++)
			{
				for (int z = -r; z <= r; z++)
				{
					c.getSource().getWorld().generateNewChunk(x, z);
				}
			}

			return 1;
		}))).then(literal("here").executes(c ->
		{

			c.getSource().getWorld().generateNewChunk((int) Math.floor(c.getSource().getPlayer().getX()) >> 4, (int) Math.floor(c.getSource().getPlayer().getZ()) >> 4);

			return 1;
		}))).then(literal("delete").then(literal("single").then(argument("chunk", ChunkArgument.chunkArgument()).executes(c ->
		{

			Vector2i chunkCoords = ChunkArgument.getChunkCoords(c, "chunk");
			c.getSource().getWorld().deleteChunk(chunkCoords.x, chunkCoords.y);

			return 1;
		}))).then(literal("radius").then(argument("chunkMin", ChunkArgument.chunkArgument()).then(argument("chunkMax", ChunkArgument.chunkArgument()).executes(c ->
		{

			Vector2i min = ChunkArgument.getChunkCoords(c, "chunkMin");
			Vector2i max = ChunkArgument.getChunkCoords(c, "chunkMax");

			for (int x = min.x; x < max.x; x++)
			{
				for (int z = min.y; z < max.y; z++)
				{
					c.getSource().getWorld().deleteChunk(x, z);
				}
			}

			return 1;
		}))))).then(literal("regenerate").then(argument("chunk", ChunkArgument.chunkArgument()).executes(c ->
		{

			ChunkArgument.getChunk(c, "chunk").rebuild();

			return 1;
		})).executes(c ->
		{

			c.getSource().getWorld().getChunks().forEach(Chunk::rebuild);

			return 1;
		})).then(literal("breakall").executes(c ->
		{

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
								subChunk.getBlock(i, j, k).onPlayerBreak(subChunk.getState(i, j, k), subChunk.getWorld(), c.getSource().getPlayer(), EnumFace.NONE, i, j, k);
							}
						}
					}
				}
			}

			return 1;
		})).then(literal("dump").executes(c ->
		{
			Chunk chunk = c.getSource().getWorld().getChunk((int) Math.floor(c.getSource().getPlayer().getX()) >> 4, (int) Math.floor(c.getSource().getPlayer().getZ()) >> 4);
			for (SubChunk sc : chunk.getSubChunks())
			{
				System.out.println(sc);
//				System.out.println("--areFeaturesGeneratedForStage=" + sc.areFeaturesGeneratedForStage(sc.lastFeatureStage));
//				System.out.println("--maxRange=" + sc.maxRange);
//				System.out.println("--presentBiomes=" + sc.presentBiomes);
			}
			return 1;
		})));
	}
}
