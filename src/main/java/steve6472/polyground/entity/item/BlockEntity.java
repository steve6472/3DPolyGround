package steve6472.polyground.entity.item;

import steve6472.polyground.CaveGame;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.entity.DynamicEntityModel;
import steve6472.polyground.entity.EntityBase;
import steve6472.polyground.entity.interfaces.IKillable;
import steve6472.polyground.entity.interfaces.IRenderable;
import steve6472.polyground.entity.interfaces.ITickable;
import steve6472.polyground.entity.interfaces.IWorldContainer;
import steve6472.polyground.entity.player.Player;
import steve6472.polyground.world.World;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 06.09.2020
 * Project: CaveGame
 *
 ***********************/
public class BlockEntity extends EntityBase implements IRenderable, ITickable, IKillable, IWorldContainer
{
	public BlockState state;
	private World world;
	private boolean forceDead = false;
	private final Player player;

	public BlockEntity(Player player, BlockState state, int x, int y, int z)
	{
		super();
		this.player = player;
		this.state = state;
		setPosition(x + 0.5f, y + 0.5f, z + 0.5f);
		setPivotPoint(.5f, .5f, .5f);
	}

	@Override
	public boolean isDead()
	{
		return forceDead;
	}

	@Override
	public void setDead(boolean dead)
	{
		this.forceDead = dead;
	}

	@Override
	public String getName()
	{
		return "item";
	}

	@Override
	public void tick()
	{
		setRotations(0, player.getCamera().getYaw() + 1.5707963267948966f, player.getCamera().getPitch());
		setPosition(player.getPosition());
	}

	@Override
	public void render()
	{
		DynamicEntityModel.QUAT.identity().rotateXYZ(getRotations().x, getRotations().y, getRotations().z);

		DynamicEntityModel.MAT.identity()
			.translate(player.getX(), player.getY() + player.eyeHeight, player.getZ())
			.rotate(DynamicEntityModel.QUAT)
			.translate(1.2f, -0.5f, -0.9f)
			.scale(0.75f)
			.translate(-getPivotPoint().x, -getPivotPoint().y, -getPivotPoint().z)
		;

		state.getBlockModel(world, 0, 0, 0).getModel().render(CaveGame.getInstance().getCamera().getViewMatrix(), DynamicEntityModel.MAT);
	}

	/*
	private float calculateSize(double y)
	{
		if (y >= 0.95)
			return 1 / 16f;
		Function<Double, Double> f1 = Math::cos;
		Function<Double, Double> f2 = x -> Math.sin((x + 1.4) * 5) * 0.4;
		Function<Double, Double> f4 = x -> 1 - smoothstep(f1.apply(x));
		Function<Double, Double> f5 = x -> x * 1.83;
		Function<Double, Double> f6 = x -> mix(f2.apply(f5.apply(x)), f4.apply(f5.apply(x)));
		return f6.apply(y).floatValue();
	}

	private double mix(double x, double a)
	{
		return x * (1d - a) + a;
	}

	private double smoothstep(double x)
	{
		double t = Util.clamp(0.0, 1.0, (x - 1.1) / -1.1);
		return t * t * (3.0 - 2.0 * t);
	}*/

	@Override
	public void setWorld(World world)
	{
		this.world = world;
	}

	@Override
	public World getWorld()
	{
		return world;
	}
}
