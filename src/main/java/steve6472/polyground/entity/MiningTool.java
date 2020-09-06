package steve6472.polyground.entity;

import steve6472.polyground.block.model.ModelLoader;
import steve6472.polyground.entity.interfaces.IRenderable;
import steve6472.polyground.entity.interfaces.ITickable;
import steve6472.polyground.world.ModelBuilder;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 06.09.2020
 * Project: CaveGame
 *
 ***********************/
public class MiningTool extends EntityBase implements IRenderable, ITickable
{
	private final StaticEntityModel model;
	private final Player player;

	public MiningTool(Player player)
	{
		this.player = player;
		model = new StaticEntityModel();
		setPivotPoint(-0.6f, 0.8f, -1f);
	}

	public void loadModel(ModelBuilder modelBuilder, ModelLoader modelLoader)
	{
		model.load(modelBuilder, modelLoader, "custom_models/mining_tool.bbmodel");
	}

	@Override
	public boolean isDead()
	{
		return false;
	}

	@Override
	public String getName()
	{
		return "mining_tool";
	}

	@Override
	public void tick()
	{
		setRotations(0, player.getCamera().getYaw() + 1.5707963267948966f, player.getCamera().getPitch());
		setPosition(player.getX() + 0.6f, player.getY() + 0.8f, player.getZ() + 1f);
	}

	@Override
	public void render()
	{
		model.render(player.getCamera().getViewMatrix(), this, this, 1);
	}
}
