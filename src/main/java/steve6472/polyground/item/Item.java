package steve6472.polyground.item;

import org.json.JSONArray;
import org.json.JSONObject;
import steve6472.polyground.CaveGame;
import steve6472.polyground.EnumFace;
import steve6472.polyground.block.model.IElement;
import steve6472.polyground.block.model.ModelLoader;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.entity.StaticEntityModel;
import steve6472.polyground.entity.player.EnumSlot;
import steve6472.polyground.entity.player.Player;
import steve6472.polyground.registry.Blocks;
import steve6472.polyground.world.World;
import steve6472.sge.main.events.MouseEvent;
import steve6472.sge.main.util.Pair;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 23.09.2019
 * Project: SJP
 *
 ***********************/
public class Item
{
	private static List<Pair<IElement[], Consumer<IElement[]>>> modelQueue = new ArrayList<>();

	public static void runQueue()
	{
		modelQueue.forEach(c -> c.getB().accept(c.getA()));
	}

	public static Item air;

	private final int id;
	public final String name;

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

	public Item(String name)
	{
		this.id = 0;
		this.name = name;
	}

	public Item(JSONObject json, int id)
	{
		this.id = id;

		name = json.getString("name");

		if (json.has("groups"))
		{
			JSONArray groups = json.getJSONArray("groups");
			for (int i = 0; i < groups.length(); i++)
			{
				CaveGame.getInstance().itemGroups.addItem(this, groups.getString(i));
			}
		}

		JSONObject js = new JSONObject(ModelLoader.read(new File("game/objects/models/" + json.getString("model") + ".json")));

		IElement[] elements = null;
		if (js.getString("type").equals("from_model"))
		{
			elements = CaveGame.getInstance().modelLoader.loadElements(ModelLoader.load("custom_models/items/" + js.getString("model_name") + ".bbmodel", true), 0, 0, 0, false);
		}

		modelQueue.add(new Pair<>(elements, (el) -> loadModel(js, el)));
	}

	public void loadModel(JSONObject json, IElement[] modelElements)
	{
		model = new StaticEntityModel();

		if (json.getString("type").equals("from_block"))
			model.load(CaveGame.getInstance().mainRender.buildHelper, Blocks.getDefaultState(json.getString("block")).getBlockModels()[0].getElements(), false);
		else if (json.getString("type").equals("from_model"))
			model.load(CaveGame.getInstance().mainRender.buildHelper, modelElements, true);
	}

	public String getName()
	{
		return name;
	}

	public int getId()
	{
		return id;
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
