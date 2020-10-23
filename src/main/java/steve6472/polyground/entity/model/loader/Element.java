package steve6472.polyground.entity.model.loader;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 22.10.2020
 * Project: CaveGame
 *
 ***********************/
public final class Element extends OutlinerElement
{
	public float fromX, fromY, fromZ;
	public float toX, toY, toZ;
	public Face north, east, south, west, up, down;

	public record Face(float u0, float v0, float u1, float v1, int texture)
	{
	}
}
