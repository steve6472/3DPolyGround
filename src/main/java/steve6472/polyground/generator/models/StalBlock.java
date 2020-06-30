package steve6472.polyground.generator.models;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 30.06.2020
 * Project: CaveGame
 *
 ***********************/
public class StalBlock implements IModel
{
	private final int size;

	public StalBlock(int size)
	{
		this.size = size;
	}

	@Override
	public String build()
	{
		return String.format("""
			{
			  "cubes":
			  [
			    {
			      "from": [ %d, 0, %d],
			      "to": [ %d, 16, %d ],
			      "faces":
			  {
			        "down":  { "texture": "stone", "autoUV": true },
			        "up":    { "texture": "stone", "autoUV": true },
			        "north": { "texture": "stone", "autoUV": true },
			        "south": { "texture": "stone", "autoUV": true },
			        "west":  { "texture": "stone", "autoUV": true },
			        "east":  { "texture": "stone", "autoUV": true }
			      }
			    }
			  ]
			}""", size, size, 16 - size, 16 - size);
	}
}
