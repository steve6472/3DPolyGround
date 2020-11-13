package steve6472.polyground;

import steve6472.sge.main.KeyList;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 13.11.2020
 * Project: CaveGame
 *
 ***********************/
public enum EnumMouseButton
{
	LEFT(KeyList.LMB), MIDDLE(KeyList.MMB), RIGHT(KeyList.RMB);

	private final int button;

	EnumMouseButton(int button)
	{
		this.button = button;
	}

	public static EnumMouseButton getButton(int button)
	{
		return switch (button)
			{
				case KeyList.LMB -> LEFT;
				case KeyList.MMB -> MIDDLE;
				case KeyList.RMB -> RIGHT;
				default -> throw new IllegalStateException("Unexpected value: " + button);
			};
	}

	public int getButton()
	{
		return button;
	}
}
