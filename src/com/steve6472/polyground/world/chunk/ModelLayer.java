package com.steve6472.polyground.world.chunk;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 17.11.2019
 * Project: SJP
 *
 ***********************/
public enum ModelLayer
{
	NORMAL(0), EMISSION_NORMAL(1), OVERLAY_0(2), /*OVERLAY_1, OVERLAY_2,*/ EMISSION_OVERLAY(3), FOLIAGE(4);

	int index;

	ModelLayer(int index)
	{
		this.index = index;
	}
}
