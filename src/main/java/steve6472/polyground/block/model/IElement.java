package steve6472.polyground.block.model;

import org.json.JSONObject;
import steve6472.polyground.world.ModelBuilder;

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
	 * @return amount of triangles
	 */
	int build(ModelBuilder builder);
}
