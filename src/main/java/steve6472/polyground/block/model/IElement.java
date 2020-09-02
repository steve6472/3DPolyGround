package steve6472.polyground.block.model;

import org.json.JSONObject;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.world.ModelBuilder;
import steve6472.polyground.world.World;
import steve6472.polyground.world.chunk.ModelLayer;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 27.08.2020
 * Project: CaveGame
 *
 ***********************/
public interface IElement
{
	void load(JSONObject element);

	void setTexture(int texture);

	void fixUv(float texel);

	/**
	 *
	 * @param builder builder
	 * @param layer layer
	 * @param world world
	 * @param state state
	 * @param x x
	 * @param y y
	 * @param z z
	 * @return amount of triangles
	 */
	int build(ModelBuilder builder, ModelLayer layer, World world, BlockState state, int x, int y, int z);

	String getName();
}
