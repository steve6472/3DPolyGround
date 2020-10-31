package steve6472.polyground.item.groups;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 31.10.2020
 * Project: CaveGame
 *
 ***********************/
public abstract class Preview implements IGroupWheelData
{
	private final String name;
	private final String id;
	private final String preview;
	private final float red;
	private final float green;
	private final float blue;
	private final PreviewType previewType;

	public Preview(String name, String id, String preview, float red, float green, float blue, PreviewType previewType)
	{
		this.name = name;
		this.id = id;
		this.preview = preview;
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.previewType = previewType;
	}

	@Override
	public String preview()
	{
		return preview;
	}

	@Override
	public PreviewType previewType()
	{
		return previewType;
	}

	@Override
	public String id()
	{
		return id;
	}

	@Override
	public String name()
	{
		return name;
	}

	@Override
	public float red()
	{
		return red;
	}

	@Override
	public float green()
	{
		return green;
	}

	@Override
	public float blue()
	{
		return blue;
	}
}
