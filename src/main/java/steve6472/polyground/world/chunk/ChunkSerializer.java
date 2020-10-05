package steve6472.polyground.world.chunk;

import net.querz.nbt.io.NBTUtil;
import net.querz.nbt.io.NamedTag;
import net.querz.nbt.tag.*;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.properties.*;
import steve6472.polyground.block.special.ILightBlock;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.registry.Blocks;
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
				}
			}
		}

		// Save palette to tag
		main.put("palette", paletteTag);

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

			sectionTag.putLongArray("states", encodeShortToLongArray(shortIdArray));

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

			return Blocks.getBlockByName(name).getDefaultState().fromStateString(propertyMap);
		}
		return Blocks.getBlockByName(name).getDefaultState();
	}

	private static short[] decodeLongToShortArray(long[] arr)
	{
		short[] out = new short[arr.length * 4];

		for (int i = 0; i < arr.length; i++)
		{
			long l = arr[i];

			short s0 = (short) (l & 0xffff);
			short s1 = (short) ((l >> 16) & 0xffff);
			short s2 = (short) ((l >> 32) & 0xffff);
			short s3 = (short) ((l >> 48) & 0xffff);

			out[i * 4] = s0;
			out[i * 4 + 1] = s1;
			out[i * 4 + 2] = s2;
			out[i * 4 + 3] = s3;
		}

		return out;
	}

	private static long[] encodeShortToLongArray(short[] arr)
	{
		long[] out = new long[arr.length / 4 + (arr.length % 4 != 0 ? 1 : 0)];

		for (int i = 0; i < out.length; i++)
		{
			long s0 = (arr.length > i * 4) ? arr[i * 4] : 0;
			long s1 = (arr.length > i * 4 + 1) ? arr[i * 4 + 1] : 0;
			long s2 = (arr.length > i * 4 + 2) ? arr[i * 4 + 2] : 0;
			long s3 = (arr.length > i * 4 + 3) ? arr[i * 4 + 3] : 0;

			if (s0 < 0 || s1 < 0 || s2 < 0 || s3 < 0)
				throw new IllegalArgumentException("Element in array can not be negative! long array index: " + i);

			long l = s0 | (s1 << 16) | (s2 << 32) | (s3 << 48);
			out[i] = l;
		}

		return out;
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
			short[] ids = decodeLongToShortArray(sectionTag.getLongArray("states"));

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

		subChunk.stage = EnumChunkStage.FINISHED;
		subChunk.rebuild();

		return subChunk;
	}
}
