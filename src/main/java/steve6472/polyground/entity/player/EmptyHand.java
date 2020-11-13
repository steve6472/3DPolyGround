package steve6472.polyground.entity.player;

import org.joml.AABBf;
import org.joml.Vector3f;
import steve6472.polyground.CaveGame;
import steve6472.polyground.world.World;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 08.09.2020
 * Project: CaveGame
 *
 ***********************/
public class EmptyHand implements IHoldable
{
	// TODO: Add hand model
	private final Vector3f position = new Vector3f();

	public final AABBf AABB = new AABBf(-0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f);

	@Override
	public Vector3f getPosition()
	{
		return position;
	}

	@Override
	public IHoldable pickUp(World world, Player player)
	{
		world.getEntityManager().removeEntity(this);
		return this;
	}

	@Override
	public AABBf getHitbox()
	{
		return AABB;
	}

	@Override
	public void place(World world, float x, float y, float z)
	{
		setPosition(x, y, z);
		world.getEntityManager().addEntity(this);
	}

	@Override
	public void tick()
	{
		position.set(CaveGame.getInstance().getPlayer().getPosition());
	}

	@Override
	public void render()
	{

	}

	@Override
	public String getName()
	{
		return "empty";
	}
}
