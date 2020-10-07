package steve6472.polyground.gfx;

import org.joml.Matrix4f;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 07.10.2020
 * Project: CaveGame
 *
 ***********************/
public interface IModel
{
	void render(Matrix4f viewMatrix, Matrix4f mat);
}
