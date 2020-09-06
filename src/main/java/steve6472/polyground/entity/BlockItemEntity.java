package steve6472.polyground.entity;

import org.joml.Vector3f;
import steve6472.polyground.CaveGame;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.model.BlockModel;
import steve6472.polyground.entity.interfaces.IKillable;
import steve6472.polyground.entity.interfaces.IRenderable;
import steve6472.polyground.entity.interfaces.ITickable;
import steve6472.polyground.entity.interfaces.IWorldContainer;
import steve6472.polyground.world.World;
import steve6472.sge.main.Util;

import java.util.function.Function;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 06.09.2020
 * Project: CaveGame
 *
 ***********************/
public class BlockItemEntity extends EntityBase implements IRenderable, ITickable, IKillable, IWorldContainer
{
	public final DynamicEntityModel model;
	private final Block blockType;
	private World world;
	private double timeAlive;
	private boolean forceDead = false;

	public BlockItemEntity(Block blockType, BlockModel blockModel, int x, int y, int z)
	{
		super();
		this.blockType = blockType;
		model = new DynamicEntityModel();
		model.load(blockModel.getElements());
		setPosition(x, y, z);
		setPivotPoint(0.5f, 0f, 0.5f);
	}

	@Override
	public boolean isDead()
	{
		return calculateSize(timeAlive) <= 0 || forceDead;
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
		timeAlive += 1f / 60d;
		if (timeAlive >= 1.2)
		{
			if (world.getState((int) getX(), (int) getY(), (int) getZ()).getBlock() != Block.air)
			{
				addPosition(0, 1f / 60f, 0);
			}
			if (world.getState((int) getX(), (int) (getY() - 0.1f), (int) getZ()).getBlock() == Block.air)
			{
				addPosition(0, -1f / 60f, 0);
			}

			if (getY() < 0)
				setY(0);

			Player player = CaveGame.getInstance().getPlayer();

			if (player.palette == null)
				return;

			if (new Vector3f(getPosition()).add(0.5f, 1f / 32f, 0.5f).distance(player.getPosition()) <= 5 && player.palette.canBeAdded(blockType))
			{
				Vector3f dir = new Vector3f(player.getPosition()).sub(getPosition()).sub(0.5f, 1f / 32f, 0.5f).normalize().mul(Math.min((float) ((timeAlive - 1.2) * (timeAlive - 1.2)) / 20f, 0.1f));
				addPosition(dir);
				if (new Vector3f(getPosition()).add(0.5f, 1f / 32f, 0.5f).distance(player.getPosition()) <= 0.1f)
				{
					setDead(player.palette.addItem(blockType, model));
				}
			}
		}
	}

	private float calculateSize(double y)
	{
		if (y >= 0.95)
			return 1 / 16f;
		Function<Double, Double> f1 = Math::cos;
		Function<Double, Double> f2 = x -> Math.sin((x + 1.4) * 5) * 0.4;
		Function<Double, Double> f4 = x -> 1 - smoothstep(1.1, 0, f1.apply(x));
		Function<Double, Double> f5 = x -> x * 1.83;
		Function<Double, Double> f6 = x -> mix(f2.apply(f5.apply(x)), 1, f4.apply(f5.apply(x)));
		return f6.apply(y).floatValue();
	}

	@Override
	public void render()
	{
		model.render(CaveGame.getInstance().getCamera().getViewMatrix(), this, this, calculateSize(timeAlive));
	}

	private double mix(double x, double y, double a)
	{
		return x * (1d - a) + y * a;
	}

	private double smoothstep(double edge0, double edge1, double x)
	{
		double t = Util.clamp(0.0, 1.0, (x - edge0) / (edge1 - edge0));
		return t * t * (3.0 - 2.0 * t);
	}

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
