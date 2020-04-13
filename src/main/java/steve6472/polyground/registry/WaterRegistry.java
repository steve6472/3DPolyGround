package steve6472.polyground.registry;

import java.util.ArrayList;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 12.04.2020
 * Project: CaveGame
 *
 ***********************/
public class WaterRegistry
{
	public static List<Double> tempVolumes = new ArrayList<>();
	public static double[] volumes;

	/**
	 * Has to be called after loading all blocks!
	 */
	public static void init()
	{
		volumes = new double[tempVolumes.size()];

		for (int i = 0; i < tempVolumes.size(); i++)
		{
			volumes[i] = tempVolumes.get(i);
		}

		tempVolumes = null;
	}
}
