package steve6472.polyground.entity;

import org.joml.AABBf;
import org.joml.Vector3f;
import steve6472.polyground.CaveGame;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.model.ModelLoader;
import steve6472.polyground.entity.interfaces.IRotation;
import steve6472.polyground.entity.player.EnumSlot;
import steve6472.polyground.entity.player.IHoldable;
import steve6472.polyground.entity.player.Player;
import steve6472.polyground.world.ModelBuilder;
import steve6472.polyground.world.World;
import steve6472.sge.main.game.mixable.IPosition3f;

import java.util.ArrayList;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 06.09.2020
 * Project: CaveGame
 *
 ***********************/
public class Palette implements IHoldable, IRotation
{
	private static final StaticEntityModel PALLETE_MODEL = new StaticEntityModel();
	private static final Dummy DUMMY = new Dummy();
	private static final AABBf AABB = new AABBf(-0.375f, 0, -0.375f, 0.375f, 0.1875f, 0.375f);

	private Block blockType;
	private Player player;
	private final List<DynamicEntityModel> items;
	private final Vector3f position, rotations, pivotPoint;

	public Palette(Player player)
	{
		this.player = player;
		this.position = new Vector3f();
		this.rotations = new Vector3f();
		this.pivotPoint = new Vector3f();
		items = new ArrayList<>();
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

	public Block getBlockType()
	{
		return blockType;
	}

	public boolean canBeAdded(Block blocktype)
	{
		return blockType == null || blocktype == this.blockType && items.size() < 8 * 8 * 8;
	}

	public boolean addItem(Block blockType, DynamicEntityModel model)
	{
		if (items.isEmpty())
		{
			this.blockType = blockType;
			items.add(model);
			return true;
		} else
		{
			if (this.blockType != blockType)
				return false;
			else
			{
				items.add(model);
				return true;
			}
		}
	}

	@Override
	public void tick(EnumSlot position)
	{
		if (position == EnumSlot.GROUND)
		{
			if (CaveGame.getInstance().world.getState((int) getX(), (int) getY(), (int) getZ()).getBlock() != Block.air)
			{
				addPosition(0, 1f / 60f, 0);
			}
			if (CaveGame.getInstance().world.getState((int) getX(), (int) (getY() - 0.1f), (int) getZ()).getBlock() == Block.air)
			{
				addPosition(0, -1f / 60f, 0);
			}

			if (getY() < 0)
				setY(0);
		} else
		{
			float z = position == EnumSlot.HAND_LEFT ? 1f : -1f;

			setPosition(player.getPosition());
			setPivotPoint(-0.6f, -0.8f, z);
			setRotations(0, player.getCamera().getYaw() + 1.5707963267948966f, 0);
			setPosition(player.getX() + 0.6f, player.getY() + 0.8f, player.getZ() - z);

			DUMMY.setRotations(0, player.getCamera().getYaw() + 1.5707963267948966f, 0);
		}
	}

	protected StaticEntityModel getModel()
	{
		return PALLETE_MODEL;
	}

	@Override
	public void render(EnumSlot position)
	{
		getModel().render(CaveGame.getInstance().getCamera().getViewMatrix(), this, this, 1f);

		int index = 0;

		for (int i = 0; i < 8; i++)
		{
			for (int j = 0; j < 8; j++)
			{
				for (int k = 0; k < 8; k++)
				{
					if (index >= items.size())
						return;

					DynamicEntityModel m = items.get(index);

					m.render(CaveGame.getInstance().getCamera().getViewMatrix(),

						DynamicEntityModel.createMatrix(this, this, 1f)
							.translate(j / 16f - 0.25f, i / 16f + 1 / 16f, k / 16f - 0.25f)
							.scale(1 / 16f))
					;
					index++;
				}
			}
		}
	}

	public boolean removeItem()
	{
		if (items.isEmpty())
		{
			return false;
		} else
		{
			items.remove(items.size() - 1);
			if (items.isEmpty())
				blockType = null;
			return true;
		}
	}

	public static void initModel(ModelBuilder modelBuilder, ModelLoader modelLoader)
	{
		PALLETE_MODEL.load(modelBuilder, modelLoader, "custom_models/small_palette.bbmodel");
	}

	@Override
	public Vector3f getRotations()
	{
		return rotations;
	}

	@Override
	public Vector3f getPivotPoint()
	{
		return pivotPoint;
	}

	@Override
	public Vector3f getPosition()
	{
		return position;
	}

	public void setPlayer(Player player)
	{
		this.player = player;
	}

	@Override
	public AABBf getHitbox()
	{
		return AABB;
	}

	@Override
	public String getName()
	{
		return "palette";
	}

	private static class Dummy implements IPosition3f, IRotation
	{
		private final Vector3f rotations, pivotPoint, position;

		public Dummy()
		{
			this.rotations = new Vector3f();
			this.pivotPoint = new Vector3f();
			this.position = new Vector3f();
		}

		@Override
		public Vector3f getRotations()
		{
			return rotations;
		}

		@Override
		public Vector3f getPivotPoint()
		{
			return pivotPoint;
		}

		@Override
		public Vector3f getPosition()
		{
			return position;
		}
	}
}
