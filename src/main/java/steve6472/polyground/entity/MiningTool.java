package steve6472.polyground.entity;

import org.joml.AABBf;
import steve6472.polyground.CaveGame;
import steve6472.polyground.block.model.ModelLoader;
import steve6472.polyground.entity.player.EnumHoldPosition;
import steve6472.polyground.entity.player.IHoldable;
import steve6472.polyground.entity.player.Player;
import steve6472.polyground.world.ModelBuilder;
import steve6472.polyground.world.World;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 06.09.2020
 * Project: CaveGame
 *
 ***********************/
public class MiningTool extends EntityBase implements IHoldable
{
	private static final StaticEntityModel MINING_TOOL_MODEL = new StaticEntityModel();
	private Player player;
	public static final AABBf AABB = new AABBf(-0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f);

	public static void initModel(ModelBuilder modelBuilder, ModelLoader modelLoader)
	{
		MINING_TOOL_MODEL.load(modelBuilder, modelLoader, "custom_models/mining_tool.bbmodel");
	}

	public MiningTool()
	{
	}

	@Override
	public IHoldable pickUp(World world, Player player)
	{
		this.player = player;
		world.getEntityManager().removeEntity(this);
		return this;
	}

	@Override
	public void place(World world, float x, float y, float z)
	{
		setPosition(x, y, z);
		setPivotPoint(0, 0, 0);
		world.getEntityManager().addEntity(this);
		this.player = null;
	}

	@Override
	public void render(EnumHoldPosition position)
	{
		MINING_TOOL_MODEL.render(CaveGame.getInstance().getCamera().getViewMatrix(), this, this, 1);
	}

	@Override
	public void tick(EnumHoldPosition position)
	{
		if (position != EnumHoldPosition.GROUND)
		{
			float z = position == EnumHoldPosition.HAND_LEFT ? -1f : 1f;

			setRotations(0, player.getCamera().getYaw() + 1.5707963267948966f, player.getCamera().getPitch());
			setPosition(player.getX() + 0.6f, player.getY() + 0.8f, player.getZ() + z);
			setPivotPoint(-0.6f, 0.8f, -z);
		}
	}

	@Override
	public AABBf getHitbox()
	{
		return AABB;
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
}
