package steve6472.polyground.entity.player;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 10.09.2020
 * Project: CaveGame
 *
 ***********************/
public enum EnumGameMode
{
	SURVIVAL(true),
	CREATIVE(false);

	public final boolean spawBlockLoot;

	EnumGameMode(boolean spawBlockLoot)
	{
		this.spawBlockLoot = spawBlockLoot;
	}
}
