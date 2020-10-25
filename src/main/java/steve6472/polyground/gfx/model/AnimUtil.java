package steve6472.polyground.gfx.model;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 25.10.2020
 * Project: CaveGame
 *
 ***********************/
public class AnimUtil
{
	public static double time(double start, double end, double time)
	{
		// Prevent NaN
		if (time - start == 0 || end - start == 0)
			return 0;

		return 1.0 / ((end - start) / (time - start));
	}

	public static double lerp(double start, double end, double value)
	{
		return start + value * (end - start);
	}

	public static double catmullLerp(double p0, double p1, double p2, double p3, double t)
	{
		return 0.5 * ((2.0 * p1) + (p2 - p0) * t + (2.0 * p0 - 5.0 * p1 + 4.0 * p2 - p3) * t * t + (3.0 * p1 - p0 - 3.0 * p2 + p3) * t * t * t);
	}
}
