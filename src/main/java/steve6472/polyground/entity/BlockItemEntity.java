package steve6472.polyground.entity;

import org.joml.AABBf;
import org.joml.Vector3f;
import steve6472.polyground.CaveGame;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.model.BlockModel;
import steve6472.polyground.block.model.CubeHitbox;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.entity.interfaces.IKillable;
import steve6472.polyground.entity.interfaces.IRenderable;
import steve6472.polyground.entity.interfaces.ITickable;
import steve6472.polyground.entity.interfaces.IWorldContainer;
import steve6472.polyground.entity.player.EnumSlot;
import steve6472.polyground.entity.player.Player;
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
	private static final AABBf HITBOX = new AABBf(-1f / 19f, -1f / 19f, -1f / 19f, 1f / 19f, 1f / 19f, 1f / 19f);

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
		setPosition(x + 0.5f, y + 0.5f, z + 0.5f);
		setPivotPoint(.5f, .5f, .5f);
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

	private static final AABBf test0 = new AABBf(), test1 = new AABBf();

	private static void move(AABBf aabb, float x, float y, float z)
	{
		aabb.minX += x; aabb.maxX += x;
		aabb.minY += y; aabb.maxY += y;
		aabb.minZ += z; aabb.maxZ += z;
	}

	private void setupTest()
	{
		move(test1.set(HITBOX), getX(), getY(), getZ());
	}

	private boolean testPosition(float x, float y, float z, boolean runCollision)
	{
		for (int i = -1; i < 2; i++)
		{
			for (int j = -1; j < 2; j++)
			{
				for (int k = -1; k < 2; k++)
				{
					int X = (int) Math.floor(x + i), Y = (int) Math.floor(y + j), Z = (int) Math.floor(z + k);
					BlockState state = world.getState(X, Y, Z);
					BlockModel model = state.getBlockModel(world, X, Y, Z);
					if (model != null && model.getCubes() != null)
					{
						for (CubeHitbox cube : model.getCubes())
						{
							if (cube.isCollisionBox())
							{
								move(test0.set(cube.getAabb()), X, Y, Z);
								if (test0.intersectsAABB(test1))
								{
									if (runCollision)
										state.getBlock().entityCollision(this, world, state, X, Y, Z);
//									MainRender.t.add(new AABBf(test0));
									return false;
								}
							}
						}
					}
				}
			}
		}

		return true;
	}

	@Override
	public void tick()
	{
		timeAlive += 1f / 60d;

		if (timeAlive >= 1.2f)
		{
			float fallSpeed = 1f / 60f;

			setupTest();

			if (testPosition(getX(), getY() - 1f / 16f, getZ(), false))
			{
				addPosition(0, -fallSpeed, 0);
			}
			if (!testPosition(getX(), getY(), getZ(), true))
			{
				setPosition(getX(), test0.maxY + 1f / 19f, getZ());
			}

			if (getY() < 0)
				setY(0);

			Player player = CaveGame.getInstance().getPlayer();

			for (EnumSlot s : EnumSlot.getSlots())
			{
				if (player.holdedItems.get(s) instanceof Palette p)
				{
					if (tryToAdd(player, p))
						break;
				}
			}
			addRotations(0.017453292519943295f, 0.017453292519943295f, 0.017453292519943295f);
		}
	}

	private boolean tryToAdd(Player player, Palette p)
	{
		if (new Vector3f(getPosition()).distance(player.getPosition()) <= 5 && p.canBeAdded(blockType))
		{
			Vector3f dir = new Vector3f(player.getPosition()).sub(getPosition()).normalize().mul(Math.min((float) ((timeAlive - 1.2f) * (timeAlive - 1.2f)) / 20f, 0.1f));
			addPosition(dir);
			if (new Vector3f(getPosition()).distance(player.getPosition()) <= 0.1f)
			{
				setDead(p.addItem(blockType, model));
				return true;
			}
		}
		return false;
	}

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

	@Override
	public void render()
	{
		DynamicEntityModel.QUAT.identity().rotateXYZ(getRotations().x, getRotations().y, getRotations().z);

		DynamicEntityModel.MAT.identity()
			.translate(getPosition().x - 0.5f, getPosition().y - 0.5f, getPosition().z - 0.5f)
			.translate(getPivotPoint())
			.scale(calculateSize(timeAlive))
			.rotate(DynamicEntityModel.QUAT)
			.translate(-getPivotPoint().x, -getPivotPoint().y, -getPivotPoint().z)
		;

		model.render(CaveGame.getInstance().getCamera().getViewMatrix(), DynamicEntityModel.MAT);
//		AABBUtil.renderAABB(getX(), getY(), getZ(), 1f / 19f, 1);
	}

	private double mix(double x, double a)
	{
		return x * (1d - a) + a;
	}

	private double smoothstep(double x)
	{
		double t = Util.clamp(0.0, 1.0, (x - 1.1) / -1.1);
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
