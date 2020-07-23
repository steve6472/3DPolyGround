package steve6472.polyground.entity;

import org.joml.Math;
import org.joml.Vector3f;
import steve6472.polyground.BasicEvents;
import steve6472.polyground.CaveGame;
import steve6472.polyground.HitResult;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.registry.ItemRegistry;
import steve6472.polyground.world.chunk.SubChunk;
import steve6472.sge.main.KeyList;
import steve6472.sge.main.events.Event;
import steve6472.sge.main.events.KeyEvent;
import steve6472.sge.main.events.MouseEvent;
import steve6472.sge.main.game.Camera;
import steve6472.sge.main.game.mixable.IMotion3f;
import steve6472.sge.main.game.mixable.IPosition3f;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 19.07.2019
 * Project: SJP
 *
 ***********************/
public class Player implements IMotion3f, IPosition3f
{
	private static final float RAD_90 = Math.toRadians(90f);

	private final Vector3f motion, position;
	private final CaveGame game;
	private final EntityHitbox hitbox;

	private Camera camera;

	public Vector3f viewDir;
	public boolean canFly = true;
	public boolean isFlying;
	public boolean isOnGround;
	public boolean isOnBlock;
	public boolean noClip = false;
	public boolean isSprinting = false;

	public float eyeHeight = 1.62f;

	public float flySpeed = 0.007f;
	public boolean processNextBlockPlace = true;
	public boolean processNextBlockBreak = true;

	public Player(CaveGame game)
	{
		camera = new Camera();

		position = new Vector3f(-2, 0.05f, 0);
		motion = new Vector3f();
		viewDir = new Vector3f();

		hitbox = new EntityHitbox(0.3f, 0.9f, 0.3f, this, this);

		//65 m/s terminal vel

		this.game = game;
	}

	public EntityHitbox getHitbox()
	{
		return hitbox;
	}

	private byte flyTimer = 0;

	public void tick()
	{
		CaveGame.itemInHand.onTickInItemBar(this);

		if (flyTimer > 0)
			flyTimer--;

//		head();

		if (isFlying)
			isOnGround = false;
		else
			isOnGround = getPosition().y <= 0 || isOnBlock;
		float speed = isOnGround ? 0.02F : 0.005F;
		if (isFlying)
			speed = flySpeed;
		isOnBlock = false;

		float xa = 0, za = 0;

		if (game.isKeyPressed(KeyList.W))
			xa += 1;
		if (game.isKeyPressed(KeyList.A))
			za += -1;
		if (game.isKeyPressed(KeyList.S))
			xa += -1;
		if (game.isKeyPressed(KeyList.D))
			za += 1;
		if (game.isKeyPressed(KeyList.SPACE) && isOnGround && !isFlying)
			getMotion().y = 0.155f;
		if (game.isKeyPressed(KeyList.SPACE) && isFlying)
			getMotion().y = flySpeed * 6f;
		if (game.isKeyPressed(KeyList.L_CONTROL) && isFlying)
			getMotion().y = -flySpeed * 6f;
		if (isSprinting = game.isKeyPressed(KeyList.L_SHIFT))
			speed *= 1.5;

		move(xa, za, speed);

		/* Apply gravity */
		if (!isOnGround && !isFlying)
			getMotion().y -= 0.005f;

		getPosition().add(getMotion());

		if (getMotion().y > 9.5f)
			getMotion().y = 9.5f;

		if (!noClip)
		{
			isOnBlock = hitbox.collideWithWorld(game.getWorld());
		}

		getMotion().x *= 0.91f;
		if (isFlying)
			getMotion().y *= 0.91f;
		else
			getMotion().y *= 0.98f;
		getMotion().z *= 0.91f;

		if (isOnGround)
		{
			getMotion().mul(0.8f);
		}

		if (getPosition().y < 0 && !isFlying)
			getPosition().y = 0;

		updateHitbox();
	}

