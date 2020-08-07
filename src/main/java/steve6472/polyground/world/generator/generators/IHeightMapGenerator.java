package steve6472.polyground.world.generator.generators;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 30.07.2020
 * Project: ThreadedGenerator
 *
 ***********************/
public interface IHeightMapGenerator
{
	float[] generate(int cx, int cz);

	IBiomeGenerator getBiomeGenerator();
}
