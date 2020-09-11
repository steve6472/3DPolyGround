package steve6472.polyground.entity.player;

import org.joml.Intersectionf;
import org.joml.Math;
import org.joml.Vector2f;
import org.joml.Vector3f;
import steve6472.polyground.CaveGame;
import steve6472.polyground.EnumFace;
import steve6472.polyground.HitResult;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.entity.EntityHitbox;
import steve6472.polyground.entity.MiningTool;
import steve6472.polyground.entity.Palette;
import steve6472.polyground.world.World;
import steve6472.sge.main.KeyList;
import steve6472.sge.main.events.Event;
import steve6472.sge.main.events.KeyEvent;
import steve6472.sge.main.events.MouseEvent;
import steve6472.sge.main.game.Camera;
import steve6472.sge.main.game.mixable.IMotion3f;
import steve6472.sge.main.game.mixable.IPosition3f;

import java.util.HashMap;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 19.07.2019
 * Project: SJP
 *
 ***********************/
public class Player implements IMotion3f, IPosition3f
{
	private static final float RAD_90 = Math.toRadians(90f);

	public static final IHoldable EMPTY_HAND = new EmptyHand();

	private final Vector3f motion, position;
	private final CaveGame game;
	private final EntityHitbox hitbox;

	private Camera camera;

	public Vector3f viewDir;
	public boolean canFly = true;
	public boolean isFlying;
	public boolean isOnGround;
	public boolean noClip = false;
	public boolean isSprinting = false;
	public World world;

	public float eyeHeight = 1.62f;

	public float flySpeed = 0.007f;
	public boolean processNextBlockPlace = true;
	public boolean processNextBlockBreak = true;
	public HashMap<EnumSlot, IHoldable> holdedItems;
	public EnumGameMode gamemode;

	public Player(CaveGame game)
	{
		camera = new Camera();
		gamemode = EnumGameMode.CREATIVE;

		position = new Vector3f(-2, 0.05f, 0);
		motion = new Vector3f();
		viewDir = new Vector3f();

		holdedItems = new HashMap<>();

		holdedItems.put(EnumSlot.HAND_LEFT, EMPTY_HAND);
		holdedItems.put(EnumSlot.HAND_RIGHT, EMPTY_HAND);
		holdedItems.put(EnumSlot.BACK, EMPTY_HAND);

		hitbox = new EntityHitbox(0.3f, 0.9f, 0.3f, this, this);

		//65 m/s terminal vel

		this.game = game;
	}

	public World getWorld()
	{
		return world;
	}

	public void setWorld(World world)
	{
		this.world = world;
	}

	public EntityHitbox getHitbox()
	{
		return hitbox;
	}

	private byte flyTimer = 0;
	private float speed;

	public void tick()
	{
		CaveGame.itemInHand.onTickInItemBar(this);

		if (flyTimer > 0)
			flyTimer--;
		if (isFlying)
			speed = flySpeed;

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
		{
			isOnGround = false;
			getMotion().y = 0.132f;
		}
		if (game.isKeyPressed(KeyList.SPACE) && isFlying)
			getMotion().y = flySpeed * 6f;
		if (game.isKeyPressed(KeyList.L_CONTROL) && isFlying)
			getMotion().y = -flySpeed * 6f;
		if (isSprinting = game.isKeyPressed(KeyList.L_SHIFT))
			speed *= 1.5;

		move(xa, za, speed);

		/* Apply gravity */
		if (!isFlying)
			getMotion().y -= 0.005f;

		getPosition().add(getMotion());

		if (getMotion().y > 9.5f)
			getMotion().y = 9.5f;

		if (!noClip)
		{
			boolean stepup;
			hitbox.expand(getMotionX(), getMotionY(), getMotionZ());
			stepup = hitbox.stepUp(game.getWorld());
			hitbox.expand(getMotionX(), getMotionY(), getMotionZ());
			isOnGround = hitbox.collideWithWorld(game.getWorld()) || stepup;
		}

		if (getY() <= 0)
			isOnGround = true;
		if (isFlying)
			isOnGround = false;
		speed = isOnGround ? 0.02F : 0.005F;

		getMotion().x *= 0.91f;
		if (isFlying)
			getMotion().y *= 0.91f;
		else
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

		if (getPosition().y < 0 && !isFlying)
			getPosition().y = 0;

		updateHitbox();

		for (EnumSlot holdPosition : holdedItems.keySet())
		{
			holdedItems.get(holdPosition).tick(holdPosition);
		}
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

	public void render()
	{
		for (EnumSlot holdPosition : holdedItems.keySet())
		{
			holdedItems.get(holdPosition).render(holdPosition);
		}
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

		// Swap hands
		if (e.getAction() == KeyList.PRESS && e.getKey() == KeyList.F)
		{
			swapHands();
		}
	}

	public void swapHands()
	{
		IHoldable temp = holdedItems.get(EnumSlot.HAND_LEFT);
		holdedItems.put(EnumSlot.HAND_LEFT, holdedItems.get(EnumSlot.HAND_RIGHT));
		holdedItems.put(EnumSlot.HAND_RIGHT, temp);
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
			World world = game.getWorld();

			BlockState state = world.getState(hr.getX(), hr.getY(), hr.getZ());

			game.world.getBlock(hr.getX(), hr.getY(), hr.getZ()).onClick(state, world, this, hr.getFace(), event, hr.getX(), hr.getY(), hr.getZ());

			if (gamemode == EnumGameMode.CREATIVE)
				CaveGame.itemInHand.onClick(world, state, this, EnumSlot.CREATIVE_BELT, hr.getFace(), event, hr.getX(), hr.getY(), hr.getZ());
		}

		if (gamemode == EnumGameMode.CREATIVE)
			CaveGame.itemInHand.onClick(this, EnumSlot.CREATIVE_BELT, event);

		if (event.getButton() == KeyList.RMB && event.getAction() == KeyList.PRESS)
		{
			pressRMB();
		}

		if (event.getButton() == KeyList.LMB && event.getAction() == KeyList.PRESS)
		{
			pressLMB();
		}

		if (event.getButton() == KeyList.MMB && event.getAction() == KeyList.PRESS)
		{
			pressMMB();
		}

		processNextBlockPlace = true;
		processNextBlockBreak = true;
	}

