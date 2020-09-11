package steve6472.polyground.entity;

import steve6472.polyground.block.model.ModelLoader;
import steve6472.polyground.entity.player.Player;
import steve6472.polyground.world.ModelBuilder;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 06.09.2020
 * Project: CaveGame
 *
 ***********************/
public class Backpack extends Palette
{
	private static final StaticEntityModel BACKPACK_MODEL = new StaticEntityModel();

	public Backpack(Player player)
	{
		super(null);
	}

	@Override
	public StaticEntityModel getModel()
	{
		return BACKPACK_MODEL;
	}

	public static void initModel(ModelBuilder modelBuilder, ModelLoader modelLoader)
	{
		BACKPACK_MODEL.load(modelBuilder, modelLoader, "custom_models/backpack.bbmodel");
	}

	@Override
	public String getName()
	{
		return "backpack";
	}
}
