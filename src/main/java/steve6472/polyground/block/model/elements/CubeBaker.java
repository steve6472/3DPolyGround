package steve6472.polyground.block.model.elements;

import org.joml.AABBf;
import steve6472.polyground.EnumFace;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 19.10.2020
 * Project: CaveGame
 *
 ***********************/
public class CubeBaker
{
	FaceBaker north;
	FaceBaker east;
	FaceBaker south;
	FaceBaker west;
	FaceBaker up;
	FaceBaker down;

	AABBf box;

	public CubeBaker(FaceBaker north, FaceBaker east, FaceBaker south, FaceBaker west, FaceBaker up, FaceBaker down, AABBf box)
	{
		this.north = north;
		this.east = east;
		this.south = south;
		this.west = west;
		this.up = up;
		this.down = down;
		this.box = box;
	}

	public FaceBaker getNorth()
	{
		return north;
	}

	public FaceBaker getEast()
	{
		return east;
	}

	public FaceBaker getSouth()
	{
		return south;
	}

	public FaceBaker getWest()
	{
		return west;
	}

	public FaceBaker getUp()
	{
		return up;
	}

	public FaceBaker getDown()
	{
		return down;
	}

	public AABBf getBox()
	{
		return box;
	}

	public int add()
	{
		int tris = 0;
		if (north != null) tris += north.add(this, EnumFace.NORTH);
		if (east != null) tris += east.add(this, EnumFace.EAST);
		if (south != null) tris += south.add(this, EnumFace.SOUTH);
		if (west != null) tris += west.add(this, EnumFace.WEST);
		if (up != null) tris += up.add(this, EnumFace.UP);
		if (down != null) tris += down.add(this, EnumFace.DOWN);
		return tris;
	}
}
