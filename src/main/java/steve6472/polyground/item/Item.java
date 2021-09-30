package steve6472.polyground.item;

import org.json.JSONArray;
import org.json.JSONObject;
import steve6472.polyground.CaveGame;
import steve6472.polyground.MouseClick;
import steve6472.polyground.block.model.IElement;
import steve6472.polyground.block.model.ModelLoader;
import steve6472.polyground.entity.item.ItemEntity;
import steve6472.polyground.entity.player.Player;
import steve6472.polyground.gfx.StaticEntityModel;
import steve6472.polyground.world.World;
import steve6472.sge.main.util.Pair;

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
	private static final List<Pair<IElement[], Consumer<IElement[]>>> modelQueue = new ArrayList<>();

	public static void runQueue()
	{
		modelQueue.forEach(c -> c.b().accept(c.a()));
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
				CaveGame.getInstance().itemGroups.add(this, groups.getString(i));
			}
		} else
		{
			CaveGame.getInstance().itemGroups.addUngrouped(this);
		}

		IElement[] elements;
		elements = ModelLoader.loadElements(ModelLoader.load("game/objects/models/item/" + json.getString("model_name") + ".bbmodel", true), 0, 0, 0);

		modelQueue.add(new Pair<>(elements, this::loadModel));
	}

	public void loadModel(IElement[] modelElements)
	{
		model = new StaticEntityModel();

		model.load(CaveGame.getInstance().mainRender.buildHelper, modelElements, true);
	}

	public String name()
	{
		return name;
	}

	public int getId()
	{
		return id;
	}

	/**
	 * Runs on item in hand
	 * @param player Player
	 * @param click  Event
	 */
	public void onClick(Player player, MouseClick click)
	{
	}

	/**
	 * Runs on block in the world
	 * @param world World
	 * @param player Player
	 * @param click event
	 */
	public void onClick(World world, Player player, MouseClick click)
	{
	}

	public boolean canBePlaced()
	{
		return true;
	}

	public void onPickup(World world, Player player, ItemEntity entity)
	{

	}

	public void onPlace(World world, Player player, ItemEntity entity)
	{

	}

	public void tickInHand(Player player, ItemEntity entity)
	{

	}

	public void tickOnGround(ItemEntity entity)
	{

	}
}
