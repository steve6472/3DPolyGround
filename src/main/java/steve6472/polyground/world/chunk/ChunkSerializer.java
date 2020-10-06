package steve6472.polyground.world.chunk;

import net.querz.nbt.io.NBTUtil;
import net.querz.nbt.io.NamedTag;
import net.querz.nbt.tag.*;
import steve6472.polyground.NBTArrayUtil;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.blockdata.BlockData;
import steve6472.polyground.block.blockdata.IBlockData;
import steve6472.polyground.block.properties.*;
import steve6472.polyground.block.special.ILightBlock;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.registry.Blocks;
import steve6472.polyground.registry.data.DataRegistry;
import steve6472.polyground.world.generator.EnumChunkStage;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 11.11.2019
 * Project: SJP
 *
 ***********************/
public class ChunkSerializer
{
	public static void serialize(SubChunk subChunk) throws IOException
	{
		File chunk = new File("game/worlds/" + subChunk.getWorld().worldName + "/chunk_" + subChunk.getParent().getX() + "_" + subChunk.getParent().getZ());
		if (!chunk.exists())
			chunk.mkdir();

		File subChunkPath = new File("game/worlds/" + subChunk.getWorld().worldName + "/chunk_" + subChunk.getParent().getX() + "_" + subChunk.getParent().getZ() + "/sub_" + subChunk.getLayer() + ".nbt");
		if (!subChunkPath.exists())
			chunk.createNewFile();

		CompoundTag main = new CompoundTag();

		// Load data
		ListTag<CompoundTag> dataTag = new ListTag<>(CompoundTag.class);

		// Load palette
		LinkedHashMap<BlockState, Short> palette = new LinkedHashMap<>();

		// And save to palette tag at the same time
		ListTag<CompoundTag> paletteTag = new ListTag<>(CompoundTag.class);

		short c = 0;

		for (int i = 0; i < 16; i++)
		{
			for (int j = 0; j < 16; j++)
			{
				for (int k = 0; k < 16; k++)
				{
					BlockState state = subChunk.getState(i, j, k);
					if (!palette.containsKey(state))
					{
						palette.put(state, c);

						CompoundTag stateTag = stateToTag(state);
						stateTag.putShort("index", c);
						paletteTag.add(stateTag);

						c++;
					}

					if (state.getBlock() instanceof IBlockData)
					{
						try
						{
							BlockData data = subChunk.getBlockData(i, j, k);
							CompoundTag tag = data.write();
							tag.putInt("x", i);
							tag.putInt("y", j);
							tag.putInt("z", k);
							tag.putString("name", data.getId());
							dataTag.add(tag);
						} catch (Exception ex)
						{
							System.err.println("Error while saving Block Data at " + i + " " + j + " " + k + " " + state.getBlock().getName());
							ex.printStackTrace();
						}
					}
				}
			}
		}

		// Save palette to tag
		main.put("palette", paletteTag);

		// Save data to tag
		main.put("data", dataTag);

		// Save blocks to tag

		ListTag<CompoundTag> sectionsTag = new ListTag<>(CompoundTag.class);
		for (int y = 0; y < 16; y++)
		{
			CompoundTag sectionTag = new CompoundTag();
			sectionTag.putByte("y", (byte) y);

			short[] shortIdArray = new short[256];
			for (int x = 0; x < 16; x++)
			{
				for (int z = 0; z < 16; z++)
				{
					short id = palette.get(subChunk.getState(x, y, z));
					shortIdArray[x + 16 * z] = id;
				}
			}

			sectionTag.putLongArray("states", NBTArrayUtil.shortToLongArray(shortIdArray));

			sectionsTag.add(sectionTag);
		}
		main.put("sections", sectionsTag);

		//		palette.forEach((k, v) -> System.out.println(k + " -> " + v));

		try
		{
			NBTUtil.write(main, subChunkPath);
			//			System.out.println(SNBTUtil.toSNBT(main));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private static CompoundTag stateToTag(BlockState state)
	{
		CompoundTag stateTag = new CompoundTag();
		stateTag.putString("name", state.getBlock().getName());

		if (state.getProperties() != null)
		{
			CompoundTag propertiesTag = new CompoundTag();
			state.getProperties().forEach((p, v) ->
			{
				if (p instanceof BooleanProperty) propertiesTag.putBoolean(p.getName(), (Boolean) v);
				if (p instanceof IntProperty) propertiesTag.putInt(p.getName(), (Integer) v);
				if (p instanceof StringProperty) propertiesTag.putString(p.getName(), (String) v);
				if (p instanceof EnumProperty<?>) propertiesTag.putString(p.getName(), v.toString());
			});

			stateTag.put("properties", propertiesTag);
		}

		return stateTag;
	}

	private static BlockState stateFromTag(CompoundTag stateTag)
	{
		String name = stateTag.getString("name");

		if (stateTag.containsKey("properties"))
		{
			Block block = Blocks.getBlockByName(name);
			if (block == Block.error)
				return block.getDefaultState();

			HashMap<String, String> propertyMap = new HashMap<>();
			CompoundTag propertiesTag = stateTag.getCompoundTag("properties");
			for (String s : propertiesTag.keySet())
			{
				// Load boolean in different fucking way cause FUCKING NBT does not differentiate between byte and boolean

				Tag<?> tag = propertiesTag.get(s);

				if (tag instanceof ByteTag)
				{
					for (IProperty<?> iProperty : block.getDefaultState().getProperties().keySet())
					{
						if (iProperty.getName().equals(s) && iProperty instanceof BooleanProperty)
						{
							propertyMap.put(s, Boolean.toString(propertiesTag.getBoolean(s)));
						}
					}
				} else if (tag instanceof StringTag st)
				{
					propertyMap.put(s, st.getValue());
				} else
				{
					propertyMap.put(s, propertiesTag.get(s).valueToString());
				}
			}

//			propertyMap.forEach((k, v) -> System.out.println("Property map: " + k + " -> " + v));

			// Properties were probably removed
			if (block.getDefaultState().getProperties() == null)
				return block.getDefaultState();

			return block.getDefaultState().fromStateString(propertyMap);
		}
		return Blocks.getBlockByName(name).getDefaultState();
	}

	public static SubChunk deserialize(SubChunk subChunk) throws IOException
	{
		File subChunkPath = new File("game/worlds/" + subChunk.getWorld().worldName + "/chunk_" + subChunk.getX() + "_" + subChunk.getZ() + "/sub_" + subChunk.getLayer() + ".nbt");

		NamedTag named = NBTUtil.read(subChunkPath);
		CompoundTag main = (CompoundTag) named.getTag();

		// Load palette from tag

		HashMap<Short, BlockState> palette = new HashMap<>();

		ListTag<CompoundTag> paletteTag = (ListTag<CompoundTag>) main.getListTag("palette");
		for (CompoundTag c : paletteTag)
		{
			palette.put(c.getShort("index"), stateFromTag(c));
		}

//		palette.forEach((k, v) -> System.out.println(k + " -> " + v));

		ListTag<CompoundTag> sectionsTag = (ListTag<CompoundTag>) main.getListTag("sections");
		sectionsTag.forEach(sectionTag ->
		{
			int y = sectionTag.getByte("y");
			short[] ids = NBTArrayUtil.longToShortArray(sectionTag.getLongArray("states"));

			for (int x = 0; x < 16; x++)
			{
				for (int z = 0; z < 16; z++)
				{
					BlockState state = palette.get(ids[x + 16 * z]);
					subChunk.setState(state, x, y, z);

					if (state.getBlock() instanceof ILightBlock lb)
						lb.spawnLight(state, subChunk.getWorld(), x + subChunk.getX() * 16, y + subChunk.getLayer() * 16, z + subChunk.getZ() * 16);
				}
			}
		});

		if (main.containsKey("data"))
		{
			ListTag<CompoundTag> dataTag = (ListTag<CompoundTag>) main.getListTag("data");
			dataTag.forEach(data ->
			{
				int x = data.getInt("x");
				int y = data.getInt("y");
				int z = data.getInt("z");
				String name = data.getString("name");

				BlockData blockData = DataRegistry.createData(name);
				blockData.read(data);
				subChunk.setBlockData(blockData, x, y, z);
			});
		}


		subChunk.stage = EnumChunkStage.FINISHED;
		subChunk.rebuild();

		return subChunk;
	}
}
