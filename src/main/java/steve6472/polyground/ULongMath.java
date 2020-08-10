package steve6472.polyground;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 09.08.2020
 * Project: CaveGame
 *
 ***********************/
public class ULongMath
{
	public static long convertToLong(float a, float b, float c, float d)
	{
		long A = (long) ((a + 1f) * 16384F);
		long B = (long) ((b + 1f) * 16384F);
		long C = (long) ((c + 1f) * 16384F);
		long D = (long) ((d + 1f) * 16384F);

		return (A << 48L) | (B << 32L) | (C << 16L) | D;
	}

	public static long convertToLong(float[] abcd)
	{
		return convertToLong(abcd[0], abcd[1], abcd[2], abcd[3]);
	}

	public static int getClosestIndex(long target, long... ns)
	{
		int ans = 0;
		long minDistance = Long.MAX_VALUE;
		for (int i = 0; i < ns.length; i++)
		{
			if (target == ns[i])
				return i;
			long curDistance = Math.abs(ns[i] - target);
			if (!isBigger(curDistance, minDistance))
			{
				ans = i;
				minDistance = curDistance;
			}
		}
		return ans;
	}

	/**
	 *
	 * @param a left number
	 * @param b right number
	 * @return a < b
	 */
	public static boolean isBigger(long a, long b)
	{
		return a == getBiggerLong(a, b);
	}

	public static long getBiggerLong(long a, long b)
	{
		if (a < 0 && b >= 0)
		{
//			System.out.printf("%d -> %d : %d\n", a, -a, b);
			if (-a == b)
				return a;
			return Math.max(-a, b);
		} else if (b < 0 && a >= 0)
		{
			if (-b == a)
				return b;
			return Math.max(-b, a);
		} else if (a < 0)
		{
			return Math.min(a, b);
		} else
		{
			return Math.max(a, b);
		}
	}
}
