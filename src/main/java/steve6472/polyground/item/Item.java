package steve6472.polyground.item;

import steve6472.polyground.EnumFace;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.registry.BlockRegistry;
import steve6472.polyground.entity.Player;
import steve6472.polyground.world.chunk.SubChunk;
import steve6472.SSS;
import steve6472.sge.main.events.MouseEvent;

import java.io.File;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 23.09.2019
 * Project: SJP
 *
 ***********************/
public class Item
{
	public static Item air;

	private final int id;

	public String name;

	private Block toPlace;

	public static Item createAir()
	{
		return air = new Item();
	}

	private Item()
	{
		this.id = 0;
		this.name = "air";
	}

	public Item(File f, int id)
	{
		this.id = id;
		name = f.getName().substring(0, f.getName().length() - 4);

		if (f.isFile())
		{
			SSS sss = new SSS(f);

			if (sss.containsName("place"))
				toPlace = BlockRegistry.getBlockByName(sss.getString("place"));

			ItemModelLoader.loadModel(sss.getString("model"), this);
		}
	}

	public String getName()
	{
		return name;
	}

	public int getId()
	{
		return id;
	}

	public Block getBlockToPlace()
	{
		return toPlace;
	}

	/**
	 * Runs on item in hand
	 *
	 * @param player Player
	 * @param click  Event
	 */
	public void onClick(Player player, MouseEvent click)
	{
	}

	/**
	 * Runs on block in the world
	 *  @param subChunk  Sub Chunk
	 * @param state Data of block
	 * @param player    Player
	 * @param clickedOn Side the player has clicked on
	 * @param click     event
	 * @param x         x position of block
	 * @param y         y position of block
	 * @param z         z position of block
	 */
	public void onClick(SubChunk subChunk, BlockState state, Player player, EnumFace clickedOn, MouseEvent click, int x, int y, int z)
	{
	}

	public void onTickInItemBar(Player player)
	{
	}
}