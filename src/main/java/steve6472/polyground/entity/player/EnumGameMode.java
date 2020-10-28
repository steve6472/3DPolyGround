package steve6472.polyground.entity.player;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 10.09.2020
 * Project: CaveGame
 *
 ***********************/
public enum EnumGameMode
{
	SURVIVAL(true, false),
	CREATIVE(false, true);

	public final boolean spawBlockLoot;
	public final boolean canFly;

	EnumGameMode(boolean spawBlockLoot, boolean canFly)
	{
		this.spawBlockLoot = spawBlockLoot;
		this.canFly = canFly;
	}
}
