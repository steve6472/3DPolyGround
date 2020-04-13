package steve6472.polyground;

import org.joml.Vector3f;
import steve6472.polyground.world.chunk.water.Water;

import java.util.Collections;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 19.07.2019
 * Project: SJP
 *
 ***********************/
public class QuickSort
{
	public static void sortEntitiesByDistance(List<Particle> list, Vector3f point)
	{
		boolean sorted = false;
		while (!sorted)
		{
			sorted = true;
			for (int i = 0; i < list.size() - 1; i++)
			{
				float currentDistance = list.get(i).getPosition().distance(point);
				float nextDistance = list.get(i + 1).getPosition().distance(point);

				if (nextDistance > currentDistance)
				{
					Collections.swap(list, i, i + 1);
					sorted = false;
				}
			}
		}
	}

	public static void sortWaterByDistance(List<Water> list, Vector3f point)
	{
		boolean sorted = false;
		while (!sorted)
		{
			sorted = true;
			for (int i = 0; i < list.size() - 1; i++)
			{
				Water w = list.get(i);
				Water nw = list.get(i + 1);
				float currentDistance = Vector3f.distanceSquared(w.x() + 0.5f, w.y() + 0.5f, w.z() + 0.5f, point.x(), point.y(), point.z());
				float nextDistance = Vector3f.distanceSquared(nw.x() + 0.5f, nw.y() + 0.5f, nw.z() + 0.5f, point.x(), point.y(), point.z());

				if (nextDistance < currentDistance)
				{
					Collections.swap(list, i, i + 1);
					sorted = false;
				}
			}
		}
	}
}
