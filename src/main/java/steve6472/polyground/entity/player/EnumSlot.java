package steve6472.polyground.entity.player;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 08.09.2020
 * Project: CaveGame
 *
 ***********************/
public enum EnumSlot
{
	GROUND, CREATIVE_BELT, HAND_LEFT, HAND_RIGHT;

	private static final EnumSlot[] VALUES = {GROUND, CREATIVE_BELT, HAND_LEFT, HAND_RIGHT};
	private static final EnumSlot[] SLOTS = {HAND_LEFT, HAND_RIGHT};

	public static EnumSlot[] getValues()
	{
		return VALUES;
	}

	public static EnumSlot[] getSlots()
	{
		return SLOTS;
	}

}
