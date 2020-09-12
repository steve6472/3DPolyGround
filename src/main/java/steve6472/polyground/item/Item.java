package steve6472.polyground.item;

import org.json.JSONObject;
import steve6472.polyground.CaveGame;
import steve6472.polyground.EnumFace;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.model.ModelLoader;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.entity.StaticEntityModel;
import steve6472.polyground.entity.player.EnumSlot;
import steve6472.polyground.entity.player.Player;
import steve6472.polyground.registry.Blocks;
import steve6472.polyground.world.World;
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

	private Block block;

	public StaticEntityModel model;

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

		if (f != null && f.isFile())
		{
			JSONObject json = new JSONObject(ModelLoader.read(f));
			name = json.getString("name");

			if (json.has("block"))
				block = Blocks.getBlockByName(json.getString("block"));

			ItemModelLoader.loadModel(json.getString("model"), this);

			loadModel(json.getString("model"));
		}
	}

	public void loadModel(String path)
	{
		JSONObject json = new JSONObject(ModelLoader.read(new File("game/objects/models/" + path + ".json")));

		model = new StaticEntityModel();

		if (json.getString("type").equals("from_block"))
			model.load(CaveGame.getInstance().mainRender.buildHelper, Blocks.getDefaultState(json.getString("block")).getBlockModels()[0].getElements(), false);
		else if (json.getString("type").equals("from_model"))
			model.load(CaveGame.getInstance().mainRender.buildHelper, CaveGame.getInstance().modelLoader, "custom_models/" + json.getString("model_name") + ".json", true);
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
		return block;
	}

	/**
	 * Runs on item in hand
	 *  @param player Player
	 * @param slot slot
	 * @param click  Event
	 */
	public void onClick(Player player, EnumSlot slot, MouseEvent click)
	{
	}

	/**
	 * Runs on block in the world
	 * @param world World
	 * @param state State of block
	 * @param player    Player
	 * @param slot slot
	 * @param clickedOn Side the player has clicked on
	 * @param click     event
	 * @param x         x position of block
	 * @param y         y position of block
	 * @param z         z position of block
	 */
	public void onClick(World world, BlockState state, Player player, EnumSlot slot, EnumFace clickedOn, MouseEvent click, int x, int y, int z)
	{
	}

	public void onTickInItemBar(Player player)
	{
	}
}
