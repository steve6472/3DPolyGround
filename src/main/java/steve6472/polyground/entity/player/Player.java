package steve6472.polyground.entity.player;

import org.joml.Intersectionf;
import org.joml.Math;
import org.joml.Vector2f;
import org.joml.Vector3f;
import steve6472.polyground.CaveGame;
import steve6472.polyground.EnumFace;
import steve6472.polyground.HitResult;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.Tags;
import steve6472.polyground.block.blockdata.BlockData;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.entity.EntityHitbox;
import steve6472.polyground.entity.item.BlockEntity;
import steve6472.polyground.entity.item.ItemEntity;
import steve6472.polyground.item.Item;
import steve6472.polyground.item.itemdata.IItemData;
import steve6472.polyground.item.itemdata.ItemData;
import steve6472.polyground.registry.Blocks;
import steve6472.polyground.world.World;
import steve6472.sge.main.KeyList;
import steve6472.sge.main.events.Event;
import steve6472.sge.main.events.KeyEvent;
import steve6472.sge.main.events.MouseEvent;
import steve6472.sge.main.game.Camera;
import steve6472.sge.main.game.mixable.IMotion3f;
import steve6472.sge.main.game.mixable.IPosition3f;
import steve6472.sge.main.util.Pair;

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
	public boolean noClip = false;
	public boolean isSprinting = false;
	public World world;

	public float eyeHeight = 1.62f;

	public float flySpeed = 0.007f;
	public boolean processNextBlockPlace = true;
	public boolean processNextBlockBreak = true;
	public EnumGameMode gamemode;

	public BlockEntity heldBlock;
	public ItemEntity heldItem;

	public Player(CaveGame game)
	{
		camera = new Camera();
		gamemode = EnumGameMode.CREATIVE;

		position = new Vector3f(-2, 0.05f, 0);
		motion = new Vector3f();
		viewDir = new Vector3f();

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
		canFly = gamemode.canFly;

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

		if (getHitResult().isHit())
		{
			HitResult hr = game.hitPicker.getHitResult();
			World world = game.getWorld();

			BlockState state = world.getState(hr.getX(), hr.getY(), hr.getZ());

			state.getBlock().onClick(state, world, this, hr.getFace(), event, hr.getX(), hr.getY(), hr.getZ());

			if (gamemode == EnumGameMode.CREATIVE)
			{
				CaveGame.itemInHand.onClick(world, state, this, EnumSlot.CREATIVE_BELT, hr.getFace(), event, hr.getX(), hr.getY(), hr.getZ());
			} else
			{
				if (heldBlock != null)
					heldBlock.state.getBlock().item.onClick(world, state, this, EnumSlot.CREATIVE_BELT, hr.getFace(), event, hr.getX(), hr.getY(), hr.getZ());
			}
		}

		if (gamemode == EnumGameMode.CREATIVE && event.getAction() == KeyList.PRESS)
		{
			CaveGame.itemInHand.onClick(this, EnumSlot.CREATIVE_BELT, event);
		} else if (event.getAction() == KeyList.PRESS)
		{
			if (heldBlock != null)
				heldBlock.state.getBlock().item.onClick(this, EnumSlot.CREATIVE_BELT, event);
		}

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
		if (gamemode == EnumGameMode.CREATIVE && Blocks.getBlockByName(CaveGame.itemInHand.getName()) == Block.error && getHitResult().isHit())
		{
			Item item = CaveGame.itemInHand;
			ItemData data = null;
			if (item instanceof IItemData id)
				data = id.createNewItemData();

			ItemEntity entity = new ItemEntity(null, item, data, getHitResult().getPx(), getHitResult().getPy() + 0.001f, getHitResult().getPz());
			world.getEntityManager().addEntity(entity);

			return;
		}

		if (heldBlock != null && processNextBlockPlace)
		{
			HitResult hr = game.hitPicker.getHitResult();
			Block blockToPlace = heldBlock.state.getBlock();

			if (hr.isHit() && blockToPlace != null)
			{
				EnumFace face = hr.getFace();

				int x = hr.getX();
				int y = hr.getY();
				int z = hr.getZ();

				boolean merge = blockToPlace.canMerge(heldBlock.state, world.getState(x, y, z), this, face, x, y, z);

				if (!merge)
				{
					x += face.getXOffset();
					y += face.getYOffset();
					z += face.getZOffset();

					// If state can not merge with the clicked block try the offseted block
					merge = blockToPlace.canMerge(heldBlock.state, world.getState(x, y, z), this, face, x, y, z);
				}

				if (merge)
				{
					BlockState curr = world.getState(x, y, z);

					Pair<BlockState, BlockState> pair = blockToPlace.merge(world, curr, heldBlock.state == null ? Block.air.getDefaultState() : heldBlock.state, this, face, x, y, z);

					if (pair != null)
					{
						world.setState(pair.getB(), x, y, z, 1);

						if (pair.getA() == null || pair.getA().isAir())
						{
							getWorld().getEntityManager().removeEntity(heldBlock);
							heldBlock = null;
						} else
						{
							if (heldBlock == null)
							{
								BlockEntity item = new BlockEntity(this, pair.getA(), world.getData(x, y, z), x, y, z);
								heldBlock = item;
								getWorld().getEntityManager().addEntity(item);
							} else
							{
								heldBlock.state = pair.getA();
							}
						}
					}
				} else
				{
					Pair<BlockState, BlockState> pair = blockToPlace.getStateForPlacement(world, heldBlock.state, this, face, x, y, z);
					if (pair.getB().getBlock().isValidPosition(pair.getB(), world, x, y, z) && pair.getB().getBlock() != world.getState(x, y, z).getBlock())
					{
						world.setState(pair.getB(), x, y, z, 1);
						world.setData(heldBlock.data, x, y, z);

						if (pair.getA() == null || pair.getA().isAir())
						{
							getWorld().getEntityManager().removeEntity(heldBlock);
							heldBlock = null;
						} else
						{
							if (heldBlock == null)
							{
								BlockEntity item = new BlockEntity(this, pair.getA(), world.getData(x, y, z), x, y, z);
								heldBlock = item;
								getWorld().getEntityManager().addEntity(item);
							} else
							{
								heldBlock.state = pair.getA();
							}
						}
					}
				}
			}
		}
	}

	private void pressLMB()
	{
		if (gamemode == EnumGameMode.CREATIVE)
		{
			if (getHitResult().isHit() && processNextBlockBreak)
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
		} else
		{
			if (getHitResult().isHit() && processNextBlockBreak)
			{
				HitResult hr = CaveGame.getInstance().hitPicker.getHitResult();
				World world = CaveGame.getInstance().world;

				int x = hr.getX();
				int y = hr.getY();
				int z = hr.getZ();

				BlockState state = world.getState(x, y, z);
				if (state.hasTag(Tags.PICKABLE) || state.getBlock().isPickable(state, this))
				{
					Pair<BlockState, BlockState> pair = state.getBlock().getStatesForPickup(world, state,
						heldBlock == null ? Block.air.getDefaultState() : heldBlock.state,
						this, hr.getFace(), x, y, z);

					if (pair != null)
					{
						if (heldBlock == null)
						{
							BlockEntity item = new BlockEntity(this, pair.getA(), world.getData(x, y, z), x, y, z);
							heldBlock = item;
							getWorld().getEntityManager().addEntity(item);
						} else
						{
							heldBlock.state = pair.getA();
						}
						world.setState(pair.getB(), x, y, z);
					}
				}
			}
		}
	}

	private void pressMMB()
	{
		ItemEntity target = getTargetedItem();

		if (heldItem == null)
		{
			if (target != null)
			{
				heldItem = target;
				heldItem.setPlayer(this);
			}

		} else
		{
			if (!getHitResult().isHit())
				return;

			heldItem.setPlayer(null);
			heldItem.setPosition(getHitResult().getPx(), getHitResult().getPy(), getHitResult().getPz());

			if (target == null)
			{
				heldItem = null;
			} else
			{
				heldItem = target;
				heldItem.setPlayer(this);
			}
		}
	}

	public ItemEntity getTargetedItem()
	{
		Vector2f target = new Vector2f();
		Vector3f distanceTest = new Vector3f();
		float distance = Float.MAX_VALUE;
		ItemEntity r = null;

		for (Object o : game.world.getEntityManager().getEntities())
		{
			if (o instanceof ItemEntity p)
			{
				if (heldItem != null && p == heldItem)
					continue;

				if (Intersectionf.intersectRayAab(
					camera.getX(), camera.getY(), camera.getZ(),
					viewDir.x, viewDir.y, viewDir.z,
					p.getHitbox().minX, p.getHitbox().minY, p.getHitbox().minZ,
					p.getHitbox().maxX, p.getHitbox().maxY, p.getHitbox().maxZ, target))
				{
					float currDistance = distanceTest.set(
						viewDir.x, viewDir.y, viewDir.z).mul(target.x).add(camera.getX(), camera.getY(),
						camera.getZ()).distance(camera.getX(), camera.getY(), camera.getZ());

					if (currDistance < distance)
					{
						r = p;
						distance = currDistance;
					}
				}
			}
		}

		return r;
	}

	public BlockState getBlockInHand()
	{
		if (heldBlock == null)
			return null;

		return heldBlock.state;
	}

	public BlockData getBlockDataInHand()
	{
		if (heldBlock == null)
			return null;

		return heldBlock.data;
	}

	public boolean holdsBlock()
	{
		return getBlockInHand() != null;
	}

	public Item getItemInHand()
	{
		if (heldItem == null)
			return null;

		return heldItem.item;
	}

	public ItemData getItemDataInHand()
	{
		if (heldItem == null)
			return null;

		return heldItem.itemData;
	}

	public boolean holdsItem()
	{
		return getItemInHand() != null;
	}

	public boolean canBreakBlocks()
	{
		return gamemode == EnumGameMode.CREATIVE;
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