	public void updateHitbox()
	{
		camera.setPosition(getPosition().x, getPosition().y + eyeHeight, getPosition().z);
		hitbox.setHitbox(getX(), getY() + 0.9f, getZ());
	}

	private void move(float xa, float za, float speed)
	{
		float dist = xa * xa + za * za;
		if (dist < 0.01F)
		{
			return;
		}
		dist = speed / Math.sqrt(dist);
		xa *= dist;
		za *= dist;

		float sin = Math.sin(camera.getYaw() + RAD_90);
		float cos = Math.cos(camera.getYaw() + RAD_90);

		getMotion().x += xa * cos + za * sin;
		getMotion().z += za * cos - xa * sin;
	}

	@Event
	public void fly(KeyEvent e)
	{
		if (!canFly)
			return;
		if (game.world == null || game.options.isInMenu)
			return;
		if (game.inGameGui.chat.isFocused())
			return;

		if (e.getAction() == KeyList.PRESS && e.getKey() == KeyList.SPACE)
		{
			if (flyTimer == 0)
			{
				flyTimer = 20;
			} else
			{
				if (!isOnGround)
				{
					isFlying = !isFlying;
				}
				flyTimer = 0;
			}
		}

		if (e.getAction() == KeyList.PRESS && e.getKey() == KeyList.N)
		{
			noClip = !noClip;
		}
	}

	@Event
	public void mouseEvent(MouseEvent event)
	{
		if (game.world == null || game.options.isInMenu)
			return;
		if (game.inGameGui.chat.isFocused())
			return;
		if (game.mainRender.dialogManager.isActive())
			return;

		if (game.hitPicker.hit)
		{
			HitResult hr = game.hitPicker.getHitResult();

			SubChunk subChunk = game.world.getSubChunkFromBlockCoords(hr.getX(), hr.getY(), hr.getZ());

			BlockState state = subChunk.getState(hr.getCx(), hr.getCy(), hr.getCz());

			game.world.getBlock(hr.getX(), hr.getY(), hr.getZ()).onClick(subChunk, state, this, hr.getFace(), event, hr.getCx(), hr.getCy(), hr.getCz());

			CaveGame.itemInHand.onClick(subChunk, state, this, hr.getFace(), event, hr.getCx(), hr.getCy(), hr.getCz());
		}

		CaveGame.itemInHand.onClick(this, event);

		if (event.getButton() == KeyList.RMB && event.getAction() == KeyList.PRESS)
		{
			if (game.hitPicker.hit)
			{
				HitResult hr = game.hitPicker.getHitResult();

				if (processNextBlockPlace)
				{
					BasicEvents.place(CaveGame.itemInHand.getBlockToPlace(), hr.getFace(), this);
				} else
				{
					processNextBlockPlace = true;
				}
			}
		}

		if (event.getButton() == KeyList.LMB && event.getAction() == KeyList.PRESS)
		{
			if (game.hitPicker.hit)
			{
				if (processNextBlockBreak)
				{
					BasicEvents.breakBlock(this);
				} else
				{
					processNextBlockBreak = true;
				}
			}
		}

		if (event.getButton() == KeyList.MMB && event.getAction() == KeyList.PRESS)
		{
			if (game.hitPicker.hit)
			{
				HitResult hr = game.hitPicker.getHitResult();
				Block block = game.world.getBlock(hr.getX(), hr.getY(), hr.getZ());

				if (ItemRegistry.getItemByName(block.getName()) != null)
				{
					CaveGame.itemInHand = ItemRegistry.getItemByName(block.getName());
				}
			}
		}
	}

	@Override
	public Vector3f getMotion()
	{
		return motion;
	}

	@Override
	public Vector3f getPosition()
	{
		return position;
	}

	public Camera getCamera()
	{
		return camera;
	}

	public float getEyeHeight()
	{
		return eyeHeight;
	}

	public HitResult getHitResult()
	{
		return game.hitPicker.getHitResult();
	}

	public void setCamera(Camera camera)
	{
		this.camera = camera;
	}
}
