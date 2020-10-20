package steve6472.polyground.block.model.elements;

import org.joml.AABBf;
import steve6472.polyground.EnumFace;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 19.10.2020
 * Project: CaveGame
 *
 ***********************/
public final class CubeBakerBuilder
{
	FaceBaker north;
	FaceBaker east;
	FaceBaker south;
	FaceBaker west;
	FaceBaker up;
	FaceBaker down;
	AABBf box;

	private CubeBakerBuilder()
	{
	}

	public static CubeBakerBuilder cubeBaker()
	{
		return new CubeBakerBuilder();
	}

	public CubeBakerBuilder withFace(FaceBaker baker, EnumFace... faces)
	{
		for (EnumFace face : faces)
		{
			switch (face)
			{
				case NORTH -> withNorth(baker);
				case EAST -> withEast(baker);
				case SOUTH -> withSouth(baker);
				case WEST -> withWest(baker);
				case UP -> withUp(baker);
				case DOWN -> withDown(baker);
			}
		}
		return this;
	}

	public CubeBakerBuilder withNorth(FaceBaker north)
	{
		this.north = north;
		return this;
	}

	public CubeBakerBuilder withEast(FaceBaker east)
	{
		this.east = east;
		return this;
	}

	public CubeBakerBuilder withSouth(FaceBaker south)
	{
		this.south = south;
		return this;
	}

	public CubeBakerBuilder withWest(FaceBaker west)
	{
		this.west = west;
		return this;
	}

	public CubeBakerBuilder withUp(FaceBaker up)
	{
		this.up = up;
		return this;
	}

	public CubeBakerBuilder withDown(FaceBaker down)
	{
		this.down = down;
		return this;
	}

	public CubeBakerBuilder withBox(AABBf box)
	{
		this.box = box;
		box.setMin(box.minX / 16f, box.minY / 16f, box.minZ / 16f);
		box.setMax(box.maxX / 16f, box.maxY / 16f, box.maxZ / 16f);
		return this;
	}


	public CubeBakerBuilder but()
	{
		return cubeBaker()
			.withNorth(north)
			.withEast(east)
			.withSouth(south)
			.withWest(west)
			.withUp(up)
			.withDown(down)
			.withBox(box);
	}

	public CubeBaker build()
	{
		return new CubeBaker(north, east, south, west, up, down, box);
	}
}
