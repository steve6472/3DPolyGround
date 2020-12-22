package steve6472.polyground.item.itemdata;

import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;
import steve6472.polyground.block.blockdata.logic.AbstractGate;
import steve6472.polyground.block.blockdata.logic.GateReg;
import steve6472.polyground.block.blockdata.logic.other.Chip;
import steve6472.polyground.block.blockdata.logic.other.Input;
import steve6472.polyground.block.blockdata.logic.other.Output;
import steve6472.polyground.registry.itemdata.ItemDataRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 22.12.2020
 * Project: CaveGame
 *
 ***********************/
public class ChipData extends ItemData
{
	public List<AbstractGate> components;
	public int[][] model;
	public int width, height, depth;

	public ChipData()
	{

	}

	public void setData(List<AbstractGate> components, int[][] model)
	{
		this.components = AbstractGate.copy(components);
		this.model = findSize(model);
	}

	private int[][] findSize(int[][] model)
	{
		int minI = 22, maxI = 0, minJ = 22, maxJ = 0, minK = 22, maxK = 0;
		for (int i = 0; i < 22; i++)
		{
			for (int j = 0; j < 22; j++)
			{
				for (int k = 0; k < 22; k++)
				{
					if (model[i][k + j * 22] != 0)
					{
						minI = Math.min(minI, i);
						maxI = Math.max(maxI, i);

						minJ = Math.min(minJ, j);
						maxJ = Math.max(maxJ, j);

						minK = Math.min(minK, k);
						maxK = Math.max(maxK, k);
					}
				}
			}
		}

		for (AbstractGate c : components)
		{
			if (c instanceof Input || c instanceof Output)
				c.setPosition(c.getPosition().x - minK, c.getPosition().y - minI, c.getPosition().z - minJ);
		}

//		System.out.printf("%d, %d | %d, %d | %d, %d\n", minI, maxI, minJ, maxJ, minK, maxK);
//		System.out.printf("%d | %d | %d\n", maxI - minI + 1, maxJ - minJ + 1, maxK - minK + 1);

		width = maxK - minK + 1;
		height = maxI - minI + 1;
		depth = maxJ - minJ + 1;
//		System.out.printf("%d | %d | %d\n", width, height, depth);

		int[][] newModel = new int[height][width * depth];

		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				for (int k = 0; k < depth; k++)
				{
					int c = model[i + minI][(j + minK) + (k + minJ) * 22];
					if (c != 0xff000000 && c != 0xffffffff)
						newModel[i][j + k * width] = c;
				}
			}
		}

		return newModel;
	}

	@Override
	public CompoundTag write()
	{
		final CompoundTag tag = new CompoundTag();

		ListTag<CompoundTag> list = new ListTag<>(CompoundTag.class);

		for (AbstractGate g : components)
		{
			list.add(g.write());
		}

		tag.putInt("width", width);
		tag.putInt("height", height);
		tag.putInt("depth", depth);

		tag.put("components", list);

		for (int i = 0; i < height; i++)
		{
			tag.putIntArray("layer" + i, model[i]);
		}

		return tag;
	}

	@Override
	public void read(CompoundTag tag)
	{
		this.components = readComponents(tag);
		width = tag.getInt("width");
		height = tag.getInt("height");
		depth = tag.getInt("depth");
		model = new int[height][];
		for (int i = 0; i < height; i++)
		{
			int[] layer = tag.getIntArray("layer" + i);
			model[i] = layer;
		}
	}

	private List<AbstractGate> readComponents(CompoundTag tag)
	{
		final ListTag<CompoundTag> components = (ListTag<CompoundTag>) tag.getListTag("components");
		final List<AbstractGate> gates = new ArrayList<>();
		for (CompoundTag c : components)
		{
			final String type = c.getString("type");
			if (type.equals("chip"))
			{
				Chip g = new Chip(readComponents(c));
				g.read(c);
				gates.add(g);
			} else
			{
				AbstractGate g = GateReg.newGate(type);
				g.read(c);
				gates.add(g);
			}
		}
		for (CompoundTag c : components)
		{
			final AbstractGate gate = AbstractGate.findGate(gates, UUID.fromString(c.getString("uuid")));
			gate.read(c, gates);
		}

		return gates;
	}

	@Override
	public String getId()
	{
		return ItemDataRegistry.chipData.getId();
	}
}
