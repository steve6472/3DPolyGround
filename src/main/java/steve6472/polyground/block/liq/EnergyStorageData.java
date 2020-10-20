package steve6472.polyground.block.liq;

import net.querz.nbt.tag.CompoundTag;
import steve6472.polyground.EnumFace;
import steve6472.polyground.block.blockdata.BlockData;
import steve6472.polyground.block.blockdata.IWorld;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.world.World;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 19.10.2020
 * Project: CaveGame
 *
 ***********************/
public abstract class EnergyStorageData extends BlockData implements IWorld
{
	protected int capacity;
	protected int energy;

	protected World world;
	protected int x, y, z;

	public EnergyStorageData(int capacity)
	{
		this.capacity = capacity;
	}

	@Override
	public void setWorld(World world, int x, int y, int z)
	{
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	protected CompoundTag writeEnergy()
	{
		CompoundTag tag = new CompoundTag();
		tag.putInt("energy", energy);
		return tag;
	}

	protected void readEnergy(CompoundTag tag)
	{
		this.energy = tag.getInt("energy");
	}

	public int sendEnergy(BlockState state, int maxSend)
	{
		int energyExtracted = 0;
		for (EnumFace face : EnumFace.getFaces())
		{
			if (canSend(state, face))
			{
				BlockData data = world.getData(x + face.getXOffset(), y + face.getYOffset(), z + face.getZOffset());
				if (data instanceof EnergyStorageData en)
				{
					BlockState s = world.getState(x + face.getXOffset(), y + face.getYOffset(), z + face.getZOffset());

					if (en.getEnergyStored() < energy && en.getEnergyStored() != en.getMaxEnergyStored())
					{
						if (en.canReceive(s, face.getOpposite()))
						{
							energyExtracted += en.receiveEnergy(Math.min(energy, Math.max(0, Math.min(maxSend(), maxSend) - energyExtracted)));
						}
					}

				}
			}
		}
		energy -= energyExtracted;
		return energyExtracted;
	}

	public int receiveEnergy(int maxReceive)
	{
		int energyReceived = Math.min(capacity - energy, Math.min(maxReceive(), maxReceive));
		energy += energyReceived;
		return energyReceived;
	}

	public int getMaxEnergyStored()
	{
		return capacity;
	}

	public void setEnergy(int energy)
	{
		this.energy = energy;
	}

	public void consumeEnergy(int energy)
	{
		this.energy -= energy;
	}

	public int getEnergyStored()
	{
		return energy;
	}

	public abstract int maxSend();

	public abstract int maxReceive();

	public abstract boolean canSend(BlockState state, EnumFace face);

	public abstract boolean canReceive(BlockState state, EnumFace face);

	public abstract EnumEnergyType getEnergyType();
}
