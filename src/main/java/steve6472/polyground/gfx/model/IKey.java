package steve6472.polyground.gfx.model;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 25.10.2020
 * Project: CaveGame
 *
 ***********************/
public interface IKey
{
	double time();
	float x(double time);
	float y(double time);
	float z(double time);
	EnumKeyType keyType();
}
