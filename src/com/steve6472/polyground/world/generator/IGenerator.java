package com.steve6472.polyground.world.generator;

import com.steve6472.polyground.world.chunk.SubChunk;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 09.09.2019
 * Project: SJP
 *
 ***********************/
public interface IGenerator
{
	void generate(SubChunk subChunk);
}
