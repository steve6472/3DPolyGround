package com.steve6472.polyground;

import org.joml.Vector3f;

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
}