	private void pressRMB()
	{
		IHoldable l = holdedItems.get(EnumSlot.HAND_LEFT);
		IHoldable r = holdedItems.get(EnumSlot.HAND_RIGHT);
		Palette palette = null;

		if (l instanceof Palette p)
			palette = p;
		else if (r instanceof Palette p)
			palette = p;

		if (game.hitPicker.hit)
		{
			if (processNextBlockPlace)
			{
				HitResult hr = game.hitPicker.getHitResult();
				Block blockToPlace = null;
				if (gamemode == EnumGameMode.CREATIVE)
				{
					blockToPlace = CaveGame.itemInHand.getBlockToPlace();
				} else if (palette != null)
				{
					blockToPlace = palette.getBlockType();
				}

				if (blockToPlace != null)
				{
					if (gamemode == EnumGameMode.SURVIVAL && palette != null)
						palette.removeItem();

					EnumFace face = hr.getFace();
					int x = hr.getX() + face.getXOffset();
					int y = hr.getY() + face.getYOffset();
					int z = hr.getZ() + face.getZOffset();

					BlockState stateToPlace = blockToPlace.getStateForPlacement(game.world, this, face, x, y, z);
					game.world.setState(stateToPlace, x, y, z, 5);
				}
			}
		}
	}

	private void pressLMB()
	{
		if (game.hitPicker.hit && processNextBlockBreak && canBreakBlocks())
		{
			HitResult hr = CaveGame.getInstance().hitPicker.getHitResult();
			World world = CaveGame.getInstance().world;

			int x = hr.getX();
			int y = hr.getY();
			int z = hr.getZ();

			BlockState state = world.getState(x, y, z);

			state.getBlock().onPlayerBreak(state, world, this, hr.getFace(), x, y, z);
			world.setBlock(Block.air, hr.getX(), hr.getY(), hr.getZ());
		}
	}

	private void pressMMB()
	{
		HitResult hr = game.hitPicker.getHitResult();
		IHoldable targetHoldable = getFirstTargetedHoldable();

		if (!game.hitPicker.hit)
			return;

		if (targetHoldable != null)
		{
			if (targetHoldableDistance <= 3f)
			{
				if (holdedItems.get(EnumSlot.HAND_RIGHT) == EMPTY_HAND)
				{
					holdedItems.put(EnumSlot.HAND_RIGHT, targetHoldable.pickUp(world, this));
				} else if (holdedItems.get(EnumSlot.HAND_LEFT) == EMPTY_HAND)
				{
					holdedItems.put(EnumSlot.HAND_LEFT, targetHoldable.pickUp(world, this));
				} else
				{
					// Swap items
					holdedItems.get(EnumSlot.HAND_LEFT).place(world, hr.getPx(), hr.getPy(), hr.getPz());
					holdedItems.put(EnumSlot.HAND_LEFT, targetHoldable.pickUp(world, this));
				}
			}
		} else
		{
			if (hr.getFace() == EnumFace.UP)
			{
				if (holdedItems.get(EnumSlot.HAND_RIGHT) != EMPTY_HAND)
				{
					holdedItems.get(EnumSlot.HAND_RIGHT).place(world, hr.getPx(), hr.getPy(), hr.getPz());
					holdedItems.put(EnumSlot.HAND_RIGHT, EMPTY_HAND);
				} else if (holdedItems.get(EnumSlot.HAND_LEFT) != EMPTY_HAND)
				{
					holdedItems.get(EnumSlot.HAND_LEFT).place(world, hr.getPx(), hr.getPy(), hr.getPz());
					holdedItems.put(EnumSlot.HAND_LEFT, EMPTY_HAND);
				}
			}
		}

	}

	private static final Vector2f target = new Vector2f();
	private static final Vector3f distanceTest = new Vector3f();
	private static float targetHoldableDistance;

	private IHoldable getFirstTargetedHoldable()
	{
		for (Object o : game.world.getEntityManager().getEntities())
		{
			if (o instanceof IHoldable p)
			{
				if (Intersectionf.intersectRayAab(
					camera.getX(), camera.getY(), camera.getZ(),
					viewDir.x, viewDir.y, viewDir.z,
					p.getHitbox().minX + p.getX(), p.getHitbox().minY + p.getY(), p.getHitbox().minZ + p.getZ(),
					p.getHitbox().maxX + p.getX(), p.getHitbox().maxY + p.getY(), p.getHitbox().maxZ + p.getZ(), target))
				{
					targetHoldableDistance = distanceTest.set(
						viewDir.x, viewDir.y, viewDir.z).mul(target.x).add(camera.getX(), camera.getY(),
						camera.getZ()).distance(camera.getX(), camera.getY(), camera.getZ());
					return p;
				}
			}
		}
		return null;
	}

	public boolean canBreakBlocks()
	{
		return (gamemode == EnumGameMode.CREATIVE || holdedItems.get(EnumSlot.HAND_RIGHT) instanceof MiningTool);
	}

	public EnumGameMode getGamemode()
	{
		return gamemode;
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
