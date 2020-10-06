package steve6472.polyground;

import java.io.IOException;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 05.10.2020
 * Project: CaveGame
 *
 ***********************/
public class NBTArrayUtil
{
	public static short[] longToShortArray(long[] arr)
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

	public static long[] shortToLongArray(short[] arr)
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

	public static long[] boolToLongArray(boolean[] arr)
	{
		long[] out = new long[arr.length / 64 + (arr.length % 64 != 0 ? 1 : 0)];

		for (int i = 0; i < out.length; i++)
		{
			long l = 0;

			for (int j = 0; j < 64; j++)
			{
				if (arr.length <= j + i * 64)
				{
					out[i] = l;
					return out;
				}

				if (arr[i * 64 + j])
				{
					l |= 1L << j;
				}
			}

			out[i] = l;
		}

		return out;
	}

	public static boolean[] longToBoolArray(long[] arr)
	{
		boolean[] out = new boolean[arr.length * 64];

		for (int i = 0; i < arr.length; i++)
		{
			long l = arr[i];

			for (int j = 0; j < 64; j++)
			{
				if ((l & (1L << j)) != 0)
				{
					out[i * 64 + j] = true;
				}
			}
		}

		return out;
	}


	public static void main(String[] args) throws IOException
	{/*
		boolean[] arr = new boolean[256];

		for (int i = 0; i < 256; i++)
		{
			arr[i] = RandomUtil.flipACoin();
		}

		long[] longArr = boolToLongArray(arr);

		printBoolArray(arr);
		System.out.println(Arrays.toString(longArr));

		printBoolArray(longToBoolArray(longArr));*/

//		CompoundTag tag = (CompoundTag) NBTUtil.read("D:\\CaveGame\\game\\worlds\\house\\chunk_0_0\\sub_0.nbt").getTag();
//		ListTag<CompoundTag> d = (ListTag<CompoundTag>) tag.getListTag("data");
//		CompoundTag a = d.get(0);
//		CompoundTag b = d.get(1);
//
//		a.putInt("x", 9);
//		a.putInt("y", 1);
//		a.putInt("z", 9);
//
//		b.putInt("x", 1);
//		b.putInt("y", 1);
//		b.putInt("z", 0);
//
//		d.forEach(ct ->
//		{
//			for (String s : ct.keySet())
//			{
//				if (!s.contains("layer"))
//				{
//					try
//					{
//						System.out.println(s + ":" + SNBTUtil.toSNBT(ct.get(s)));
//					} catch (IOException e)
//					{
//						e.printStackTrace();
//					}
//				}
//			}
//			System.out.println();
//		});
//		System.out.println(SNBTUtil.toSNBT(d));
//		NBTUtil.write(tag, "D:\\CaveGame\\game\\worlds\\house\\chunk_0_0\\sub_0.nbt");
	}

	public static void printBoolArray(boolean[] arr)
	{
		for (boolean b : arr)
		{
			System.out.print(b ? "1" : "0");
		}
		System.out.println();
	}
}
