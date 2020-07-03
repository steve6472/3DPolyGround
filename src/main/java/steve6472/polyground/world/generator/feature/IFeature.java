package steve6472.polyground.world.generator.feature;

import steve6472.polyground.world.chunk.SubChunk;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 26.05.2020
 * Project: CaveGame
 *
 ***********************/
public interface IFeature
{
	void generate(SubChunk sc, int x, int y, int z);

	/**
	 * Specifies how many chunks in square radius have to be present for this feature to generate
	 * max is 3
	 * 0 is self
	 *
	 * @return size
	 */
	int size();

	boolean canGenerate(SubChunk sc, int x, int y, int z);

	EnumFeaturePlacement getPlacement();
}