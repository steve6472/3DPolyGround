package com.steve6472.polyground.entity;

import com.steve6472.polyground.BasicEvents;
import com.steve6472.polyground.CaveGame;
import com.steve6472.polyground.HitResult;
import com.steve6472.polyground.block.Block;
import com.steve6472.polyground.block.blockdata.BlockData;
import com.steve6472.polyground.block.registry.BlockRegistry;
import com.steve6472.polyground.item.registry.ItemRegistry;
import com.steve6472.polyground.world.chunk.SubChunk;
import com.steve6472.sge.main.KeyList;
import com.steve6472.sge.main.events.Event;
import com.steve6472.sge.main.events.KeyEvent;
import com.steve6472.sge.main.events.MouseEvent;
import com.steve6472.sge.main.game.Camera;
import com.steve6472.sge.main.game.mixable.IMotion3f;
import com.steve6472.sge.main.game.mixable.IPosition3f;
import org.joml.Math;
import org.joml.Vector3f;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 19.07.2019
 * Project: SJP
 *
 ***********************/
public class Player implements IMotion3f, IPosition3f
{
	private Vector3f motion, position;
	private CaveGame pg;
	private EntityHitbox hitbox;

	private Camera camera;

	public Vector3f viewDir;
	public boolean isFlying;
	public boolean isOnGround;
	public boolean isOnBlock;
	public boolean noClip = false;

	public float eyeHeight = 1.62f;

	public float flySpeed = 0.007f;
	public boolean processNextBlockPlace = true;
	public boolean processNextBlockBreak = true;

	public Player(CaveGame pg)
	{
		camera = new Camera();
		hitbox = new EntityHitbox(0.3f, 0.9f, 0.3f);

		position = new Vector3f(-2, 0.05f, 0);
		motion = new Vector3f();
		viewDir = new Vector3f();

		//65 m/s terminal vel

		this.pg = pg;
	}

	public EntityHitbox getHitbox()
	{
		return hitbox;
	}

	private boolean flag;
	private byte flyTimer = 0;

	public void tick()
	{
		CaveGame.itemInHand.onTickInItemBar(this);

		if (flyTimer > 0) flyTimer--;

		head();

		if (isFlying)
			isOnGround = false;
		else
			isOnGround = getPosition().y <= 0 || isOnBlock;
		float speed = isOnGround ? 0.02F : 0.005F;
		if (isFlying) speed = flySpeed;
		isOnBlock = false;

		float xa = 0, za = 0;

		if (pg.isKeyPressed(KeyList.W)) xa += 1;
		if (pg.isKeyPressed(KeyList.A)) za += -1;
		if (pg.isKeyPressed(KeyList.S)) xa += -1;
		if (pg.isKeyPressed(KeyList.D)) za += 1;
		if (pg.isKeyPressed(KeyList.SPACE) && isOnGround && !isFlying) getMotion().y = 0.155f;
		if (pg.isKeyPressed(KeyList.SPACE) && isFlying) getMotion().y = flySpeed * 6f;
		if (pg.isKeyPressed(KeyList.L_CONTROL) && isFlying) getMotion().y = -flySpeed * 6f;

		move(xa, za, speed);

		/* Apply gravity */
		if (!isOnGround && !isFlying)
			getMotion().y -= 0.005f;

		getPosition().add(getMotion());

		if (getMotion().y > 9.5f)
			getMotion().y = 9.5f;

		if (!noClip)
		{
			isOnBlock = hitbox.collideWithWorld(this, this);
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
		dist = speed / (float)Math.sqrt(dist);
		xa *= dist;
		za *= dist;

		float sin = (float) Math.sin(camera.getYaw() + Math.toRadians(90f));
		float cos = (float) Math.cos(camera.getYaw() + Math.toRadians(90f));

		getMotion().x += xa * cos + za * sin;
		getMotion().z += za * cos - xa * sin;
	}

	private void head()
	{
		if (camera.canMoveHead())
		{
			if (!flag)
			{
				camera.oldx = pg.getMouseX();
				camera.oldy = pg.getMouseY();
				flag = true;
			}
			camera.head(pg.getMouseX(), pg.getMouseY(), CaveGame.getInstance().options.mouseSensitivity);
		}
	}

	@Event
	public void fly(KeyEvent e)
	{
		if (pg.inGameGui.chat.isFocused()) return;

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
		if (pg.inGameGui.chat.isFocused()) return;

		if (camera.canMoveHead() && event.getAction() == KeyList.RELEASE && event.getButton() == KeyList.RMB)
		{
			flag = false;
		}


		if (pg.hitPicker.hit)
		{
			HitResult hr = pg.hitPicker.getHitResult();

			SubChunk subChunk = pg.world.getSubChunkFromBlockCoords(hr.getX(), hr.getY(), hr.getZ());

			BlockData data = subChunk.getBlockData(hr.getCx(), hr.getCy(), hr.getCz());

			BlockRegistry.getBlockById(pg.world.getBlockId(hr.getX(), hr.getY(), hr.getZ()))
				.onClick(subChunk, data, this, hr.getFace(), event, hr.getX(), hr.getY(), hr.getZ());

			CaveGame.itemInHand
				.onClick(subChunk, data, this, hr.getFace(), event, hr.getX(), hr.getY(), hr.getZ());
		}

		CaveGame.itemInHand
			.onClick(this, event);

		if (event.getButton() == KeyList.RMB && event.getAction() == KeyList.PRESS)
		{
			if (pg.hitPicker.hit)
			{
				HitResult hr = pg.hitPicker.getHitResult();

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
			if (pg.hitPicker.hit)
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
			if (pg.hitPicker.hit)
			{
				HitResult hr = pg.hitPicker.getHitResult();
				Block block = pg.world.getBlock(hr.getX(), hr.getY(), hr.getZ());

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
		return pg.hitPicker.getHitResult();
	}

	public void setCamera(Camera camera)
	{
		this.camera = camera;
	}
}
