package steve6472.polyground.entity.item;

import org.joml.Math;
import steve6472.polyground.AABBUtil;
import steve6472.polyground.CaveGame;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.entity.HitboxEntityBase;
import steve6472.polyground.entity.interfaces.IKillable;
import steve6472.polyground.entity.interfaces.IRenderable;
import steve6472.polyground.entity.interfaces.ITickable;
import steve6472.polyground.entity.interfaces.IWorldContainer;
import steve6472.polyground.entity.player.Player;
import steve6472.polyground.gfx.DynamicEntityModel;
import steve6472.polyground.item.Item;
import steve6472.polyground.item.itemdata.ItemData;
import steve6472.polyground.world.World;
import steve6472.sge.main.game.mixable.IPosition3f;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 06.09.2020
 * Project: CaveGame
 *
 ***********************/
public class ItemEntity extends HitboxEntityBase implements IRenderable, ITickable, IKillable, IWorldContainer, IPosition3f
{
	public Item item;
	public ItemData itemData;
	private World world;
	private boolean forceDead = false;
	private Player player;

	public ItemEntity(Item item, ItemData itemData, float x, float y, float z)
	{
		super(0.25f / 2f, 0.25f / 2f, 0.25f / 2f);
		this.item = item;
		this.itemData = itemData;
		setPosition(x, y, z);
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
		if (!isHeld())
		{
			/* Apply gravity */
			getMotion().y -= 0.005f;

			getPosition().add(getMotion());

			if (getMotion().y > 9.5f)
				getMotion().y = 9.5f;

			getEntityHitbox().expand(getMotionX(), getMotionY(), getMotionZ());
			boolean isOnGround = getEntityHitbox().collideWithWorld(getWorld(), this);

			if (getY() <= 0)
				isOnGround = true;

			getMotion().x *= 0.91f;
			getMotion().y *= 0.98f;
			getMotion().z *= 0.91f;

			if (Math.abs(getMotion().x) <= 0x1.10210e863e3ccp-30)
				getMotion().x = 0;
			if (Math.abs(getMotion().z) <= 0.00000000099)
				getMotion().z = 0;
			if (Math.abs(getMotion().y) <= 9.9E-10)
				getMotion().y = 0;

			if (isOnGround)
			{
				getMotion().mul(0.8f);
			}

			if (getPosition().y < 0)
				getPosition().y = 0;

			updateHitbox();

			if (isOnGround)
			{
				BlockState state = world.getState((int) Math.floor(getX()), (int) Math.floor(getY() - 0.00001f), (int) Math.floor(getZ()));
				state.getBlock().entityOnBlockCollision(this, world, state, (int) Math.floor(getX()), (int) Math.floor(getY()), (int) Math.floor(getZ()));
			}
		}

		if (isHeld())
		{
			setRotations(0, player.getCamera().getYaw() + 1.5707963267948966f, player.getCamera().getPitch());
			setPosition(player.getPosition());
			setMotion(0, 0, 0);
			getEntityHitbox().setHitbox(getX(), getY(), getZ());
		} else
		{
			setRotations(0, getRotations().y, 0f);
		}
	}

	@Override
	public void render()
	{
		DynamicEntityModel.QUAT.identity().rotateXYZ(getRotations().x, getRotations().y, getRotations().z);

		if (isHeld())
		{
			DynamicEntityModel.MAT.identity()
				.translate(player.getX(), player.getY() + player.eyeHeight, player.getZ())
				.rotate(DynamicEntityModel.QUAT)
				.translate(1.2f, -0.5f, +0.9f)
				.scale(0.75f)
				.translate(-getPivotPoint().x, -getPivotPoint().y, -getPivotPoint().z)
			;
		} else
		{
			DynamicEntityModel.MAT.identity()
				.translate(getX(), getY(), getZ())
				.rotate(DynamicEntityModel.QUAT)
				.translate(-0.5f, 0, -0.5f)
			;
		}

		item.model.render(CaveGame.getInstance().getCamera().getViewMatrix(), DynamicEntityModel.MAT);

		if (CaveGame.getInstance().options.renderItemEntityOutline)
			AABBUtil.renderAABBf(getHitbox(), 1);
	}

	public void remove(Player player)
	{
		world.getEntityManager().removeEntity(this);
		if (player != null) player.heldItem = null;
	}

	public void setPlayer(Player player)
	{
		this.player = player;
	}

	public boolean isHeld()
	{
		return player != null;
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
