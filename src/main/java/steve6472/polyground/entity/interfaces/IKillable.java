package steve6472.polyground.entity.interfaces;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 11.04.2020
 * Project: CaveGame
 *
 ***********************/
public interface IKillable
{
	boolean isDead();

	void setDead(boolean dead);

	default void die()
	{
		setDead(true);
	}

	/**
	 * Gets called right before removing the entity
	 */
	default void onDeath() {}
}
